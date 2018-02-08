define(function(require) {
	require.async('jquery', function() {
		var alert = require('alertUtils')
		var $alert = new alert();
		$(".btn-login").click(function() {
			var n = $("#adminEmail").val(),
				p = $("#adminPwd").val();
			$.ajax({
				url: "shop/login",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				type: "post",
				postType: 'json',
				data: {
					adminEmail: n,
					adminPwd: p
				},
				async: true,
				success: function(data) {
					switch (data) {
						case "logerr":
							$alert._alert("店铺不存在或者密码错误");
							break;
						case "freeze":
							$alert._alert("您的店铺已冻结");
							break;
						default:
							$("#login").submit()
							break;
					}
				},
				error: function() {
					$alert._alert("店铺不存在或者密码错误");
				}
			});

		});
		$(document).keydown(function(event) {
			if (event.keyCode == 13) {
				$(".btn-login").click();
			}
		});
		$("#status").ready(function(e) {
			var s = $("#status").data("status");
			switch (s) {
				case "nologin":
					$alert._alert("请登录后再进行操作");
					break;
			}


			/**
			 * 官方联系方式
			 */
			$.post('/xyshop/params/search', {
				'type': 'contact_us'
			}, function(data, textStatus, xhr) {
				/*optional stuff to do after success */
				if (data && data.length > 0) {
					var eles = new Array();
					// eles.push('<span class="block m-t-xs" style="margin-bottom: 15px;"><strong class="font-bold">官方联系方式</strong></span>');
					// for (var i = 0; i < data.length; i++) {
					// 	eles.push('<p>' + data[i].name + '：<span id="lx_phone">' + data[i].paramValue + '</span></p>');
					// }
					// $(".contactUs").append(eles.join(' '));
					// $(".contactUsLi").show();
				}
			}, 'json');
		});
	});
});