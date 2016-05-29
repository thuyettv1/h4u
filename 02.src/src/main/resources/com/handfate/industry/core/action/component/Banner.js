window.com_handfate_industry_core_action_component_Banner = function() {
    var connectorId = this.getParentId();
    var element = this.getElement(connectorId);
    
    element.innerHTML =
        " <html> " + 
        " <div style='z-index: -1; width: 100%; text-align:center;' id='container'> " + 
        "     <embed width='950' height='220'  " + 
        "            flashvars='wmode=transparent&amp;file=/industry/VAADIN/themes/mytheme/img/industry/layout/banner/madrid.xml&amp;width=950&amp;height=220'  " + 
        "            quality='high' name='rotator' id='rotator' style='undefined'  " + 
        "            src='/industry/VAADIN/themes/mytheme/img/industry/layout/banner/imagerotator.swf' type='application/x-shockwave-flash'/> " + 
        " </div> " + 
        " </html> ";

};