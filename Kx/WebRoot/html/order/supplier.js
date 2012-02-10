(function($) {
	$.fn.order_supplier = function() {
		var content = $(this);
		var C = function(name) {
			return content.find(name);
		}
		var ctrl = {
			ListTable : '#listTable',
			Btn : {
				Add : '#btn_add',
				Del : '#btn_del',
				Save : '#btn_save',
				Cancel : '#btn_cancel'
			}
		}
	
		C(ctrl.ListTable).edatagrid({
			title:"供应商列表",
			pagination:false,
			height:content.height() - 50,
			url:"../order/supplier!manager2.action",
			saveUrl: '../order/supplier!add2.action',
			updateUrl: '../order/supplier!edit2.action',
			destroyUrl: '../order/supplier!delete2.action',
	
			columns:[[   
		        {field:'name',title:'供应商',width:100,
		        	editor:{type:'validatebox',options:{required:true,validType:'length[1,50]'}}},   
		        {field:'contact',title:'联系人',width:100,
		        	editor:{type:'validatebox',options:{required:true,validType:'length[1,20]'}}},   
		        {field:'remark',title:'备注',width:100,
		        	editor:{type:'validatebox',options:{required:true,validType:'length[1,50]'}}}   
		    ]],
		    
		    toolbar:'#ORDER_S_toolbar',
			onAfterEdit:function(){$(this).edatagrid('reload');},
			onDestroy:function(){$(this).edatagrid('reload');}
		});
		
		C(ctrl.Btn.Add).click(function() {
			C(ctrl.ListTable).edatagrid('addRow');
		});
		
		C(ctrl.Btn.Del).click(function() {
			C(ctrl.ListTable).edatagrid('destroyRow');
		});
		
		C(ctrl.Btn.Save).click(function() {
			C(ctrl.ListTable).edatagrid('saveRow');
		});
		
		C(ctrl.Btn.Cancel).click(function() {
			C(ctrl.ListTable).edatagrid('cancelRow');
		});
	}
})(jQuery);