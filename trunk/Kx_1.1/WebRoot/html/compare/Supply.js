(function($) {
	$.fn.compare_Supply = function() {
		var content = $(this);
		
		var C = function(id) {
			return content.find(id);
		};
		
		var ctrl = {
			MsgPanel : '#panel',
			UploadPanel : '#upload',
			PanelContent : '#panel_content',
			Form : '#form',
			Btn : '#btn_upload'
		};
		
		function initPanel() {
			C(ctrl.MsgPanel).panel();
			C(ctrl.UploadPanel).panel();
		}
		
		function loadExcelProperty() {
			$.post('../compare/compare-excel-property!loadProperty.action',
				{'input.category':'库存表'},
			function(data){
				var rows = data.rows;
				var html = "";
				for(var i = 0; i < rows.length; i ++) {
					html += "<dt>" + rows[i].remark + "</dt>";
					html += "<dd>&nbsp;" + rows[i].col + "&nbsp;</dd>";
				}
				html = "<dl>" + html + "</dl>";
				C(ctrl.MsgPanel).find(ctrl.PanelContent).html(html);
			},'json');
		}
		
		C(ctrl.Btn).click(function() {C(ctrl.Form).submit();});
		
		loadExcelProperty();
		initPanel();
	};
})(jQuery);