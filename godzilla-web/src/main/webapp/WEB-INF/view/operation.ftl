<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作界面-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="operation" class="operation">
		<div class="main">
		<div class="head  clearfix">
        	<h1><a class="logo" href="/${basePath}/user/${sid}/home.do" title="回到首页">哥斯拉</a></h1>
            <div class="r">你好，${user.userName}！<a id="logout" href="#" title="退出系统" class="btn1">退出</a></div>
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
					<a href="javascript:void(0);" class="a2" title="管理权限">管理权限</a>
					</#if>
				</h2>

                <div class="table2">
                <table width="100%">
                <thead>
                  <tr>
                    <th width="15%">编号</th>
                    <th width="15%">用户</th>
                    <th width="54%">工作台组</th>
                    <th width="16%" colspan="2">操作</th>
                  </tr>
                 </thead>
                 <tbody>
                 <#list userAuthList as uas>
                  <tr>
                    <td>${uas.index}</td>
                    <td>${uas.username}</td>
                    <td width="55%" style="text-align:left;">
                    <#list uas.projects as pro>
                    	${pro.projectCode},
                    </#list>
                    </td>
                    <td class="sp" width="8%"><a class="add_btn" href="javascript:;" ><span class="edit">添加用户</span></a></td>
                    <td class="sp02" width="8%"><a href="/${basePath}/user/${sid}/editWorkDesk.do?editUsername=${uas.username}" ><span class="edit">编辑工作台</span></a></td>
                  </tr>
                  </#list>
                  </tbody>
                </table>
                </div>
                <h4><a  href="#" class="btn2" title="更多信息">更多信息</a></h4>
            </div>
            
            <div id="shadow" class="shadow" style="display: none"></div>
            <div id="shadow_box2" class="shadow_box">
                  <h5>添加新用户<span id="close2" class="close">关闭</span></h5>
                  <form action="" class="clearfix">
                  	<div class="shadow_con">
                    		<div class="user_con clearfix">
                         		  <label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label>
                         		  <input id="username" type="text" name="username" placeholder="输入内容" />        
                         </div>
                         <div class="user_con clearfix">
                         		  <label>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
                         		  <input id="password" type="text" name="password" placeholder="输入密码" />        
                         </div>
                         <div class="user_con clearfix">
                         		  <label>再次确认：</label>
                         		  <input id="confirm" type="password" name="confirm" placeholder="输入密码" />        
                               <!--<div class="tips">两次密码不一致</div>-->       
                         </div>
                        
                        <input id="add_submit" type="button" class="shadow_btn mar150_l" value="确定" />
                      </div>
                  </form>
                  
                	
            </div>
        </div>
        <div class="footerWrap">
            <div class="footer">
                <p>
                   </span>
                </p>
            </div>
        </div>
	</div>
<script src="/${basePath}/js/jquery-1.8.2.min.js"></script>
<script src="/${basePath}/js/common.js"></script>

<script>
$(document).ready(function() {
	// 退出
    $("#logout").on("click", function() {
		window.location.href = '/${basePath}/user/logout/${sid}.do';
	});
	//添加新用户 页面
	$(".add_btn").on("click", function() {
		showWindow(2);
	});
	//提交 用户
	$("#add_submit").on("click", function() {
		var username = $("#username").val();
		var password = $("#password").val();
		var confirm = $("#confirm").val();
		
		$.ajax({
	            type: "GET",
	            url: "/${basePath}/user/${sid}/addUser.do",
	            data: {
	                username: username,
	                password: password,
	                confirm: confirm,
	            },
	            dataType: "json",
	            success: function(data) {
					if(data=="SUCCESS") {
						window.location.href = "/${basePath}/user/${sid}/userAuthList.do";
					} else {
						alert("添加失败");
					}
				}
	        });
	});
	
});
</script>
<script>
function shadowClose(index) {
	var oClose = document.getElementById('close' + index);
	var oShadow = document.getElementById('shadow');
	var oShadowBox = document.getElementById('shadow_box' + index);
	oClose.onclick = function() {
		oShadow.style.display = 'none';
		oShadowBox.style.display = 'none';
	}
}
window.onload = function() {
	//关闭弹出层
	shadowClose(2);
	document.getElementById('close' + '2').click();
}
function showWindow(index) {
	var oShadow = document.getElementById('shadow');
	var oShadowBox = document.getElementById('shadow_box' + index);
	oShadow.style.display = '';
	oShadowBox.style.display = '';
}
</script>
</body>
</html>
