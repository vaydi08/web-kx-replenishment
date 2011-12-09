function loadScript(content,param) {
	var ctrl = {
		Toolbar : {
			Add : '#btn_add',
			Edit : '#btn_edit',
			Del : '#btn_del'
		},
		ListTable : '#listTable',
		Dialog : '#dlg',
		Form : '#form',
		Input : {
			Id : '#id',
			Scode : '#scode',
			Name : '#name',
			Address : '#address',
			Tel : '#tel',
			Shorttel : '#shorttel',
			Remark : '#remark'
		}
	}
	
	function onSave() {
		SF.submit($(ctrl.Form),{
			url : '../info/info-shop!add.action',
			onSubmit:function(){
				return $(ctrl.Form).form("validate");
			}
		},
		onSuccess)
	}
	function onSuccess() {
		$(ctrl.ListTable).datagrid('reload');
		$(ctrl.Dialog).dialog('close');
	}
	
	function init() {
		// 数据表
		var listTableConfig = {
			url:"../info/info-shop!manager.action"
		}
		SF.grid($(ctrl.ListTable),listTableConfig);
		// 对话框
		SF.dialog($(ctrl.Dialog),{},onSave);
		
		// 按钮绑定
		$(ctrl.Toolbar.Add).click(function() {
			$(ctrl.Form).form('clear');
			$(ctrl.Dialog).dialog('open');
		});
		$(ctrl.Toolbar.Del).click(function() {
			SF.confirm($(ctrl.ListTable),'../info/info-shop!delete.action');
		});
	}
	
	init();
}