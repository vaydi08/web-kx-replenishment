function loadScript(content,param) {
	var ctrl = {
		QueryPanel : {
			Panel : $('#queryPanel'),
			Stocktype : $('#stocktype'),
			Shop : $('#shop'),
			Type1 : $('#type1'),
			Type2 : $('#type2'),
			Type3 : $('#type3'),
			Btn : $('#btn_product')
		},
		ListTable : $('#listTable'),
		Toolbar : {
			Stock : $('#btn_stock')
		},
		ImgDiv : {
			Dialog : $('#imgDiv'),
			Img : $('#largeImg')
		},
		CheckDiv : {
			Dialog : $('#checkDiv'),
			ListTable : $('#checkTable'),
			Add : $('#check_btn_add'),
			Del : $('#check_btn_del'),
			Save : $('#check_btn_save'),
			Undo : $('#check_btn_undo')
		}
	}
	
	
	// 核定表单datagrid
	function createCheckTable(pid) {
		ctrl.CheckDiv.ListTable.edatagrid({
			//title:"核定列表",
			noheader:true,
			//height:300,
			style:{'background-color':'#fff'},
			url:"../stock/check!manager.action",
			saveUrl: '../stock/check!add2.action',
			updateUrl: '../stock/check!edit2.action',
			destroyUrl: '../stock/check!delete2.action',
			queryParams:{'input.shopid':ctrl.QueryPanel.Shop.combobox('getValue'),'input.pid':pid,'input.stocktype':ctrl.QueryPanel.Stocktype.combobox('getValue')},
		
			onSave:function(){ctrl.CheckDiv.ListTable.edatagrid('reload');},
			onDestroy:function(){ctrl.CheckDiv.ListTable.edatagrid('reload');}
		});
		
		ctrl.CheckDiv.Add.click(function() {
			ctrl.CheckDiv.ListTable.edatagrid('addRow',{
				'shopid':ctrl.QueryPanel.Shop.combobox('getValue'),
				'pid':ctrl.CheckDiv.ListTable.edatagrid('options').queryParams['input.pid'],
				'stocktype':ctrl.QueryPanel.Stocktype.combobox('getValue')
			});
		});
		ctrl.CheckDiv.Del.click(function() {
			ctrl.CheckDiv.ListTable.edatagrid('destroyRow');
		});
		ctrl.CheckDiv.Save.click(function() {
			ctrl.CheckDiv.ListTable.edatagrid('saveRow');
		});
		ctrl.CheckDiv.Undo.click(function() {
			ctrl.CheckDiv.ListTable.edatagrid('cancelRow');
		});
	}
		
	// 核定数据
	function stockCheck(row) {
		var flag = content.data('stock-check-datagrid');
		if(!flag) {
			createCheckTable(row.id);
			content.data('stock-check-datagrid',true);
		} else
			ctrl.CheckDiv.ListTable.edatagrid('load',{'input.shopid':ctrl.QueryPanel.Shop.combobox('getValue'),'input.pid':row.id,'input.stocktype':ctrl.QueryPanel.Stocktype.combobox('getValue')});
		ctrl.CheckDiv.Dialog.dialog('open');
	}
		
	function init() {
		// 初始化查询框体
		var queryPanelConfig = {};
		$.extend(queryPanelConfig,SOL.defaultConfig.queryPanel.panel,{height:100});
		ctrl.QueryPanel.Panel.panel(queryPanelConfig);
		// 查询框体内控件
		ctrl.QueryPanel.Stocktype.combobox({width:100});
		ctrl.QueryPanel.Shop.combobox({url:'../info/info-shop!shopCombo.action'});
		ctrl.QueryPanel.Type1.combobox({
			url:"../info/info-category!combobox.action?clevel=1&parent=0",
			width:100,
			onSelect:function(row) {
				if(row.value > 0)
					ctrl.QueryPanel.Type2.combobox('reload',"../info/info-category!combobox.action?clevel=2&parent=" + row.value);
				else {
					ctrl.QueryPanel.Type2.combobox('clear');
					ctrl.QueryPanel.Type3.combobox('clear');
				}
			}
		});
		ctrl.QueryPanel.Type2.combobox({
			width:100,
			onSelect:function(row) {
				if(row.value > 0)
					ctrl.QueryPanel.Type3.combobox('reload',"../info/info-category!combobox.action?clevel=3&parent=" + row.value);
				else
					ctrl.QueryPanel.Type3.combobox('clear');
			}
		});
		ctrl.QueryPanel.Type3.combobox({width:100});
		// 数据表
		var listTableConfig = {
			//url:"../stock/check!productList.action",
			url : '../info/info-product!manager2.action',
			columns:[[
					{field:'type',title:'类别',width:120,
				        formatter:function(value,rec) {
							return rec.type1name + "-" + rec.type2name + "-" + rec.type3name + "-" + rec.type4name;
			        }},
			        {field:'pname',title:'产品名称',width:80},
			        {field:'pcode',title:'产品代码',width:80},
			        {field:'image',title:'产品图片',width:80,align:'center',
				        formatter:function(value,row,index){
							return '<img src="../pic.action?img=' + value + '" height="50"/>';
				        },
				        styler:function(value,row,index){return 'cursor:pointer';}}
			    ]],
		    onClickCell:function(index,field,value) {
				if(field == "image") {
					ctrl.ImgDiv.Img.attr("src","../pic.action?img=" + value);
					ctrl.ImgDiv.Img.load(function(){
						var height = $(this).height() > (content.height() -100) ? content.height() -100 : $(this).height();
						var width = $(this).width() > (content.width() -100) ? content.width() -100 : $(this).width();
						height = height < 300 ? 300 : height;
						width = width < 300 ? 300 : width;
						ctrl.ImgDiv.Dialog.dialog('resize',{height:height,width:width,left:150,top:50});
						ctrl.ImgDiv.Dialog.dialog('open');
					});
				}
			},
			onDblClickRow : function(index,row) {
				stockCheck(row);
			}
		}
		SF.grid(ctrl.ListTable,listTableConfig);
		// 核定按钮
		ctrl.Toolbar.Stock.click(function(){
			var row = ctrl.ListTable.datagrid('getSelected');
			if(row)
				stockCheck(row);
		});
		// 检索按钮
		ctrl.QueryPanel.Btn.click(function() {
			var type1 = ctrl.QueryPanel.Type1.combobox('getValue');
			var type2 = ctrl.QueryPanel.Type2.combobox('getValue');
			var type3 = ctrl.QueryPanel.Type3.combobox('getValue');
			
			var param = {};
			if(type1 && type1 > 0)
				param['input.type1'] = type1;
			if(type2 && type2 > 0)
				param['input.type2'] = type2;
			if(type3 && type3 > 0)
				param['input.type3'] = type3;
			
			ctrl.ListTable.datagrid('load',param);
		});
		
		// 核定部分
		// 扩展easyui-datagrid
		$.extend($.fn.datagrid.defaults.editors, {   
			soltext: {   
		        init: function(container, options){   
		            var input = $('<input type="text" class="datagrid-editable-input" readonly="readonly">').appendTo(container);   
		            return input;   
		        },   
		        getValue: function(target){   
		            return $(target).val();   
		        },
		        setValue: function(target, value){
			        if(!value) {
			        	var rows = $(ctrl.CheckDiv.ListTable).edatagrid('getRows');
	
			        	var minweight;
			        	if(rows.length > 1)
				        	minweight = rows[rows.length - 2].maxweight;
			        	else if(rows[0].maxweight)
			        		minweight = rows[0].maxweight;
			        	else
			        		minweight = 0;
						ctrl.CheckDiv.ListTable.data('minweight',minweight);
			            $(target).val(minweight);
			        } else {
			        	ctrl.CheckDiv.ListTable.data('minweight',value);
			        	$(target).val(value);
			        }
		        },   
		        resize: function(target, width){   
		            var input = $(target);   
		            if ($.boxModel == true){   
		                input.width(width - (input.outerWidth() - input.width()));   
		            } else {   
		                input.width(width);   
		            }   
		        }   
		    }   
		});
		$.extend($.fn.validatebox.defaults.rules, {   
		    compareWeight: {   
		        validator: function(value, param){
		            return Number(value) > Number(ctrl.CheckDiv.ListTable.data('minweight'));   
		        },   
		        message: '输入范围有误'  
		    }   
		});
		
	}
	
	init();
}