<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置修改页-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="query">
		<div class="main">
		<div class="head  clearfix">
        	<h1><a class="logo" href="/${basePath}/user/${sid}/home.do" title="回到首页">哥斯拉</a></h1>
            <div class="r">你好，${user.userName}！<a id="logout" href="javascript:void(0);" title="退出系统" class="btn1">退出</a></div>
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
				 <a class="backindex" href="/${basePath}/project/${sid}/${projectCode}/TEST/projectConfig.do" title="${projectCode}"><h3 class="location">当前应用：${projectCode}</h3></a>
 
                <div class="clearfix" id="query_tab1">
                    <h4 class="title">测试环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:block">
                     <tbody id="tab_1">
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="35%" style="text-align:left;"><input type="text" class="input_key query_inp" /></td>
                        <td width="35%" style="text-align:left;" class="pos"><input type="text" class="input_value query_inp" /></td>
                        <td width="20%" class="pos"><input type="button" class="btn_add query_add l mar10_r" /><input type="button" class="btn_del query_del l" /></td>
                      </tr>
                     </tbody>
                    </table>
				</div>
				<div class="clearfix" id="query_tab2">
                    <h4 class="title">预发标准环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:block">
                     <tbody id="tab_2">
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="35%" style="text-align:left;"><input type="text" class="input_key query_inp" /></td>
                        <td width="35%" style="text-align:left;" class="pos"><input type="text" class="input_value query_inp" /></td>
                        <td width="20%" class="pos"><input type="button" class="btn_add query_add l mar10_r" /><input type="button" class="btn_del query_del l" /></td>
                      </tr>
                      </tbody>
                    </table>
                    
                   </div>
                   
                   <div class="clearfix" id="query_tab3">
                     <h4 class="title">生产标准环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:block">
                     <tbody id="tab_3">
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="35%" style="text-align:left;"><input type="text" class="input_key query_inp" /></td>
                        <td width="35%" style="text-align:left;" class="pos"><input type="text" class="input_value query_inp" /></td>
                        <td width="20%" class="pos"><input type="button" class="btn_add query_add l mar10_r" /><input type="button" class="btn_del query_del l" /></td>
                      </tr>
                      </tbody>
                    </table>
                   </div>
                   
                <ul id="tab2" class="clearfix">
                	  <li class="current" style="border-left:0"><a href="javascript:void(0);" class="current" title="测试环境">测试环境</a></li>
                    <li><a href="javascript:void(0);" title="预发标准环境">预发标准环境</a></li>
                    <li><a href="javascript:void(0);" title="生产标准环境">生产标准环境</a></li>
                 </ul>
                 <ul id="tabCon2">
                    <li style="display:block">
                        <div class="table1" id="show_1">
        				   </div>
                    </li>
                    <li>
                   			<div class="table2" id="show_2">
        				   </div>

					</li>
                    <li>
							<div class="table3" id="show_3">
        				   </div>
					</li>
                 </ul>
                
                <h4 style="text-align:center; margin-top: 30px;"><a id="btn_submit" href="javascript:void(0);" class="btn2" title="提交审核">提交审核</a></h4>
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
<script src="/${basePath}/js/query.js"></script>
<script>
//缓存 数据库各环境配置 
var propTest = '${propTest}';
var propQuasiProduct = '${propQuasiProduct}';
var propProduct = '${propProduct}';
//缓存 当前添加的各环境配置
var p1 = new Map();
var p2 = new Map();
var p3 = new Map();

var propTestMap = new Map();
var propQuasiProductMap = new Map();
var propProductMap = new Map();

$(function(){
	json2Map(propTest, propTestMap);
	json2Map(propQuasiProduct, propQuasiProductMap);
	json2Map(propProduct, propProductMap);
	//添加一行
	$(".btn_add").live("click", function(){
		
		var newTR = '<tr>'
                        +'<td width="5%"><span class="q_icon">图标</span></td>'
                        +'<td width="35%" style="text-align:left;"><input type="text" class="input_key query_inp" /></td>'
                        +'<td width="35%" style="text-align:left;" class="pos"><input type="text" class="input_value query_inp" /></td>'
                        +'<td width="20%" class="pos"><input type="button" class="btn_add query_add l mar10_r" /><input type="button" class="btn_del query_del l" /></td>'
                      +'</tr>';
		$(this).parent().parent().parent().append(newTR);
	});
	//属性[键]获得焦点时
	$(".input_key").live("focus", function() {
		var key = $(this).val();
		key = $.trim(key);
		var tbody = $(this).parent().parent().parent();
		
		updateShowDoc(this, key, '', tbody, 'delete');
	});
	//属性[键]丢失焦点时
	$(".input_key").live("blur", function() {
		var key = $(this).val();
		var value = $(this).parent().parent().find("input:eq(1)").val();
		key = $.trim(key);
		value = $.trim(value);
		var tbody = $(this).parent().parent().parent();
		if(key!="") {
			updateShowDoc(this, key, value, tbody, 'update');
		}
	});
	//属性[值]丢失焦点时
	$(".input_value").live("blur", function() {
		var key = $(this).parent().parent().find("input:eq(0)").val();
		var value = $(this).val();
		key = $.trim(key);
		value = $.trim(value);
		var tbody = $(this).parent().parent().parent();
		if(key!="") {
			updateShowDoc(this, key, value, tbody, 'update');
		}
		
	});
	//删除本行
	$(".btn_del").live("click", function(){
		var key = $(this).parent().parent().find("input:eq(0)").val();
		key = $.trim(key);
		var tbody = $(this).parent().parent().parent();
		
		updateShowDoc(this, key, '', tbody, 'deleteRow');
	});
	
	$("#btn_submit").on("click", function() {
		
		var p1json = JSON.stringify(p1.container);
		var p2json = JSON.stringify(p2.container);
		var p3json = JSON.stringify(p3.container);
		
		//alert(encodeURI(p1Json));
		$.ajax({ 
		 	type: 'POST', 
			url: '/${basePath}/prop/${sid}/${projectCode}/updateProp.do',
			data: {
				p1:p1json,
				p2:p2json,
				p3:p3json
				},
			success: function(data){
				if(data == 'SUCCESS') {
					alert("SUCCESS");
					//重新进入查询页
					window.location.href = "/${basePath}/project/${sid}/${projectCode}/TEST/projectConfig.do"
				} else {
					alert("FAILURE");
				}
				
      	}});
	});
	
});


</script>
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
