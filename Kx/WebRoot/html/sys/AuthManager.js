function loadScript(content, param) {
	var ctrl = {
		QueryPanel : {
			Panel : $('#queryPanel'),
			Group : $('#group')
		},
		ListTable : $('#listTable'),
		Toolbar : {
			Save : $('#btn_save'),
			Undo : $('#btn_undo')
		}
	}
	
	function init() {
		// 查询框体
		ctrl.QueryPanel.Panel.panel({height:40});
		// 群组选择框
		ctrl.QueryPanel.Group.combobox({
			onSelect:function(record) {
				ctrl.ListTable.edatagrid('load',{'input.groupid' : record.value});
			}
		});
		// 数据表
		ctrl.ListTable.edatagrid({
			height:content.height() - 50,
			url:'../sys/auth!manager2.action',
			queryParams : {'input.groupid' : 2},
			saveUrl: '',
			updateUrl: '../sys/auth!edit2.action',
			destroyUrl: '',
			fitColumns:true,
			singleSelect:true,
			columns:[[
	            {field:'description',title:'名称',width:100},   
	            {field:'uri',title:'访问路径',width:100},   
	            {field:'status',title:'许可状态',width:100,
	            	formatter:function(value,rowData,rowIndex) {return value == "1" ? "允许" : "不允许";},
	            	editor:{type:'checkbox',options:{on:'1',off:'0'}}}   
			]],
			
			onAfterEdit:function(){$(this).edatagrid('reload');},
			onDestroy:function(){$(this).edatagrid('reload');}
		});
		
	}
	
	init();
}