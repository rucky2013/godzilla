<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页-哥斯拉</title>
<link type="text/css" href="css/meta.css" rel="stylesheet"/>
</head>
<body id="index" class="index">
	<div class="main clearfix">	
		<div class="head  clearfix">
        	<h1><a class="logo" hidden="index.html" title="回到首页">哥斯拉</a></h1>
            <div class="r">你好，刘宝剑！<a href="#" title="退出系统" class="btn1">退出</a></div>
        </div>
        <div class="mainCon clearfix">
        	<div class="mainConL l">
            	<h3>个人信息</h3>
                <table>
                  <tr>
                    <td>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
                    <td>刘宝剑</td>
                  </tr>
                  <tr>
                    <td>登录时间：</td>
                    <td>2015-05-30</td>
                  </tr>
                  <tr>
                    <td>部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门：</td>
                    <td>财务与结算中心</td>
                  </tr>
                  <tr>
                    <td>上次操作：</td>
                    <td>2015-05-30</td>
                  </tr>
                </table>
            </div>
        	<div class="mainConR r">
            	<h2 id="tab1" class="current clearfix"><a href="jvascript:void(0)" class="a1" title="工作空间">工作空间</a><a  href="jvascript:void(0)" class="a2" title="管理权限">管理权限</a></h2>
            	<table width="200" border="0" class="table1">
                  <tr>
                    <#list projects as item>
                    	<td><a href="project/check.html?projectCode=${item.projectCode}&profile=test" title="${item.projectCode}"><span></span>${item.projectName}</a></td>
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
                        <td>${log.operateCode}</td>
                        <td class="fail">${log.executeResult}</td>
                        <td>${log.resultInfo}</td>
                      </tr>
                     </#list>
                      
                      </tbody>
                    </table>
                </div>
                <h4><a  href="#" class="btn2" title="更多信息">更多信息</a></h4>
            </div>
        </div>
	</div>
<script src="js/jquery-1.8.2.min.js"></script>
<script src="js/common.js"></script>
</body>
</html>
