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
		<div class="head  clearfix">
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
					<a href="jvascript:;" class="a1" title="工作空间">工作空间</a><a
						href="jvascript:;" class="a2">管理权限</a>
				</h2>
				<h3 class="location">当前应用：Xuanyuan</h3>
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
							<tr>
								<td>1</td>
								<td>刘宝剑</td>
								<td width="55%" style="text-align: left;">gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;</td>
								<td class="sp" width="8%"><a href="javascript:;"
									name="edit"><span class="edit">添加用户</span></a></td>
								<td class="sp02" width="8%"><a href="javascript:;"
									name="edit"><span class="edit">编辑工作台</span></a></td>
							</tr>
							<tr>
								<td>2</td>
								<td>刘宝剑</td>
								<td width="55%" style="text-align: left;">gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;gaia;hero;xuanyuan;cupid;</td>
								<td class="sp" width="8%"><a href="javascript:;"
									name="edit"><span class="edit">添加用户</span></a></td>
								<td class="sp02" width="8%"><a href="javascript:;"
									name="edit" onClick="shadowShow()"><span class="edit">编辑工作台</span></a></td>
							</tr>

						</tbody>
					</table>
				</div>
				<h4>
					<a href="#" class="btn2" title="更多信息">更多信息</a>
				</h4>
			</div>
			<div id="shadow"></div>
			<div id="shadow_box">
				<h5>
					添加新用户<span id="close">关闭</span>
				</h5>
				<form action="" class="clearfix">
					<div class="shadow_con">
						<div class="user_con clearfix">
							<label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label> <input
								type="text" name="username" placeholder="输入内容" />
						</div>
						<div class="user_con clearfix">
							<label>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label> <input
								type="password" name="username" placeholder="输入密码" />
						</div>
						<div class="user_con clearfix">
							<label>再次确认：</label> <input type="password" name="username"
								placeholder="输入密码" />
							<!--<div class="tips">两次密码不一致</div>-->
						</div>

						<input type="submit" class="shadow_btn mar150_l" value="确定" />
					</div>
				</form>
			</div>
			
		</div>
	</div>

</body>
</html>