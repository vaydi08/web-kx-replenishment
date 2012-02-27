(function($){
	$.fn.stock_Checked = function() {
		var content = $(this);
		var C = function(name) {
			return content.find(name);
		};
		var ctrl = {
			Query : {
				Panel : '#shopDiv',
				Shop : '#shop',
				ReloadType1 : '#reloadType1'
			},
			ListTable : {
				Type1 : '#type1Table',
				Type2 : '#type2Table',
				Type3 : '#type3Table',
				Type4 : '#type4Table'
			}
		};
		
		// 4数据表
		var contentwidth = content.width() / 2 - 10;
		var contentheight = (content.height() - 100) / 2 - 10;
		var gridConfig = function() {
			return {
				'width' : contentwidth,
				'height' : contentheight,
				'pagination' : false,
				columns : [[
						{field:'cname',title:'名称',width:10,rowspan:2},
						{title:'一般日',colspan:2},
						{title:'节假日',colspan:2}
					],[
						{field:'stock_type1',title:'件数',width:10},
						{field:'sum_stock_type1',title:'估算克重',width:10,
							formatter:function(value,row,index){return Number(value).toFixed(3);}},
						{field:'stock_type2',title:'件数',width:10},
						{field:'sum_stock_type2',title:'估算克重',width:10,
							formatter:function(value,row,index){return Number(value).toFixed(3);}}
					]]
			};
		};
		
		var LoadType1Table = function() {
			C(ctrl.ListTable.Type1).datagrid('load',{
				'input.shopid' : C(ctrl.Query.Shop).combobox('getValue'),
				'input.clevel':1
			});
		};
		
		// 查询面板
		C(ctrl.Query.Panel).panel({height:70,style:{'padding':'10px'}});
		// 门店选择
		C(ctrl.Query.Shop).combobox({
			url:'../info/info-shop!shopCombo.action',
			onSelect : function(record) {
				C(ctrl.ListTable.Type1).datagrid('load',{'shopid':record.value});
			}
		});
		C(ctrl.Query.ReloadType1).bind('click',LoadType1Table);
		
		var type1config = $.extend(gridConfig(),{
			url : '../stock/already-checked.action',
			queryParams : {'input.shopid':-1,'input.clevel':1},
			onClickRow : function(index,row) {
				C(ctrl.ListTable.Type2).datagrid('load',{
					'input.shopid' : C(ctrl.Query.Shop).combobox('getValue'),
					'input.parent' : row.ptype,'input.clevel' : 2
				});
			}
		});
		C(ctrl.ListTable.Type1).datagrid(type1config);
			
		var type234url = '../stock/already-checked.action';
		var type2config = $.extend(gridConfig(),{
			url : type234url,
			queryParams : {'input.shopid':-1,'input.clevel':2},
			onClickRow : function(index,row) {
				C(ctrl.ListTable.Type3).datagrid('load',{
					'input.shopid' : C(ctrl.Query.Shop).combobox('getValue'),
					'input.parent' : row.ptype,'input.clevel' : 3
				});
			}
		});
		C(ctrl.ListTable.Type2).datagrid(type2config);
		
		var type3config = $.extend(gridConfig(),{
			url : type234url,
			queryParams : {'input.shopid':-1,'input.clevel':3},
			onClickRow : function(index,row) {
				C(ctrl.ListTable.Type4).datagrid('load',{
					'input.shopid' : C(ctrl.Query.Shop).combobox('getValue'),
					'input.parent' : row.ptype,'input.clevel' : 4
				});
			}
		});
		C(ctrl.ListTable.Type3).datagrid(type3config);
		
		var type4config = $.extend(gridConfig(),{
			url : type234url,
			queryParams : {'input.shopid':-1,'input.clevel':4}
		});
		C(ctrl.ListTable.Type4).datagrid(type4config);
	};
})(jQuery);