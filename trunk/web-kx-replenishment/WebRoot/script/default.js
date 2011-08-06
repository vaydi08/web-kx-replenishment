$(document).ready(function() {
	// 菜单
	if($("#left").html() != null) {
		$(".submenu").each(function(){
			$(this).hide();
		})
		$("#left").leftmenu();
	}
	
	
	// 全选
	if($("#allsel")) {
	   	$("#allsel").click(function() {
			if($(this).attr("checked")) {
				$(".tdcb").each(function() {$(this).attr("name");$(this).attr("checked",true);});
			} else {
				$(".tdcb").each(function() {$(this).removeAttr("checked");});
			}				
		});
	}
});