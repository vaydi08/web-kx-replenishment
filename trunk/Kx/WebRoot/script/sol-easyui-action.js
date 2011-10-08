// 表单的url
var form_url;

// 预定义函数
// 添加框函数
function onAddBtn(addUrl) {
	form_url = addUrl;
	$("#dlg").dialog('open');
	$("#form").form('clear');
}
function onEditBtn(editUrl) {
	var row = $("#listTable").datagrid('getSelected');
	if (row){
		form_url = editUrl,
		$("#dlg").dialog('open');
		$("#form").form('load',row);
	}
}
function onRemoveBtn(removeUrl) {
	var row = $("#listTable").datagrid('getSelected');
	if(row) {
		$.messager.confirm('确认删除','将要删除此记录,删除操作不可恢复,请确认?',function(b){
			if(b) {
				$.post(removeUrl,{"input.id":row.id},function(data){
					var result = eval('(' + data + ')');
					if(result.success)
						$("#listTable").datagrid('reload');
					else
						$.messager.show({title:"Error",msg:result.msg});
				});
			}
		})
	}
}
(function($) {
// 扩展
// 查询面板
jQuery.fn.queryPanel = function(config) {
	// 调用者自身
	var me = $(this);
	
	// 点击查询按钮的动作
	var onQuery = function(value,name) {
		$(p.datagrid).datagrid('load',p.getparam(value));
	}
	
	// 默认参数
	var p = $.extend({
		title:"查询条件",
		iconCls:"icon-search",
		collapsible:"true",
				
		searchbox:"#EU_queryType",
		searcher:onQuery,
		getparam:function(value){
					var queryType = $("#queryType").val();
					return {"queryType":queryType,"queryValue":(value == null?"":value)};
				},
		prompt:"请输入查询项,留空表示查询全部",
		menu:"#EU_queryValue",
		width:800,
		height:80,
		
		datagrid:"#listTable"
		
	},config);
	
	// 生成面板
	me.panel(p);
	
	// 生成searchbox
	p.width = 300;
	$(p.searchbox).searchbox(p);
}
})(jQuery);

// 数据表
(function($) {
jQuery.fn.grid = function(config) {
	// 调用者自身
	var me = $(this);

	// 解释工具栏
	var getToolbar = function() {
		var tools = config.tools;
		var out = new Array();
		for(i = 0; i < tools.length; i=i+1) {
			if(tools[i].type == "add") {
				out.push({
					id:'btnadd',
					text:tools[i].text,
					iconCls:'icon-add',
					handler:function(){
						form_url = config.addUrl;
						$("#dlg").dialog('open');
						$("#form").form('clear');
					}});
			}
			if(tools[i].type == "edit") {
				out.push({
					id:'btnedit',
					text:tools[i].text,
					iconCls:'icon-edit',
					handler:function(){
						var row = $("#listTable").datagrid('getSelected');
						if (row){
							form_url = config.editUrl,
							$("#dlg").dialog('open');
							$("#form").form('load',row);
						}
					}});
			}
			if(tools[i].type == "remove") {
				out.push({
					id:'btnremove',
					text:tools[i].text,
					iconCls:'icon-remove',
					handler:function(){}});
			}
			if(tools[i].type == "-")
				out.push('-');
			if(tools[i].type == "other") {
				out.push({
					id:tools[i].id,
					text:tools[i].text,
					iconCls:tools[i].iconCls,
					handler:tools[i].handler});
			}
		}
		return out;
	}
	
	// 默认参数
	var p = $.extend({
		datagrid:"#listTable",
		dlg:"#dlg",
		form:"#form",
		title:"",
		url:"",
		frozenColumns:(config.multi ? [[{field:'ck',checkbox:true}]] : null),
		toolbar:(config.tools != null ?getToolbar() : config.toolbar),
		
		width:800,
		height:$("#right").height()-200,
		iconCls:"icon-save",
		nowrap:true,
		singleSelect:(config.multi ? false : true),
		striped:true,
		pagination:true,
		rownumbers:true,
		fitColumns:true
	},config);

	// 生成datagrid
	me.datagrid(p);
}
})(jQuery);

// 下拉列表
(function($) {
	jQuery.fn.mycombo = function(config) {
		// 调用者自身
		var me = $(this);
		
		// 默认参数
		var p = $.extend({
			url:"",
			
			textField:"text",
			valueField:"value",
			width:100,
			editable:false
		},config);
		
		// 生成datagrid
		me.combobox(p);
	}
})(jQuery);

// 增删改查 表单
(function($) {
	jQuery.fn.crud = function(config) {
		// 调用者自身
		var me = $(this);
		
		var save = function() {
			$(p.form).form('submit',{
				url: form_url,
				onSubmit: function(){
					return p.onSubmit();
				},
				success: function(result){
					return p.onSuccess(result);
				}
			});
		}
		
		var onSubmit = function() {
			return $(p.form).form('validate');
		}
		var onSuccess = function(result) {
			var result = eval('('+result+')');
			if (result.success){
				me.dialog('close');		// close the dialog
				$(p.datagrid).datagrid('reload');	// reload the user data
			} else {
				$.messager.show({
					title: 'Error',
					msg: result.msg
				});
			}
		}
		var cancel = function() {
			$(p.dlg).dialog('close');
		}
		
		var remove = function() {
			alert(3);
		}
		// 默认参数
		var p = $.extend({
			removeUrl:"",
			datagrid:"#listTable",
			dlg:"#dlg",
			form:"#form",
			
			title:"Dialog",
			width:600,
			height:300,
			left:100,
			top:100,
			closed:true,
			//buttons:["#dlg-buttons"],
			
			saveBtn:"#dlg-save",
			cancelBtn:"#dlg-cancel",
			
			onSubmit:onSubmit,
			onSuccess:onSuccess
		},config);
		
		if(p.form != "") {
						
			// 保存按钮
			$(p.saveBtn).bind("click",save);
			// 取消按钮
			$(p.cancelBtn).bind("click",cancel);

		}
		
		me.dialog(p);
	}
})(jQuery);

