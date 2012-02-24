(function($) {
	$.fn.compare_Supply = function() {
	var ctrl = {
		QueryPanel : {
			Panel : $(this).find('#queryPanel'),
			Form : $(this).find('#form'),
			Shop : $(this).find('#shop'),
			Shopid : $(this).find('#shopid'),
			File : $(this).find('#uploadStockFile'),
			Btn : $(this).find('#btn_add'),
			LoadMsg : $(this).find('#loadMsg')
		},
		ListTable : $(this).find('#listTable'),
		Btn_Download : $(this).find('#btn_download')
	};
	
	function init() {
		SF.grid(ctrl.ListTable,{});
		
		// 查询面板
		ctrl.QueryPanel.Panel.panel({height:70,style:{'padding':'10px'}});
		// form
		ctrl.QueryPanel.Form.form({
			url : '../compare/compare!uploadSupply.action',
			onSubmit : function() {
				if(ctrl.QueryPanel.File.val() == "") {
					SOL.showWarning("未选择上传文件,请选择一个库存文件");
					ctrl.QueryPanel.LoadMsg.hide();
					return false;
				}
			},
			success : function(data) {
				ctrl.QueryPanel.LoadMsg.hide();
				var ret = $.parseJSON(data);
				if(ret.success) {
					ctrl.ListTable.datagrid('loadData',ret);
					ctrl.Btn_Download.linkbutton('enable');
				} else {
					SOL.showError(ret.msg);
					ctrl.Btn_Download.linkbutton('disable');
				}
			}
		});
		// shop
		ctrl.QueryPanel.Shop.combobox({
			url:'../info/info-shop!shopCombo.action',
			onSelect : function(record) {
				ctrl.QueryPanel.Shopid.val($(this).combobox('getValue'));
			}
		});
		// btn
		ctrl.QueryPanel.Btn.click(function() {
			ctrl.QueryPanel.LoadMsg.show();
			ctrl.QueryPanel.Form.submit();
		});
	}
	init();
	};
})(jQuery);