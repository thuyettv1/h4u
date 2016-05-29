package com.handfate.industry.core.action.component;

import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Image;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author HienDM
 */
public class UploadField extends org.vaadin.easyuploads.UploadField {
    public DownloadLink downloadLink = null;
    public Image image = null;
    public boolean isPicture = false;
    Set<String> pictureExtension = new HashSet<String>(Arrays.asList(
        //BMP
        ".bmp",".rle",".dib",
        //PNG
        ".png",".pns",
        //GIF
        ".gif",
        //JPEG
        ".jpg",".jpeg",".jpe",
        //JPEG 2000
        ".jpf",".jpx",".jp2",
        ".j2c",".j2k",".jpc",
        //JPEG Stereo
        ".jps",
        //TIFF
        ".tif",".tiff"));
    
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public long getLastFileSize() {
        return super.getLastFileSize();
    }

    @Override
    public String getLastMimeType() {
        return super.getLastMimeType();
    }

    @Override
    public String getLastFileName() {
        return super.getLastFileName();
    }
    
    @Override
    public void buildDefaulLayout() {
        super.buildDefaulLayout();
    }
    
    /**
     * Hàm thiết lập download link cho màn hình sửa
     *
     * @since 19/11/2014 HienDM
     * @param component link download file
     */
    public void setDownloadLink(DownloadLink component) throws Exception {
        buildDefaulLayout();
        downloadLink = new DownloadLink();
        downloadLink.setCaption(component.getCaption());
        String filePath = component.getFilePath();
        downloadLink.setFilePath(filePath);
        if(filePath != null && pictureExtension.contains(filePath.substring(filePath.lastIndexOf(".")))) {
            isPicture = true;
        } else isPicture = false;
        
        final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
        downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                downloaderForLink.setFilePath(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                        + downloadLink.getFilePath());
            }
        });
        downloaderForLink.extend(downloadLink);
        if(filePath != null && isPicture) {
            // Image as a file resource
            FileResource resource = new FileResource(new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                            + downloadLink.getFilePath()));
            // Show the image in the application
            image = new Image("", resource);
            image.setWidth("100px");
            image.setHeight("100px");
            getRootLayout().addComponent(image);
        } else {
            getRootLayout().addComponent(downloadLink);
        }
    }
    
    /**
     * Hàm xoá download link
     *
     * @since 19/11/2014 HienDM
     */
    public void removeDownloadLink() {
        if(downloadLink != null) {
            getRootLayout().removeComponent(downloadLink);
            downloadLink = null;
        }
    }
}
