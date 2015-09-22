<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>哥斯拉后台开发系统</title>
	<link rel="stylesheet" type="text/css" href="/${basePath}/css/meta.css">
</head>
<body>
	<div class="login-win-body">
		<div class="header"><h1>哥斯拉后台开发系统</h1></div>
		<div class="bodyMain-wrap">
			<div class="login-warp">
				<div class="login-box">
						<form class="loginForm" action="" method="POST">
						<div id="UnameMsg" class="accountNum item-div" >
							<s class="userName_icon"></s>
							<input id="username" type="text" class="inputBox userName" placeholder="请输入用户名" />
						</div>
						<div id="PwdMsg" class="passWord item-div">
							<s class="UserpassWord_icon"></s>
							<input id="password" type="password" class="inputBox UserpassWord" maxlength="16" placeholder="密码"/>
						</div>
						<div class="resetWrap_psd item-div">
							<!--<a href="javascript:void(0);" target="_blank" class="resetPassword">密码重置</a>-->
						</div>
						<div class="submitFieldset item-div">
							<input id="login" type="button" class="btn" value="登 录"/>
						</div>
						</form>
				</div>
			</div>
		</div>
	</div>		
	
</body>
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

/*window.onload=function(){
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
}*/
</script>
<script type="text/javascript">
	$(function(){
		$('.inputBox').focus(function(){
			$(this).parent().addClass('input_onfocus');
		}).blur(function(){
			$(this).parent().removeClass('input_onfocus');
		})
	})
</script>
</html>