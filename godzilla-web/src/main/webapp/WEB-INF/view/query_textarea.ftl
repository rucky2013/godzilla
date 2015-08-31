<!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置查询-哥斯拉</title>
<link type="text/css" href="/${basePath}/css/meta.css" rel="stylesheet"/>
</head>
<body id="query">
<input type="hidden" name="projectCode" id="projectCode" value="${projectCode}" />
<input type="hidden" name="sid" id="sid" value="${sid}" />
<input type="hidden" name="createBy" id="createBy" value="${createBy}" />
<input type="hidden" name="profile" id="profile" value="${profile}" />

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
				<h3 class="location">当前应用：${projectCode}</h3>
                
                <div class="clearfix" id="query_tab1">
                    <h4 class="title">测试环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:block">
                     <tbody id="tab_1">
                     </tbody>
                    </table>
				</div>
				<div class="clearfix" id="query_tab2">
                    <h4 class="title">预发标准环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:block">
                     <tbody id="tab_2">
                      </tbody>
                    </table>
                    
                   </div>
                   
                   <div class="clearfix" id="query_tab3">
                     <h4 class="title">生产标准环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:block">
                     <tbody id="tab_3">
                      </tbody>
                    </table>
                    </div>
                    
                  <h4 class="title">意见</h4>
                  <div class="ft-Textarea">
                    <div class="textareaWrap">
                      <textarea id="auditor_text" rows="5" cols="40"></textarea>
                    </div>
                  </div>
                  <div class="checkBtn">
                  	<button class="notcheckPass verify_btn" value1="2">审核未通过</button>
                  	<button class="checkPass verify_btn" value1="1">审核通过</button>
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
//缓存 数据库各环境old配置 
var oldpropTest = '${oldpropTest}';
var oldpropQuasiProduct = '${oldpropQuasiProduct}';
var oldpropProduct = '${oldpropProduct}';
//缓存 当前添加的各环境配置
var p1 = new Map();
var p2 = new Map();
var p3 = new Map();

var propTestMap = new Map();
var propQuasiProductMap = new Map();
var propProductMap = new Map();

function propShow(map, p, showId) {
	var a = map.keySet();
	var table = $('#tab_'+showId);
	//清空
	table.empty();
	//重绘
	for(var i = 0;i < a.length; i++) {
		//如果存在旧值，显示旧值
		if(p.contains(a[i])) {
			var newTR = '<tr>'
                        +'<td width="5%"><span class="q_icon">图标</span></td>'
                        +'<td width="35%" style="text-align:left;">'+a[i]+'</td>'
                        +'<td width="35%" style="text-align:left;" class="pos">'+map.get(a[i])+'<span class="data_odd">原来：'+p.get(a[i])+'</span>'+'</td>'
                        +'<td width="20%" class="pos"></td>'
                      +'</tr>';
			table.append(newTR);
		} else {
			var newTR = '<tr>'
                        +'<td width="5%"><span class="q_icon">图标</span></td>'
                        +'<td width="35%" style="text-align:left;">'+a[i]+'</td>'
                        +'<td width="35%" style="text-align:left;" class="pos">'+map.get(a[i])+'</td>'
                        +'<td width="20%" class="pos"></td>'
                      +'</tr>';
			table.append(newTR);
		}
	}
}

$(document).ready(function() {
	json2Map(propTest, propTestMap);
	json2Map(propQuasiProduct, propQuasiProductMap);
	json2Map(propProduct, propProductMap);
	
	json2Map(oldpropTest, p1);
	json2Map(oldpropQuasiProduct, p2);
	json2Map(oldpropProduct, p3);
	
	propShow(propTestMap, p1, '1');
	propShow(propQuasiProductMap, p2, '2');
	propShow(propProductMap, p3, '3');
	
	$(".verify_btn").on("click", function() {
		
		var sid = $("#sid").val();
		var createBy = $("#createBy").val();
		var projectCode = $("#projectCode").val();
		var profile = $("#profile").val();
		
		var status = $(this).attr("value1");
		var auditor_text = $("#auditor_text").text();
		
		$.ajax({ 
		 	type: 'POST', 
			url: '/${basePath}/prop/' + sid + '/' + projectCode +　'/' + createBy + '/' + profile + '/' + 'verifyProp.do',
			data: {
				auditor_text:auditor_text,
				status:status,
				},
			success: function(data){
				if(data == 'SUCCESS') {
					alert("SUCCESS");
					window.location.href = '/${basePath}/prop/${sid}/${projectCode}/verifyProp.do';
				} else {
					alert("FAILURE");
				}
			}	 
      	});
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
