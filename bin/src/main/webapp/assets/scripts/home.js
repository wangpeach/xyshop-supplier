define(function(require) {
	require.async(['jquery'], function() {
		var time=require('time');
		var date=require('laydateUtils');
		var echartsUtils=require('echartsUtils');
		$(document).ready(function(e){
			/*初始化统计图表*/
//			$('.js-begin').val(time.getDifDay(-10));
//			$('.js-end').val(time.getToday());
//			registInit();
//			teacherOrderInit();
//			supplierOrderInit();
//			storeOrderInit();
		});
		
		/*技师注册**/
		var $regist=new date()._init("#js-regist-begin","#js-regist-end",function(e){
			registInit();
		});
		var registInit=function(){
			$.ajax({
				url:"chart/new",
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				type: "post",
				dataType:"json",
				data:{
					rbegin:$('#js-regist-begin').val(),
					rend:$('#js-regist-end').val(),
				},
				async:true,success:function(data){
					echartsUtils.getBar("技师注册量",'echarts-userNew',data["xKeys"],data["yDatas"]);
				}
		  });
		}
		/*技师订单*/
		new date()._init("#js-teacher-order-begin","#js-teacher-order-end",function(e){
			teacherOrderInit();
		});
		var teacherOrderInit=function(){
			$.ajax({
				url:"chart/serviceorder",
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				type: "post",
				dataType:"json",
				data:{
					rbegin:$('#js-teacher-order-begin').val(),
					rend:$('#js-teacher-order-end').val(),
				},
				async:true,success:function(data){
					echartsUtils.getBar("技师订单量",'echarts-teacherOrderNew',data["xKeys"],data["yDatas"]);
				}
		  });
		}
		/*商品订单*/
		new date()._init("#js-supplier-order-begin","#js-supplier-order-end",function(e){
			supplierOrderInit();
		});
		var supplierOrderInit=function(){
			$.ajax({
				url:"chart/goodsorder",
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				type: "post",
				dataType:"json",
				data:{
					rbegin:$('#js-supplier-order-begin').val(),
					rend:$('#js-supplier-order-end').val(),
				},
				async:true,success:function(data){
					echartsUtils.getBar("供应商商品订单量",'echarts-supplierOrderNew',data["xKeys"],data["yDatas"]);
				}
		  });
		}
		/*门店订单*/
		new date()._init("#js-store-order-begin","#js-store-order-end",function(e){
			storeOrderInit();
		});
		var storeOrderInit=function(){
			$.ajax({
				url:"chart/storeorder",
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				type: "post",
				dataType:"json",
				data:{
					rbegin:$('#js-store-order-begin').val(),
					rend:$('#js-store-order-end').val(),
				},
				async:true,success:function(data){
					echartsUtils.getBar("门店订单量",'echarts-storeOrderNew',data["xKeys"],data["yDatas"]);
				}
		  });
		}
	});
});