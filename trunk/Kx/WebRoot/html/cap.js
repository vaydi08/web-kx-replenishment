var WebCap = function() {
	var isloaded = false;
	
	WebCap.prototype.initCap = function() {
		if(!isloaded) {
			var cap = document.getElementById("WebVideoCap1");
			cap.stop();
			cap.hiddenControllButtons();
			cap.autofill(636,false);
			
			$(SOL.capDialog).find("#cap_btn_cap").click(function(){
				document.getElementById("WebVideoCap1").cap();
			});
			
			$(SOL.capDialog).find('#dlg-save').click(function() {
				var callback = $(SOL.capDialog).data('callback');
				callback(document.getElementById("WebVideoCap1").jpegBase64Data);
				$(SOL.capDialog).dialog('close');
			});
			
			isloaded = true;
			
			$('#capStatusDiv').text('已加载,已初始化完成');
		}
	}
}