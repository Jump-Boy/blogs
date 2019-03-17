/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// "admins/index" DOM加载完再加载,默认初次页面加载会调用一个菜单类目节点
$(function() {

	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;

	// 菜单事件
	$(".blog-menu .list-group-item").click(function() {
        //获取带"/"的项目名，如：/uimcardprj
		var url = pathName.substring(0, pathName.substr(1).indexOf('/')+1) + $(this).attr("url");
		
		// 先移除其他的点击样式，再添加当前的点击样式
		$(".blog-menu .list-group-item").removeClass("active");
		$(this).addClass("active");  
 
		// 加载其他模块的页面到右侧工作区(渲染某个菜单类目按钮所返回的页面信息)
		 $.ajax({ 
			 url: url, 
			 success: function(data){
			 	//渲染"admins/index"中的"#rightContainer"这个div
				 $("#rightContainer").html(data);
		 },
		 error : function() {
		     alert("error");
		     }
		 });
	});
	
	
	// 选中菜单第一项，触发第一项类目的点击事件
	 $(".blog-menu .list-group-item:first").trigger("click");
});