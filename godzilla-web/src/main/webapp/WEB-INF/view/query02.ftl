<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置查询02-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>


</head>
<body id="query" class="query">
<input type="hidden" name="projectCode" id="projectCode" value="${projectCode}" />
<input type="hidden" name="sid" id="sid" value="${sid}" />

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
				 <a class="backindex" href="/${basePath}/project/${sid}/${projectCode}/TEST/projectConfig.do" title="${projectCode}"><h3 class="location">当前应用：${projectCode}</h3></a>

              <form id="search_form" action="/${basePath}/prop/${sid}/${projectCode}/queryProp.do" method="POST">
              		<fieldset>
                  	<label>提交人：</label><input type="text" name="createBy" placeholder="输入内容" value="${createBy}" />
                	<label>环境：</label>
                	<select name="selectedProfile">
                		<option value="" selected="selected">All</option>
                		<#list profileList?keys as key>
							<option value="${profileList[key]}">${key}</option>
                    	</#list>
              		</select>
              		
              		<input type="submit" name="submit" value="查询" />
                	<!-- <a href="javascript:;" class="search">查询</a>-->
                  </fieldset>
              		
              </form>
              
              <div class="table2">
                <table width="100%">
                <thead>
                  <tr>
                    <th width="20%">配置项名称</th>
                    <th width="40%">配置值</th>
                    <th width="10%">提交人</th>
                    <th width="15%">应用名称</th>
                    <th width="15%">环境</th>
                  </tr>
                 </thead>
                 <tbody>
                 	<#list propList as prop>
             		  <tr>
	                    <td>${prop.proKey}</td>
	                    <td>${prop.proValue}</td>
	                    <td>${prop.createBy}</td>
	                    <td>${prop.projectCode}</td>
	                    <td>
	                    <#if prop.profile == 'TEST'>
	                    	测试环境
						<#elseif prop.profile == 'PRODUCT'>
							生产环境
						<#elseif prop.profile == 'QUASIPRODUCT'>
							 准生产环境
						</#if>  
	                    </td>
	                  </tr>
                 	</#list>
                  
                  </tbody>
                </table>
                </div>
                <!--<h4><a  href="#" class="btn2" title="更多信息">更多信息</a></h4>-->
            </div>
        </div>
	</div>
<script src="/${basePath}/js/jquery-1.8.2.min.js"></script>
<script src="/${basePath}/js/common.js"></script>

<script>
$(document).ready(function(){
	// 退出
    $("#logout").on("click", function() {
		window.location.href = '/${basePath}/user/logout/${sid}.do';
	});
})
</script>
</body>
</html>
