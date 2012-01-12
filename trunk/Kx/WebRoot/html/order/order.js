(function($) {
	$.fn.order_order = function() {

		var content = $(this);
		var C = function(name) {
			return content.find(name);
		}
		var ctrl = {
			FormDiv : '#formDiv',
			Input : {
				Form : '#form',
				Submit : '#btn_submit',
				Shop : '#shop',
				Pid : '#pid',
				
				PnameText : '#order_pname',
				PcodeText : '#order_pcode'
			},
			ProductDiv : '#productDiv',
			Query : {
				Type1 : '#type1',
				Type2 : '#type2',
				Type3 : '#type3',
				Btn : '#btn_product'
			},
			ListTable : '#listTable'
		}
		
		var loadedCtrl = [];
		function init(control,method,param) {
			if(!control.data('sol-isloaded')) {
				$.fn[method].call(control,param || {});
				control.data('sol-isloaded',true);
				
				loadedCtrl.push(control);
			}
		}
		function removeLoaded() {
			for(i = 0; i < loadedCtrl.length; i ++) {
				loadedCtrl[i].removeData('sol-isloaded');
			}
			C(ctrl.ListTable).data('sol-remoteload',false);
		}
			
		function initDiv() {
			init(C(ctrl.FormDiv),'panel',{style:{'padding':'10px'}});
			init(C(ctrl.ProductDiv),'panel',{style:{'padding':'10px'}});
		}
		
		initDiv();
		
		function loadShopCombo() {
			C(ctrl.Input.Shop).combobox({
				url:'../info/info-shop!shopCombo.action',
				onLoadSuccess : function() {
					loadDatagrid();
				}
			});
		}
		
		function loadDatagrid() {
			init(C(ctrl.ListTable),'datagrid',
				{
					height : content.height() - C(ctrl.FormDiv).height() - 150,
					rownumbers:false,
					url:"../info/info-product!manager2.action",
					view:cardview,
					
					onLoadSuccess : function() {
						loadType();
						
						if(C(ctrl.ListTable).data('sol-remoteload'))
							C(ctrl.ListTable).data('sol-remoteload',false);
					},
					onClickRow : function(index,data) {
						C(ctrl.Input.PnameText).text(
							data['type1name'] + '-' + data['type2name'] + '-' + 
		            		data['type3name'] + '-' + data['type4name']);
		            	C(ctrl.Input.PcodeText).text(data['pcode']);
		            	C(ctrl.Input.Pid).val(data.id);
					}
				}
			);
		}
		
		function loadType() {
			init(C(ctrl.Query.Type1),'combobox',{
				url:"../info/info-category!combobox.action?clevel=1&parent=0",
				width:100,
				editable:false,
				onSelect:function(row) {
					if(row.value > 0)
						C(ctrl.Query.Type2).combobox('reload',"../info/info-category!combobox.action?clevel=2&parent=" + row.value);
					}
				}
			);
			
			init(C(ctrl.Query.Type2),'combobox',{
				width:100,
				editable:false,
				onSelect:function(row) {
					if(row.value > 0)
						C(ctrl.Query.Type3).combobox('reload',"../info/info-category!combobox.action?clevel=3&parent=" + row.value);
					}
				}
			);
			
			init(C(ctrl.Query.Type3),'combobox',{
				width:100,
				editable:false
				}
			);
			
			C(ctrl.Query.Btn).click(function() {
				var type1 = C(ctrl.Query.Type1).combobox('getValue');
				var type2 = C(ctrl.Query.Type2).combobox('getValue');
				var type3 = C(ctrl.Query.Type3).combobox('getValue');

				var queryObject = {};
				if(Number(type1) > 0)
					queryObject['input.type1'] = type1;
				if(Number(type2) > 0)
					queryObject['input.type2'] = type2;
				if(Number(type3) > 0)
					queryObject['input.type3'] = type3;
				
				if(!C(ctrl.ListTable).data('sol-remoteload')) {
					C(ctrl.ListTable).datagrid('load',queryObject);
					C(ctrl.ListTable).data('sol-remoteload',true);
				}
			});
		}
		
		var cardview = $.extend({}, $.fn.datagrid.defaults.view, {   
		    renderRow: function(target, fields, frozen, rowIndex, rowData){   
		        var cc = [];   
		        cc.push('<td colspan="4" width="30%" style="padding:10px 5px;border:0;">');   
		        if (!frozen){   
		            cc.push('<img src="../pic.action?img=' + rowData.image + '" style="height:150px;margin-left:20px;float:left">');
		            cc.push('</td><td width="70%" colspan="4" border="0" style="padding:10px 5px">')
		            cc.push('<div style="float:left;margin-left:20px;">');  
		            
		            var t = "类别";
		            var v = rowData['type1name'] + '-' + rowData['type2name'] + '-' + 
		            		rowData['type3name'] + '-' + rowData['type4name'];
		            cc.push('<p><span>产品名称: </span>' + rowData['pname'] + '</p>');
		            cc.push('<p><span>产品代码: </span>' + rowData['pcode'] + '</p>');
		            cc.push('<p><span>单位: </span>' + rowData['unit'] + '</p>');
		            cc.push('</div>');
		        }   
		        cc.push('</td>');   
		        return cc.join('');   
		    }   
		});  

		loadShopCombo();
		
		function initForm() {
			init(C(ctrl.Input.Form),'form',{
				url : '../order/order!add2.action',
				onSubmit : function() {
					if(C(ctrl.Input.Pid).val() == "") {
						SOL.showWarning('请选择一个产品');
						return false;
					}
					
					return $(this).form('validate');
				},
				success : function(data) {
					var ret = $.parseJSON(data);
					if(ret.success) {
						$.messager.alert('消息','订单已经成功提交');
						removeLoaded();
						$(SOL.content).tabs('close','订购货品');
					} else {
						SOL.showError(ret.msg);
					}
				}
			});
			
			C(ctrl.Input.Submit).click(function() {
				C(ctrl.Input.Form).submit();
			});
		}
		
		initForm();
	}
})(jQuery);