(function($) {
	$.fn.stock_CheckProductManager = function() {
		var content = $(this);
		
		var C = function(id) {
			return content.find(id);
		};
		
		var ctrl = {
			QueryPanel : {
				Panel : '#queryPanel',
				Type1 : '#type1',
				Type2 : '#type2',
				Type3 : '#type3',
				Btn : '#btn_product'
			},
			ListTable : '#listTable',
			ImgDiv : {
				Dialog : '#imgDiv',
				Img : '#largeImg'
			}
		};
		
		
		
		// 缓存对象
		var LargeImgDialog = C(ctrl.ImgDiv.Dialog);
		var LargeImg = C(ctrl.ImgDiv.Img);
		
		// 初始化查询框体
		var queryPanelConfig = {};
		$.extend(queryPanelConfig,SOL.defaultConfig.queryPanel.panel,{height:100});
		C(ctrl.QueryPanel.Panel).panel(queryPanelConfig);
		
		// 检索按钮
		C(ctrl.QueryPanel.Btn).click(function() {
			var type1 = C(ctrl.QueryPanel.Type1).combobox('getValue');
			var type2 = C(ctrl.QueryPanel.Type2).combobox('getValue');
			var type3 = C(ctrl.QueryPanel.Type3).combobox('getValue');
			
			var param = {};
			if(type1 && type1 > 0)
				param['input.type1'] = type1;
			if(type2 && type2 > 0)
				param['input.type2'] = type2;
			if(type3 && type3 > 0)
				param['input.type3'] = type3;
			
			C(ctrl.ListTable).datagrid('load',param);
		});
		
		//查询面板下拉菜单
		C(ctrl.QueryPanel.Type1).combobox({
			url:"../info/info-category!typeQueryCombo.action?input.clevel=1&input.parent=0",
			width:100,
			editable:false,
			onSelect:function(row) {
				if(row.value > 0)
					C(ctrl.QueryPanel.Type2).combobox('reload',"../info/info-category!typeQueryCombo.action?input.clevel=2&input.parent=" + row.value);
			}
		});
		C(ctrl.QueryPanel.Type2).combobox({
			width:100,
			editable:false,
			onSelect:function(row) {
				if(row.value > 0)
					C(ctrl.QueryPanel.Type3).combobox('reload',"../info/info-category!typeQueryCombo.action?input.clevel=3&input.parent=" + row.value);
			}
		});
		C(ctrl.QueryPanel.Type3).combobox({
			width:100,
			editable:false
		});
		
		// 进入核定
		var stockCheck = function(row) {
			var title = '核定';
			var url = '../url.action?url=/html/stock/Stock&ext=html';
			var param = {url:'/html/stock/Stock',id:row.id,image:row.image,
						type2name:row.type2name,type3name:row.type3name};
			// 生成tabs
			if($(SOL.content).tabs('exists',title)) {
				$(SOL.content).tabs('select',title);
				
				var panel = $(SOL.content).tabs('getTab',title);
				panel.stock_Stock(param);
			} else {
				$(SOL.content).tabs('add',{title:title,href:url,solparam:param});
			}
		};
		
		// 数据表
		var listTableConfig = {
			url : '../info/info-product!manager.action',
			height : content.height() - 120,
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
			toolbar : [
				{
					text : '核定',
					handler : function(){
						var row = C(ctrl.ListTable).datagrid('getSelected');
						if(row)
							stockCheck(row);
					}
				}
			],
		    onClickCell:function(index,field,value) {
				if(field == "image") {
					LargeImg.attr("src","../pic.action?img=" + value);
				}
			},
			onDblClickRow : function(index,row) {
				stockCheck(row);
			}
		};
		SF.grid(C(ctrl.ListTable),listTableConfig);
		
		LargeImg.load(function(){
//			console.info($(document).width())
//			var height = $(this).height() > (content.height() -100) ? content.height() -100 : $(this).height();
//			var width = $(this).width() > (content.width() -100) ? content.width() -100 : $(this).width();
//			height = height < 300 ? 300 : height;
//			width = width < 300 ? 300 : width;
			var height = $(document).height() - 200 < 600 ? $(document).height() - 100 : $(document).height() - 200;
			var width = $(document).width() - 400 < 1000 ? $(document).width() - 200 : $(document).width() - 400;
			LargeImgDialog.dialog('resize',{height:height,width:width,left:150,top:50});
			LargeImgDialog.dialog('open');
		});
					
		// 大图显示对话框
		C(ctrl.ImgDiv.Dialog).dialog();
	};
})(jQuery);