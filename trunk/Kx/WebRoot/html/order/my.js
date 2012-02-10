(function($) {
	$.fn.order_my = function() {
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
		var orderTake = function() {
			var row = C(ctrl.ListTable).datagrid('getSelected');
			
			if(row && (row.status == 2 || row.status == 3)) {
				var ret = window.showModalDialog('order/orderRepost.html',[row.id],'dialogHeight:600px;dialogWidth:750px');
				
				C(ctrl.ListTable).datagrid('reload');
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
					var row = C(ctrl.ListTable).datagrid('getSelected');
					if(row)
						orderInfo(row);
					}
				},{
				id:'btncancelorder',
				text:'取消订单',
				iconCls:'icon-remove',
				handler:function(){
					var row = C(ctrl.ListTable).datagrid('getSelected');
					if(row) {
						$.messager.prompt('取消确认', '确定要取消此订单,订单取消后就不能再进行处理<br/>请输入订单取消的原因:', function(r){
							if(r!= null && r != '') {
								$.post('../order/order!orderCancel.action',{'input.id':row.id,'input.cancelReason':r},function(data){
									if(data.success)
										C(ctrl.ListTable).datagrid('reload');
									else
										$.messager.alert("Error",data.msg);
								},'json');
							} else {
								$.messager.alert("消息",'请输入一个取消理由');
							}
						});
					}
					}
				},'-',{
				id:'btnsend',
				text:'发送产品',
				//iconCls:'icon-remove',
				handler:function(){
					var row = C(ctrl.ListTable).datagrid('getSelected');
					if(row) {
						if(row.status != 4)
							return;
							
						$.messager.confirm('确认', '此订单开始发送往门店', function(r){
							if(r) {
								$.post('../order/order!edit2.action',{'input.id':row.id,'input.status':5},function(data){
									if(data.success)
										C(ctrl.ListTable).datagrid('reload');
									else
										$.messager.alert("Error",data.msg);
								},'json');
							} else {
								$.messager.alert("消息",'请输入一个取消理由');
							}
						});
					}
				}
				},'-',{
				id:'btnsend',
				text:'完成订单',
				//iconCls:'icon-remove',
				handler:function(){
					var row = C(ctrl.ListTable).datagrid('getSelected');
					if(row) {
						if(row.status != 5)
							return;
							
						$.messager.confirm('确认', '订单已发送至门店,此订单完结', function(r){
							if(r) {
								$.post('../order/order!edit2.action',{'input.id':row.id,'input.status':6},function(data){
									if(data.success)
										C(ctrl.ListTable).datagrid('reload');
									else
										$.messager.alert("Error",data.msg);
								},'json');
							} else {
								$.messager.alert("消息",'请输入一个取消理由');
							}
						});
					}
				}
				}
			],
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