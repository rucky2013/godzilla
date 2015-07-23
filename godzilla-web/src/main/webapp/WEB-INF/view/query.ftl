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
                    <table  width="100%" border="0" class="table2" style="display:none">
                     <tbody id="tab_1">
                      <tr>
                        <td width="5%"><span class="q_icon">图标</span></td>
                        <td width="25%" style="text-align:left;"><input type="text" class="input_key query_inp" /></td>
                        <td width="35%" style="text-align:left;"><input type="text" class="input_value query_inp" /></td>
                        <td width="30%"><input type="button" class="btn_add query_add l mar10_r" /><input type="button" class="btn_del query_del l" /></td>
                      </tr>
                     </tbody>
                    </table>
				</div>
				<div class="clearfix" id="query_tab2">
                    <h4 class="title">预发标准环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:none">
                     <tbody id="tab_2">
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
                   
                   <div class="clearfix" id="query_tab3">
                     <h4 class="title">生产标准环境<span title="展开">展开</span></h4>
                    <table  width="100%" border="0" class="table2" style="display:none">
                     <tbody id="tab_3">
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
                        <div class="table1" id="show_1">
                            <p class="query_con">com.xunyuan.crm.ip=10.100.139.234</p>
                            <p class="query_con">com.xunyuan.crm.ip=3000</p>
                            <p class="query_con">com.xuanyuan.callphp.url=</p>
        				   </div>
                    </li>
                    <li>
                   			<div class="table2" id="show_2">
                            <p class="query_con">com.xunyuan.crm.ip=10.100.139.234</p>
                            <p class="query_con">com.xunyuan.crm.ip=3000</p>
                            <p class="query_con">com.xuanyuan.callphp.url=</p>
        				   </div>

					</li>
                    <li>
							<div class="table3" id="show_3">
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
	
	//缓存 数据库各环境配置 
	var propTest = '${propTest}';
	var propQuasiProduct = '${propQuasiProduct}';
	var propProduct = '${propProduct}';
	//缓存 当前添加的各环境配置
	var p1 = new Map();
	var p2 = new Map();
	var p3 = new Map();
	
	var propTestMap = new Map();
	json2Map(propTest, propTestMap);
	
	var propQuasiProductMap = new Map();
	json2Map(propTest, propQuasiProductMap);
	
	var propProductMap = new Map();
	json2Map(propTest, propProductMap);
	//添加一行
	$(".btn_add").on("click", function(){
		
		var newTR = '<tr>'
                        +'<td width="5%"><span class="q_icon">图标</span></td>'
                        +'<td width="25%" style="text-align:left;"><input type="text" class="input_key query_inp" /></td>'
                        +'<td width="35%" style="text-align:left;"><input type="text" class="input_value query_inp" /></td>'
                        +'<td width="30%"><input type="button" class="btn_add query_add l mar10_r" /><input type="button" class="btn_del query_del l" /></td>'
                      +'</tr>';
		$(this).parent().parent().after(nnewTRewTr);
	});
	//属性[键]丢失焦点时
	$(".input_key").on("blur", function() {
		var key = $(this).val();
		var value = $(this).parent().find("input:eq(1)").val();
		var tbody = $(this).parent().parent().parent();
		
		updateShowDoc(key, value, tbody, 'update');
		
	});
	//属性[值]丢失焦点时
	$(".input_value").on("blur", function() {
		var key = $(this).parent().find("input:eq(0)").val();
		var value = $(this).val();
		var tbody = $(this).parent().parent().parent();
		
		updateShowDoc(key, value, tbody, 'update');
	});
	//删除本行
	$(".btn_del").on("click", function(){
		var key = $(this).parent().find("input:eq(0)").val();
		var tbody = $(this).parent().parent().parent();
		
		updateShowDoc(key, '', tbody, 'delete');
	});
	
});

//更新整个doc视图
function updateShowDoc(this, key, value, tbody, opera){
	if(opera=='update'){
		var tbodyId = tbody.attr("id");
		
		switch(tbodyId) {
			case 'tab_1':
				doUpdate(p1, key, value, this, 'show_1');
				break;
			case 'tab_2':
				doUpdate(p2, key, value, this, 'show_2');
				break;
			case 'tab_3':
				doUpdate(p3, key, value, this, 'show_3');
				break;
		}
	} else if(opera=='delete') {
		var tbodyId = tbody.attr("id");
		
		switch(tbodyId) {
			case 'tab_1':
				doDelete(p1, key, this, 'show_1');
				break;
			case 'tab_2':
				doDelete(p2, key, this, 'show_2');
				break;
			case 'tab_3':
				doDelete(p3, key, this, 'show_3');
				break;
		}
	}
}

function doDelete(map, key, this, showId) {
	//更新 本地缓存 delete
	p1.remove(key);
	//判断  属性 为 添加 还是 更新
	showAddorUpdate(key, this);
	//将添加属性 重绘 在showtable里
	reDrawShow(map, showId);
}

function doUpdate(map, key, value, this, showId) {
	//更新 本地缓存 update
	p1.put(key, value);
	//判断  属性 为 添加 还是 更新
	showAddorUpdate(key, this);
	//将添加属性 重绘 在showtable里
	reDrawShow(map, showId);
}
 
function showAddorUpdate(key, this) {
	if(propTestMap.contains(key)) {
		$(this).after('<span class="data_odd">原来：'+propTestMap.get(key)+'</span>');
	}
}

function reDrawShow(map, showId) {
	var a = map.keySet();
	var showDiv = $(showId);
	//清空
	showDiv.empty();
	//重绘
	for(var i = 0;i < a.length; i++) {
		showDiv.append('<p class="query_con">' +a[i]+ '='+ map.get(a[i]) +'</p>');
	}
}

</script>
</body>
</html>
