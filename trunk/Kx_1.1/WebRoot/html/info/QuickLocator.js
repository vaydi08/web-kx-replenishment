(function($) {
	$.fn.info_QuickLocator = function() {
		var ctrl = {
			content : $(this),
			QueryPanel : {
				Panel : $(this).find('#queryPanel'),
				Search : $(this).find('#INFO_Q_queryType')
			},
			Locator : {
				Panel : $(this).find('#locator'),
				Type1 : $(this).find('#type1'),
				Type2 : $(this).find('#type2'),
				Type3 : $(this).find('#type3'),
				Type4 : $(this).find('#type4'),
				Type4Str : $(this).find('#type4Str'),
				
				CategoryType4Div : $(this).find('#categoryType4Div'),
				CategoryType4Select : $(this).find('#categoryType4Select'),
				TypeSelect : $(this).find('#typeSelect')
			},
			ImgPanel : {
				Panel : $(this).find('#imgPanel'),
				Search : $(this).find('#img_queryType'),
				Div : $(this).find('#imgPanelDiv'),
				Select : $(this).find('#imgPanelSelect')
			},
			Stock : {
				Dialog : $(this).find('#stock'),
				ListTable : $(this).find('#stockTable')
			},
			ListTable : $(this).find('#listTable'),
			Dialog : $(this).find('#dlg'),
			Form : $(this).find('#form'),
			Pic : {
				Startcap : $(this).find('#startcam'),
				picType : $(this).find('input[name=picType]:radio:first'),
				picData : '#picData',
				picFile : '#picFile'
			},
			Input : {
				Clevel : $(this).find('#clevel'),
				Parent :$(this).find('#parent'),
				Ccode : $(this).find('#ccode'),
				CodeMsg : $(this).find('#codeMsg'),
				Cname : $(this).find('#cname')
			}
		}

		function onSave() {
			SF.submit(ctrl.Form,{
				url : '../info/info-category!add.action',
				onSubmit:function(){
					if(ctrl.content.find('input[name=picType]:checked').val() == "webcap" && ctrl.content.find(ctrl.Pic.picData).val() == "") {
						SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
						return false;
					}
					if(ctrl.content.find('input[name=picType]:checked').val() == "file" && ctrl.content.find(ctrl.Pic.picFile).val() == "") {
						SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
						return false;
					}
					
					if(ctrl.Input.CodeMsg.text() != '') {
						SOL.showWarning('有重复记录,请确认');
						return false;
					}
	
					return ctrl.Form.form("validate");
				}
			},
			onSuccess);
		}
		function onSuccess() {
			ctrl.ListTable.datagrid('reload');
			ctrl.Dialog.dialog('close');
			ctrl.Locator.CategoryType4Div.dialog('close');
			ctrl.Locator.Type3.combobox('select',ctrl.Locator.Type3.combobox('getValue'));
		}
		
		function init() {
			ctrl.Stock.Dialog.dialog();
			ctrl.Locator.CategoryType4Div.dialog();
			ctrl.ImgPanel.Div.dialog();
			
			// 初始化查询框体
			SF.queryPanel(ctrl.QueryPanel.Panel,{},ctrl.QueryPanel.Search,{
				getparam:function(value,name){
					return {"queryType":name,"queryValue":(value == null?"":value)};
				}
			},ctrl.ListTable);
			// 快速定位框体
			ctrl.Locator.Panel.panel({height:100,style:{'padding':'10px','background-color':'#fafafa'}});
			// 数据表
			var listTableConfig = {
				url:"../info/info-product!manager.action",
				height:ctrl.content.height() - 300,
				queryParams : {'input.type1' : -1},
				columns:[[
				        {field:'type',title:'类别',width:120,
					        formatter:function(value,rec) {
								return rec.type1name + "-" + rec.type2name + "-" + rec.type3name + "-" + rec.type4name;
				        }},
				        {field:'pname',title:'产品名称',width:80},
				        {field:'pcode',title:'产品代码',width:80},
				        {field:'unit',title:'单位',width:40}
				    ]],
			    onClickRow : function(index,row) {
			    	SOL.viewImg('../image.jsp?img=' + row.image);
			    },
			    onLoadSuccess : function() {
			    	var flag = ctrl.content.data('info-quick-stock');
			    	if(!flag) {
				    	// 核定数量框体
						ctrl.Stock.ListTable.treegrid({
							url:"../info/info-product!quickLocatorStock.action",
							style:{'background-color':'#fafafa'},
							queryParams:{pid:-1},
							rownumbers:true,
							treeField:"shopname",
							idField:"treeid",
							fitColumns:true
						});
						ctrl.content.data('info-quick-stock',true);
			    	}
			    },
			    onDblClickRow : function(index,row) {
			    	$.post('../info/info-product!quickLocatorStock.action',{pid:row.id},function(data) {
			    		ctrl.Stock.ListTable.treegrid('loadData',data);
			    	},'json');

			    	ctrl.Stock.Dialog.dialog('open');
			    }
			}
			SF.grid(ctrl.ListTable,listTableConfig);
			// 对话框
			SF.dialog(ctrl.Dialog,{
				width:480,
				onOpen:function(){
					ctrl.Form.form('clear');
					ctrl.Pic.picType.click();
					ctrl.Input.Clevel.val(4);
					ctrl.Input.Parent.combobox('select',ctrl.Locator.Type3.combobox('getValue'));
				}
			},onSave);
			// 对话框内控件 父类别
			ctrl.Input.Parent.combobox({url:'../info/info-category!newTypeCombo.action?input.clevel=3',required:true});
			// 产品代码
			ctrl.Input.Ccode.change(function() {
				$.post('../info/info-category!checkCode.action',{'input.ccode':$(this).val(),'input.clevel':4,'input.parent':ctrl.Input.Parent.combobox('getValue')},function(data){
					var result = eval('(' + data + ')');
					if(result.success) {
						ctrl.Input.CodeMsg.text('');
					} else {
						ctrl.Input.CodeMsg.text(result.msg);
					}
				});
			});
			// 打开摄像头对话框
			ctrl.Pic.Startcap.click(function() {
				SF.openCap(function(result){
					ctrl.content.find(ctrl.Pic.picData).val(result);
				});
			});
			
			//查询面板下拉菜单
			ctrl.Locator.Type1.combobox({
				url:"../info/info-category!typeQueryCombo.action?input.clevel=1&input.parent=0",
				width:100,
				editable:false,
				onSelect:function(row) {
					if(row.value > 0)
						ctrl.Locator.Type2.combobox('reload',"../info/info-category!typeQueryCombo.action?input.clevel=2&input.parent=" + row.value);
				}
			});
			ctrl.Locator.Type2.combobox({
				width:100,
				editable:false,
				onSelect:function(row) {
					if(row.value > 0)
						ctrl.Locator.Type3.combobox('reload',"../info/info-category!typeQueryCombo.action?input.clevel=3&input.parent=" + row.value);
				}
			});
			ctrl.Locator.Type3.combobox({
				width:100,
				editable:false,
				onSelect : function(row) {
					if(row.value > 0) {
						ctrl.Locator.CategoryType4Select.categoryType4Select({
							'url':'../info/info-product!select.action',
					     	'queryParams':{
					     		'input.type1':ctrl.Locator.Type1.combobox('getValue'),
					     		'input.type2':ctrl.Locator.Type2.combobox('getValue'),
					     		'input.type3':ctrl.Locator.Type3.combobox('getValue')
					     	},
					     	'newType4':true,
					     	onClick : function(value,html,id) {
					     		ctrl.Locator.CategoryType4Div.dialog('close');
		
					     		ctrl.ListTable.datagrid('load',{'input.pcode':value});

					     		ctrl.Locator.Type4Str.html(html.replace('<BR>','&nbsp;&nbsp;'));
						     },
						     onNewType4 : function() {
						     	ctrl.Dialog.dialog('open');
						     }
						});
		
						ctrl.Locator.CategoryType4Div.dialog('open');
					}
				}
			});
			// 快速图片检索展示
			ctrl.ImgPanel.Panel.panel({height:100,style:{'padding':'10px','background-color':'#fafafa'}});
			ctrl.ImgPanel.Search.searchbox({
				searcher:function(value,name) {
					if(value != null && value != "") {
						ctrl.ImgPanel.Select.categoryType4Select({
							'url':'../info/info-product!select.action',
					     	'queryParams':{
					     		'input.pcode':value
					     	},
					     	'newType4':false
						});
		
						ctrl.ImgPanel.Div.dialog('open');
					}
				}
			});
			
		}
		
		init();
		
		webCap.initCap();
	}
})(jQuery);