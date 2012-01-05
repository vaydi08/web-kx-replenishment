(function($) {
	$.fn.info_ProductManager = function() {
		var ctrl = {
			content : $(this),
			QueryPanel : {
				Panel : $(this).find('#queryPanel'),
				Search : $(this).find('#INFO_P_queryType'),
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
				picData : '#INFO_P_picData',
				picFile : '#INFO_P_picFile'
			},
			Input : {
				Clevel : $(this).find('#clevel'),
				Parent :$(this).find('#parent'),
				Ccode : $(this).find('#ccode'),
				CodeMsg : $(this).find('#codeMsg'),
				Cname : $(this).find('#cname')
			}
		}
	
		// From提交情况
		function onSave() {
			SF.submit(ctrl.Form,{
				url : '../info/info-category!add.action',
				onSubmit:function(){
					if(ctrl.Dialog.find('.INFO_P_picType:checked').val() == "webcap" && $(ctrl.Pic.picData).val() == "") {
						SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
						return false;
					}
					if(ctrl.Dialog.find('.INFO_P_picType:checked').val() == "file" && $(ctrl.Pic.picFile).val() == "") {
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
		}
		
		function init() {
			// 初始化查询框体
			SF.queryPanel(ctrl.QueryPanel.Panel,{},ctrl.QueryPanel.Search,{
				getparam:function(value,name){
					var type1 = ctrl.QueryPanel.Type1.combobox('getValue');
					var type2 = ctrl.QueryPanel.Type2.combobox('getValue');
					var type3 = ctrl.QueryPanel.Type3.combobox('getValue');

					var queryObject = {"queryType":name,"queryValue":(value == null?"":value)};
					if(Number(type1) > 0)
						queryObject['input.type1'] = type1;
					if(Number(type2) > 0)
						queryObject['input.type2'] = type2;
					if(Number(type3) > 0)
						queryObject['input.type3'] = type3;
					
					return queryObject;
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
				title:"产品列表",
				url:"../info/info-product!manager2.action",
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
			    }
			}
			SF.grid(ctrl.ListTable,listTableConfig);
	
			// 对话框
			SF.dialog(ctrl.Dialog,{
				width:480,
				onOpen:function(){
					ctrl.Pic.picType.click();
					ctrl.Input.Clevel.val(4);
				}
			},onSave);
			// 对话框内控件 父类别
			ctrl.Input.Parent.combobox({url:'../info/info-category!combobox1.action?clevel=3',required:true});
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
		}
	
		init();
		
		webCap.initCap();
	
		// 按钮绑定
		ctrl.Toolbar.Add.click(function() {
			ctrl.Form.form('clear');
			ctrl.Dialog.dialog('open');
		});
		ctrl.Toolbar.Del.click(function() {
			var row = ctrl.ListTable.datagrid('getSelected');
			if(row) {
				$.messager.confirm('确认删除','将要删除此记录,删除操作不可恢复,请确认?',function(b){
					if(b) {
						$.post('../info/info-category!delete2.action',{"input.id":row.type4},function(data){
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
		ctrl.Toolbar.Edit.click(function() {
			var row = ctrl.ListTable.datagrid('getSelected');
			if(row) {
				$.messager.prompt('修改','只允许修改类别名称,请输入新的名称',function(val){
					if(val) {
						$.post('../info/info-category!edit2.action',{"input.id":row.type4,'input.cname':val},function(data){
							var result = eval('(' + data + ')');
							if(result.success)
								ctrl.ListTable.datagrid('reload');
							else
								SOL.showError(result.msg);
						});
					}
				});
			}
		});
		// 打开摄像头对话框
		ctrl.Pic.Startcap.click(function() {
			SF.openCap(function(result){
				$(ctrl.Pic.picData).val(result);
			});
		});
	}
		
})(jQuery);