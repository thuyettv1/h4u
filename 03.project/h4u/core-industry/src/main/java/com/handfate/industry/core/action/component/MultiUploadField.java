package com.handfate.industry.core.action.component;

import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.Base64Utils;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author HienDM
 */
public class MultiUploadField extends org.vaadin.easyuploads.MultiFileUpload {
    
    public List<DownloadLink> lstDownloadLink = new ArrayList();
    public VerticalLayout downloadLinkArea = new VerticalLayout();
    public String subFolder = "";

    public List<DownloadLink> getLstDownloadLink() {
        return lstDownloadLink;
    }

    public void setLstDownloadLink(List<DownloadLink> lstDownloadLink) {
        this.lstDownloadLink = lstDownloadLink;
    }

    public VerticalLayout getDownloadLinkArea() {
        return downloadLinkArea;
    }

    public void setDownloadLinkArea(VerticalLayout downloadLinkArea) {
        this.downloadLinkArea = downloadLinkArea;
    }

    public String getSubFolder() {
        return subFolder;
    }

    public void setSubFolder(String subFolder) {
        this.subFolder = subFolder;
    }
    
    @Override
    protected void initialize() {
        super.initialize();
        addComponent(downloadLinkArea);
    }
    
    @Override
    public void handleFile(File file, String fileName, String mimeType, long length) {
        setComponentError(null);
        boolean check = true;
        if (!FileUtils.checkSafeFileName(fileName)) {
            setComponentError(new UserError(ResourceBundleUtils.
                    getLanguageResource("Common.Error.FileInvalid")));
            check = false;
        } else if (!FileUtils.isAllowedType(fileName,
                    ResourceBundleUtils.getConfigureResource("FileAllowList"))) {
            setComponentError(new UserError(
                    ResourceBundleUtils.getLanguageResource("Common.Error.FileFormat") + " ("
                    + ResourceBundleUtils.getConfigureResource("FileAllowList") + ")"));
            check = false;            
        }
        
        if(check) {
            Calendar cal = Calendar.getInstance();
            String fileDirectory
                    = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + subFolder;
            File directory = new File(fileDirectory);
            if (!directory.exists()) {
                directory.mkdir();
            }
            String fullFileName = "" + cal.get(Calendar.YEAR)
                    + (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DATE)
                    + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND)
                    + "_" + FileUtils.extractFileNameNotExt(fileName);
            String encodeFileName = Base64Utils.encodeBytes(fullFileName.getBytes())
                    + FileUtils.extractFileExt(fileName);
            String finalFileName = subFolder + File.separator + encodeFileName;
            DownloadLink dl = new DownloadLink();
            dl.setFilePath(finalFileName);
            if(fileName.length() > 30) fileName = fileName.substring(0,25) + "---" + FileUtils.extractFileExt(fileName);
            dl.setCaption(fileName);
            final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
            downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
                @Override
                public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                    String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                            + dl.getFilePath();
                    downloaderForLink.setFilePath(filePath);
                }
            });
            downloaderForLink.extend(dl);        
            lstDownloadLink.add(dl);

            HorizontalLayout cell = new HorizontalLayout();
            Embedded iconDelete = new Embedded(null, new ThemeResource("img/industry/button/delete.png"));
            cell.addComponent(dl);
            cell.setComponentAlignment(dl, Alignment.MIDDLE_LEFT);
            cell.addComponent(iconDelete);
            cell.setComponentAlignment(iconDelete, Alignment.MIDDLE_LEFT);
            downloadLinkArea.addComponent(cell);

            iconDelete.addClickListener(new MouseEvents.ClickListener() {
                public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                    try {
                        downloadLinkArea.removeComponent(cell);
                        lstDownloadLink.remove(dl);
                    } catch (Exception ex) {
                        VaadinUtils.handleException(ex);
                        mainLogger.debug("Industry error: ", ex);
                    }
                }
            });
            file.renameTo(new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + finalFileName));
        }
    }   
    
    /**
     * Hàm xoá download link
     *
     * @since 19/11/2014 HienDM
     */
    public void removeAllDownloadLink() {
        downloadLinkArea.removeAllComponents();
        lstDownloadLink = new ArrayList();
    }
    
    /**
     * Hàm ẩn icon xóa
     *
     * @since 19/11/2014 HienDM
     */
    public void hideDeleteIcon() {
        for (int i = 0; i < downloadLinkArea.getComponentCount(); i++) {
            ((HorizontalLayout)downloadLinkArea.getComponent(i)).getComponent(1).setVisible(false);
        }
    }
    
    /**
     * Hàm hiển thị icon xóa
     *
     * @since 19/11/2014 HienDM
     */
    public void showDeleteIcon() {
        for (int i = 0; i < downloadLinkArea.getComponentCount(); i++) {
            ((HorizontalLayout)downloadLinkArea.getComponent(i)).getComponent(1).setVisible(true);
        }
    }
}
