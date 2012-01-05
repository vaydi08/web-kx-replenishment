(function($) {
	$.fn.info_CategoryManager = function() {

		var ctrl = {
			content : $(this),
			QueryPanel : {
				Panel : $(this).find('#queryPanel'),
				Search : $(this).find('#INFO_C_queryType'),
				Type1 : $(this).find('#type1'),
				Type2 : $(this).find('#type2'),
				Type3 : $(this).find('#type3')
			},
			ListTable : $(this).find('#listTable'),
			Toolbar : {
				Add : $(this).find('#btn_add'),
				Edit : $(this).find('#btn_edit'),
				Del : $(this).find('#btn_del')
			},
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
				Parent : $(this).find('#parent'),
				Ccode : $(this).find('#ccode'),
				CodeMsg : $(this).find('#codeMsg'),
				Cname : $(this).find('#cname'),
				CapTable : $(this).find('#INFO_C_capTable')
			}
		}
		
		// From提交情况
		function onSave() {
			SF.submit(ctrl.Form,{
				url : '../info/info-category!add.action',
				onSubmit:function(){
					if(ctrl.Input.Clevel.combobox('getValue') == 4) {
						if(ctrl.content.find('input[name=picType]:checked').val() == "webcap" && ctrl.content.find(ctrl.Pic.picData).val() == "") {
							SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
							return false;
						}
						if(ctrl.content.find('input[name=picType]:checked').val() == "file" && ctrl.content.find(ctrl.Pic.picFile).val() == "") {
							SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
							return false;
						}
					}
	
					if(ctrl.Input.CodeMsg.text() != '') {
						SOL.showWarning('有重复记录,请确认');
						return false;
					}
	
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
			// 初始化查询框体
			SF.queryPanel(ctrl.QueryPanel.Panel,{},ctrl.QueryPanel.Search,{
				getparam:function(value,name){
					var type1 = ctrl.QueryPanel.Type1.combobox('getValue');
					var type2 = ctrl.QueryPanel.Type2.combobox('getValue');
					var type3 = ctrl.QueryPanel.Type3.combobox('getValue');
	
					return {"queryType":name,"queryValue":(value == null?"":value),"type1":type1,"type2":type2,"type3":type3};
				}
			},ctrl.ListTable);
			//查询面板下拉菜单
			ctrl.QueryPanel.Type1.combobox({
				url:"../info/info-category!combobox.action?clevel=1&parent=0",
				width:100,
				editable:false,
				onSelect:function(row) {
					if(row.value > 0)
						ctrl.QueryPanel.Type2.combobox('reload',"../info/info-category!combobox.action?clevel=2&parent=" + row.value);
				}
			});
			ctrl.QueryPanel.Type2.combobox({
				width:100,
				editable:false,
				onSelect:function(row) {
					if(row.value > 0)
						ctrl.QueryPanel.Type3.combobox('reload',"../info/info-category!combobox.action?clevel=3&parent=" + row.value);
				}
			});
			ctrl.QueryPanel.Type3.combobox({
				width:100,
				editable:false
			});
			// 数据表
			var listTableConfig = {
				title:"分类列表",
				url:"../info/info-category!manager.action",
				columns:[[
			        {field:'ccode',title:'类别代码',width:80},
			        {field:'cname',title:'类别名称',width:80},
			        {field:'clevel',title:'类别',width:80,
			        	formatter:function(value,rec){
						if(value == 1)
							return '<span>大类</span>';
						else if(value == 2)
							return '<span>小类</span>';
						else if(value == 3)
							return '<span>货型</span>';
						else if(value == 4)
							return '<span>款式</span>';
				        }
			        }
			    ]],
			    onClickRow : function(index,row) {
			    	if(row.clevel == 4) 
			    		SOL.viewImg('../image.jsp?img=' + row.image);
			    	else
			    		SOL.hideView();
			    }
			}
			SF.grid(ctrl.ListTable,listTableConfig);
			// 对话框
			SF.dialog(ctrl.Dialog,{
				width:480,
				onOpen:function(){
					ctrl.Pic.picType.click();
					ctrl.Input.Clevel.combobox('select',1);
				}
			},onSave);
			
			// 对话框内控件 父类别
			ctrl.Input.Parent.combobox({url:'../info/info-category!combobox1.action?clevel=0'});
			// 对话框内控件 类别
			ctrl.Input.Clevel.combobox({
				onSelect : function() {
					var clevel = ctrl.Input.Clevel.combobox('getValue');
					if(clevel > 1) {
						ctrl.Input.Parent.combobox('reload','../info/info-category!combobox1.action?clevel=' + (clevel-1));
					} else {
						ctrl.Input.Parent.combobox('reload','../info/info-category!combobox1.action?clevel=0');
						ctrl.Input.Parent.combobox('clear');
					}
		
					if(clevel == 4)
						ctrl.Input.CapTable.show();
					else
						ctrl.Input.CapTable.hide();
				}
			});
			// 产品代码
			ctrl.Input.Ccode.change(function() {
				$.post('../info/info-category!checkCode.action',{'input.ccode':$(this).val(),'input.clevel':ctrl.Input.Clevel.combobox('getValue'),'input.parent':ctrl.Input.Parent.combobox('getValue')},function(data){
					var result = eval('(' + data + ')');
					if(result.success) {
						ctrl.Input.CodeMsg.text('');
					} else {
						ctrl.Input.CodeMsg.text(result.msg);
					}
				});
			});
		}
		
		init();
		
		webCap.initCap();
		
		// 按钮绑定
		ctrl.Toolbar.Add.click(function() {
			ctrl.Form.form('clear');
			ctrl.Dialog.dialog('open');
		});
		ctrl.Toolbar.Del.click(function() {
			SF.confirm(ctrl.ListTable,'../info/info-category!delete2.action');
		});
		ctrl.Toolbar.Edit.click(function() {
			var row = ctrl.ListTable.datagrid('getSelected');
			if(row) {
				$.messager.prompt('修改','只允许修改类别名称,请输入新的名称',function(val){
					if(val) {
						$.post('../info/info-category!edit2.action',{"input.id":row.id,'input.cname':val},function(data){
							var result = eval('(' + data + ')');
							if(result.success)
								ctrl.ListTable.datagrid('reload');
							else
								SOL.showError(result.msg);
						});
					}
				})
			}
		});
		// 打开摄像头对话框
		ctrl.Pic.Startcap.click(function() {
			SF.openCap(function(result){
				ctrl.content.find(ctrl.Pic.picData).val(result);
			});
		});
	}
})(jQuery);