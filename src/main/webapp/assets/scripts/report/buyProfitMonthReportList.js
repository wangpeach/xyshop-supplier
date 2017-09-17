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
	            field: 'totalProfit', title: '产品销售额',  align: 'center',
	            formatter: function(value, row, index) {
                	return "¥"+value;
	            },
	        }];
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
	});
});