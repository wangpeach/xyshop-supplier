<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
    <title>XXXX店铺管理后台 - 产品销售量报表</title>
    <jsp:include page="../head/baseHead.jsp"></jsp:include>
	<jsp:include page="../head/tableCss.jsp"></jsp:include>
	<jsp:include page="../head/layerDate.jsp"></jsp:include>
</head>
<body class="gray-bg">
	<input type="hidden" id="js-shop-uuid" value="${sessionScope.shopUuid }">
	<input type="hidden" id="js-begin-date">
	<input type="hidden" id="js-end-date">
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-sm-4">
            <h2>产品销售量报表</h2>
        </div>
    </div>
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-12">
            <div id="order-toolbar" class="btn-group">
             	<label>产品销售量报表</label>
             	<select class="form-control js-change-table-params" id="js-choise-day" style="width: 250px;display: inline-block;">
             		<option value="today" selected="selected">今日数据统计</option>
             		<option value="yesterday">昨日数据统计</option>
             		<option value="7day">7日内数据统计</option>
             		<option value="30day">30日数据统计</option>
             		<option value="week">本周数据统计</option>
             		<option value="month">本月数据统计</option>
             		<option value="appoint" id="js-appoint-time">指定统计时间</option>
				</select>
				<span id="js-time-span"></span>
			</div>
                <table id="reportTable"></table>
            </div>
        </div>
    </div>
    
<!-- Modal -->
  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
   <div class="modal-dialog"> 
    <div class="modal-content"> 
     <div class="modal-header"> 
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> 
      <h4 class="modal-title" id="myModalLabel">订单详情</h4> 
     </div> 
     <div class="modal-body" id="orderInfo">
     </div> 
     <div class="modal-footer"> 
      <a type="button" class="btn btn-outline btn-outline"  data-dismiss="modal">关闭</a> 
     </div> 
    </div> 
    <!-- /.modal-content --> 
   </div> 
   <!-- /.modal-dialog --> 
  </div> 
 <!-- /.modal --> 
 <!-- Modal -->
  <div class="modal fade" id="timeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
   <div class="modal-dialog"> 
    <div class="modal-content"> 
     <div class="modal-header"> 
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> 
      <h4 class="modal-title" id="myModalLabel">指定统计时间</h4> 
     </div> 
     <div class="modal-body" id="orderInfo">
     	开始时间：<input class="form-control layer-date " style="width: 150px;vertical-align: baseline;"   id="js-date-begin" placeholder="开始时间">
		结束时间：<input class="form-control layer-date " style="width: 150px;vertical-align: baseline;"  id="js-date-end" placeholder="结束时间"> 
     </div> 
     <div class="modal-footer"> 
      <a type="button" class="btn btn-outline btn-default"  data-dismiss="modal">关闭</a>
      <a type="button" class="btn btn-outline btn-info" id="js-time-btn"  >确定</a>  
     </div> 
    </div> 
    <!-- /.modal-content --> 
   </div> 
   <!-- /.modal-dialog --> 
  </div> 
 <!-- /.modal --> 
  
<jsp:include page="../head/scripts.jsp"></jsp:include>
<script>seajs.use("report/buyNumReportList");</script>
</body>
</html>
