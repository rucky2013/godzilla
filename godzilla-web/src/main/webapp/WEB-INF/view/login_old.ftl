<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作界面-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="operation" class="operation">
	<div class="main">	
		<div class="head  clearfix">
        	<h1><a class="logo" href="#" title="回到首页">哥斯拉</a></h1>
            <div class="r">您好，请登录<a id="showlogin" href="#" title="登陆系统" class="btn1"  >登陆</a></div>
        </div>
        <div class="mainCon clearfix">
        	
            <div id="shadow" class="shadow"></div>
            <div id="shadow_box" class="shadow_box">
                  <h5>用户登陆<span id="close" class="close">关闭</span></h5>
                  	<div class="shadow_con">
                  		<div id="messagebox" class="user_con clearfix">
                         		  <label id="message">&nbsp;</label>
                         </div>
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
<script src="/${basePath}/js/md5.js"></script>
<script>
//回车按键登录
//document.getElementById('shadow_box').onkeydown=keyDownSearch;  
document.onkeydown=keyDownSearch;
function keyDownSearch(e) {    
    // 兼容FF和IE和Opera    
    var theEvent = e || window.event;    
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;    
    if (code == 13) {    
        //alert('回车');//具体处理函数    
        dologin();
        return false;    
    }    
    return true;    
}  
$(function(){
	
	//显示登录框
	$("#showlogin").on("click", function(){
		showlogin();
	});
	$("#message").text(" ");
	$("#username").focus();
	
	$("#username").on("focus", function() {
		$("#message").text(" ");
	});
	
	$("#password").on("focus", function() {
		$("#message").text(" ");
	});
	//登录
	$("#login").on("click", function() {
		dologin();
	});
})

function dologin() {
	var username = $("#username").val();
	var password = $("#password").val();
	
	if(isEmpty(username)) {
		$("#message").text("*用户名为空");
	} else if(isEmpty(password)) {
		$("#message").text("*密码为空");
	} else {
		password = hex_md5(password);
		//alert(password);
		window.location.href = '/${basePath}/user/login/' + username + '/' + password + '.do';
		/*$.ajax({
			type : "GET",
			url : '/${basePath}/user/login/' + username + '/' + password + '.do',
			dataType : "json",
			data : {
			},
			success : function(data) {
				if (data == "SUCCESS") {

				} else {
					alert("failed");
				}
			}
		});*/
	}
}
function isEmpty(obj) {
	if(obj== null || obj =='') {
		return true;		
	}
	return false;
}

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
