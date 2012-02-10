$(document).ready(function() {
	var id = window.dialogArguments[0];
	var inputData;

	$.post('../../order/order!take.action',{'input.id':id},function(data) {
			$('#form').find('input').each(function() {
				var name = $(this).attr('name');
				if(data[name])
					$(this).val(data[name]);
			});

			if(data.image)
				$('#image').attr('src','../../pic.action?img=' + data.image);

			$('#id').val(id);
			
			inputData = data;
		},'json');
	
	$("#supplier").combobox({
		url:"../../order/supplier!suppliers.action",
		valueField:'text',
		onSelect:function(record) {
			$("#supplier_contact").val(record.reserve);
		},
		onLoadSuccess:function() {
			var data = $(this).combobox('getData');
			if(data.length > 0)
				$("#supplier_contact").val(data[0].reserve);
		}
	});

	$('#btn_submit').click(function() {
			var param = {
				'input.id' : id,
				'input.status' : inputData.status,
				'input.userid' : inputData.userid,
				'input.gettime' : inputData.gettime.substring(0,19),
			
				'feedback.orderid' : id,
				'feedback.supplier' : $("#supplier").combobox('getValue'),
				'feedback.contact' : $('#supplier_contact').val(),
				'feedback.ordernum' : 0
			};
		
			$.post('../../order/order!accept.action',param,function(data) {
				if(data.success)
					window.close();
				else
					$.messager.alert("Error",data.msg);
			},'json');
	});
	
	$('#cancelOrder').click(function() {
		$.messager.prompt('取消确认', '确定要取消此订单,订单取消后就不能再进行处理<br/>请输入订单取消的原因:', function(r){
			if(r!= null && r != '') {
				$.post('../../order/order!orderCancel.action',{'input.id':id,'input.cancelReason':r},function(data){
					if(data.success)
						window.close();
					else
						$.messager.alert("Error",data.msg);
				},'json');
			} else {
				$.messager.alert("消息",'请输入一个取消理由');
			}
		});
	});
});

