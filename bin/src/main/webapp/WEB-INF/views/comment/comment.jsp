<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>管理后台 - 用户评论审核</title>
    <jsp:include page="../head/baseHead.jsp"></jsp:include>
    <jsp:include page="../head/tableCss.jsp"></jsp:include>
    <jsp:include page="../head/layerDate.jsp"></jsp:include>
    <style>
    .js-img-open {
        height: 40px;
    }
    #toolTipLayer img {
        width: 550px;
    }
    </style>
</head>
<body class="gray-bg">
    <input type="hidden" id="js-shop-uuid" value="${shopUuid }">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-12">
                <div id="toolbar" class="btn-group">
                    <label style="padding-left: 10px;">用户身份:</label>
                    <select class="form-control js-change-table-params" id="js-choise-reply" style="width: 150px;display: inline-block;">
                        <option value="" selected="selected">全部</option>
                        <option value="1">已回复</option>
                        <option value="0">未回复</option>
                    </select>
                    <!-- <label>注册时间</label>
                    <input class="form-control layer-date js-change-table-params" style="width: 150px;vertical-align: baseline;" id="js-date-begin" placeholder="开始时间">
                    <input class="form-control layer-date js-change-table-params" style="width: 150px;vertical-align: baseline;" id="js-date-end" placeholder="结束时间"> -->
                </div>
                <table id="table"></table>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="replyModal" tabindex="-1" role="dialog" aria-labelledby="userAuthLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">回复</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="cmt-uuid" />
                    <p id="cmt-msg"></p>
                    <div class="form-group">
                        <label style="float:left;line-height:100px">回复内容：</label>
                        <textarea style="float: left; height: 100px; width: 80%;  resize: none;" id="replyctx"></textarea>
                    </div>
                </div>
                <div class="modal-footer" style="margin-top: 80px;">
                    <a type="button" class="btn btn-outline btn-danger" data-dismiss="modal">取消</a>
                    <a type="button" class="btn btn-outline btn-primary btn-reply">确定</a>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <jsp:include page="../head/scripts.jsp"></jsp:include>
    <script>
    	seajs.use("comment/comment");
    </script>
</body>
</html>