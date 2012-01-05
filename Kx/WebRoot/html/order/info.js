(function($) {
	$.fn.order_info = function(param) {
		var content = $(this);
		var C = function(name) {
			return content.find(name);
		}
		var ctrl = {
			Form : '#orderInfoForm',
			Input : {
				Image : '#image'
			}
		}
		
		$.post('../order/order!orderInfo.action',{'input.id':param.id},function(data) {
			loadFormData(data);
		},'json');
		
		function loadFormData(data) {
			C(ctrl.Form).form('load',data);
			
			C(ctrl.Input.Image).attr('src','../pic.action?img=' + data.image);
		}
	}
})(jQuery);