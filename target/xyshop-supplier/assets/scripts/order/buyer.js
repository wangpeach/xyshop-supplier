define(function(require) {
	require.async(['jquery', 'icheck'], function() {
		var common = require('common');
		var $common = new common();
		require('jqueryUtils')
		var alert = require('alertUtils')
		var $alert = new alert();
		var table = require('tableUtils');
		/*订单表*/
		var ordert = new table();
		var columns = [{
			field: 'userName',
			title: '姓名',
			align: 'center',
		}, {
			field: 'buyerPhone',
			title: '手机号码',
			align: 'center',
		}, {
			field: 'totalPrice',
			title: '消费金额',
			align: 'center',
			formatter: function(value, row, index) {
				if ($common._noEmpty(value)) return "￥" + value;
				else return "-";
			}
		}, {
			field: 'addTime',
			title: '消费时间',
			align: 'center',
		}, {
			field: 'payWay',
			title: '支付方式',
			align: 'center',
			formatter: function(value, row, index) {
				switch (value) {
					case 'coin':
						return "<span class='badge badge-warning'>金币支付</span>";
					case 'weixin':
						return "<span class='badge badge-primary'>微信支付</span>";
					case 'alipay':
						return "<span class='badge badge-danger'>支付宝</span>";
					case 'unionPay':
						return "<span class='badge badge-success'>银联支付</span>";
				}
				return "-";
			}
		}, {
			field: 'status',
			title: '状态',
			align: 'center',
			formatter: function(value, row, index) {
				switch (value) {
					case 'waitPay':
						return "<span>待支付</span>";
					case 'paySuccess':
						return "<span>支付成功</span>";
					case 'payFail':
						return "<span>支付失败</span>";
				}
				return "-";
			}
			
		}];
		ordert._setSort('addTime', 'desc');
		var $ordert = ordert._init("buyertable", "union-orders/ajax/buyered", columns, function(d) {
			d.shopUuid = $('#js-shop-uuid').val();
			d.payWay = $("#js-choise-buytype").val();
		});
		/*指定选条件，刷新列表*/
		$(".js-change-table-params").change(function(e) {
			$ordert._refresh();
		});
	});
});