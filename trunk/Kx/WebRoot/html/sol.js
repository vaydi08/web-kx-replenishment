// 自身配置
function SolConfig() {
	// 菜单控件名
	SolConfig.prototype.menu = '#menu';
	SolConfig.prototype.menuScript = 'menu.json';
	// 主框架控件名
	SolConfig.prototype.content = '#content';
	// 数据表控件名
	SolConfig.prototype.listTable = '#listTable';
	// 消息框
	SolConfig.prototype.message = '#message';
	SolConfig.prototype.view = '#view';
	SolConfig.prototype.messageHideDelay = 4000;
	// 摄像头
	SolConfig.prototype.capDialog = '#cap';
	SolConfig.prototype.loadCap = '#loadCap';
	SolConfig.prototype.cap = document.getElementById("WebVideoCap1");

	
	// URL ACTION加载路径
	SolConfig.prototype.urlactionPath = "../";
	// 默认动态加载脚本执行函数名
	SolConfig.prototype.dynamicScript = 'loadScript';
	
	// 默认参数设置
	SolConfig.prototype.defaultConfig = {
		// 动态js载入参数
		ajaxLoad : {type:'get',async:false,timeout:2000,dataType:'script'},
		// 查询面板
		queryPanel : {
			panel : {
				iconCls:"icon-search",
				collapsible:"true",
				style:{'padding':'10px','background':'#fafafa'},
				title:"查询条件",
				height:90
			}
		},

		// 对话框
		dialog : {}
	}
	
	// 修改easyui默认值
	$.extend($.fn.datagrid.defaults, {
		style:{'padding':'10px','background-color':'#fafafa'},
		nowrap:true,
		singleSelect:true,
		striped:true,
		pagination:true,
		rownumbers:true,
		fitColumns:true
	});
	$.extend($.fn.edatagrid.defaults, {
		style:{'background-color':'#fafafa'},
		//width:$(SOL.content).width() - 30,
		nowrap:true,
		singleSelect:true,
		striped:true,
		pagination:true,
		rownumbers:true,
		fitColumns:true,
		destroyMsg:{
			norecord:{
				title:'警告',
				msg:'没有选中任何项目'
			},
			confirm:{
				title:'确认',
				msg:'删除此条记录,此操作不可恢复?'
			}
		},
		destroyConfirmTitle: '确认',
		destroyConfirmMsg: '删除此条记录,此操作不可恢复?'
	});
	$.extend($.fn.combobox.defaults,{width:150,editable : false});
	$.extend($.fn.searchbox.defaults,{width:300,prompt:"请输入查询项,留空表示查询全部"});
	
	$.extend($.fn.datagrid.defaults.editors, {   
	    solpassword: {   
	        init: function(container, options){
	            var input = $('<input type="password" class="datagrid-editable-input" />').appendTo(container);   
	            return input;   
	        },   
	        getValue: function(target){   
	            return $(target).val();   
	        },   
	        setValue: function(target, value){   
	            $(target).val(value);   
	        },   
	        resize: function(target, width){   
	            var input = $(target);   
	            if ($.boxModel == true){   
	                input.width(width - (input.outerWidth() - input.width()));   
	            } else {   
	                input.width(width);   
	            }   
	        }   
	    }   
	}); 
	  
	
	// 显示消息
	SolConfig.prototype.showMessage = function(text) {
		$('body').layout('expand','south');
		$(SOL.message).empty();
		$(SOL.message).text(text);
	}
	SolConfig.prototype.showWarning = function(text) {
		$('body').layout('expand','south');
		$(SOL.message).empty();
		var html = '<div style="background:url(' + SOL.urlactionPath + 'script/easyui/themes/icons/help.png) no-repeat;padding-left:20px;color:#cc9933;">' + text + '</div>'
		$(SOL.message).html(html);
		
		setTimeout("SOL.hideMessage()",SOL.messageHideDelay);
	}
	SolConfig.prototype.showError = function(text) {
		$('body').layout('expand','south');
		$(SOL.message).empty();
		var html = '<div style="background:url(' + SOL.urlactionPath + 'script/easyui/themes/icons/no.png) no-repeat;padding-left:20px;color:red;">' + text + '</div>'
		$(SOL.message).html(html);
		
		setTimeout("SOL.hideMessage()",SOL.messageHideDelay);
	}
	SolConfig.prototype.alert = function(text) {
		$.messager.alert('信息',text);
	}
	SolConfig.prototype.hideMessage = function() {
		$('body').layout('collapse','south');
	}
	// 显示右边视图
	SolConfig.prototype.viewImg = function(url) {
		$(SOL.view).panel('refresh',url);
		$('body').layout('expand','east');
		
		setTimeout("SOL.hideView()",SOL.messageHideDelay);
	}
	SolConfig.prototype.hideView = function() {
		$('body').layout('collapse','east');
	}
	
	// 提交函数
	SolConfig.prototype.submit = function(url,param,callback) {
		$.post(url,param,function(data){
			var ret = eval('(' + data + ')');
			if(ret.success) {
				callback(ret);
			} else
				SOL.showError(ret.msg);
		});
	}
	
	
	// Clone
	SolConfig.prototype.clone = function(myObj){
	  if(typeof(myObj) != 'object') return myObj;
	  if(myObj == null) return myObj;
	  
	  var myNewObj = new Object();
	  
	  for(var i in myObj)
	     myNewObj[i] = SOL.clone(myObj[i]);
	  
	  return myNewObj;
	}

}
var SOL = new SolConfig();

function SoLFunction() {
	// 摄像头
	SoLFunction.prototype.openCap = function(callback) {
		$(SOL.capDialog).dialog('open');
		document.getElementById("WebVideoCap1").clear();
		document.getElementById("WebVideoCap1").start();
		
		$(SOL.capDialog).data('callback',callback);
	}
	
	// 查询面板
	SoLFunction.prototype.queryPanel = function(panel,pconfig,search,sconfig,datagrid) {
		var p = $.extend({},SOL.defaultConfig.queryPanel.panel);
		$.extend(p,pconfig);
		panel.panel(p);
		
		var s = {};
		$.extend(s,{
			searcher : function(value,name) {
				datagrid.datagrid('load',sconfig.getparam(value,name));
			}
		},sconfig);
		search.searchbox(s);
	}
	// 数据表
	SoLFunction.prototype.grid = function(ctrl,config) {
		// 用来设定控件高度
		if(!config.height)
			$.extend(config,{height:$(SOL.content).height()-130});
		ctrl.datagrid(config);
	}
	// 对话框
	SoLFunction.prototype.dialog = function(ctrl,config,callback_save) {
		var p = SOL.clone(SOL.defaultConfig.dialog);
		$.extend(p,config);
		ctrl.dialog(p);
		
		ctrl.find('#dlg-save').bind('click',callback_save);
		ctrl.find('#dlg-cancel').click(function(){ctrl.dialog('close');});
	}
	// 提交对话框
	SoLFunction.prototype.submit = function(ctrl,config,callback_successed) {
		var p = {
			success: function(result) {
				var result = eval('('+result+')');
				if (result.success){
					callback_successed();
				} else {
					SOL.showError(result.msg);
				}
			}
		}
		$.extend(p,config);
		
		ctrl.form('submit',p);
	}
	// 删除确认
	SoLFunction.prototype.confirm = function(datagrid,url) {
		var row = datagrid.datagrid('getSelected');
		if(row) {
			$.messager.confirm('确认删除','将要删除此记录,删除操作不可恢复,请确认?',function(b){
				if(b) {
					$.post(url,{"input.id":row.id},function(data){
						var result = eval('(' + data + ')');
						if(result.success)
							datagrid.datagrid('reload');
						else
							SOL.showError(result.msg);
					});
				}
			})
		}
	}

	
	// 用于载入js
	SoLFunction.prototype.loadjs = function() {
		var panel = $(SOL.content).data('sol-tabs-panel');
		var script = $(SOL.content).data('sol-tabs-script');
		var param = $(SOL.content).data('sol-tabs-param');

		panel.jsLoader(script,param);
	}
}

var SF = new SoLFunction();

var webCap = new WebCap();
	
$(document).ready(function(){
	var loadScript = function(url) {
		var jsUrl = SOL.urlactionPath + 'url.action?ext=js&url=' + url;
		$.getScript(jsUrl);
	}
	// 初始化主框架Panel
	var openTab = function(title,param) {
		if(!param)
			return;
		
		var url;
		if(param.type) {
			if(param.type == "redirect")
				url = param.url;
		} else {
			url = SOL.urlactionPath + 'url.action?url=' + param.url;
			if(param.ext)
				url += '&ext=' + param.ext;
		}
		// 生成tabs
		if($(SOL.content).tabs('exists',title))
			$(SOL.content).tabs('select',title);
		else {
			$(SOL.content).tabs('add',{title:title,href:url,solparam:param});
		}
	}
	
	// 初始化控件
	var init = function() {
		// 菜单
		$(SOL.menu).tree({
			url:'menu.json',
			onSelect:function(node){
				openTab(node.text,node.attributes);
			}
		});
		
		// TABS
		$(SOL.content).tabs({
			border:false,
			fit:true,
//			onAdd:function(title) {
//				//alert($(this).find('#info_productManager').html());
//			},
			onLoad:function(panel) {
				// 读取DOM完成后,执行对应的js函数
				var param = panel.panel('options').solparam;
				if(param.isSolScript)
					return;
					
				var script = param.url.substring(6).replace('/',"_");

				if(!$(SOL.content).data('sol-tabsload-' + script)) {
					$(SOL.content).data('sol-tabsload-' + script,true);

					// 延迟加载js 防止DOM读取不完整
					$(SOL.content).data('sol-tabs-panel',panel);
					$(SOL.content).data('sol-tabs-script',script);
					$(SOL.content).data('sol-tabs-param',param);
					
					setTimeout('SF.loadjs()',100);
				}
			}
		});

		// 右侧视图
		$(SOL.view).panel({
			noheader : true,
			border : false
		});
		
		// 收起下部信息条
		$('body').layout('collapse','south');
		// 收起右边信息条
		$('body').layout('collapse','east');
		
		// 摄像头
		$(SOL.loadCap).click(function() {
			if(!$(SOL.content).tabs('exists','摄像头')) {
				$(SOL.content).tabs('add',{title:'摄像头',href:'cap.html',solparam:{isSolScript:true}});
			}
		});
//		SolConfig.prototype.cap = document.getElementById("WebVideoCap1");
//		SOL.cap.stop();
//		SOL.cap.hiddenControllButtons();
//		SOL.cap.autofill(636,false);
//		$(SOL.capDialog).find("#cap_btn_cap").click(function(){
//			SOL.cap.cap();
//		});
//		$(SOL.capDialog).find('#dlg-save').click(function() {
//			var callback = $(SOL.capDialog).data('callback');
//			callback(SOL.cap.jpegBase64Data);
//			$(SOL.capDialog).dialog('close');
//		});
	}
	
	init();

});

