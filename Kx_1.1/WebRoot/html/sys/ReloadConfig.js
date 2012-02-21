(function($) {
	$.fn.sys_ReloadConfig = function() {
		$(this).find('#panel').panel({style:{'padding':'10px'}});
		
		$(this).find('#btn').click(function() {
			$.getJSON('../sys/sys-auth!reloadAuthConfig.action',function(data) {
				if(data.success)
					location.reload();
				else
					SOL.showError(data.msg);
			});
		});
	};
})(jQuery);