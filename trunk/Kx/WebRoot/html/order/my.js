(function($) {
	$.fn.order_my = function() {
		var ctrl = {
			content : $(this),
			ListTable : $(this).find('#listTable')
		}
		
		var statusMap = {'-1':{text:'已取消',color:'#708090'},
			'1':{text:'等待处理',color:'#00FFFF'},
			'2':{text:'已接单,等待确认',color:'#D2691E'},
			'3':{text:'供应商反馈',color:'#00008B'},
			'4':{text:'供应商发货',color:'#1E90FF'},
			'5':{text:'发送产品',color:'#D2B48C'},
			'6':{text:'订购完成',color:'#00FFFF'}
		};

		var orderInfo = function(row) {
			if(row) {
				location.href = "order!orderInfo.action?input.id=" + row.id
			}
		}
		// 表格
		var gridConfig = {
			title:"订单列表",
			url:'../order/order!managerSelf.action',
			toolbar:[{
				id:'btngetorder',
				text:'处理',
				iconCls:'icon-print',
				handler:orderTake
				},{
				id:'btngetorder',
				text:'订单信息',
				iconCls:'icon-print',
				handler:function() {
					var row = ctrl.ListTable.datagrid('getSelected');
					orderInfo(row);
					}
				},{
				id:'btncancelorder',
				text:'取消订单',
				iconCls:'icon-remove',
				handler:function(){
					var row = ctrl.ListTable.datagrid('getSelected');
					if(row) {
						$.messager.prompt('取消确认', '确定要取消此订单,订单取消后就不能再进行处理<br/>请输入订单取消的原因:', function(r){
							if(r!= null && r != '') {
								$.post('order!orderCancel.action',{'input.id':row.id,'input.cancelReason':r},function(data){
									var result = eval('(' + data + ')');
									if(result.success)
										ctrl.ListTable.datagrid('reload');
									else
										$.messager.show({title:"Error",msg:result.msg});
								});
							}
						});
					}
				}
			}],
			columns:[[
		        {field:'id',title:'订单编号',width:120},
		        {field:'pname',title:'产品名称',width:80},
		        {field:'pcode',title:'产品代码',width:80},
		        {field:'shopname',title:'shopname',width:40},
		        {field:'fromwho',title:'下单人员',width:80},
		        {field:'num',title:'订购数量',width:80},
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
		

	}
})(jQuery);