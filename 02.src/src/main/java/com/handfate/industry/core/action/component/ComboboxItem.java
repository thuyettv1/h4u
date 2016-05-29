package com.handfate.industry.core.action.component;

/**
 * @since 12/12/2014
 * @author HienDM
 */
public class ComboboxItem {
    private String value = "";
    private String caption = "";

    public ComboboxItem() {
    }

    public ComboboxItem(String value, String caption) {
        this.value = value;
        this.caption = caption;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return caption;
    }
}
