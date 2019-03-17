/*!
  * Bolg main JS.
 * 
 * @since: 1.0.0 2017/3/9
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=main.js
 
// 在"/users/list" DOM 加载完再加载,在"fragments/page"中会被调用,用于发起分页查询
$(function() {
	
	var _pageSize; // 存储用于搜索

    var pathName = window.document.location.pathname;
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
	
	// 根据用户名、页面索引、页面大小获取用户列表
	function getUersByName(pageIndex, pageSize) {
		 $.ajax({ 
			 url: projectName + "/users",
			 contentType : 'application/json',
			 data:{
				 "async":true, 
				 "pageIndex":pageIndex,
				 "pageSize":pageSize,
				 "name":$("#searchName").val()
			 },
			 success: function(data){
			 	//仅重新渲染list模块中的#mainContainer 数据信息这个div
				 $("#mainContainer").html(data);
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	}
	
	// 分页
	$.tbpage("#mainContainer", function (pageIndex, pageSize) {
		getUersByName(pageIndex, pageSize);
		_pageSize = pageSize;
	});
   
	// 搜索
	$("#searchNameBtn").click(function() {
		getUersByName(0, _pageSize);
	});
	
	// 获取添加用户的界面，初始化一个form
	$("#addUser").click(function() {
		$.ajax({ 
			 url: projectName + "/users/add",
			 success: function(data){
				 $("#userFormContainer").html(data);
		     },
		     error : function(data) {
		    	 toastr.error("error!");
		     }
		 });
	});
	
	// 获取编辑用户的界面
	$("#rightContainer").on("click",".blog-edit-user", function () { 
		$.ajax({ 
			 url: projectName + "/users/edit/" + $(this).attr("userId"),
			 success: function(data){
				 $("#userFormContainer").html(data);
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	});
	
	// 提交变更后，清空表单
	$("#submitEdit").click(function() {
		$.ajax({ 
			 url: projectName +  "/users",
			 type: 'POST',
			 data:$('#userForm').serialize(),
			 success: function(data){
				 $('#userForm')[0].reset();  
				 
				 if (data.success) {
					 // 从新刷新主界面
					 getUersByName(0, _pageSize);
				 } else {
					 toastr.error(data.message);
				 }

		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	});
	
	// 删除用户
	$("#rightContainer").on("click",".blog-delete-user", function () { 
		// 获取 CSRF Token（csrf token已经从header中dom解析的时候加载进来了）
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		
		
		$.ajax({ 
			 url: projectName + "/users/" + $(this).attr("userId") ,
			 type: 'DELETE', 
			 beforeSend: function(request) {
                 // 添加 CSRF Token 到http请求头中，SpringSecurity防护CSRF的核心过滤器会进行处理，得到token进行校验
                 request.setRequestHeader(csrfHeader, csrfToken);
             },
			 success: function(data){
				 if (data.success) {
					 // 从新刷新主界面
					 getUersByName(0, _pageSize);
				 } else {
					 toastr.error(data.message);
				 }
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	});
});