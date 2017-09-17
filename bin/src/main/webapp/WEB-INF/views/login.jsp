<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>XXXX-联盟店铺登录</title>
    <meta name="keywords" content="XXXX,联盟店铺">
    <link href="img/favicon.ico" rel="shortcut icon" type="image/x-icon">
    <link href="css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="css/animate.min.css" rel="stylesheet">
    <link href="css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <link href="css/login.min.css" rel="stylesheet">
    <link href="css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link href="css/home.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>if(window.top !== window.self){ window.top.location = window.location;}</script>
</head>

<body class="gray-bg signin">
    <div class="middle-box text-center loginscreen  animated fadeInDown">
        <div>
            <h3>XXXX-联盟店铺登录</h3>
            <form class="m-t" role="form" method="POST" id="login" action="index.html">
                <div class="form-group">
                    <input  class="form-control" placeholder="店铺名称或店主手机号" id="adminEmail"  name="adminEmail" autofocus  required="">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" placeholder="登录密码" id="adminPwd" name="adminPwd" type="password" required="">
                </div>
                <a class="btn btn-primary block full-width btn-login">登 录</a>
            </form>
        </div>
    </div>
    
	<script src="scripts/sea.js" ></script>
	<script>
		seajs.config({
		  alias: {
		    "jquery": "/shop-supplier/base/jquery.min",
		    "bootstrap":"/shop-supplier/base/bootstrap.min",
		    "sweet":"/shop-supplier/base/plugins/sweetalert/sweetalert.min",
		    "alertUtils":"/shop-supplier/scripts/utils/alertUtils",
		  }
		});
	</script>
	<script>seajs.use("login");</script>
</body>
</html>