//更新整个doc视图
function updateShowDoc(obj, key, value, tbody, opera){
	if(opera=='update'){
		var tbodyId = tbody.attr("id");
		
		switch(tbodyId) {
			case 'tab_1':
				doUpdate(p1, propTestMap, key, value, obj, 'show_1');
				break;
			case 'tab_2':
				doUpdate(p2, propQuasiProductMap, key, value, obj, 'show_2');
				break;
			case 'tab_3':
				doUpdate(p3, propProductMap, key, value, obj, 'show_3');
				break;
		}
	} else if(opera=='deleteRow') {
		var tbodyId = tbody.attr("id");
		
		switch(tbodyId) {
			case 'tab_1':
				doDeleteRow(p1, key, obj, 'show_1');
				break;
			case 'tab_2':
				doDeleteRow(p2, key, obj, 'show_2');
				break;
			case 'tab_3':
				doDeleteRow(p3, key, obj, 'show_3');
				break;
		}
	} else if(opera=='delete') {
		var tbodyId = tbody.attr("id");
		
		switch(tbodyId) {
			case 'tab_1':
				doDelete(p1, key, obj, 'show_1');
				break;
			case 'tab_2':
				doDelete(p2, key, obj, 'show_2');
				break;
			case 'tab_3':
				doDelete(p3, key, obj, 'show_3');
				break;
		}
	}
}
function doDelete(map, key, obj, showId) {
	//更新 本地缓存 delete
	map.remove(key);
	//将添加属性 重绘 在showtable里
	reDrawShow(map, showId);
}

function doDeleteRow(map, key, obj, showId) {
	//更新 本地缓存 delete
	map.remove(key);
	//删除本行
	$(obj).parent().parent().remove();
	//将添加属性 重绘 在showtable里
	reDrawShow(map, showId);
}

function doUpdate(map, dbMap, key, value, obj, showId) {
	//更新 本地缓存 update
	map.put(key, value);
	//判断  属性 为 添加 还是 更新
	showAddorUpdate(dbMap, key, obj);
	//将添加属性 重绘 在showtable里
	reDrawShow(map, showId);
}
 
function showAddorUpdate(map, key, obj) {
	if(map.contains(key)) {
		$(obj).parent().parent().find("input:eq(1)").remove("span");
		$(obj).parent().parent().find("input:eq(1)").after('<span class="data_odd">原来：'+map.get(key)+'</span>');
	}
}

function reDrawShow(map, showId) {
	var a = map.keySet();
	var showDiv = $('#'+showId);
	//清空
	showDiv.empty();
	//重绘
	for(var i = 0;i < a.length; i++) {
		showDiv.append('<p class="query_con">' +a[i]+ '='+ map.get(a[i]) +'</p>');
	}
}