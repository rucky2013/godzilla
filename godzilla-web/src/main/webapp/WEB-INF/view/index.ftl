<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工作台-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
<link rel="shortcut icon" href="/${basePath}/img/gesilla.jpg">
</head>
<body id="index" class="index">
		<div class="main">
		<div class="head  clearfix">
        	<h1><a class="logo" href="/${basePath}/user/${sid}/home.do" title="回到首页">哥斯拉</a></h1>
            <div class="r">你好，${user.userName}！<a id="changepassword" href="javascript:void(0);" title="修改密码" class="btn1">修改密码</a><a id="logout" href="javascript:void(0);" title="退出系统" class="btn1">退出</a></div>
        </div>
        <div class="mainCon clearfix">
        	<div class="mainConL l">
            	<h3>个人信息</h3>
                <table>
                  <tr>
                    <td>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
                    <td>${user.userName}</td>
                  </tr>
                  <tr>
                    <td>登录时间：</td>
                    <td>${user.loginTime?string("yyyy-MM-dd")!''}</td>
                  </tr>
                  <tr>
                    <td>部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门：</td>
                    <td>${user.departName!''}</td>
                  </tr>
                  <tr>
                    <td>上次操作：</td>
                    <td>${user.lastOperation!user.loginTime?string("yyyy-MM-dd")}</td>
                  </tr>
                </table>
            </div>
			<div class="mainConR r">
				<h2 id="tab1" class="current">
					<a href="/${basePath}/user/${sid}/home.do" class="a1" title="工作空间">工作空间</a>
					<#if user.isAdmin = 1>
					<a href="/${basePath}/user/${sid}/userAuthList.do" class="a2" title="管理权限">管理权限</a>
					<#else>
					<!-- <a href="javascript:void(0);" class="a2" title="管理权限">管理权限</a> -->
					</#if>
				</h2>

				<table width="200" border="0" class="table1">
                  		<tr>
	                    <#list projects as item>
	                    	<#if (item_index+1) % 4 == 0>
		                    		<td><a href="/${basePath}/project/${sid}/${item.projectCode}/TEST/projectConfig.do" title="${item.projectCode}"><span></span>
									${item.projectName}</a></td>
	                    			</tr><tr>
	                  		<#else>
	                    			<td><a href="/${basePath}/project/${sid}/${item.projectCode}/TEST/projectConfig.do" title="${item.projectCode}"><span></span>
									${item.projectName}</a></td>
	                  		</#if>
	                    </#list>
                		</tr>
                </table>
                <div class="table2">
                    <table>
                    <thead>
                      <tr>
                        <th>操作时间</th>
                        <th>操作应用</th>
                        <th>执行动作</th>
                        <th>执行结果</th>
                        <th>备注</th>
                      </tr>
                     </thead>
                     <tbody>
                     
                     <#list logs as log>
                     	<tr>
                        <td>${log.executeTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td>${log.projectCode}</td>
                        <td>${log.operation}</td>
                        <#if log.executeResult == 1>
							<td class="fail">成功</td>
						<#elseif log.executeResult == -2>
							<td class="fail">失败</td>
						</#if>
                        <td>${log.resultInfo}</td>
                      </tr>
                     </#list>
                      
                      </tbody>
                    </table>
                </div>
                <!--<h4><a  href="javascript:void(0);" class="btn2" title="更多信息">更多信息</a></h4>-->
            </div>
        </div>
	</div>
	<div id="shadow_box1" class="shadow_box" style="display: none">
				<!--<h5>
					console<span id="close1" class="close">关闭</span>
				</h5>
				<div class="shadow_con">
					<div id="messagebox" class="user_con clearfix"></div>
				</div>-->
				<h5>
					更改密码<span class="close" id="close1">关闭</span>
				</h5>
				<div class="shadow_con">
					<div class="user_con clearfix">
						<label>旧密码：</label> <input id="oldpassword" type="text" name="oldpassword" /><label id="errormsg1"></label>
						<label>新密码：</label> <input id="password" type="text" name="password" /><label id="errormsg2"></label>
						<label>确认新密码：</label> <input id="confirm" type="text" name="confirm" /><label id="errormsg3"></label>
					</div>
					<input id="dochange" type="button" class="shadow_btn mar150_l" value="提交" />
				</div>
			</div>
			
			
			<script>
		function shadowClose(index) {
			var oClose = document.getElementById('close' + index);
			var oShadowBox = document.getElementById('shadow_box' + index);
			oClose.onclick = function() {
				oShadowBox.style.display = 'none';
			}
		}
		window.onload = function() {
			//关闭弹出层
			shadowClose(1);
			document.getElementById('close' + '1').click();
		}
		
		function showWindow(index) {
			var oShadowBox = document.getElementById('shadow_box' + index);
			oShadowBox.style.display = '';
		}
		function hideWindow(index) {
			var oShadowBox = document.getElementById('shadow_box' + index);
			oShadowBox.style.display = 'none';
		}
	</script>
<script src="/${basePath}/js/jquery-1.8.2.min.js"></script>
<script src="/${basePath}/js/common.js"></script>
<script src="/${basePath}/js/md5.js"></script>
<script>
$(document).ready(function() {
	// 退出
    $("#logout").on("click", function() {
		window.location.href = '/${basePath}/user/logout/${sid}.do';
	});
	$("#oldpassword").live("focus", function() {
		$("#errormsg1").text("");
	});
	$("#password").live("focus", function() {
		$("#errormsg2").text("");
		$("#errormsg3").text("");
	});
	$("#confirm").live("focus", function() {
		$("#errormsg2").text("");
		$("#errormsg3").text("");
	});
	
	$("#changepassword").on("click", function() {
		showWindow(1);
	});
	
	//修改密码
	$("#dochange").on("click", function() {
		$("#errormsg1").text("");
		$("#errormsg2").text("");
		$("#errormsg3").text("");
		
		var oldpassword = $("#oldpassword").val();
		var password = $("#password").val();
		var confirm = $("#confirm").val();
		oldpassword = oldpassword.replace(/(^\s*)|(\s*$)/g,"");
		password = password.replace(/(^\s*)|(\s*$)/g, "");
		confirm = confirm.replace(/(^\s*)|(\s*$)/g, "");
		
		if(isEmpty(oldpassword)) {
			$("#errormsg1").text("*密码为空");
		}
		
		if(isEmpty(password)) {
			$("#errormsg2").text("*密码为空");
		}
	
		if(oldpassword == password) {
			$("#errormsg1").text("*新旧密码相同");
			return ;
		}
		if(!(confirm == password)) {
			$("#errormsg2").text("*两次输入密码不同");
			$("#errormsg3").text("*两次输入密码不同");
			return ;
		}
		
		password = hex_md5(password);
		oldpassword = hex_md5(oldpassword);
		
		$.ajax({
			type: "post",
			url: '/${basePath}/user/${sid}/changePassword.do',
			data: {
				password: password,
				oldpassword: oldpassword,
			},
			dataType: "json",
			success: function(data) {
                    $("#alert").css("display", "block");
					$("#alert_title").text(data.returnmsg);
					$("#alert_text").text(data.returnmemo);
			}
		});
		//alert 弹出框关闭
		$("#alert_close").on("click", function() {
			$("#alert").css("display", "none");
			window.location.href = '/${basePath}/user/${sid}/home.do';
		});
	});
});
function isEmpty(obj) {
	if(obj== null || obj =='') {
		return true;		
	}
	return false;
}
</script>
<div id="alert" class="shadow_con" style="display:none">


				<style>
			.alert_wrap {
				overflow: hidden;
			}
			
			.alert_wrap span, .alert_wrap p {
				<!--float: left;-->
				display: block;
				text-align: left;
				font-size: 14px;
			}
			
			.alert_wrap span {
				<!--text-align: left;-->
				height: 32px;
				width: 100px;
			}
			
			.alert_wrap p {
				height: auto;
				width: 340px;
			}
			#alert{position:absolute;left:50%;top:50%;padding:20px 10px; z-index:999;width:380px;margin-left:-200px;height:auto;min-height:92px;background:#fff;border:2px solid #ddd;}
			</style>
			
				<div class="alert_wrap">
					<span id="alert_title"></span>
					<p id="alert_text"></p>
				</div>
			
				<input id="alert_close" type="button" class="shadow_btn mar150_l" value="确定" />
			</div>
</body>

</html>
