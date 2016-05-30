package com.handfate.industry.core.chart;

import com.vaadin.annotations.JavaScript;

/**
 * This is the chart class we are using in the demo application.
 * It loads jquery 1.7.1 and highcharts.js located in the same package.
 * Extend you own class in order to load other JS files.
 * Make sure "highcharts-connector.js" is loaded at the end.
 *
 * @author Stefan Endrullis
 */
@JavaScript({"jquery.min_1.8.2.js", "highcharts.js", "highcharts-connector.js", "exporting.js"})
public class HighChart extends AbstractHighChart {
}
