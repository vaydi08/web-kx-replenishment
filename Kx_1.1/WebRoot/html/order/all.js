(function($) {
	$.fn.order_all = function() {
		var content = $(this);
		var C = function(name) {
			return content.find(name);
		}
		var ctrl = {
			ListTable : '#listTable'
		}
		
		var statusMap = {'-1':{text:'已取消',color:'#708090'},
			'1':{text:'等待处理',color:'#00FFFF'},
			'2':{text:'已接单',color:'#D2691E'},
			'3':{text:'联系供应商',color:'#00008B'},
			'4':{text:'供应商发货',color:'#1E90FF'},
			'5':{text:'发送产品',color:'#D2B48C'},
			'6':{text:'订购完成',color:'#00FFFF'}
		};

		var orderInfo = function(row) {
			var title = '订单明细';
			var url = '../url.action?url=/html/order/info&ext=html';
			var param = {url : '/html/order/info',id:row.id}
			// 生成tabs
			if($(SOL.content).tabs('exists',title)) {
				$(SOL.content).tabs('select',title);
				
				var panel = $(SOL.content).tabs('getTab',title);
				panel.order_info(param);
			} else {
				$(SOL.content).tabs('add',{title:title,href:url,solparam:param});
			}
		}
		// 表格
		var gridConfig = {
			title:"订单列表",
			url:'../order/order!selectAll.action',
			toolbar:[{
				id:'btngetorder',
				text:'订单信息',
				iconCls:'icon-print',
				handler:function() {
					var row = C(ctrl.ListTable).datagrid('getSelected');
					if(row)
						orderInfo(row);
					}
				}],
			columns:[[
		        {field:'id',title:'订单编号',width:120},
		        {field:'pname',title:'产品名称',width:80},
		        {field:'pcode',title:'产品代码',width:80},
		        {field:'shopname',title:'订购门店',width:40},
		        {field:'fromwho',title:'下单人员',width:80},
		        {field:'num',title:'订购数量',width:80},
		        {field:'weight',title:'订购重量',width:80},
		        {field:'ordertime',title:'下单时间',width:40},
		        {field:'status',title:'处理状态',width:40,
		        	formatter : function(value,row,index) {
						return '<span style="color:' + statusMap[value].color +'">' + statusMap[value].text + '</span>';
					}
		        }
		    ]],
			onDblClickRow:function(index,row) {
				orderInfo(row);
			}
		};
		
		// 初始化表单
		C(ctrl.ListTable).datagrid(gridConfig);
	}
})(jQuery);