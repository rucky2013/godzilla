<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作界面-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="operation" class="operation">
	<div class="main">	
		<div class="head  clearfix">
        	<h1><a class="logo" hidden="index.html" title="回到首页">哥斯拉</a></h1>
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
            	<h2 id="tab1" class="current"><a href="jvascript:;" class="a1" title="工作空间">工作空间</a><a  href="jvascript:;" class="a2">管理权限</a></h2>
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
                    <td class="sp" width="8%"><a id="add_btn" href="javascript:;" value1="${uas.username}"><span class="edit">添加用户</span></a></td>
                    <td class="sp02" width="8%"><a id="edit_btn" href="javascript:;" value1="${uas.username}"><span class="edit">编辑工作台</span></a></td>
                  </tr>
                  </#list>
                  </tbody>
                </table>
                </div>
                <h4><a  href="#" class="btn2" title="更多信息">更多信息</a></h4>
            </div>
            
            <div id="shadow" class="checkBoxWrap shadow" style="display: block"></div>
			<div id="shadow_box1" class="shadow_box" style="display: block">
            		<h5>编辑工作台<span class="close" id="close1">关闭</span></h5>
                  	<div class="shadow_con">
                        <p class="uname">用户名：${editUsername}</p>
                        <ul class="check_con" name="chenk_con">
                        <style>
						
						    .checkBoxWrap{width:600px height:450px !important;margin-left:-300px !important;}
							.item_checkBox label{ cursor:pointer;display:inline-block;}
							.item_checkBox label input{ vertical-align:top;margin:6px 4px 0 0;*margin:0 4px 0 0;}
							.checkBoxWrap .shadow_btn{margin-left:230px;}
						</style>
	                        <#list userProjects as userPro>
		                    	<#if userPro.auth = '1'>
		                    	<li class="item_checkBox"><label><input name="pro" type="checkbox" checked="true" value="${userPro.projectCode}" />${userPro.projectCode}</label></li> 
		                    	<#else>
		                    	<li class="item_checkBox"><label><input name="pro" type="checkbox" value="${userPro.projectCode}" />${userPro.projectCode}</label></li> 
		                    	</#if>
	                    	</#list>
                        </ul>
                        <input id="conform_btn" type="button" class="shadow_btn mar150_l" value="确定" />
                      </div>
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
	//编辑工作台-->确定
	$("#conform_btn").on("click", function() {
		
		var selectProjects = '';
		var editUsername = '${editUsername}';
		$('input:checkbox[name=pro]:checked').each(function(i){
			selectProjects += $(this).val()+',';
		});
       	$.ajax({
            type: "POST",
            url: "/${basePath}/user/${sid}/editWorkDesk.do",
            data: {
                selectProjects: selectProjects,
                editUsername: editUsername,
            },
            dataType: "json",
            success: function(data) {
				if(data=="SUCCESS") {
					window.location.href = "/${basePath}/user/${sid}/userAuthList.do";
				} else {
					alert("提交失败");
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
		window.location.href = "/${basePath}/${sid}/userAuthList.do"
	}
}
</script>
</body>
</html>
