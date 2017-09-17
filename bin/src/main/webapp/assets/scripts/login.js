define(function(require) {
	  require.async('jquery', function() {
		  var alert=require('alertUtils')
		  var $alert=new alert();
		  $(".btn-login").click(function(){
			  var n=$("#adminEmail").val(),p=$("#adminPwd").val();
			  $.ajax({
					url:"ajax/loginCheck",
					contentType: "application/x-www-form-urlencoded;charset=utf-8", 
					type: "post",postType:'json',
					data:{
						adminEmail:n,
						adminPwd:p
					},
					async:true,success:function(data){
						switch(data){
							case "0":{$("#login").submit();}break;
							case "104":{$alert._alert('店铺不存在或者密码错误！')}break;
							case "106": {$alert._alert('商铺已冻结')}break;
						}
					},error:function(){
						$alert._alert("'店铺不存在或者密码错误");
					}
			  	});
			  
		  });
		  $(document).keydown(function (event) {
			  if(event.keyCode==13){
				  $(".btn-login").click();  
			  }
		  });
		  $("#status").ready(function(e){
			  var s=$("#status").data("status");
			  switch(s){
			  case "nologin":$alert._alert("请登录后再进行操作");break;
			  }
		  });
	  });
});