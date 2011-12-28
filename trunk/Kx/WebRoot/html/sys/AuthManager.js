(function($) {
	$.fn.sys_AuthManager = function() {
	var ctrl = {
		content : $(this),
		QueryPanel : {
			Panel : $(this).find('#queryPanel'),
			Group : $(this).find('#group')
		},
		ListTable : $(this).find('#listTable'),
		Toolbar : {
			Save : $(this).find('#btn_save'),
			Undo : $(this).find('#btn_undo')
		}
	}
	
	function init() {
		// 查询框体
		ctrl.QueryPanel.Panel.panel({height:70,style:{'padding':'10px'}});
		// 群组选择框
		ctrl.QueryPanel.Group.combobox({
			onSelect:function(record) {
				ctrl.ListTable.edatagrid('load',{'input.groupid' : record.value});
			}
		});
		// 数据表
		ctrl.ListTable.edatagrid({
			height:ctrl.content.height() - 80,
			style:{'padding':'10px'},
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
		
		// Toolbar
		ctrl.Toolbar.Save.click(function() { ctrl.ListTable.edatagrid('saveRow'); });
		ctrl.Toolbar.Undo.click(function() { ctrl.ListTable.edatagrid('cancelRow'); });
	}
	
	init();
	}
})(jQuery);