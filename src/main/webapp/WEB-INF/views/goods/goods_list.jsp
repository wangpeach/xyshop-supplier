<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>

<head>
    <base href="<%=basePath%>">
    <title>管理后台 - 商品列表界面</title>
    <jsp:include page="../head/baseHead.jsp"></jsp:include>
    <jsp:include page="../head/tableCss.jsp"></jsp:include>
    <jsp:include page="../head/uploadCss.jsp"></jsp:include>
    <style>
        .js-img-open {
            height: 48px;
        }

        #headImgShow,
        #otherheadImgShow{
            margin: 5px 0px;
        }
        #headImg,
        .otherImg {
            width: 100px;
        }
    </style>
</head>

<body class="gray-bg">
<input type="hidden" id="js-shop-uuid" value="${_loginshop_.uuid }">
<input type="hidden" id="js-shop-name" value="${_loginshop_.name }">
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-4">
        <h2>商品管理</h2>
    </div>
</div>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-12">
            <div id="toolbar" class="btn-group">
                <a type="button" id="js-add" class="btn btn-outline btn-primary">添加商品</a>
                <label style="padding-left: 10px;">商品状态:</label>
                <select class="form-control js-change-table-params" id="js-choise-online"
                        style="width: 150px;display: inline-block;">
                    <option value="" selected="selected">全部</option>
                    <option value="wait">待上架</option>
                    <option value="online">已上架</option>
                    <option value="offline">已下架</option>
                    <option value="freeze">已冻结</option>
                    <option value="reject">已驳回</option>
                </select>
            </div>
            <table id="table"></table>
        </div>
    </div>
</div>
<form>
    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">添加/修改商品基础信息</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="updateuuid"/>
                    <div class="form-group">
                        <label>商品图片</label>
                        <small>(图片大小建议:宽x高=540x280)</small>
                        <div id="headImgShow"></div>
                        <div id="imgDiv">上传商品图片</div>
                    </div>
                    <div class="form-group">
                        <label>更多商品图片</label>
                        <small>(图片大小建议:宽x高=540x280,可以添加多张图片)</small>
                        <div id="otherheadImgShow"></div>
                        <div id="imgOtherDiv">上传更多商品图片</div>
                        <span style="position: absolute; margin-top: -30px; left: 170px; color: chocolate;">(点击图片即可删除)</span>
                    </div>
                    <div class="form-group">
                        <label>商品编号</label>
                        <input class="form-control" id="goodno" placeholder="商品编号(如不填写系统自动生成)">
                    </div>
                    <div class="form-group">
                        <label>商品名</label>
                        <input class="form-control required" id="name" placeholder="商品名">
                    </div>
                    <div class="form-group">
                        <label>门市价</label>
                        <input class="form-control required" id="orgprice" placeholder="价格">
                    </div>
                    <div class="form-group">
                        <label>价格</label>
                        <input class="form-control required" id="price" placeholder="价格">
                    </div>
                    <%--<div class="form-group">--%>
                        <%--<label>库存</label>--%>
                        <%--<input class="form-control" id="stock" placeholder="库存(可以为空)">--%>
                    <%--</div>--%>
                    <!-- 选择分类：开始 -->
                    <%--<div class="form-group">--%>
                        <%--<label style="width: 100px;">选择所属分类</label>--%>
                        <%--<input class="form-control" style="width: 55%;display: inline-block;" id="cat-name"--%>
                               <%--placeholder="输入分类名字,点击搜索进行选择">--%>
                        <%--<input class="hide" id="cat-uuid">--%>
                        <%--<span style="width: 20%;float: right;" class="btn  btn-info" id="js-cat-select-btn">搜索</span>--%>
                    <%--</div>--%>
                    <%--<div class="form-group hide" id="cat-selectBox">--%>
                        <%--<label style="width: 100px;"></label>--%>
                        <%--<select class="form-control" style="width: 56%;display: inline-block;" id="js-cat-select">--%>
                        <%--</select>--%>
                        <%--<span class="btn btn-outline btn-primary" id="js-cat-sure" style="float: right;">确定</span>--%>
                    <%--</div>--%>
                    <!-- 选择分类：结束 -->
                    <!-- 选择品牌：开始 -->
                    <%--<div class="form-group">--%>
                            <%--<label style="width: 100px;">选择所属品牌</label>--%>
                            <%--<input class="form-control" style="width: 55%;display: inline-block;" id="brand-name" placeholder="输入品牌名字,可以为空">--%>
                            <%--<input class="hide" id="brand-uuid">--%>
                             <%--<span style="width: 20%;float: right;"  class="btn  btn-info" id="js-brand-select-btn"  >搜索</span>--%>
                        <%--</div>--%>
                        <%--<div class="form-group hide" id="brand-selectBox">--%>
                            <%--<label style="width: 100px;"></label>--%>
                            <%--<select class="form-control" style="width: 56%;display: inline-block;" id="js-brand-select">--%>
                            <%--</select>--%>
                            <%--<span class="btn btn-outline btn-primary"  id="js-brand-sure" style="float: right;">确定</span>--%>
                        <%--</div>--%>
                    <!-- 选择分类：结束 -->
                    <div class="form-group">
                        <label>商品详情 </label>
                        <small>(上传图片大小最好在2M以内)</small>
                        <div class="form-group" id="editorItem">
                            <script id="editor" type="text/plain" style="height:500px;"></script>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a type="button" class="btn btn-outline btn-default" data-dismiss="modal">取消</a>
                    <a type="button" id="js-add-goods-btn" class="btn btn-outline btn-primary">添加</a>
                    <a type="button" id="js-upate-goods-btn" class="btn btn-outline btn-primary hide">保存</a>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
</form>
<jsp:include page="../head/scripts.jsp"></jsp:include>
<script>
    seajs.use("goods/goodsList");
</script>
</body>

</html>
