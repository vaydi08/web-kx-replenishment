function loadScript(content,param) {
	var ctrl = {
		QueryPanel : {
			Panel : $('#queryPanel'),
			Form : $('#form'),
			Shop : $('#shop'),
			Shopid : $('#shopid'),
			File : $('#uploadStockFile'),
			Btn : $('#btn_add'),
			LoadMsg : $('#loadMsg')
		},
		ListTable : {
			Tabs : $('#tabs'),
			T1 : $('#listTable1'),
			T2 : $('#listTable2')
		}
	}
	
	function init() {
		// 查询面板
		ctrl.QueryPanel.Panel.panel({height:60});
		// form
		ctrl.QueryPanel.Form.form({
			url : '../compare/supply!uploadSupply.action',
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
					loadDatagrid(ctrl.ListTable.T1,ret.stocktype1);
					loadDatagrid(ctrl.ListTable.T2,ret.stocktype2);
				} else {
					SOL.showError(ret.msg);
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
		
		//tabs
		ctrl.ListTable.Tabs.tabs({height : content.height() - 70});
		// 数据表
		function loadDatagrid(dg,data) {
			dg.datagrid('loadData',data);
		}
	}
	init();
}