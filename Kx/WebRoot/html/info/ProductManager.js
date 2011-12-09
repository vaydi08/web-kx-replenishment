function loadScript(content,param) {
	var ctrl = {
		QueryPanel : {
			Panel : '#queryPanel',
			Search : '#EU_queryType',
			QueryType : '#queryType',
			Type1 : '#type1',
			Type2 : '#type2',
			Type3 : '#type3'
		},
		ListTable : '#listTable',
		Toolbar : {
			Add : '#btn_add',
			Edit : '#btn_edit',
			Del : '#btn_del'
		},
		Dialog : '#dlg',
		Form : '#form',
		Pic : {
			Startcap : '#startcam',
			picType : 'input[name=picType]:radio:first',
			picData : '#picData',
			picFile : '#picFile'
		},
		Input : {
			Clevel : '#clevel',
			Parent :'#parent',
			Ccode : '#ccode',
			CodeMsg : '#codeMsg',
			Cname : '#cname'
		}
	}
	
	// From提交情况
	function onSave() {
		SF.submit($(ctrl.Form),{
			url : '../info/info-category!add.action',
			onSubmit:function(){
				if($('input[name=picType]:checked').val() == "webcap" && $(ctrl.Pic.picData).val() == "") {
					SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
					return false;
				}
				if($('input[name=picType]:checked').val() == "file" && $(ctrl.Pic.picFile).val() == "") {
					SOL.showWarning('请首先使用摄像头程序照相 或上传一个图片文件');
					return false;
				}
				
				if($("#codeMsg").text() != '') {
					SOL.showWarning('有重复记录,请确认');
					return false;
				}

				return $(ctrl.Form).form("validate");
			}
		},
		onSuccess);
	}
	function onSuccess() {
		$(ctrl.ListTable).datagrid('reload');
		$(ctrl.Dialog).dialog('close');
	}
	
	function init() {
		// 初始化查询框体
		SF.queryPanel($(ctrl.QueryPanel.Panel),{},$(ctrl.QueryPanel.Search),{
			getparam:function(value){
				var queryType = $(ctrl.QueryPanel.QueryType).val();
				var type1 = $(ctrl.QueryPanel.Type1).combobox('getValue');
				var type2 = $(ctrl.QueryPanel.Type2).combobox('getValue');
				var type3 = $(ctrl.QueryPanel.Type3).combobox('getValue');

				var queryObject = {"queryType":queryType,"queryValue":(value == null?"":value)};
				if(Number(type1) > 0)
					queryObject['input.type1'] = type1;
				if(Number(type2) > 0)
					queryObject['input.type2'] = type2;
				if(Number(type3) > 0)
					queryObject['input.type3'] = type3;
				
				return queryObject;
			}
		},$(ctrl.ListTable));
		//查询面板下拉菜单
		$(ctrl.QueryPanel.Type1).combobox({
			url:"../info/info-category!combobox.action?clevel=1&parent=0",
			width:100,
			editable:false,
			onSelect:function(row) {
				if(row.value > 0)
					$(ctrl.QueryPanel.Type2).combobox('reload',"../info/info-category!combobox.action?clevel=2&parent=" + row.value);
			}
		});
		$(ctrl.QueryPanel.Type2).combobox({
			width:100,
			editable:false,
			onSelect:function(row) {
				if(row.value > 0)
					$(ctrl.QueryPanel.Type3).combobox('reload',"../info/info-category!combobox.action?clevel=3&parent=" + row.value);
			}
		});
		$(ctrl.QueryPanel.Type3).combobox({
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
		SF.grid($(ctrl.ListTable),listTableConfig);
		// 对话框
		SF.dialog($(ctrl.Dialog),{
			width:480,
			onOpen:function(){
				$(ctrl.Pic.picType).click();
				$(ctrl.Input.Clevel).val(4);
			}
		},onSave);
		// 对话框内控件 父类别
		$(ctrl.Input.Parent).combobox({url:'../info/info-category!combobox1.action?clevel=3',required:true});
		// 产品代码
		$(ctrl.Input.Ccode).change(function() {
			$.post('../info/info-category!checkCode.action',{'input.ccode':$(this).val(),'input.clevel':4,'input.parent':$(ctrl.Input.Parent).combobox('getValue')},function(data){
				var result = eval('(' + data + ')');
				if(result.success) {
					$(ctrl.Input.CodeMsg).text('');
				} else {
					$(ctrl.Input.CodeMsg).text(result.msg);
				}
			});
		});
	}
	
	init();
	
	// 按钮绑定
	$(ctrl.Toolbar.Add).click(function() {
		$(ctrl.Form).form('clear');
		$(ctrl.Dialog).dialog('open');
	});
	$(ctrl.Toolbar.Del).click(function() {
		SF.confirm($(ctrl.ListTable),'../info/info-category!delete2.action');
	});
	$(ctrl.Toolbar.Edit).click(function() {
		var row = $(ctrl.ListTable).datagrid('getSelected');
		if(row) {
			$.messager.prompt('修改','只允许修改类别名称,请输入新的名称',function(val){
				if(val) {
					$.post('../info/info-category!edit2.action',{"input.id":row.type4,'input.cname':val},function(data){
						var result = eval('(' + data + ')');
						if(result.success)
							$(ctrl.ListTable).datagrid('reload');
						else
							SOL.showError(result.msg);
					});
				}
			});
		}
	});
	// 打开摄像头对话框
	$(ctrl.Pic.Startcap).click(function() {
		SF.openCap(function(result){
			$(ctrl.Pic.picData).val(result);
		});
	});
}