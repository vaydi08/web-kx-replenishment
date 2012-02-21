$(document).ready(function() {
	function getQueryString(){
	     var result = location.search.match(new RegExp("[\?\&][^\?\&]+=[^\?\&]+","g")); 

	     if(result == null){
	         return "";
	     }

	     var out = {};
	     for(var i = 0; i < result.length; i++){
	         var value = result[i].split("=");
	         out[value[0].substring(1)] = value[1];
	     }
	     return out;
	}
	
	function loadPrintData() {
		var tb1 = '<table border="0" cellspacing="0" cellpadding="0" class="PageNext" width="700">' + 
				  '<thread><tr><th width="70">大类</th><th width="70">小类</th><th width="70">货型</th><th width="70">款式</th><th width="300">产品名称</th><th width="100">产品代码</th></tr></thead>';
		var tb2 = '</table><br/>';
		var tb = '';
		
		var pagesize = 50;
		
		var param = getQueryString();

		$.post('../../info/info-product!printPreview.action',param,function(data) {
			for(i = 0; i < (data.rows.length / pagesize); i ++) {
				tb = tb1;
				
				for(j = i * pagesize; j < (data.rows.length < (i + 1) * pagesize ? data.rows.length : (i + 1) * pagesize); j ++) {
					tb += '<tr><td>' + data.rows[j].type1name + '</td><td>' + data.rows[j].type2name + '</td><td>'
						+ data.rows[j].type3name + '</td><td>' + data.rows[j].type4name + '</td><td>'
						+ data.rows[j].pname + '</td><td>' + data.rows[j].pcode + '</td></tr>'
				}
				
				tb += tb2;
				
				$('.printScope').append(tb);
			}
		},'json');
	}
	
	loadPrintData();
	
//	$('#btn_print').click(function() {
//		var prnHtml = $('.printScope').html();
//		
//		$('body').html(prnHtml);
//		window.print(); 
//	});
});