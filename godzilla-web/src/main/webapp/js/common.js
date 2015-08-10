$("#tab1 .a1").click(
	function(){
		$(this).parent("#tab1").addClass("current")
	}
);
$("#tab1 .a2").click(
	function(){
		$(this).parent("#tab1").removeClass("current")
	}
);

$("#tab2 li").click(
	function(){
		var num=$(this).index();
		$(this).addClass("current").siblings().removeClass("current");
		$("#tabCon2 li:eq("+num+")").show().siblings().hide();
	}
);

$(".sp a,.sp02 a").hover(
	function(){
		$(this).find("span.edit").css("display","block");
	},function(){
		$(this).find("span.edit").css("display","none");
	}
);
//多选框
$(".check_con li").toggle(
	function(){
		$(this).find("b").addClass("checked");
	},function(){
		$(this).find("b").removeClass("checked");			
	}
);
$("#query h4.title span").toggle(
	function(){
		$(this).addClass("click").html("收起").attr("title","收起").parents("h4").next("table").show();
		
	},function(){
		$(this).removeClass("click").html("展开").attr("title","展开").parents("h4").next("table").hide();
	}
)

