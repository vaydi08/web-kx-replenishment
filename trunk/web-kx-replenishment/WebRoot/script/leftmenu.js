//jQuery(function($) {
	jQuery.fn.leftmenu = function(options) {
		var options = $.extend({},options);
		
		this.find(".menu").click(function() {
			var submenu = $(this).find(".submenu").toggle();
		});
	};
		
	
//})(jQuery);

