(function($) {
	$.fn.sys_UserManager = function() {
	var ctrl = {
		content : $(this),
		ListTable : $(this).find('#listTable'),
		Toolbar : {
			Add : $(this).find('#btn_add'),
			Del : $(this).find('#btn_del'),
			Save : $(this).find('#btn_save'),
			Undo : $(this).find('#btn_undo')
		},
		GroupMap : [
			{text:'系统管理组',value:1},
			{text:'管理员组',value:2},
			{text:'操作员组',value:3},
			{text:'访问组',value:4}
	    ]
	};

	function init() {
		// 数据表
		ctrl.ListTable.edatagrid({
			columns:[[
					{field:'username',title:'用户名',width:120,
				        editor:{type:'validatebox',options:{required:true,validType:'length[1,20]'}}
			        },
			        {field:'password',title:'密码',width:80,
			        	formatter:function(value,rowData,rowIndex) {return "********";},
			        	editor:{type:'solpassword',options:{required:true,validType:'length[1,20]'}}
			        },
			        {field:'shorttel',title:'集团短号',width:80,
			        	editor:{type:'validatebox',options:{required:true,validType:'length[1,20]'}}
			        },
			        {field:'groupid',title:'群组',width:80,align:'center',
				        formatter:function(groupid,rowData,rowIndex) {
							for(var key in ctrl.GroupMap) {
								if(ctrl.GroupMap[key].value == groupid)
									return ctrl.GroupMap[key].text;
							}
						},
				        editor:{type:'combobox',options:{required:true,data:ctrl.GroupMap}}
			        }
			    ]],
			    
			height:ctrl.content.height()-10,
			style:{'background-color':'#fafafa','padding':'10px'},
			pagination:false,
			url:"../sys!manager.action",
			saveUrl: '../sys!add.action',
			updateUrl: '../sys!edit.action',
			destroyUrl: '../sys!delete.action',

    		onSave:function(){$(this).edatagrid('reload');},
			onDestroy:function(){$(this).edatagrid('reload');}
		});
		// Btn
		ctrl.Toolbar.Add.click(function() {
			ctrl.ListTable.edatagrid('addRow');
		});
		ctrl.Toolbar.Del.click(function() {
			ctrl.ListTable.edatagrid('destroyRow');
		});
		ctrl.Toolbar.Save.click(function() {
			ctrl.ListTable.edatagrid('saveRow');
		});
		ctrl.Toolbar.Undo.click(function() {
			ctrl.ListTable.edatagrid('cancelRow');
		});
	}
	
	init();
	};
})(jQuery);