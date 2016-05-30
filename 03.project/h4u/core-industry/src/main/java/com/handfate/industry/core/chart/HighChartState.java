package com.handfate.industry.core.chart;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * State of the chart which is transferred to the web browser whenever a property changed.
 *
 * @author Stefan Endrullis
 */
public class HighChartState extends JavaScriptComponentState {
    public String domId;
    public String hcjs;
}
