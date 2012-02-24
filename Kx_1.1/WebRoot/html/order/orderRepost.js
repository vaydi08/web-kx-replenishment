$(document).ready(function() {
	var id = window.dialogArguments[0];
	var inputData;

	$.post('../../order/order!take.action',{'input.id':id},function(data) {
			$('input').each(function() {
				var name = $(this).attr('name');
				if(data[name])
					$(this).val(data[name]);
			});

			if(data.image)
				$('#image').attr('src','../../pic.action?img=' + data.image);

			$('#id').val(id);
			
			inputData = data;
		},'json');

	$("#listTable").datagrid({
		height:150,
		pagination:false,
		title:"供应商情况列表",
		style:{'background-color':'#fafafa'},
		url:'../../order/feedback!manager.action?input.orderid=' + id,
		onLoadSuccess:function(data) {
			$("#feedbackid").val(data.reserve[0]);
		}
	});

	$('#formTabs').tabs({
		onSelect : function(title) {
			if(title == '供应商无货') {
				if(!$('#formTabs').data('sol-tabs-nopost')) {
					$('#formTabs').data('sol-tabs-nopost',true);

					var form = $('#failForm');
					form.find('#feedbackorderid').val(id);
					
					form.find("#supplier").combobox({
						url:"../../order/supplier!combo.action",
						valueField:'text',
						onSelect:function(record) {
							form.find("#supplier_contact").val(record.reserve);
						},
						onLoadSuccess:function() {
							var data = $(this).combobox('getData');
							if(data.length > 0)
								form.find("#supplier_contact").val(data[0].reserve);
						}
					});
				}
			}

			if(title == '部分发货') {
				if(!$('#formTabs').data('sol-tabs-subfail')) {
					$('#formTabs').data('sol-tabs-subfail',true);

					var form = $('#subfailForm');
					form.find('#feedbackorderid').val(id);
					
					form.find("#supplier").combobox({
						url:"../../order/supplier!combo.action",
						valueField:'text',
						onSelect:function(record) {
							form.find("#supplier_contact").val(record.reserve);
						},
						onLoadSuccess:function() {
							var data = $(this).combobox('getData');
							if(data.length > 0)
								form.find("#supplier_contact").val(data[0].reserve);
						}
					});
				}
			}
		}
	});

	$('#successForm').find('#btn_submit').click(function() {
		var param = {
			'input.id' : id,
			'input.status' : 4,

			'feedbackid' : $('#feedbackid').val(),
			'feedback' : $('#successForm').find('#feedback').val()
		};

		$.post('../../order/order!repostOk.action',param,function(data) {
			if(data.success)
				window.close();
			else
				$.messager.alert("Error",data.msg);
		},'json');
	});

	function failFormSubmit(form) {
		var param = {
			'input.id' : id,
			'input.status' : 3,

			'feedbackid' : $('#feedbackid').val(),
			'feedback' : form.find('#feedback').val(),

			'supplier' : form.find("#supplier").combobox('getValue'),
			'contact' : form.find('#supplier_contact').val()
		};

		$.post('../../order/order!repostFail.action',param,function(data) {
			if(data.success)
				window.close();
			else
				$.messager.alert("Error",data.msg);
		},'json');
	}
	$('#failForm').find('#btn_submit').click(function() {
		failFormSubmit($('#failForm'));
	});
	$('#subfailForm').find('#btn_submit').click(function() {
		failFormSubmit($('#subfailForm'));
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