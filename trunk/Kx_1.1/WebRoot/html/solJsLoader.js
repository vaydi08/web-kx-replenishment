// 反向定位函数 加载器
(function($) {
	$.fn.jsLoader = function(method,param) {
//		return $.fn.jsLoader.methods[method]($(this),param);
		var sp = $.fn[method];
		if(sp)
			return sp.call($(this),param);
	}

})(jQuery);