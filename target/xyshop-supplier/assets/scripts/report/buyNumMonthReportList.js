define(function(require) {
	require.async(['jquery','icheck'], function() {
			var common=require('common');
			var $common=new common();
			require('jqueryUtils')
			var alert=require('alertUtils')
			var $alert=new alert();
			var table=require('tableUtils');
			/*订单表*/
			var ordert=new table();
			var columns=[{
	            field: 'name', title: '产品名字',  align: 'center',
	        },{
			    field: 'img', title: '产品图片',  align: 'center',
			    formatter:function(value, row, index){
					return  "<img class='js-img-open otherImg' data-value='"+value+"' src='http://huifenshopimg-1253471825.file.myqcloud.com/"+value+"?x-oss-process=image/resize,w_200'/>";
	            }
			},{
			    field: 'totalNum', title: '产品销量',  align: 'center',
			}];
//			ordert._showDetail(function(index, row, $detail){
//				var $el=$detail.html('<table class="ordesrDetailList" ></table>').find('table');
//				var detailTable=new table();
//				detailTable._setInitTable($el);
//				detailTable._init("","/huatuo-store/detail/ajax/reportDetail",detail_coluns,function(d){
//					d.beginTime=row.beginTime,
//	            	d.endTime=row.endTime,
//	            	d.storeUuid=row.storeUuid
//				});
//			});
			var $ordert = ordert._init("reportTable","/shop-orders/report/month/reportlist",columns,function(d){
				d.day=$('#js-choise-day').val();
				d.pointMonth=$('#js-date-begin').val();
				d.shopUuid=$('#js-shop-uuid').val();
			});
			/*指定订单筛选条件，刷新订单列表*/
			$(".js-change-table-params").change(function(e){
				if($(this).val()!='pointMonth'){
					$('#js-time-span').html('');
					$ordert._refresh();
				}
				else $("#timeModal").modal({
	        		backdrop:'static'
				});
			});
			$(".js-change-table-params").mouseenter(function(e){
				$(this).val('');
			});
			$(document).on("click",'#js-time-btn',function(e){
				if($('#js-date-begin').val()==''){
					$alert._alert('开始时间不能为空');
					return;
				}
				$('#js-time-span').html('显示:'+$('#js-date-begin').val()+"统计数据");
				$ordert._refresh();
				$("#timeModal").modal('hide');
			});
	
			/*时间选择器*/
			/*加载layerdate时间选择器*/
			require('laydate');
		  	var start = {
				elem: "#js-date-begin",
				format: "YYYY-MM",
				min: "2016-06-16",
				max: "2099-06-16",
				istime: false,
				istoday: true,
				choose: function(datas) {
				}
			};
			$("#js-date-begin").click(function(e){
				laydate(start);
			});
			
			/*查看订单详情*/
			$(document).on("click",".js-see",function(e){
				$.ajax({
					url:"/huatuo-order/sorder/detail",
					contentType: "application/x-www-form-urlencoded; charset=utf-8", 
					type: "post",
					data:{
						orderId:$(this).data('orderid'),
					},
					dataType:"json",async:true,success:function(row){
						var temp=[];
			        	temp.push( insertDetailStr("下单用户昵称",row.userName));
			        	temp.push( insertDetailStr("下单用户手机号",row.userPhoneNum));
			        	temp.push( insertDetailStr("订单上门服务地址",row.userAddressDetail));
			        	temp.push( insertDetailStr("上门服务的联系人姓名和电话",row.linkNamePhone));
			        	temp.push( insertDetailStr("技师姓名",row.skillTeacherName));
			        	temp.push( insertDetailStr("技师手机号",row.teacherPhoneNum));
			        	temp.push( insertDetailStr("技师头像",'<img src="'+imgBaseUrl+row.skillTeacherHead+'@!head"/>'));
			        	temp.push( insertDetailStr("技师性别",row.skillTeacherGender));
			        	temp.push( insertDetailStr("技师星级",row.skillTeacherLevel));
			        	temp.push( insertDetailStr("技师好评率",row.skillTeacherGoodChance));
			        	temp.push( insertDetailStr("订单总价格",row.totalPrice));
			        	temp.push( insertDetailStr("交通费",row.carFee));
			        	temp.push( insertDetailStr("技师起步价",row.teacherStartFee));
			        	temp.push( insertDetailStr("项目服务费",row.diseaseFee));
			        	temp.push( insertDetailStr("门店使用费",row.storeUseFee));
			        	temp.push( insertDetailStr("预约的上门服务时间",row.serviceTime));
			        	temp.push( insertDetailStr("用下单时间",row.addOrderTime));
			        	temp.push( insertDetailStr("订单状态",row.orderStatus));
			        	temp.push( insertDetailStr("订单备注信息",row.otherInfo));
			        	temp.push( insertDetailStr("服务主体",row.serverBody));
			        	temp.push( insertDetailStr("详见病症史说明",row.diseaseHistory));
			        	temp.push( insertDetailStr("以前的理疗场所",row.prePlace));
			        	temp.push( insertDetailStr("本次服务场所",row.thisPlace));
			        	temp.push( insertDetailStr("病原陪护情况",row.accompany));
			        	temp.push( insertDetailStr("服务开始时间",row.serviceStartTime));
			        	temp.push( insertDetailStr("服务结束时间",row.serviceEndTime));
			        	temp.push( insertDetailStr("服务时长，单位：分钟",row.serviceDuration));
			        	temp.push( insertDetailStr("被服务者年龄",row.linkAge));
			        	temp.push( insertDetailStr("被服务者性别",row.linkGender));
			        	temp.push( insertDetailStr("门店信息",row.storeName));
			        	$("#orderInfo").html(temp.join(' '));
			        	$("#myModal").modal({
			        		backdrop:'static'
						});
					}
				  });
			});
			function insertDetailStr(name,value){
				return '<a class="list-group-item"><label>'+name+':</label>'+value+'</a>';
			}
	});
});