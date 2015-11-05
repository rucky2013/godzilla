<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div class="main">
		<div class="head clearfix">
			<h1>
				<a class="logo" hidden="index.html" title="回到首页">哥斯拉</a>
			</h1>
			<div class="r">
				你好，刘宝剑！<a href="#" title="退出系统" class="btn1">退出</a>
			</div>
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
				<h2 id="tab1" class="current">
					<a href="../index.html" class="a1" title="工作空间">工作空间</a><a
						href="jvascript:void(0)" class="a2" title="管理权限">管理权限</a>
				</h2>
				<h3 class="location">当前应用：${projectCode}</h3>

				<ul id="tab2" class="clearfix">
					<#if profile = 'TEST'>
					<li class="current" style="border-left: 0"><a
						href="/${basePath}/project/${sid}/${projectCode}/TEST/projectConfig.do"
						title="日常环境">日常环境</a></li>
					<li><a
						href="/${basePath}/project/${sid}/${projectCode}/QUASIPRODUCT/projectConfig.do"
						title="预发标准环境">预发标准环境</a></li>
					<li><a
						href="/${basePath}/project/${sid}/${projectCode}/PRODUCT/projectConfig.do"
						title="生产标准环境">生产标准环境</a></li> <#elseif profile = 'QUASIPRODUCT'>
					<li><a
						href="/${basePath}/project/${sid}/${projectCode}/TEST/projectConfig.do"
						title="日常环境">日常环境</a></li>
					<li class="current" style="border-left: 0"><a
						href="/${basePath}/project/${sid}/${projectCode}/QUASIPRODUCT/projectConfig.do"
						title="预发标准环境">预发标准环境</a></li>
					<li><a
						href="/${basePath}/project/${sid}/${projectCode}/PRODUCT/projectConfig.do"
						title="生产标准环境">生产标准环境</a></li> <#elseif profile = 'PRODUCT'>
					<li><a
						href="/${basePath}/project/${sid}/${projectCode}/TEST/projectConfig.do"
						title="日常环境">日常环境</a></li>
					<li><a
						href="/${basePath}/project/${sid}/${projectCode}/QUASIPRODUCT/projectConfig.do"
						title="预发标准环境">预发标准环境</a></li>
					<li class="current" style="border-left: 0"><a
						href="/${basePath}/project/${sid}/${projectCode}/PRODUCT/projectConfig.do"
						title="生产标准环境">生产标准环境</a></li> </#if>
				</ul>
				<ul id="tabCon2">
					<li style="display: block">
						<div class="table2">
							<table width="100%" border="0">
								<thead>
									<tr>
										<th colspan="2" align="left">godzilla@${clientConfig.remoteIp}</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td width="80" class="paddingR0">部署操作：</td>
										<td class="bg1"><span class="spanArrange"><a
												id="deploy" href="javacript:;" value1="" title="部署">部署</a></span><span
											class="spanUseAgain"><a href="#" id="restart"
												title="重新启用">重新启用</a></span></td>
									</tr>
									<tr>
										<td class="paddingR0">SVN操作：</td>
										<td class="bg1"><span class="spanViewState"><a
												id="show" href="javacript:;" title="查看状态">查看状态</a></span> <span
											class="spanMerge"><a id="merge" href="javacript:;"
												title="合并代码">合并代码</a></span> <span class="spnSubmit"><a
												id="commit" href="javacript:;" title="提交主干">提交主干</a></span></td>
									</tr>
									<tr>
										<td class="paddingR0">设置操作：</td>
										<td class="bg1"><span class="spanSoureCode"><a
												id="src_a" href="javacript:;" title="源代码设置">源代码设置</a></span> <span
											class="spanBranch"><a id="branch_a" href="javacript:;"
												title="分之设置">分支设置</a></span></td>
									</tr>
									<tr>
										<td class="paddingR0">配置管理：</td>
										<td class="bg1"><span class="spanAdd"><a
												class="prop_btn" value1="add"
												href="/${basePath}/prop/${sid}/${projectCode}.do"
												title="配置添加">配置添加</a></span><span class="spanQuery"><a
												class="prop_btn" value1="query"
												href="/${basePath}/prop/${sid}/${projectCode}/queryProp.do"
												title="配置查询">配置查询</a></span><span class="spanExamine"><a
												class="prop_btn" value1="verify"
												href="/${basePath}/prop/${sid}/${projectCode}/verifyProp.do"
												title="配置审核">配置审核</a></span></td>
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
							<th>源代码存放路径</th>
							<th>当前版本号</th>
							<th>部署版本号</th>
						</tr>
					</thead>
					<tbody>
						<#if project.repositoryUrl = ''>
						<tr>
							<td width="216"><small></small></td>
							<td width="216"><small></small></td>
							<td></td>
							<td></td>
						</tr>
						<#else>
						<tr>
							<td width="216"><small>${project.repositoryUrl}</small></td>
							<td width="216"><small>${project.checkoutPath}</small></td>
							<td>${project.version}</td>
							<td>${project.deployVersion}</td>
						</tr>
						</#if>

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
							<td width="310"><small>${branch.branchUrl}</small></td>
							<td>${branch.createBy}</td>
							<td>${branch.projectCode}</td>
							<td>${branch.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
							<td><a href="javascript:viod(0);" title="编辑">编辑</a></td>
						</tr>
						</#list>
					</tbody>
				</table>
				<h4 class="title">部署日志</h4>
				<div id="recordTolls">
					<ul>
						<li class="l"><span>部署状态：<!--$/{projStatus.currentStatus}--></span><span
							class="progress"><strong
								style="width: $/{projStatus.processRate"></strong></span><span>
								<!--$/{projStatus.processRate}-->%
						</span></li>
						<li class="r sp02"><a href="javascript:;" class="r tools1"><span
								class="edit">比较部署包信息</span></a><a href="javascript:;"
							class="r tools2"><span class="edit">比较部署包信息</span></a><a
							href="javascript:;" class="r tools3"><span class="edit">比较部署包信息</span></a></li>
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
				<h4>
					<a href="#" class="btn2" title="更多信息">更多信息</a>
				</h4>
			</div>
		
			<div id="shadow1" style="display: none"></div>
			<div id="shadow_box1" style="display: none">
				<h5>
					console<span id="close1">关闭</span>
				</h5>
				<div class="shadow_con">
					<div id="messagebox" class="user_con clearfix"></div>
				</div>
			</div>
			
			
			<div id="shadow2"></div>
			<div id="shadow_box2">
				<h5>
					源代码设置->add<span id="close2">关闭</span>
				</h5>
				<div class="shadow_con">
	
					<div class="user_con clearfix">
						<label>源代码svn路径：</label> <input id="repositoryUrl" type="text"
							name="repositoryUrl" value="${project.repositoryUrl}!''" />
					</div>
					<div class="user_con clearfix">
						<label>源代码存放路径：</label> <input id="checkoutPath" type="text"
							name="checkoutPath" value="${project.checkoutPath}!''" />
					</div>
					<div class="user_con clearfix">
						<label>当前版本号：</label> <input id="version" type="text"
							name="version" value="${project.version}!''" />
					</div>
					<div class="user_con clearfix">
						<label>部署版本号：</label> <input id="deployVersion" type="text"
							name="deployVersion" value="${project.deployVersion}!''" />
					</div>
					<input id="editSrcBtn" type="button" class="shadow_btn mar150_l"
						value="修改" />
				</div>
			</div>
	
			<div id="shadow3"></div>
			<div id="shadow_box3">
				<h5>
					分支设置->add<span id="close3">关闭</span>
				</h5>
				<div class="shadow_con">
					<div class="user_con clearfix">
						<label>分支路径：</label> <input id="branchUrl" type="text"
							name="branchUrl" />
					</div>
					<div class="user_con clearfix">
						<label>当前版本号：</label> <input id="currentVersion" type="text"
							name="currentVersion" />
					</div>
					<input id="addBranchBtn" type="button" class="shadow_btn mar150_l"
						value="添加" />
				</div>
			</div>
		</div>
		
	</div>


		

		

</body>
</html>