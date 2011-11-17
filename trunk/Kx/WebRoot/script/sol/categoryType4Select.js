(function($) {        
$.fn.categoryType4Select = function(options) {      
     var defaults = {
     	'url':'',
     	'queryParams':{},
     	'newType4':true,
     	onClick:function(value,text,id){}
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
        	 html += '<td height="30" width="150" align="center" valign="middle"><a href="javascript:void(0)" value="' + params[row].ccode + '" id="' + params[row].id + '">' + 
        	 '<img border="0" width="150" src="../pic.action?img=' + params[row].image + '"/><br/>' + 
        	 params[row].cname + '&nbsp;' + params[row].ccode +
        	 '</a></td><td width="10">&nbsp;</td>';
 	 	
        	 if(i ++ % 3 == 0) {
        		html += '</tr><tr>';
        	 }
         }
         
         if(opts.newType4)
        	 html += '<td height="30" width="150" align="center" valign="middle"><input type="button" id="newType4" value="增加新款式" style="border:1px solid #666;"/></td>'
         
         html += '</tr>';
         html += '</table>';

         ctrl.html(html);
         
         // 绑定点击事件
         ctrl.find('a').click(function() {
        	 var value = $(this).attr("value");
        	 var id = $(this).attr("id");
        	 var text = $(this).text();
        	 
        	 opts.onClick(value,text,id);
         });
         
         ctrl.find('#newType4').click(function() {
        	 $("#dlg_newtype4").dialog('open');
        	 
        	 var row = $("#dlg_newtype4").data('type3row');
        	 $("#dlg_newtype4").find("#parentText").text(row.text);
        	 $("#dlg_newtype4").find("#parent").val(row.value);
         });
     }
};      
})(jQuery); 