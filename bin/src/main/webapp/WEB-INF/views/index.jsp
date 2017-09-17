<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>XXXX-商家:${shopName}</title>
    <!--[if lt IE 9]>
<meta http-equiv="refresh" content="0;ie.html" />
<![endif]-->
    <jsp:include page="head/baseHead.jsp"></jsp:include>
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <input type="hidden" id="area" value="${area }">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <span><img alt="image" class="img-circle" src="img/icon_userHead.png" width="65px"/></span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                       <span class="block m-t-xs"><strong class="font-bold">欢迎~${shopName}</strong></span>
                                <span class="text-muted text-xs block">更多操作:<b class="caret"></b></span>
                                </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a class="js-index-update-pwd">修改密码</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="login.html">安全退出</a>
                                </li>
                            </ul>
                        </div>
                        <div class="logo-element">汇
                        </div>
                    </li>
                    <!-- <li>
                        <a class="J_menuItem" href="record.html"><i class="fa fa-home"></i><span class="nav-label">首页</span></a>
                    </li> -->
                    <li>
                        <a class="J_menuItem" href="record.html"><i class="fa fa-credit-card"></i> <span class="nav-label">收支记录</span></a>
                    </li>
                    <li>
                        <a><i class="fa fa-envelope"></i> <span class="nav-label">商家管理 </span></a>
                        <ul class="nav nav-second-level">
                            <li><a class="J_menuItem" href="union-goodslist.html">商品管理</a></li>
                            <li><a class="J_menuItem" href="orders.html">商品订单管理</a></li>
                            <li><a class="J_menuItem" href="shoporders.html">店铺订单管理</a></li>
                        </ul>
                    </li>
                    <li>
                        <a><i class="fa fa-envelope"></i> <span class="nav-label">分销报表</span></a>
                        <ul class="nav nav-second-level">
                            <li><a class="J_menuItem" href="buyreport.html">产品销售量报表</a></li>
                            <li><a class="J_menuItem" href="buyprofitreport.html">产品销售额报表</a></li>
                            <li><a class="J_menuItem" href="buymonthreport.html">产品销售量月报表</a></li>
                            <li><a class="J_menuItem" href="buyprofitmonthreport.html">产品销售量月报表</a></li>
                        </ul>
                    </li>
                    <li>
                        <a class="J_menuItem" href="setting.html"><i class="fa fa-home"></i> <span class="nav-label">店铺设置</span></a>
                    </li>
                    <li>
                        <a class="J_menuItem" href="buyer.html"><i class="fa fa-credit-card"></i> <span class="nav-label">会员管理</span></a>
                    </li>
                    <li>
                        <a class="J_menuItem" href="comment.html"><i class="fa fa-credit-card"></i> <span class="nav-label">评论管理</span></a>
                    </li>
                    
                    <li></li>
                    <li></li>
                    <li></li>
                    <li class="nav-header" style="padding: 15px;">
                        <div class="dropdown profile-element">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="margin-bottom: 15px;"><strong class="font-bold">直属代理商联系电话</strong></span>
                                    <p>代理商名称：<span id="agent-name">--</span></p>
                                    <p>联系电话(手机)：<span id="agent-tel">--</span></p>
                                    <p>联系电话(座机)：<span id="agent-hottel">--</span></p>
                                </span>
                            </a>
                        </div>
                        <div class="logo-element">联
                        </div>
                    </li>
                    <li class="nav-header" style="padding: 15px;">
                        <div class="dropdown profile-element">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="margin-bottom: 15px;"><strong class="font-bold">XXXX联系电话</strong></span>
                                    <p>联系电话：<span id="lx_phone">--</span></p>
                                    <p>客户热线：<span id="lx_hottel">--</span></p>
                                    <p>邮箱：<span id="lx-email">--</span></p>
                                </span>
                            </a>
                        </div>
                        <div class="logo-element">联
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                        <!--  <div class="form-group">
                        <input type="text" placeholder="请输入您需要查找的内容 …" class="form-control" name="top-search" id="top-search">
                    </div> -->
                    </div>
                    <ul class="nav navbar-top-links navbar-right">
                        <li class="dropdown hidden-xs">
                            <a class="right-sidebar-toggle" aria-expanded="false">
                                <i class="fa fa-tasks"></i> 主题
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            <div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab" data-id="record.html">首页</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>
                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="divider"></li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a href="login.html" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="record.html" frameborder="0" data-id="record.html" seamless></iframe>
                <%--  <jsp:include page="${pages}"></jsp:include> --%>
            </div>
            <div class="footer">
                <div class="pull-right">&copy; 2017 <a target="_blank">XXXX</a>
                </div>
            </div>
        </div>
        <!--右侧部分结束-->
        <!--右侧边栏开始-->
        <div id="right-sidebar">
            <div class="sidebar-container">
                <ul class="nav nav-tabs navs-3">
                    <li class="active">
                        <a data-toggle="tab" href="#tab-1">
                            <i class="fa fa-gear"></i> 主题
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="tab-1" class="tab-pane active">
                        <div class="sidebar-title">
                            <h3> <i class="fa fa-comments-o"></i> 主题设置</h3>
                            <small><i class="fa fa-tim"></i> 你可以从这里选择和预览主题的布局和样式，这些设置会被保存在本地，下次打开的时候会直接应用这些设置。</small>
                        </div>
                        <div class="skin-setttings">
                            <div class="title">主题设置</div>
                            <div class="setings-item">
                                <span>收起左侧菜单</span>
                                <div class="switch">
                                    <div class="onoffswitch">
                                        <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="collapsemenu">
                                        <label class="onoffswitch-label" for="collapsemenu">
                                            <span class="onoffswitch-inner"></span>
                                            <span class="onoffswitch-switch"></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="setings-item">
                                <span>固定顶部</span>
                                <div class="switch">
                                    <div class="onoffswitch">
                                        <input type="checkbox" name="fixednavbar" class="onoffswitch-checkbox" id="fixednavbar">
                                        <label class="onoffswitch-label" for="fixednavbar">
                                            <span class="onoffswitch-inner"></span>
                                            <span class="onoffswitch-switch"></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="setings-item">
                                <span>固定宽度</span>
                                <div class="switch">
                                    <div class="onoffswitch">
                                        <input type="checkbox" name="boxedlayout" class="onoffswitch-checkbox" id="boxedlayout">
                                        <label class="onoffswitch-label" for="boxedlayout">
                                            <span class="onoffswitch-inner"></span>
                                            <span class="onoffswitch-switch"></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="title">皮肤选择</div>
                            <div class="setings-item default-skin nb">
                                <span class="skin-name ">
                            <a href="#" class="s-skin-0">默认皮肤</a>
                        </span>
                            </div>
                            <div class="setings-item blue-skin nb">
                                <span class="skin-name ">
                <a href="#" class="s-skin-1">蓝色主题</a>
            </span>
                            </div>
                            <div class="setings-item yellow-skin nb">
                                <span class="skin-name ">
                <a href="#" class="s-skin-3">黄色/紫色主题</a>
            </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--右侧边栏结束-->
    </div>
    <!-- 修改密码 -->
    <div class="modal fade" id="pwdIndexModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">修改登录密码</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>输入原密码</label>
                        <input class="form-control" id="js-index-old-pwd" placeholder="原密码">
                    </div>
                    <div class="form-group">
                        <label>输入新密码</label>
                        <input class="form-control" id="js-index-new-pwd" placeholder="新密码">
                    </div>
                    <div class="form-group">
                        <label>再次输入新密码</label>
                        <input class="form-control" id="js-index-replace-pwd" placeholder="再次输入新密码">
                    </div>
                </div>
                <div class="modal-footer">
                    <a type="button" class="btn btn-outline btn-default" data-dismiss="modal">取消</a>
                    <a type="button" id="js-index-pwd-btn" class="btn btn-outline btn-primary">修改</a>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.修改密码 -->
    <jsp:include page="head/scripts.jsp"></jsp:include>
    <script>
    	seajs.use("index");
    </script>
</body>
</html>