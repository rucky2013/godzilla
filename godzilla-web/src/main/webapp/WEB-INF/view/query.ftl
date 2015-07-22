<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置查询-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="query">
<input type="hidden" name="projectCode" id="projectCode" value="${projectCode}" />

	<div class="main">	
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
                    <td>${user.userName}</td>
                  </tr>
                  <tr>
                    <td>登录时间：</td>
                    <td>${user.lastLoginTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                  </tr>
                  <tr>
                    <td>部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门：</td>
                    <td>${user.departName}</td>
                  </tr>
                  <tr>
                    <td>上次操作：</td>
                    <td>$//{user.lastOperaTime}</td>
                  </tr>
                </table>
            </div>
        	<div class="mainConR r">
            	<h2 id="tab1" class="current"><a href="jvascript:void(0)" class="a1" title="工作空间">工作空间</a><a href="jvascript:void(0)" class="a2" title="管理权限">管理权限</a></h2>
            	<h3 class="location">当前应用：${projectCode}</h3>
                
                
                <div class="clearfix" id="query_tab1">
                    <h4 class="title">测试环境<span title="展开">展开</span></h4>
                    <table width="100%" border="0" class="table2" style="display:none">
                     <tbody>
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="25%" style="text-align:left;"><input type="text" class="query_inp" /></td>
                        <td width="35%" style="text-align:left;"><input type="text" class="query_inp" /></td>
                        <td width="30%"><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                     </tbody>
                    </table>
				</div>
				
				<div class="clearfix" id="query_tab2">
                    <h4 class="title">预发标准环境<span title="展开">展开</span></h4>
                    <table width="100%" border="0" class="table2" style="display:none">
                     <tbody>
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="25%" style="text-align:left;">com.xunyuan.crm.ip</td>
                        <td width="35%" style="text-align:left;"><input type="text" class="query_inp" /></td>
                        <td width="30%"><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                      <tr>
                        <td><span class="query_icon q_icon">图标</span></td>
                        <td style="text-align:left;">com.xunyuan.timeout</td>
                        <td style="text-align:left;" class="pos"><input type="text" class="query_inp" /><span class="data_odd">原来：2000</span></td>
                        <td><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                      <tr>
                        <td><span class="query_icon q_icon">图标</span></td>
                        <td style="text-align:left;">com.xunyuan.callphp.url</td>
                        <td style="text-align:left;" class="pos"><input type="text" class="query_inp" /><span class="data_odd">原来：2000</span></td>
                        <td><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                      </tbody>
                    </table>
                    
                   </div>
                   
                   <div class="clearfix" id="query_tab2">
                     <h4 class="title">生产标准环境<span title="展开">展开</span></h4>
                    <table width="100%" border="0" class="table2" style="display:none">
                     <tbody>
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="25%" style="text-align:left;">com.xunyuan.crm.ip</td>
                        <td width="35%" style="text-align:left;"><input type="text" class="query_inp" /></td>
                        <td width="30%"><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                      <tr>
                        <td><span class="query_icon q_icon">图标</span></td>
                        <td style="text-align:left;">com.xunyuan.timeout</td>
                        <td style="text-align:left;" class="pos"><input type="text" class="query_inp" /><span class="data_odd">原来：2000</span></td>
                        <td><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                      <tr>
                        <td><span class="query_icon q_icon">图标</span></td>
                        <td style="text-align:left;">com.xunyuan.callphp.url</td>
                        <td style="text-align:left;" class="pos"><input type="text" class="query_inp" /><span class="data_odd">原来：2000</span></td>
                        <td><input type="button" class="query_add l mar10_r" /><input type="button" class="query_del l" /></td>
                      </tr>
                      </tbody>
                    </table>
                   </div>
                   
                <ul id="tab2" class="clearfix">
                	  <li class="current" style="border-left:0"><a href="jvascript:void(0)" class="current" title="测试环境">测试环境</a></li>
                    <li><a href="jvascript:void(0)" title="预发标准环境">预发标准环境</a></li>
                    <li><a href="jvascript:void(0)" title="生产标准环境">生产标准环境</a></li>
                 </ul>
                 <ul id="tabCon2">
                    <li style="display:block">
                        <div class="table1">
                            <p class="query_con">com.xunyuan.crm.ip=10.100.139.234</p>
                            <p class="query_con">com.xunyuan.crm.ip=3000</p>
                            <p class="query_con">com.xuanyuan.callphp.url=</p>
        				   </div>
                    </li>
                    <li>
                   			<div class="table2">
                            <p class="query_con">com.xunyuan.crm.ip=10.100.139.234</p>
                            <p class="query_con">com.xunyuan.crm.ip=3000</p>
                            <p class="query_con">com.xuanyuan.callphp.url=</p>
        				   </div>

					</li>
                    <li>
							<div class="table3">
                            <p class="query_con">com.xunyuan.crm.ip=10.100.139.234</p>
                            <p class="query_con">com.xunyuan.crm.ip=3000</p>
                            <p class="query_con">com.xuanyuan.callphp.url=</p>
        				   </div>
					</li>
                 </ul>
                
                <h4 style="text-align:center"><a  href="#" class="btn2 btnGreen" title="预览">预览</a><a  href="#" class="btn2" title="提交审核">提交审核</a></h4>
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
<script src="/${basePath}/js/map.js"></script>
<script>

$(function(){
	
	
	var propTest = '${propTest}';
	alert(propTest);
	
	var map = new Map();
	json2Map(propTest, map);
	
	alert(map.toString());
});
  
</script>
</body>
</html>
