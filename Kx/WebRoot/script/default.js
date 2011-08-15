$(document).ready(function() {
	// 菜单
	if($("#left").html() != null) {
		$(".submenu").each(function(){
			$(this).hide();
		})
		$("#left").leftmenu();
	}
	
	
	// 表单提交
	$("#form").submit(function() {
			var box=$(".validatebox-text",$(this));
			if(box.length){
				box.validatebox("validate");
				box.trigger("blur");
				var ctrl=$(".validatebox-invalid:first",$(this)).focus();
				return ctrl.length==0;
			}
	});
});