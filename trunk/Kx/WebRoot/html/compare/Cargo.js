function loadScript(content,param) {
	var ctrl = {
		QueryPanel : {
			Panel : $('#queryPanel'),
			Form : $('#form'),
			Minallot : $('#minallot'),
			File : {
				Supply : $('#supplyFile'),
				Sale : $('#saleFile'),
				Stock : $('#stockFile')
			},
			Btn : $('#btn_add'),
			LoadMsg : $('#loadMsg')
		},
		ListTable : {
			Tabs : $('#tabs'),
			T1_Result : $('#stocktype1_result'),
			T1_Stock : $('#stocktype1_stock'),
			T2_Result : $('#stocktype2_result'),
			T2_Stock : $('#stocktype2_stock')
		}
	}
	
	function init() {
		// 查询面板
		ctrl.QueryPanel.Panel.panel({height:60});
		// form
		ctrl.QueryPanel.Form.form({
			url : '../compare/cargo!uploadCargo.action',
			onSubmit : function() {
				if(ctrl.QueryPanel.File.Supply.val() == '' ||
				   ctrl.QueryPanel.File.Sale.val() == '' ||
				   ctrl.QueryPanel.File.Stock.val() == '') {
					SOL.showWarning("未选择上传文件,请选择文件");
					ctrl.QueryPanel.LoadMsg.hide();
					return false;
				}
			},
			success : function(data) {
				ctrl.QueryPanel.LoadMsg.hide();
				var ret = $.parseJSON(data);
				if(ret.success) {
					loadDatagrid(ctrl.ListTable.T1_Result,ret.stocktype1.result);
					loadDatagrid(ctrl.ListTable.T1_Stock,ret.stocktype1.stock);
					loadDatagrid(ctrl.ListTable.T2_Result,ret.stocktype2.result);
					loadDatagrid(ctrl.ListTable.T2_Stock,ret.stocktype2.stock);
				} else {
					SOL.showError(ret.msg);
				}
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