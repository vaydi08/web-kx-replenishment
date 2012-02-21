(function($) {
	$.fn.info_ShopManager = function() {
		var ctrl = {
			Toolbar : {
				Add : $(this).find('#btn_add'),
				Edit : $(this).find('#btn_edit'),
				Del : $(this).find('#btn_del')
			},
			ListTable : $(this).find('#listTable'),
			Dialog : $(this).find('#dlg'),
			Form : $(this).find('#form'),
			Input : {
				Id : $(this).find('#id'),
				Scode : $(this).find('#scode'),
				Name : $(this).find('#name'),
				Address : $(this).find('#address'),
				Tel : $(this).find('#tel'),
				Shorttel : $(this).find('#shorttel'),
				Remark : $(this).find('#remark')
			}
		}
		
		var url = {
			manager : '../info/info-shop!select.action',
			add : '../info/info-shop!add.action',
			edit : '../info/info-shop!edit.action',
			del : '../info/info-shop!delete.action'
		}
		
		var saveUrl = '';
		
		function onSave() {
			SF.submit(ctrl.Form,{
				url : saveUrl,
				onSubmit:function(){
					return ctrl.Form.form("validate");
				}
			},
			onSuccess)
		}
		function onSuccess() {
			ctrl.ListTable.datagrid('reload');
			ctrl.Dialog.dialog('close');
		}
		
		function init() {
			// 数据表
			var listTableConfig = {
				url:url.manager,
				pagination:false
			}
			SF.grid(ctrl.ListTable,listTableConfig);
			// 对话框
			SF.dialog(ctrl.Dialog,{},onSave);
			
			// 按钮绑定
			ctrl.Toolbar.Add.click(function() {
				ctrl.Form.form('clear');
				ctrl.Dialog.dialog('open');
				saveUrl = url.add;
			});
			ctrl.Toolbar.Del.click(function() {
				SF.confirm(ctrl.ListTable,url.del);
			});
			ctrl.Toolbar.Edit.click(function() {
				var row = ctrl.ListTable.datagrid('getSelected');
				if(row) {
					ctrl.Form.form('load',row);
					ctrl.Dialog.dialog('open');
					saveUrl = url.edit;
				}
			});
		}
		
		init();
	}
})(jQuery);