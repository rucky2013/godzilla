<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作界面-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="operation" class="operation">
	<div class="main">	
		<div class="head  clearfix">
        	<h1><a class="logo" hidden="index.html" title="回到首页">哥斯拉</a></h1>
            <div class="r">您好，请登录<a href="#" title="登陆系统" class="btn1" onclick="showlogin();" >登陆</a></div>
        </div>
        <div class="mainCon clearfix">
        	
            <div id="shadow" class="shadow"></div>
            <div id="shadow_box" class="shadow_box">
                  <h5>用户登陆<span id="close">关闭</span></h5>
                  	<div class="shadow_con">
                    		<div class="user_con clearfix">
                         		  <label>用户名：</label>
                         		  <input id="username" type="text" name="username" placeholder="邮箱前缀" />        
                         </div>
                         <div class="user_con clearfix">
                         		  <label>密&nbsp;&nbsp;码：</label>
                         		  <input id="password" type="password" name="password" placeholder="输入密码" />        
                         </div>
                        
                        <input id="login" type="button" class="shadow_btn mar150_l" value="登陆" />
                      </div>
            </div>
        </div>
	</div>
<script src="/${basePath}/js/jquery-1.8.2.min.js"></script>
<script src="/${basePath}/js/common.js"></script>
<script>
$(function(){

	$("#login").on("click", function() {
		var username = $("#username").val();
		var password = $("#password").val();
		
		window.location.href = '/${basePath}/user/login/' + username + '/' + password + '.do';
	});

})

window.onload=function(){
	//关闭弹出层
	function shadowClose(){
		var oClose=document.getElementById('close');
		var oShadow=document.getElementById('shadow');
		var oShadowBox=document.getElementById('shadow_box');
		oClose.onclick=function(){
			oShadow.style.display='none';	
			oShadowBox.style.display='none';	
		}
	}
	shadowClose();
}
function showlogin(){
	var oShadow=document.getElementById('shadow');
	var oShadowBox=document.getElementById('shadow_box');
	oShadow.style.display='';	
	oShadowBox.style.display='';	
}
</script>
</body>
</html>
