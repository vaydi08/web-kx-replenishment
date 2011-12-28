(function($) {
	$.fn.compare_Cargo = function() {
	var ctrl = {
		content : $(this),
		QueryPanel : {
			Panel : $(this).find('#queryPanel'),
			Form : $(this).find('#form'),
			Minallot : $(this).find('#minallot'),
			File : {
				Supply : $(this).find('#supplyFile'),
				Sale : $(this).find('#saleFile'),
				Stock : $(this).find('#stockFile')
			},
			Btn : $(this).find('#btn_add'),
			LoadMsg : $(this).find('#loadMsg')
		},
		ListTable : {
			Tabs : $(this).find('#tabs'),
			T1_Result : $(this).find('#stocktype1_result'),
			T1_Stock : $(this).find('#stocktype1_stock'),
			T2_Result : $(this).find('#stocktype2_result'),
			T2_Stock : $(this).find('#stocktype2_stock')
		}
	}
	
	function init() {
		
		// 查询面板
		ctrl.QueryPanel.Panel.panel({height:70,style:{'padding':'10px'}});
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
		ctrl.ListTable.Tabs.tabs({height : ctrl.content.height() - 80});
		// 数据表
		function loadDatagrid(dg,data) {
			dg.datagrid('loadData',data);
		}
		
		// datagrid
		ctrl.ListTable.T1_Result.datagrid({height:ctrl.ListTable.Tabs.height() - 55});
		ctrl.ListTable.T1_Stock.datagrid({height:ctrl.ListTable.Tabs.height() - 55});
		ctrl.ListTable.T2_Result.datagrid({height:ctrl.ListTable.Tabs.height() - 55});
		ctrl.ListTable.T2_Stock.datagrid({height:ctrl.ListTable.Tabs.height() - 55});
	}
	init();
	}
})(jQuery);