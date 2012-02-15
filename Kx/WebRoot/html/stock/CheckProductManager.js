(function($) {
	$.fn.stock_CheckProductManager = function() {
	var ctrl = {
		content : $(this),
		QueryPanel : {
			Panel : $(this).find('#queryPanel'),
			Type1 : $(this).find('#type1'),
			Type2 : $(this).find('#type2'),
			Type3 : $(this).find('#type3'),
			Btn : $(this).find('#btn_product')
		},
		ListTable : $(this).find('#listTable'),
		Toolbar : {
			Stock : $(this).find('#btn_stock')
		},
		ImgDiv : {
			Dialog : $(this).find('#imgDiv'),
			Img : $(this).find('#largeImg')
		},
		CheckDiv : {
			Dialog : $(this).find('#checkDiv'),
			CheckTableDiv1 : $(this).find('#checkTableDiv1'),
			Shop : $(this).find('#shop'),
			ListTable : $(this).find('#checkTable'),
			Reload : $(this).find('#reloadListTable'),
			Add : $(this).find('#check_btn_add'),
			Del : $(this).find('#check_btn_del'),
			Save : $(this).find('#check_btn_save'),
			Undo : $(this).find('#check_btn_undo')
		},
		CountTable : $(this).find('#countTable'),
		CheckImgDiv : {
			Panel : $(this).find('#checkImgDiv'),
			Img : $(this).find('#checkImgPreview')
		},
		CheckCopyButton : {
			CopyToType2 : $('#STOCK_C_CMM_copyToType2'),
			CopyToType3 : $('#STOCK_C_CMM_copyToType3'),
			CopyToProduct : $('#STOCK_C_CMM_copyToProduct')
		}
	}
	
	
	// 核定表单datagrid
	function createCheckTable(pid) {
		ctrl.CheckDiv.ListTable.edatagrid({
			pagination:false,
			width:400,
			height:ctrl.content.height()-100,
			url:"../stock/check!manager.action",
			saveUrl: '../stock/check!add2.action',
			updateUrl: '../stock/check!edit2.action',
			destroyUrl: '../stock/check!delete2.action',
			queryParams:{'input.shopid':ctrl.CheckDiv.Shop.combobox('getValue'),'input.pid':pid},
		
			onAfterEdit:function(){$(this).edatagrid('reload');},
			onDestroy:function(){$(this).edatagrid('reload');},
			
			onSave : function(index,row,data) {
				if(!data.success)
					SOL.showError('服务器产生了一个错误 : ' + data.msg + ' , 请尝试刷新页面并重新提交数据');
			},
			onLoadSuccess : function() {
				var param = {
					'input.shopid' : ctrl.CheckDiv.Shop.combobox('getValue'),
					'input.pid' :ctrl.content.data('stock-check-row').id
				}
				
				ctrl.CountTable.datagrid({width:270,style:{'padding':'0px'},pagination:false,fitColumns:false,rownumbers:false});
				
				var dgData = {total:122,rows:[]};
				$.post('../stock/check!stockCheckSum.action',param,function(data){
					dgData.rows.push({title:data.type1name + ' 已核定',type1:data.shop_stocktype1,type2:data.shop_stocktype2});
					dgData.rows.push({title:'产品已核定',type1:data.shop_product_stocktype1,type2:data.shop_product_stocktype2});
					dgData.rows.push({title:data.type1name + ' 约估克重',type1:Number(data.sum_type1_stocktype1).toFixed(3),type2:Number(data.sum_type1_stocktype2).toFixed(3)});
					dgData.rows.push({title:data.type2name + ' 约估克重',type1:Number(data.sum_type2_stocktype1).toFixed(3),type2:Number(data.sum_type2_stocktype2).toFixed(3)});
					ctrl.CountTable.datagrid('loadData',dgData);
				},'json');
			}
		});
		
		ctrl.CheckDiv.Reload.click(function() {ctrl.CheckDiv.ListTable.edatagrid('reload')});
		ctrl.CheckDiv.Add.click(function() {
			ctrl.CheckDiv.ListTable.edatagrid('addRow',{
				'shopid':ctrl.CheckDiv.Shop.combobox('getValue'),
				'pid':ctrl.content.data('stock-check-row').id
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
		ctrl.content.data('stock-check-row',row);
		var flag = ctrl.CheckDiv.ListTable.data('stock-check-datagrid');
		if(!flag) {
			createCheckTable(row.id);
			ctrl.CheckDiv.ListTable.data('stock-check-datagrid',true);
		} else {
			ctrl.CheckDiv.ListTable.edatagrid('load',{'input.shopid':ctrl.CheckDiv.Shop.combobox('getValue'),'input.pid':row.id});
		}
		// 预览图片
		ctrl.CheckImgDiv.Img.attr('src','pic.action?img=' + row.image)
		// copy功能文字
		ctrl.CheckCopyButton.CopyToType2.find('.menu-text').text('复制到小类-' + row.type2name);
		ctrl.CheckCopyButton.CopyToType3.find('.menu-text').text('复制到货型-' + row.type3name);
		ctrl.CheckDiv.Dialog.dialog('open');
	}
		
	function init() {
		ctrl.ImgDiv.Dialog.dialog();
		
		// 初始化查询框体
		var queryPanelConfig = {};
		$.extend(queryPanelConfig,SOL.defaultConfig.queryPanel.panel,{height:100});
		ctrl.QueryPanel.Panel.panel(queryPanelConfig);
		// 查询框体内控件
		
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
			url : '../info/info-product!manager2.action',
			height : ctrl.content.height() - 110,
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
						var height = $(this).height() > (ctrl.content.height() -100) ? ctrl.content.height() -100 : $(this).height();
						var width = $(this).width() > (ctrl.content.width() -100) ? ctrl.content.width() -100 : $(this).width();
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
		ctrl.CheckDiv.Dialog.dialog({maximizable:true,maximized:true});
		ctrl.CheckDiv.Shop.combobox({
			url:'../info/info-shop!shopCombo.action',
			onSelect : function(record) {
				ctrl.CheckDiv.ListTable.edatagrid('load',{'input.shopid':record.value,'input.pid':ctrl.content.data('stock-check-row').id});
			}
		});
		// 扩展easyui-datagrid
		$.extend($.fn.datagrid.defaults.editors, {   
			soltext: {   
		        init: function(container, options){
		            var input = $('<input type="text" class="datagrid-editable-input" readonly="readonly" datagrid="' + options.datagrid + '">').appendTo(container);   
		            return input;   
		        },   
		        getValue: function(target){   
		            return $(target).val();   
		        },
		        setValue: function(target, value){
		        	var datagrid = $('#' + target.attr('datagrid'));
		        	// 编辑行模式
		        	if(datagrid.data('edatagrid-editmode') == 'edit') {
		        		datagrid.data('minweight',value);
		        		$(target).val(value);
		        		return;
		        	}
		        	
			        if(!value) {
			        	var rows = datagrid.edatagrid('getRows');
	
			        	var minweight;
			        	if(rows.length > 1)
				        	minweight = rows[rows.length - 2].maxweight;
			        	else if(rows[0].maxweight)
			        		minweight = rows[0].maxweight;
			        	else
			        		minweight = 0;
						datagrid.data('minweight',minweight);
			            $(target).val(minweight);
			        } else {
			        	datagrid.data('minweight',value);
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
		            return Number(value) > Number($('#' + param[0]).data('minweight'));   
		        },   
		        message: '输入范围有误'  
		    }   
		});
		
		// 核定框体 图片预览
		ctrl.CheckImgDiv.Panel.panel();
		
		var getCopyUrl = function(clevel) {
			return '../stock/check!copyCheck.action?input.pid=' + ctrl.content.data('stock-check-row').id + 
				   '&input.shopid=' + ctrl.CheckDiv.Shop.combobox('getValue') + '&clevel=' + clevel;
		}
		// 核定数复制功能按钮
		ctrl.CheckCopyButton.CopyToType2.click(function() {
			$.post(getCopyUrl(2),
				function(data) {
					if(data.success)
						SOL.alert('复制成功');
					else
						SOL.alert('复制失败,服务器返回的错误信息:' + data.msg);
				}
			,'json');
		});
		ctrl.CheckCopyButton.CopyToType3.click(function() {
			$.post(getCopyUrl(3),
				function(data) {
					if(data.success)
						SOL.alert('复制成功');
					else
						SOL.alert('复制失败,服务器返回的错误信息:' + data.msg);
				}
			,'json');
		});
	}
	
	init();
	}
})(jQuery);