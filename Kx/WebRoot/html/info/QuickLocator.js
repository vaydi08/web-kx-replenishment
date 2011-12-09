function loadScript(content,param) {
	var ctrl = {
		QueryPanel : {
			Panel : '#queryPanel',
			Search : '#EU_queryType',
			QueryType : '#queryType'
		},
		Locator : {
			Panel : '#locator',
			Type1 : '#type1',
			Type2 : '#type2',
			Type3 : '#type3',
			Type4 : '#type4',
			Type4Str : '#type4Str',
			
			CategoryType4Div : '#categoryType4Div',
			CategoryType4Select : '#categoryType4Select',
			TypeSelect : '#typeSelect'
		},
		ImgPanel : {
			Panel : '#imgPanel',
			Search : '#img_queryType',
			Div : '#imgPanelDiv',
			Select : '#imgPanelSelect'
		},
		Stock : {
			Dialog : '#stock',
			ListTable : '#stockTable'
		},
		ListTable : '#listTable',
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
		$(ctrl.Locator.CategoryType4Div).dialog('close');
		$(ctrl.Locator.Type3).combobox('select',$(ctrl.Locator.Type3).combobox('getValue'));
	}
	
	function init() {
		// 初始化查询框体
		SF.queryPanel($(ctrl.QueryPanel.Panel),{height:400},$(ctrl.QueryPanel.Search),{
			getparam:function(value){
				return {"queryType":$(ctrl.QueryPanel.QueryType).val(),"queryValue":(value == null?"":value)};
			}
		},$(ctrl.ListTable));
		// 快速定位框体
		$(ctrl.Locator.Panel).panel({height:100,style:{'padding':'10px','background-color':'#fafafa'}});
		// 数据表
		var listTableConfig = {
			url:"../info/info-product!manager2.action",
			height:290,
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
		    	var flag = content.data('info-quick-stock');
		    	if(!flag) {
			    	// 核定数量框体
					$(ctrl.Stock.ListTable).treegrid({
						url:"../info/info-product!quickLocatorStock.action",
						style:{'background-color':'#fafafa'},
						queryParams:{pid:-1},
						rownumbers:true,
						treeField:"shopname",
						idField:"treeid",
						fitColumns:true
					});
					content.data('info-quick-stock',true);
		    	}
		    },
		    onDblClickRow : function(index,row) {
		    	$.getJSON('../info/info-product!quickLocatorStock.action',{pid:row.id},function(data) {
		    		$(ctrl.Stock.ListTable).treegrid('loadData',data);
		    	});
		    	$(ctrl.Stock.Dialog).dialog('open');
		    }
		}
		SF.grid($(ctrl.ListTable),listTableConfig);
		// 对话框
		SF.dialog($(ctrl.Dialog),{
			width:480,
			onOpen:function(){
				$(ctrl.Form).form('clear');
				$(ctrl.Pic.picType).click();
				$(ctrl.Input.Clevel).val(4);
				$(ctrl.Input.Parent).combobox('select',$(ctrl.Locator.Type3).combobox('getValue'));
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
		// 打开摄像头对话框
		$(ctrl.Pic.Startcap).click(function() {
			SF.openCap(function(result){
				$(ctrl.Pic.picData).val(result);
			});
		});
		
		//查询面板下拉菜单
		$(ctrl.Locator.Type1).combobox({
			url:"../info/info-category!combobox.action?clevel=1&parent=0",
			width:100,
			editable:false,
			onSelect:function(row) {
				if(row.value > 0)
					$(ctrl.Locator.Type2).combobox('reload',"../info/info-category!combobox.action?clevel=2&parent=" + row.value);
			}
		});
		$(ctrl.Locator.Type2).combobox({
			width:100,
			editable:false,
			onSelect:function(row) {
				if(row.value > 0)
					$(ctrl.Locator.Type3).combobox('reload',"../info/info-category!combobox.action?clevel=3&parent=" + row.value);
			}
		});
		$(ctrl.Locator.Type3).combobox({
			width:100,
			editable:false,
			onSelect : function(row) {
				if(row.value > 0) {
					$(ctrl.Locator.CategoryType4Select).categoryType4Select({
						'url':'../info/info-product!findType4Select.action',
				     	'queryParams':{
				     		'input.type1':$(ctrl.Locator.Type1).combobox('getValue'),
				     		'input.type2':$(ctrl.Locator.Type2).combobox('getValue'),
				     		'input.type3':$(ctrl.Locator.Type3).combobox('getValue')
				     	},
				     	'newType4':true,
				     	onClick : function(value,html,id) {
				     		$(ctrl.Locator.CategoryType4Div).dialog('close');
	
				     		$(ctrl.ListTable).datagrid('load',{'input.pcode':value});
				     		$(ctrl.Locator.Type4Str).html(html);
					     },
					     onNewType4 : function() {
					     	$(ctrl.Dialog).dialog('open');
					     }
					});
	
					$(ctrl.Locator.CategoryType4Div).dialog('open');
				}
			}
		});
		// 快速图片检索展示
		$(ctrl.ImgPanel.Panel).panel({height:100,style:{'padding':'10px','background-color':'#fafafa'}});
		$(ctrl.ImgPanel.Search).searchbox({
			searcher:function(value,name) {
				if(value != null && value != "") {
					$(ctrl.ImgPanel.Select).categoryType4Select({
						'url':'../info/info-product!findType4Select.action',
				     	'queryParams':{
				     		'input.pcode':value
				     	},
				     	'newType4':false
					});
	
					$(ctrl.ImgPanel.Div).dialog('open');
				}
			}
		});
		
	}
	
	init();
}