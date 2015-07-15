<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Xuanyuan-哥斯拉</title>
<link type="text/css" href="http://localhost:8080/godzilla-web/css/meta.css" rel="stylesheet"/>
</head>
<body id="gesila1">
	<div class="main">	
		<div class="head clearfix">
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
            	<h2 id="tab1" class="current"><a href="../index.html" class="a1" title="工作空间">工作空间</a><a href="jvascript:void(0)" class="a2" title="管理权限">管理权限</a></h2>
            	<h3 class="location">当前应用：${projectCode}</h3>
            	<h3>操作分支：${projPrivate.currentBranchUrl}</h3></br>
            	
            	<#if projPrivate.ifVirtual==0>
            		<h4><a  href="#" class="btn2" title="开启虚拟主干">开启虚拟主干</a></h4>
            	<#elseif projPrivate.ifVirtual==1>	
            		<h3>虚拟主干：${projPrivate.virtualTruckUrl}</h3>
            		<h4><a  href="#" class="btn2" title="关闭虚拟主干">关闭虚拟主干</a></h4>
            	</#if>
                <ul id="tab2" class="clearfix">
                	<li <#if profile == "test">class="current"</#if> style="border-left:0"><a href="../project/check.html?projectCode=cupid&profile=test" class="current" title="测试环境">测试环境</a></li>
                    <li <#if profile == "pro_deploy">class="current"</#if> ><a href="../project/check.html?projectCode=cupid&profile=pro_deploy" title="预发标准环境">预发标准环境</a></li>
                    <li <#if profile == "online">class="current"</#if>><a href="../project/check.html?projectCode=cupid&profile=online" title="生产标准环境">生产标准环境</a></li>
                 </ul>
                 <ul id="tabCon2">
                 	<li style="display:block">
                        <div class="table2">
                            <table width="100%" border="0">
                            <thead>
                              <tr>
                                <th colspan="2" align="left">godzilla@${remoteIp}</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <td width="80" class="paddingR0">部署操作：</td>
                                <td class="bg1"><span class="spanArrange"><a href="javacript:;" title="部署">部署</a></span><span class="spanUseAgain"><a href="#" id="restart" title="重新启用">重新启用</a></span></td>
                              </tr>
                              <tr>
                                <td class="paddingR0">SVN操作：</td>
                                <td class="bg1"><span class="spanViewState"><a href="javacript:;" title="查看状态">查看状态</a></span><span class="spanMerge"><a href="javacript:;" title="合并代码">合并代码</a></span><span class="spnSubmit"><a href="javacript:;" title="提交主干">提交主干</a></span></td>
                              </tr>
                              <tr>
                                <td class="paddingR0">设置操作：</td>
                                <td class="bg1"><span class="spanSoureCode"><a href="javacript:;" title="源代码设置">源代码设置</a></span><span class="spanBranch"><a href="javacript:;" title="分之设置">分支设置</a></span></td>
                              </tr>
                              <tr>
                                <td class="paddingR0">配置管理：</td>
                                <td class="bg1"><span class="spanAdd"><a href="javacript:;" title="配置添加">配置添加</a></span><span class="spanQuery"><a href="javacript:;" title="配置查询">配置查询</a></span><span class="spanExamine"><a href="javacript:;" title="配置审核">配置审核</a></span></td>
                              </tr>
                              </tbody>
                            </table>
                        </div>
                    </li>
                    <li>t2</li>
                    <li>t3</li>
                 </ul>
                <h4 class="title">源代码信息列表</h4>
                <table width="100%" border="0" class="table2">
                <thead>
                  <tr>
                    <th>源代码svn路径</th>
                    <th>项目英文名称</th>
                  </tr>
                 </thead>
                 <tbody>
                  <tr>
                    <td width="310">${repositoryUrl}</td>
                    <td width="216">${projectCode}</td>
                  </tr>
                  </tbody>
                </table>
                <h4 class="title">分支设置</h4>
                <table width="100%" border="0" class="table2">
                <thead>
                  <tr>
                    <th>分支路径</th>
                    <th>设置人</th>
                    <th>项目名称</th>
                    <th>设置时间</th>
                    <th>操作</th>
                  </tr>
                 </thead>
                 <tbody>
                 <#list svnBranchConfigs as branch>
                  <tr>
                    <td width="220"><small>${branch.branchUrl}</small></td>
                    <td>${branch.createBy}</td>
                    <td>${branch.projectCode}</td>
                    <td>${branch.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td><a href="javascript:viod(0);" title="编辑">编辑</a>&nbsp;&nbsp;<a href="javascript:viod(0);" title="编辑">选定</a>&nbsp;&nbsp;<a href="javascript:viod(0);" title="编辑">设为虚拟主干</a></td>
                  </tr>
                  </#list>
                  </tbody>
                </table>
                <h4 class="title">部署日志</h4>
                <div id="recordTolls">
                	<ul>
                    	<li class="l"><span>部署状态：${projStatus.currentStatus}</span><span class="progress"><strong style="width:${projStatus.processRate}%;"></strong></span><span>${projStatus.processRate}%</span></li>
                        <li class="r sp02"><a href="javascript:;" class="r tools1"><span class="edit">比较部署包信息</span></a><a href="javascript:;" class="r tools2"><span class="edit">比较部署包信息</span></a><a href="javascript:;" class="r tools3"><span class="edit">比较部署包信息</span></a></li>
                    </ul>
                <table width="100%" border="0" class="table2">
                <thead>
                  <tr>
                    <th width="180">部署时间</th>
                    <th>部署人</th>
                    <th>操作</th>
                    <th>部署结果</th>
                    <th>备注</th>
                  </tr>
                 </thead>
                 <tbody>
                  <#list operateLogs as log>
                  <tr>
                    <td>${log.executeTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${log.userName}</td>
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
	
			<div>
             <div style="z-index: 99999;" id="shadow"></div>
             <div style="z-index: 99999;width:50%;" id="shadow_box">
                  <h5>Console<span id="close">关闭</span></h5>
                  <form action="" class="clearfix">
                  	<div class="shadow_con">
                    		
                         <div class="clearfix">
                         		  <div>2015-07-09 17:19:15.392 167026968 [http-nio-8080-exec-25] INFO  c.c.f.c.c.n.filter.ParameterFilter - COST 调用方法为method:com.cupid.fso.app.message.detail; 调用终端为 useragent:cupid-an-sdk-java; 服务器逻辑花费时间cost time:0 millionsecond; 0 seconds;</div>
                         		  <div>2015-07-09 17:19:15.392 167026968 [http-nio-8080-exec-25] INFO  c.c.f.c.c.n.filter.ParameterFilter - COST 调用方法为method:com.cupid.fso.app.message.detail; 调用终端为 useragent:cupid-an-sdk-java; 服务器逻辑花费时间cost time:0 millionsecond; 0 seconds;</div>
                         <div>2015-07-09 17:19:15.392 167026968 [http-nio-8080-exec-25] INFO  c.c.f.c.c.n.filter.ParameterFilter - COST 调用方法为method:com.cupid.fso.app.message.detail; 调用终端为 useragent:cupid-an-sdk-java; 服务器逻辑花费时间cost time:0 millionsecond; 0 seconds;</div>
                         </div>
                        
                        <input type="submit" class="shadow_btn mar150_l" value="确定" />
                      </div>
                  </form>
             </div>
	   	    </div>
	
<input type="hidden" name="projectCode" id="projectCode" value="${projectCode}" />
<input type="hidden" name="currentBranchUrl" id="currentBranchUrl" value="${projPrivate.currentBranchUrl}" />
<input type="hidden" name="virtualTruckUrl" id="virtualTruckUrl" value="${projPrivate.virtualTruckUrl}" />
<input type="hidden" name="ifVirtual" id="ifVirtual" value="${projPrivate.ifVirtual}" />
<input type="hidden" name="profile" id="profile" value="${profile}" />
<input type="hidden" name="remoteIp" id="remoteIp" value="${remoteIp}" />

<script src="../js/jquery-1.8.2.min.js"></script>
<script src="../js/common.js"></script>

<script>

function restart(){

	var ip = $("#remoteIp").val();
	alert(ip);
}

</script>

<script>

$(function(){

    $('#restart').click(function(){
    	alert("1111");

         $.ajax({

             type: "GET",

             url: "../tomcat/restart.html",

             data: {ip:$("#remoteIp").val()},

             dataType: "json",

             success: function(data){

                      

                      }

         });

    });

});
  
</script>
<script>
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
</script>
</body>
</html>
