package com.handfate.industry.core.action.component;

import com.family.common.util.CSVTransformer;
import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.util.EncryptDecryptUtils;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import java.io.ByteArrayInputStream;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PagedTable extends Table {
    private static final long serialVersionUID = 6881455780158545828L;
    private Button first;
    private Button previous;
    private Button next;
    private Button last;
    private TextField currentPageTextField;
    private Label totalPagesLabel;
    private ComboBox itemsPerPageSelect;
    private IntegerRangeValidator validator;
    private Label totalLabel;
    
    public int DEFAULT_PAGE_LENGTH = 25;

    public interface PageChangeListener {
        public void pageChanged(PagedTableChangeEvent event);
    }

    public class PagedTableChangeEvent {

        final PagedTable table;

        public PagedTableChangeEvent(PagedTable table) {
            this.table = table;
        }

        public PagedTable getTable() {
            return table;
        }

        public int getCurrentPage() {
            return table.getCurrentPage();
        }

        public int getTotalAmountOfPages() {
            return table.getTotalAmountOfPages();
        }
    }

    private List<PageChangeListener> listeners = null;

    private PagedTableContainer container;

    public PagedTable() {
        this(null);
    }

    public PagedTable(String caption) {
        super(caption);
        setPageLength(DEFAULT_PAGE_LENGTH);
        addStyleName("pagedtable");
    }

    public HorizontalLayout createControls() {
        Label itemsPerPageLabel = new Label(
                ResourceBundleUtils.getLanguageResource("Common.ItemPerPage"));
        itemsPerPageSelect = new ComboBox();
        itemsPerPageSelect.addItem("5");
        itemsPerPageSelect.addItem("10");
        itemsPerPageSelect.addItem("25");
        itemsPerPageSelect.addItem("50");
        itemsPerPageSelect.addItem("100");
        itemsPerPageSelect.addItem("600");
        itemsPerPageSelect.setImmediate(true);
        itemsPerPageSelect.setNullSelectionAllowed(false);
        itemsPerPageSelect.setWidth("50px");
        itemsPerPageSelect.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                setPageLength(Integer.valueOf(String.valueOf(event
                        .getProperty().getValue())));
            }
        });
        itemsPerPageSelect.select("" + DEFAULT_PAGE_LENGTH);
        
        Label pageLabel = new Label(
                ResourceBundleUtils.getLanguageResource("Common.Page") + 
                "&nbsp;", ContentMode.HTML);
        currentPageTextField = new TextField();
        currentPageTextField.setValue(String.valueOf(getCurrentPage()));
        currentPageTextField.setConverter(Integer.class);
        validator = new IntegerRangeValidator(
                ResourceBundleUtils.getLanguageResource("Common.WrongPageNumber"), 
                1, getTotalAmountOfPages());
        currentPageTextField.addValidator(validator);
        Label separatorLabel = new Label("&nbsp;/&nbsp;", ContentMode.HTML);
        totalPagesLabel = new Label(
                String.valueOf(getTotalAmountOfPages()), ContentMode.HTML);
        currentPageTextField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        currentPageTextField.setImmediate(true);
        currentPageTextField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                if (currentPageTextField.isValid()
                        && currentPageTextField.getValue() != null) {
                    int page = Integer.valueOf(String
                            .valueOf(currentPageTextField.getValue()));
                    setCurrentPage(page);
                }
            }
        });
        pageLabel.setWidth(null);
        currentPageTextField.setWidth("20px");
        separatorLabel.setWidth(null);
        totalPagesLabel.setWidth(null);

        HorizontalLayout controlBar = new HorizontalLayout();
        HorizontalLayout pageSize = new HorizontalLayout();
        HorizontalLayout pageManagement = new HorizontalLayout();
        
        //Xuất file
        Embedded iconExcel = new Embedded(null, new ThemeResource("img/industry/button/excel.png"));
        iconExcel.setHeight("25px");
        pageSize.addComponent(iconExcel);
        iconExcel.addClickListener(new MouseEvents.ClickListener() {
            public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                try {
                    exportExcel();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });        

        pageSize.addComponent(iconExcel);

        Embedded iconCSV = new Embedded(null, new ThemeResource("img/industry/button/csv.png"));
        iconCSV.setHeight("23px");
        pageSize.addComponent(iconCSV);    
        iconCSV.addClickListener(new MouseEvents.ClickListener() {
            public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                try {
                    exportCSV();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        
//        Embedded iconXML = new Embedded(null, new ThemeResource("img/industry/button/xml.png"));
//        pageSize.addComponent(iconXML);        
        
        first = new Button("<<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            public void buttonClick(ClickEvent event) {
                setCurrentPage(0);
            }
        });
        previous = new Button("<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            public void buttonClick(ClickEvent event) {
                previousPage();
            }
        });
        next = new Button(">", new ClickListener() {
            private static final long serialVersionUID = -1927138212640638452L;

            public void buttonClick(ClickEvent event) {
                nextPage();
            }
        });
        last = new Button(">>", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            public void buttonClick(ClickEvent event) {
                setCurrentPage(getTotalAmountOfPages());
            }
        });
        first.setStyleName(Reindeer.BUTTON_LINK);
        previous.setStyleName(Reindeer.BUTTON_LINK);
        next.setStyleName(Reindeer.BUTTON_LINK);
        last.setStyleName(Reindeer.BUTTON_LINK);

        itemsPerPageLabel.addStyleName("pagedtable-itemsperpagecaption");
        itemsPerPageSelect.addStyleName("pagedtable-itemsperpagecombobox");
        pageLabel.addStyleName("pagedtable-pagecaption");
        currentPageTextField.addStyleName("pagedtable-pagefield");
        separatorLabel.addStyleName("pagedtable-separator");
        totalPagesLabel.addStyleName("pagedtable-total");
        first.addStyleName("pagedtable-first");
        previous.addStyleName("pagedtable-previous");
        next.addStyleName("pagedtable-next");
        last.addStyleName("pagedtable-last");

        itemsPerPageLabel.addStyleName("pagedtable-label");
        itemsPerPageSelect.addStyleName("pagedtable-combobox");
        pageLabel.addStyleName("pagedtable-label");
        currentPageTextField.addStyleName("pagedtable-label");
        separatorLabel.addStyleName("pagedtable-label");
        totalPagesLabel.addStyleName("pagedtable-label");
        first.addStyleName("pagedtable-button");
        previous.addStyleName("pagedtable-button");
        next.addStyleName("pagedtable-button");
        last.addStyleName("pagedtable-button");

        pageSize.addComponent(itemsPerPageLabel);
        pageSize.addComponent(itemsPerPageSelect);
        
        pageSize.setComponentAlignment(itemsPerPageLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(itemsPerPageSelect,
                Alignment.MIDDLE_LEFT);
        pageSize.setSpacing(true);
        Label totalDescription = new Label(ResourceBundleUtils.getLanguageResource("Table.Size"));
        totalLabel = new Label(container.getRealSize() + " ");
        totalLabel.addStyleName("pagedtable-pagecaption");
        totalDescription.addStyleName("pagedtable-pagecaption");
        pageManagement.addComponent(totalDescription);
        pageManagement.addComponent(totalLabel);
        pageManagement.addComponent(first);
        pageManagement.addComponent(previous);
        pageManagement.addComponent(pageLabel);
        pageManagement.addComponent(currentPageTextField);
        pageManagement.addComponent(separatorLabel);
        pageManagement.addComponent(totalPagesLabel);
        pageManagement.addComponent(next);
        pageManagement.addComponent(last);
        pageManagement.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalDescription, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(currentPageTextField,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(separatorLabel,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalPagesLabel,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
        pageManagement.setWidth(null);
        pageManagement.setSpacing(true);
        controlBar.addComponent(pageSize);
        controlBar.addComponent(pageManagement);
        controlBar.setComponentAlignment(pageManagement,
                Alignment.MIDDLE_CENTER);
        controlBar.setWidth("100%");
        controlBar.setExpandRatio(pageSize, 1);
        addListener(new PageChangeListener() {
            public void pageChanged(PagedTableChangeEvent event) {
                first.setEnabled(container.getStartIndex() > 0);
                previous.setEnabled(container.getStartIndex() > 0);
                next.setEnabled(container.getStartIndex() < container
                        .getRealSize() - getPageLength());
                last.setEnabled(container.getStartIndex() < container
                        .getRealSize() - getPageLength());
                currentPageTextField.setValue(String.valueOf(getCurrentPage()));
                totalPagesLabel.setValue(String.valueOf(getTotalAmountOfPages()));
                totalLabel.setValue("" + container.getRealSize() + " ");
                itemsPerPageSelect.setValue(String.valueOf(getPageLength()));
                validator.setMaxValue(getTotalAmountOfPages());
            }
        });
        return controlBar;
    }

    public void updateControl() {
        if(first != null) {
            first.setEnabled(container.getStartIndex() > 0);
            previous.setEnabled(container.getStartIndex() > 0);
            next.setEnabled(container.getStartIndex() < container
                    .getRealSize() - getPageLength());
            last.setEnabled(container.getStartIndex() < container
                    .getRealSize() - getPageLength());
            currentPageTextField.setValue(String.valueOf(getCurrentPage()));
            totalPagesLabel.setValue(String.valueOf(getTotalAmountOfPages()));
            totalLabel.setValue("" + container.getRealSize() + " ");
            itemsPerPageSelect.setValue(String.valueOf(getPageLength()));
            validator.setMaxValue(getTotalAmountOfPages());        
        }
    }
    
    @Override
    public Container.Indexed getContainerDataSource() {
        return container;
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        if (!(newDataSource instanceof Container.Indexed)) {
            throw new IllegalArgumentException(
                    "PagedTable can only use containers that implement Container.Indexed");
        }
        PagedTableContainer pagedTableContainer = new PagedTableContainer(
                (Container.Indexed) newDataSource);
        pagedTableContainer.setPageLength(getPageLength());
        super.setContainerDataSource(pagedTableContainer);
        this.container = pagedTableContainer;
        firePagedChangedEvent();
    }

    private void setPageFirstIndex(int firstIndex) {
        if (container != null) {
            if (firstIndex <= 0) {
                firstIndex = 0;
            }
            if (firstIndex > container.getRealSize() - 1) {
                int size = container.getRealSize() - 1;
                int pages = 0;
                if (getPageLength() != 0) {
                    pages = (int) Math.floor(0.0 + size / getPageLength());
                }
                firstIndex = pages * getPageLength();
            }
            container.setStartIndex(firstIndex);
            setCurrentPageFirstItemIndex(firstIndex);
            containerItemSetChange(new Container.ItemSetChangeEvent() {
                private static final long serialVersionUID = -5083660879306951876L;

                public Container getContainer() {
                    return container;
                }
            });
            if (alwaysRecalculateColumnWidths) {
                for (Object columnId : container.getContainerPropertyIds()) {
                    setColumnWidth(columnId, -1);
                }
            }
            firePagedChangedEvent();
        }
    }

    private void firePagedChangedEvent() {
        if (listeners != null) {
            PagedTableChangeEvent event = new PagedTableChangeEvent(this);
            for (PageChangeListener listener : listeners) {
                listener.pageChanged(event);
            }
        }
    }

    @Override
    public void setPageLength(int pageLength) {
        if (pageLength >= 0 && getPageLength() != pageLength) {
            container.setPageLength(pageLength);
            super.setPageLength(pageLength);
            firePagedChangedEvent();
        }
    }

    public void nextPage() {
        setPageFirstIndex(container.getStartIndex() + getPageLength());
    }

    public void previousPage() {
        setPageFirstIndex(container.getStartIndex() - getPageLength());
    }

    public int getCurrentPage() {
        double pageLength = getPageLength();
        int page = (int) Math.floor((double) container.getStartIndex()
                / pageLength) + 1;
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    public void setCurrentPage(int page) {
        int newIndex = (page - 1) * getPageLength();
        if (newIndex < 0) {
            newIndex = 0;
        }
        if (newIndex >= 0 && newIndex != container.getStartIndex()) {
            setPageFirstIndex(newIndex);
        }
    }

    public int getTotalAmountOfPages() {
        int size = container.getContainer().size();
        double pageLength = getPageLength();
        int pageCount = (int) Math.ceil(size / pageLength);
        if (pageCount < 1) {
            pageCount = 1;
        }
        return pageCount;
    }

    public void addListener(PageChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(PageChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener>();
        }
        listeners.remove(listener);
    }

    public void setAlwaysRecalculateColumnWidths(
            boolean alwaysRecalculateColumnWidths) {
        this.alwaysRecalculateColumnWidths = alwaysRecalculateColumnWidths;
    }

    public void exportExcel() throws Exception {
        // Tạo file download
        Object[] allItemIds = ((java.util.Collection) this.getItemIds()).toArray();
        Object[][] totalData = new Object[allItemIds.length + 1][this.getColumnHeaders().length];

        String[] header = this.getColumnHeaders();
        totalData[0] = header;
        for (int i = 0; i < allItemIds.length; i++) {
            Object[] data = new Object[header.length];
            Item item = this.getItem(allItemIds[i]);
            for (int j = 0; j < header.length; j++) {
                Object value = item.getItemProperty(header[j]).getValue();
                if(value instanceof ComboboxItem) {
                    data[j] = ((ComboboxItem)value).getCaption();
                } else {
                    data[j] = value;
                }
            }
            totalData[i + 1] = data;
        }
        String strDirectory = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") +  "Temp";
        File fileDirectory = new File(strDirectory);
        if(!fileDirectory.exists()) fileDirectory.mkdir();
        EncryptDecryptUtils edu = new EncryptDecryptUtils();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String strName = cal.get(java.util.Calendar.YEAR) + (cal.get(java.util.Calendar.MONTH) + 1) + 
                cal.get(java.util.Calendar.DATE) + cal.get(java.util.Calendar.HOUR) + cal.get(java.util.Calendar.MINUTE) + 
                cal.get(java.util.Calendar.SECOND) + VaadinUtils.getSessionAttribute("G_UserName").toString();
        strName = edu.encodePassword(strName);
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        strName = strName.replaceAll(pattern, "_");
        strName = strName.replaceAll("/", "_");
        String strPath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Temp"
                + File.separator + strName;
        String filePath = "";
        filePath = strPath + ".xls";
        FileUtils.exportLargeDataToExcel(totalData, filePath);

        //Download file

        File downloadFile = new File(filePath);
        if (!downloadFile.exists()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.FileNotExist"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
            
        final FileResource stream = new FileResource(downloadFile) {
            @Override
            public DownloadStream getStream() {
                DownloadStream ds = null;
                try {
                    ByteArrayInputStream in = new ByteArrayInputStream(
                            org.apache.commons.io.FileUtils.readFileToByteArray(downloadFile));
                    ds = new DownloadStream(in, "application/octet-stream", "Export.xlsx");
                    // Need a file download POPUP
                    ds.setParameter("Content-Disposition",
                            "attachment; filename=Export.xlsx");
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);                    
                }
                return ds;
            }
        };
        stream.setCacheTime(0);
        Page.getCurrent().open(stream, "Download", true);
    }
    
    public void exportCSV() throws Exception {
        // Tạo file download
        Object[] allItemIds = ((java.util.Collection) this.getItemIds()).toArray();
        Object[][] totalData = new Object[allItemIds.length + 1][this.getColumnHeaders().length];

        String[] header = this.getColumnHeaders();
        totalData[0] = header;
        for (int i = 0; i < allItemIds.length; i++) {
            Object[] data = new Object[header.length];
            Item item = this.getItem(allItemIds[i]);
            for (int j = 0; j < header.length; j++) {
                Object value = item.getItemProperty(header[j]).getValue();
                if(value instanceof ComboboxItem) {
                    data[j] = ((ComboboxItem)value).getCaption();
                } else {
                    data[j] = value;
                }
            }
            totalData[i + 1] = data;
        }
        String strDirectory = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") +  "Temp";
        File fileDirectory = new File(strDirectory);
        if(!fileDirectory.exists()) fileDirectory.mkdir();
        EncryptDecryptUtils edu = new EncryptDecryptUtils();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String strName = cal.get(java.util.Calendar.YEAR) + (cal.get(java.util.Calendar.MONTH) + 1) + 
                cal.get(java.util.Calendar.DATE) + cal.get(java.util.Calendar.HOUR) + cal.get(java.util.Calendar.MINUTE) + 
                cal.get(java.util.Calendar.SECOND) + VaadinUtils.getSessionAttribute("G_UserName").toString();
        strName = edu.encodePassword(strName);
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        strName = strName.replaceAll(pattern, "_");
        strName = strName.replaceAll("/", "_");
        String strPath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Temp"
                + File.separator + strName;
        String filePath = "";
        filePath = strPath + ".csv";
        CSVTransformer.transformCSV(totalData, filePath);

        //Download file

        File downloadFile = new File(filePath);
        if (!downloadFile.exists()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.FileNotExist"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
            
        final FileResource stream = new FileResource(downloadFile) {
            @Override
            public DownloadStream getStream() {
                DownloadStream ds = null;
                try {
                    ByteArrayInputStream in = new ByteArrayInputStream(
                            org.apache.commons.io.FileUtils.readFileToByteArray(downloadFile));
                    ds = new DownloadStream(in, "application/octet-stream", "Export.csv");
                    // Need a file download POPUP
                    ds.setParameter("Content-Disposition",
                            "attachment; filename=Export.csv");
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);                    
                }
                return ds;
            }
        };
        stream.setCacheTime(0);
        Page.getCurrent().open(stream, "Download", true);
    }    
}