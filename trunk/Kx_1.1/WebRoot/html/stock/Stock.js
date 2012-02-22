(function($) {
	$.fn.stock_Stock = function(param) {
		var content = $(this);
		
		var C = function(id) {
			return content.find(id);
		};
		
		var ctrl = {
			CheckDiv : {
				Dialog : '#checkDiv',
				CheckTableDiv1 : '#checkTableDiv1',
				Shop : '#shop',
				ListTable : '#checkTable',
				Reload : '#reloadListTable',
				Add : '#check_btn_add',
				Del : '#check_btn_del',
				Save : '#check_btn_save',
				Undo : '#check_btn_undo'
			},
			CountTable : '#countTable',
			CheckImgDiv : {
				Panel : '#checkImgDiv',
				Img : '#checkImgPreview'
			},
			CheckCopyButton : {
				CopyToType2 : '#STOCK_C_CMM_copyToType2',
				CopyToType3 : '#STOCK_C_CMM_copyToType3',
				CopyToProduct : '#STOCK_C_CMM_copyToProduct'
			}
		};
	
		function pid(){return content.data('sol-param').id || param.id;};
		
		function loadCountData(param) {
			var dgData = {total:0,rows:[]};
			$.post('../stock/check!stockCheckSum.action',param,function(data){
				dgData.rows.push({title:data.type1name + ' 已核定',type1:data.shop_stocktype1,type2:data.shop_stocktype2});
				dgData.rows.push({title:'产品已核定',type1:data.shop_product_stocktype1,type2:data.shop_product_stocktype2});
				dgData.rows.push({title:data.type1name + ' 约估克重',type1:Number(data.sum_type1_stocktype1).toFixed(3),type2:Number(data.sum_type1_stocktype2).toFixed(3)});
				dgData.rows.push({title:data.type2name + ' 约估克重',type1:Number(data.sum_type2_stocktype1).toFixed(3),type2:Number(data.sum_type2_stocktype2).toFixed(3)});
				C(ctrl.CountTable).datagrid('loadData',dgData);
			},'json');
		}
		
		function loadImgPreview(param) {
			C(ctrl.CheckImgDiv.Img).attr('src','../pic.action?img=' + param.image);
		}
		
		function loadCopyButton(param) {
			// copy功能文字
			$(ctrl.CheckCopyButton.CopyToType2).find('.menu-text').text('复制到小类-' + param.type2name);
			$(ctrl.CheckCopyButton.CopyToType3).find('.menu-text').text('复制到货型-' + param.type3name);
		}
		
		function init() {
			// 门店选择
			C(ctrl.CheckDiv.Shop).combobox({
				url:'../info/info-shop!shopCombo.action',
				onSelect : function(record) {
					C(ctrl.CheckDiv.ListTable).edatagrid('load',{'input.shopid':record.value,'input.pid':pid()});
				},
				onLoadSuccess : function() {
					if(!content.data('sol-datagrid-isload')) {
						content.data('sol-datagrid-isload',1);
						return;
					}
						
					if(content.data('sol-datagrid-isload') == 1) {
						createDatagrid();
						content.data('sol-datagrid-isload',true);
					}
				}
			});
			
			// 核定表单
			function createDatagrid() {
				C(ctrl.CheckDiv.ListTable).edatagrid({
					pagination:false,
					width:400,
					height:content.height()-100,
					url:'../stock/check!select.action',
					saveUrl: '../stock/check!add.action',
					updateUrl: '../stock/check!edit.action',
					destroyUrl: '../stock/check!delete.action',
					queryParams:{'input.shopid':C(ctrl.CheckDiv.Shop).combobox('getValue'),'input.pid':pid()},
				
					onAfterEdit:function(){$(this).edatagrid('reload');},
					onDestroy:function(){$(this).edatagrid('reload');},
					
					onSave : function(index,row,data) {
						if(!data.success)
							SOL.showError('服务器产生了一个错误 : ' + data.msg + ' , 请尝试刷新页面并重新提交数据');
					},
					onLoadSuccess : function() {
						var param = {
							'input.shopid' : C(ctrl.CheckDiv.Shop).combobox('getValue'),
							'input.pid' :pid()
						};
						
						C(ctrl.CountTable).datagrid({width:270,style:{'padding':'0px'},pagination:false,fitColumns:false,rownumbers:false});
						
						loadCountData(param);
					}
				});
			};
			// 按钮
			C(ctrl.CheckDiv.Add).click(function() {
				C(ctrl.CheckDiv.ListTable).edatagrid('addRow',{
					'shopid':C(ctrl.CheckDiv.Shop).combobox('getValue'),
					'pid':pid()
				});
			});
			C(ctrl.CheckDiv.Del).click(function() {
				C(ctrl.CheckDiv.ListTable).edatagrid('destroyRow');
			});
			C(ctrl.CheckDiv.Save).click(function() {
				C(ctrl.CheckDiv.ListTable).edatagrid('saveRow');
			});
			C(ctrl.CheckDiv.Undo).click(function() {
				C(ctrl.CheckDiv.ListTable).edatagrid('cancelRow');
			});
			
			// 核定框体 图片预览
			C(ctrl.CheckImgDiv.Panel).panel();
			
			var getCopyUrl = function(clevel) {
				return '../stock/check!copyCheck.action?input.pid=' + pid() + 
					   '&input.shopid=' + C(ctrl.CheckDiv.Shop).combobox('getValue') + '&input.clevel=' + clevel;
			};
			// 核定数复制功能按钮
			$(ctrl.CheckCopyButton.CopyToType2).click(function() {
				alert(1);
				$.post(getCopyUrl(2),
					function(data) {
						if(data.success)
							SOL.alert('复制成功');
						else
							SOL.alert('复制失败,服务器返回的错误信息:' + data.msg);
					}
				,'json');
			});
			$(ctrl.CheckCopyButton.CopyToType3).click(function() {
				$.post(getCopyUrl(3),
					function(data) {
						if(data.success)
							SOL.alert('复制成功');
						else
							SOL.alert('复制失败,服务器返回的错误信息:' + data.msg);
					}
				,'json');
			});
			
			// 载入图片
			loadImgPreview(content.data('sol-param'));
			// copy按钮文字
			loadCopyButton(content.data('sol-param'));
		}
		
		if(!content.data('sol-content-isload')) {
			content.data('sol-param',param);
			init();
			content.data('sol-content-isload',true);
		} else {
			content.data('sol-param',param);
			function getparam() {
				return {'input.shopid':C(ctrl.CheckDiv.Shop).combobox('getValue'),'input.pid':pid()};
			}
			C(ctrl.CheckDiv.ListTable).edatagrid('load',getparam());
			loadCountData(getparam());
			loadImgPreview(content.data('sol-param'));
			loadCopyButton(content.data('sol-param'));
		}
	};
})(jQuery);