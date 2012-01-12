(function($) {
	$.fn.order_info = function(param) {
		var content = $(this);
		var C = function(name) {
			return content.find(name);
		}
		var ctrl = {
			FormDiv : '#formDiv',
			Form : '#orderInfoForm',
			Input : {
				Image : '#image'
			},
			ListTable : '#listTable',
			StatusDiv : '#statusDiv',
			Status : '#status'
		}
		
		var statusMap = {'-1':{text:'已取消',color:'#708090'},
			1:{text:'等待处理',color:'#00FFFF'},
			2:{text:'已接单,等待确认',color:'#D2691E'},
			3:{text:'供应商反馈',color:'#00008B'},
			4:{text:'供应商发货',color:'#1E90FF'},
			5:{text:'发送产品',color:'#D2B48C'},
			6:{text:'订购完成',color:'#00FFFF'}};
		
		function initDiv() {
			if(!C(ctrl.FormDiv).data('sol-isloaded')) {
				C(ctrl.FormDiv).panel({style:{'padding':'10px'}});
				C(ctrl.FormDiv).data('sol-isloaded',true);
			}
			
			if(!C(ctrl.StatusDiv).data('sol-isloaded')) {
				C(ctrl.StatusDiv).panel({style:{'padding':'10px'}});
				C(ctrl.StatusDiv).data('sol-isloaded',true);
			}
		}
		
		initDiv();
		
		$.post('../order/order!orderInfo.action',{'input.id':param.id},function(data) {
			loadFormData(data.orderType);
			loadDatagrid(data.feedbackList);
			loadStatus(data.orderType);
		},'json');
		
		function loadFormData(data) {
			C(ctrl.Form).form('load',data);
			
			C(ctrl.Input.Image).attr('src','../pic.action?img=' + data.image);
		}
		
		function loadDatagrid(data) {
			if(!C(ctrl.ListTable).data('sol-isloaded')) {
				C(ctrl.ListTable).datagrid({
					height:content.height() - C(ctrl.FormDiv).height() - C(ctrl.StatusDiv).height() - 140,
					title:"供应商情况列表",
					pagination:false
				});
				C(ctrl.ListTable).data('sol-isloaded',true);
			}
			
			C(ctrl.ListTable).datagrid('loadData',data);
		}
		
		function loadStatus(data) {
			var str = [];
			if(data.status > -1) {
				for(i = 1; i <= 6; i ++) {
					str.push('<span ' + (data.status == i ? 'style="color:' + statusMap[i].color + '"' : '') + '>' + statusMap[i].text + '</span>');
				}
			} else
				str.push('<span style="color:' + statusMap['-1'].color + '">' + statusMap['-1'].text + '</span>');
				
			C(ctrl.Status).html(str.join(' -> '));
		}
	}
})(jQuery);