(function($) {        
$.fn.panelSelect = function(options) {      
     var defaults = {
     	'url':'',
     	'queryParams':{},
     	onClick:function(value,text){}
     }
     
     var opts = $.extend(defaults,options);
     
     var ctrl = $(this);
     
     // 请求数据
     $.post(opts.url,opts.queryParams,function(data){
    	 var params = jQuery.parseJSON(data);
    	 fillCtrl(params);
     });
  
     // 填充对象
     var fillCtrl = function(params) {
         var html = "";
         
         html = '<table cellpadding="0" cellspacing="0" border="0">';
     
         var i = 1;
         html += '<tr>';
         for(row = 0; row < params.length; row=row+1) {
        	 html += '<td height="30" width="100" align="center" valign="middle"><a href="javascript:void(0)" value="' + params[row].value + '">' + params[row].text + '</a></td>';
        	 	
        	 if(i ++ % 5 == 0) {
        		html += '</tr><tr>';
        	 }
         }
         
         html += '</tr>';
         html += '</table>';
         
         ctrl.append(html);
         
         // 绑定点击事件
         ctrl.find('a').click(function() {
        	 var value = $(this).attr("value");
        	 var text = $(this).text();
        	 
        	 opts.onClick(value,text);
         });
     }
};      
})(jQuery); 