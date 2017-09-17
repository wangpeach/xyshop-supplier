define(function (require) {
	require.async('jquery', function () {
		var alert = require('alertUtils')
		var $alert = new alert();
		$(".btn-login").click(function () {
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
				success: function (data) {
					switch (data) {
						case "logerr":
							$alert._alert("店铺不存在或者密码错误");
							break;
						case "freeze":
							$alert._alert("您的店铺已冻结");
						default:
							$("#login").submit()
							break;
					}
				},
				error: function () {
					$alert._alert("店铺不存在或者密码错误");
				}
			});

		});
		$(document).keydown(function (event) {
			if (event.keyCode == 13) {
				$(".btn-login").click();
			}
		});
		$("#status").ready(function (e) {
			var s = $("#status").data("status");
			switch (s) {
				case "nologin":
					$alert._alert("请登录后再进行操作");
					break;
			}
		});
	});
});