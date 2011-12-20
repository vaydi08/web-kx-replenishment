function loadScript(content, param) {
	$('#btn').click(function() {
		$.getJSON('../sys/auth!reloadAuthConfig.action',function(data) {
			if(data.success)
				location.reload();
			else
				SF.showERROR(data.msg);
		});
	});
}