package com.handfate.industry.extend.action;

import com.handfate.industry.core.chart.HighChart;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 02/06/2014
 * @author HienDM
 */
public class HomeAction {
    private HorizontalLayout homePanel = new HorizontalLayout();
    public static List<Map> lstHomePage;
        
    /**
     * Hàm khởi tạo giao diện trang chủ 
     *
     * @since 02/06/2014 HienDM
     * @return Giao diện trang chủ
     */
    public HorizontalLayout initHomePanel() throws Exception {
        homePanel.setHeight("100%");
        homePanel.setStyleName("CenterBody");
        homePanel.addComponent(buildHomeLayout());
        return homePanel;
    }

    /**
     * Hàm tạo vùng giao diện trang chủ
     *
     * @since 02/06/2014 HienDM
     * @return Vùng giao diện trang chủ
     */
    public HorizontalLayout buildHomeLayout() throws Exception {
        HorizontalLayout centerInstallPanel = new HorizontalLayout();
        centerInstallPanel.setHeight("100%");
        centerInstallPanel.setWidth("100%");
        centerInstallPanel.setSpacing(true);
        
        if(lstHomePage == null || lstHomePage.isEmpty()){
            String sql = "    SELECT charts, cost, code FROM bm_homepage order by charts ";
            HomeAction.lstHomePage = C3p0Connector.queryData(sql, new ArrayList());            
        }
        
        if(lstHomePage != null && !lstHomePage.isEmpty()){
            SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
            Date currentDate = new Date();
            String currentMonth = formatter.format(currentDate);
            Calendar cal = Calendar.getInstance();
            Integer month = cal.get(Calendar.MONTH) + 1;
            Integer year = cal.get(Calendar.YEAR);
            String dateMinus1 = "";
            String dateMinus2 = "";
            if(month == 1) {
                dateMinus1 = "12/" + (year - 1);
                dateMinus2 = "11/" + (year - 1);
            } else if (month == 2) {
                dateMinus1 = "01/" + year;
                dateMinus2 = "12/" + (year - 1);            
            } else {
                Integer monthMinus1 = month - 1;
                Integer monthMinus2 = month - 2;
                if(monthMinus1.toString().length() == 1) { 
                    dateMinus1 = "0" + monthMinus1 + "/" + year;
                } else {
                    dateMinus1 = monthMinus1.toString() + "/" + year;                
                }
                if(monthMinus2.toString().length() == 1) { 
                    dateMinus2 = "0" + monthMinus2 + "/" + year;
                } else {
                    dateMinus2 = monthMinus2.toString() + "/" + year;                
                }            
            }

            VerticalLayout rowLayout = new VerticalLayout();
            rowLayout.setSizeFull();
            HorizontalLayout row1 = new HorizontalLayout();
            row1.setSizeFull();
            final HighChart chart1 = new HighChart();
            chart1.setSizeFull();

            String currentCost = "0";
            String currentCostMinus1 = "0";
            String currentCostMinus2 = "0";
            for(int i = 0; i < lstHomePage.size(); i++) {
                if(lstHomePage.get(i).get("charts") != null && 
                        lstHomePage.get(i).get("charts").toString().equals("3")) {
                    if(lstHomePage.get(i).get("code") != null) {
                        if (lstHomePage.get(i).get("cost") != null
                                && !lstHomePage.get(i).get("cost").toString().isEmpty()) {                        
                            if(lstHomePage.get(i).get("code").toString().equals(currentMonth)) {
                                currentCost = lstHomePage.get(i).get("cost").toString();
                            }
                            if(lstHomePage.get(i).get("code").toString().equals(dateMinus1)) {
                                currentCostMinus1 = lstHomePage.get(i).get("cost").toString();
                            }
                            if(lstHomePage.get(i).get("code").toString().equals(dateMinus2)) {
                                currentCostMinus2 = lstHomePage.get(i).get("cost").toString();
                            }
                        }
                    }
                }
            }

            String strDiagram1 = " var options = {\n" +
                        "    title: {\n" +
                        "        text: '" + ResourceBundleUtils.getLanguageResource("Charts.Revenue.Growth") + "',\n" +
                        "        x: -20 //center\n" +
                        "    },\n" +
                        "    subtitle: {\n" +
                        "        text: '" + ResourceBundleUtils.getLanguageResource("Charts.RevenueService.Caption") + "',\n" +
                        "        x: -20\n" +
                        "    },\n" +
                        "    xAxis: {\n" +
                        "        categories: ['" + dateMinus2 + "', '" + dateMinus1 + "', '" + currentMonth + "']\n" +
                        "    },\n" +
                        "    yAxis: {\n" +
                        "        min: 0,\n" +                
                        "        title: {\n" +
                        "            text: '" + ResourceBundleUtils.getLanguageResource("Charts.Revenue") + "'\n" +
                        "        },\n" +
                        "        plotLines: [{\n" +
                        "                value: 0,\n" +
                        "                width: 1,\n" +
                        "                color: '#808080'\n" +
                        "            }]\n" +
                        "    },\n" +
                        "    tooltip: {\n" +
                        "        valueSuffix: ' VNĐ'\n" +
                        "    },\n" +
                        "    legend: {\n" +
                        "        layout: 'vertical',\n" +
                        "        align: 'right',\n" +
                        "        verticalAlign: 'middle',\n" +
                        "        borderWidth: 0\n" +
                        "    },\n" +
                        "    series: [{\n" +
                        "            name: 'H4U',\n" +
                        "            data: [" + currentCostMinus2 + "," + currentCostMinus1 + "," + currentCost + "]\n" +
                        "        }]\n" +
                        "}; ";
            chart1.setHcjs(strDiagram1);
            row1.addComponent(chart1);

            final HighChart chart2 = new HighChart();
            chart2.setSizeFull();
            String strDiagram2 = " var options = {\n" +
                        "         chart: {\n" +
                        "            type: 'column'\n" +
                        "        },\n" +
                        "        title: {\n" +
                        "            text: '" + ResourceBundleUtils.getLanguageResource("Charts.RevenueService") + "'\n" +
                        "        },\n" +
                        "        subtitle: {\n" +
                        "            text: '" + ResourceBundleUtils.getLanguageResource("Charts.RevenueService.Caption") + "'\n" +
                        "        },\n" +
                        "        xAxis: {\n" +
                        "            categories: [\n" +
                        "                '" + currentMonth + "'\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        yAxis: {\n" +
                        "            min: 0,\n" +
                        "            title: {\n" +
                        "                text: '" + ResourceBundleUtils.getLanguageResource("Charts.Revenue") + "'\n" +
                        "            },\n" +
                        "            plotLines: [{\n" +
                        "                    value: 0,\n" +
                        "                    width: 1,\n" +
                        "                    color: '#808080'\n" +
                        "                }]\n" +                
                        "        },\n" +
                        "        tooltip: {\n" +
                        "            headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',\n" +
                        "            pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' +\n" +
                        "                '<td style=\"padding:0\"><b>{point.y} VNĐ</b></td></tr>',\n" +
                        "            footerFormat: '</table>',\n" +
                        "            shared: true,\n" +
                        "            useHTML: true\n" +
                        "        },\n" +
                        "        plotOptions: {\n" +
                        "            column: {\n" +
                        "                pointPadding: 0.2,\n" +
                        "                borderWidth: 0\n" +
                        "            }\n" +
                        "        },\n" +
                        "        series: [";
            boolean check1 = false;
            for(int i = 0; i < lstHomePage.size(); i++) {
                if(lstHomePage.get(i).get("charts") != null)
                if(lstHomePage.get(i).get("charts").toString().equals("1")) {
                    Object strCost = lstHomePage.get(i).get("cost");
                    if(strCost == null || strCost.toString().isEmpty())
                        strCost = "0";
                    strDiagram2 += "        {\n" +
                                    "            name: '" + lstHomePage.get(i).get("code") + "',\n" +
                                    "            data: [" + strCost + "]\n" +
                                    "\n" +
                                    "        },";
                    check1 = true;
                }
            }
            if(check1) {
                strDiagram2 = strDiagram2.substring(0,strDiagram2.length() - 1);
            }
            else {
                strDiagram2 += "        {\n"
                        + "            name: 'H4U',\n"
                        + "            data: [0]\n"
                        + "\n"
                        + "        }";                
            }
            strDiagram2 += "       ] \n" +
                        "}; ";
            chart2.setHcjs(strDiagram2);
            row1.addComponent(chart2);
        
            rowLayout.addComponent(row1);

            HorizontalLayout row2 = new HorizontalLayout();
            row2.setSizeFull();
            final HighChart chart3 = new HighChart();
            chart3.setSizeFull();
            String strDiagram3 = " var options = {\n" +
                        "         chart: {\n" +
                        "            plotBackgroundColor: null,\n" +
                        "            plotBorderWidth: 1,//null,\n" +
                        "            plotShadow: false\n" +
                        "        },\n" +
                        "        title: {\n" +
                        "            text: '" + ResourceBundleUtils.getLanguageResource("Charts.RevenueServicePercent") + "'\n" +
                        "        },\n" +
                        "        tooltip: {\n" +
                        "            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'\n" +
                        "        },\n" +
                        "        plotOptions: {\n" +
                        "            pie: {\n" +
                        "                allowPointSelect: true,\n" +
                        "                cursor: 'pointer',\n" +
                        "                dataLabels: {\n" +
                        "                    enabled: true,\n" +
                        "                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',\n" +
                        "                    style: {\n" +
                        "                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        },\n" +
                        "        series: [{\n" +
                        "            type: 'pie',\n" +
                        "            name: 'Browser share',\n" +
                        "            data: [\n";
            double totalCost = 0d;
            for (int i = 0; i < lstHomePage.size(); i++) {
                if (lstHomePage.get(i).get("charts") != null) {
                    if (lstHomePage.get(i).get("charts").toString().equals("1")) {
                        Object strCost = lstHomePage.get(i).get("cost");
                        if (strCost != null && !strCost.toString().isEmpty()) {
                            totalCost += Double.parseDouble(strCost.toString());
                        }
                    }
                }
            }        
            boolean check3 = false;
            for (int i = 0; i < lstHomePage.size(); i++) {
                if (lstHomePage.get(i).get("charts") != null) {
                    if (lstHomePage.get(i).get("charts").toString().equals("1")) {
                        Object strCost = lstHomePage.get(i).get("cost");
                        if (strCost == null || strCost.toString().isEmpty()) {
                            strCost = "0";
                        } else {
                            if(totalCost > 0) {
                                Double dCost = 100 * Double.parseDouble(strCost.toString()) / totalCost;
                                DecimalFormat df = new DecimalFormat("#.#");
                                strCost = df.format(dCost);
                            }
                        }
                        strDiagram3 += "                ['" + lstHomePage.get(i).get("code") + 
                                "',   " + strCost + "],";
                        check3 = true;
                    }
                }
            }
            if(check3) {
                strDiagram3 = strDiagram3.substring(0, strDiagram3.length() - 1);
            } else {
                strDiagram3 += "                ['H4U',100]";                
            }
            strDiagram3 +=  "            ]\n" +
                            "        }]  \n" +
                            " };";

            chart3.setHcjs(strDiagram3);
            row2.addComponent(chart3);

            /*final HighChart chart4 = new HighChart();
            chart4.setSizeFull();
            String strDiagram4 = " var options = {\n" +
                        "         chart: {\n" +
                        "            type: 'column'\n" +
                        "        },\n" +
                        "        title: {\n" +
                        "            text: '" + ResourceBundleUtils.getLanguageResource("Charts.RevenueGroup") + "'\n" +
                        "        },\n" +
                        "        subtitle: {\n" +
                        "            text: '" + ResourceBundleUtils.getLanguageResource("Charts.RevenueService.Caption") + "'\n" +
                        "        },\n" +
                        "        xAxis: {\n" +
                        "            categories: [\n" +
                        "                '" + currentMonth + "'\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        yAxis: {\n" +
                        "            min: 0,\n" +
                        "            title: {\n" +
                        "                text: '" + ResourceBundleUtils.getLanguageResource("Charts.Revenue") + "'\n" +
                        "            },\n" +
                        "            plotLines: [{\n" +
                        "                    value: 0,\n" +
                        "                    width: 1,\n" +
                        "                    color: '#808080'\n" +
                        "                }]\n" +                
                        "        },\n" +
                        "        tooltip: {\n" +
                        "            headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',\n" +
                        "            pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' +\n" +
                        "                '<td style=\"padding:0\"><b>{point.y} VNĐ</b></td></tr>',\n" +
                        "            footerFormat: '</table>',\n" +
                        "            shared: true,\n" +
                        "            useHTML: true\n" +
                        "        },\n" +
                        "        plotOptions: {\n" +
                        "            column: {\n" +
                        "                pointPadding: 0.2,\n" +
                        "                borderWidth: 0\n" +
                        "            }\n" +
                        "        },\n" +
                        "        series: [";
            boolean check2 = false;
            for(int i = 0; i < lstHomePage.size(); i++) {
                if(lstHomePage.get(i).get("charts") != null)
                if(lstHomePage.get(i).get("charts").toString().equals("2")) {
                    Object strCost = lstHomePage.get(i).get("cost");
                    if(strCost == null || strCost.toString().isEmpty())
                        strCost = "0";
                    strDiagram4 += "        {\n" +
                                    "            name: '" + lstHomePage.get(i).get("code") + "',\n" +
                                    "            data: [" + strCost + "]\n" +
                                    "\n" +
                                    "        },";
                    check2 = true;
                }
            }
            if (check2) {
                strDiagram4 = strDiagram4.substring(0,strDiagram4.length() - 1);
            } else {
                strDiagram4 += "        {\n"
                        + "            name: 'h4U',\n"
                        + "            data: [0]\n"
                        + "\n"
                        + "        }";
            }
            strDiagram4 += "       ] \n" +
                        "}; ";
            chart4.setHcjs(strDiagram4);
            row2.addComponent(chart4);     */   

            rowLayout.addComponent(row2);
            centerInstallPanel.addComponent(rowLayout);
        }
        return centerInstallPanel;
    }
}
