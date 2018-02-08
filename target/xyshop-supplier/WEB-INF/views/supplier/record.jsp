<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>商家管理后台 -收支详情界面</title>
    <jsp:include page="../head/baseHead.jsp"></jsp:include>
    <jsp:include page="../head/tableCss.jsp"></jsp:include>
</head>
<body class="gray-bg">
    <input type="hidden" id="js-shop-uuid" value="${_loginshop_.uuid }">
    <input type="hidden" id="js-shop-name" value="${_loginshop_.name }">
    <div class="row wrapper border-bottom">
    	<div>
    		<div class="col-sm-12" style="background: #ececec; padding-bottom: 1.2rem; margin-bottom: 2rem;">
             <h2 id="shopName" style="color: darkorange;"></h2>
             			【<span>上次登录时间： </span> <span id="lastTime" style="color: slategrey;">${lastTime }</span>】 【
             <span>上次登录Ip： </span> <span id="lastIp" style="color: slategrey;">${lastIp}</span>】 【
             <span>合同到期日期： </span> <span id="endTime" style="color: slategrey;">${end}</span>】
         </div>
    	</div>
    </div>
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-12">
                <div class="alert alert-dismissable hide" id="js-wait-update-alert">
                    <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                    <label><span id="js-wait-update-alert-span"></span></label>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <span style="font-size: 16px">商家余额</span>
                    </div>
                    <div class="panel-body">
                        <div class="dataTable_wrapper">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <!-- <th>用于提现的支付宝账户</th>
                                        <th>绑定的支付宝提现账户名</th> -->
                                        <th>用于提现的银行卡账户</th>
                                        <th>用于提现的持卡人</th>
                                        <th>用于提现的银行卡所在银行</th>
                                        <th>余额</th>
                                        <th>操作</th>
                                    </tr>
                                    <tr>
                                        <!-- <td id="js-alipay"></td>
                                        <td id="js-alipayName"></td> -->
                                        <td id="js-cartId"></td>
                                        <td id="js-cartUName"></td>
                                        <td id="js-cartName"></td>
                                        <td id="js-balance">0</td>
                                        <td>
                                            <a type="button" id="js-jiesuan" class="btn btn-outline btn-info ">提现</a>
                                            <a type="button" id="js-update" data-toggle="modal" data-target="#updateModal" class="btn btn-outline btn-success">修改</a>
                                        </td>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-12">
                <div class="alert alert-dismissable hide" id="js-consume-rec-alert">
                    <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                    <label><span id="js-consume-rec-alert-span"></span></label>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <span style="font-size: 16px">会员消费统计</span>
                    </div>
                    <div class="panel-body">
                        <div class="dataTable_wrapper">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>累计收单金额</th>
                                        <th>累计消费人次</th>
                                        <th>累计提现次数</th>
                                        <th>平均消费金额</th>
                                        <tr>
                                            <td id="js-getmoney">--</td>
                                            <td id="js-consumption-pep">--</td>
                                            <td id="js-consumption-money">--</td>
                                            <td id="every_money">--</td>
                                        </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-12">
                <div class="alert alert-dismissable hide" id="js-write-off-alert">
                    <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                    <label><span id="js-write-off-alert-span"></span></label>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <span style="font-size: 16px">订单核销</span>
                        <label for="search-write-off" style="margin-left: 20px;">序列码</label>
                        <input type="text" id="search-write-off" class="form-control" style="color: #585757; font-size: 14px; padding: 0px; border: 1px solid #cacaca; width: 180px; display: inline-block;">
                        <span style="font-size: 22px" id="formatCard"></span>
                    </div>
                    <div class="panel-body">
                        <div class="dataTable_wrapper">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>订单号</th>
                                        <th>购买用户名</th>
                                        <th>购买产品</th>
                                        <th>购买数量</th>
                                        <th>订单金额</th>
                                        <th>实际支付金额</th>
                                        <%--<th>优惠金额</th>--%>
                                        <th>支付方式</th>
                                        <th>购买时间</th>
                                        <th>操作</th>
                                    </tr>
                                    <tr>
                                        <td id="js-order-no">--</td>
                                        <td id="js-buy-user">--</td>
                                        <td id="js-goods-name">--</td>
                                        <td id="js-buy-num">--</td>
                                        <td id="js-order-price">--</td>
                                        <td id="js-pay-price">--</td>
                                        <%--<td id="js-favourable-price">--</td>--%>
                                        <td id="js-pay-why">--</td>
                                        <td id="js-buy-time">--</td>
                                        <td id="js-action">--</td>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-12">
                <div id="coin-toolbar" class="btn-group">
                    <label>店铺收支记录</label>
                    <label>类型:</label>
                    <select class="form-control js-change-table-params" id="js-choise-type" style="width: 150px;display: inline-block;">
                        <option value="" selected="selected">全部</option>
                        <option value="income">收入</option>
                        <option value="expend">支出</option>
                    </select>
                </div>
                <table id="cointable"></table>
            </div>
        </div>
    </div>
    <!-- 店铺提现:开始 -->
    <div class="modal fade" id="jiesuanModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">申请提现金额</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="js-update-uuid">
                    <div class="form-group">
                        <label>需要提现的金币(可提现金币:<span id="js-max-coin"></span>)</label>
                        <input type="number" class="form-control required" id="js-change-coin-number" placeholder="需要提现的金币">
                    </div>
                </div>
                <div class="modal-footer">
                    <a type="button" class="btn btn-outline btn-default" data-dismiss="modal">取消</a>
                    <a type="button" id="js-jiesuan-btn" class="btn btn-outline btn-primary">申请提现</a>
                </div>
            </div>
        </div>
    </div>
    <!--店铺提现:开始 -->
    <!--店铺修改:开始 -->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" >修改店铺结算信息(银行卡帐号不能都为空)</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="js-update-uuid">
                    <div class="form-group">
                        <label>银行卡帐号</label>
                        <input class="form-control required" id="js-update-cartId" placeholder="银行卡帐号">
                    </div>
                    <div class="form-group">
                        <label>卡主姓名</label>
                        <input class="form-control required" id="js-update-username" placeholder="卡主姓名">
                    </div>
                    <div class="form-group">
                        <label>银行卡开户行</label>
                        <input class="form-control required" id="js-update-cartName" placeholder="银行卡开户行">
                    </div>
                    <div class="form-group">
                        <label>开户行详细地址</label>
                        <input class="form-control required" id="js-update-cartOpanAddr" placeholder="开户行详细地址">
                    </div>
                </div>
                <div class="modal-footer">
                    <a type="button" class="btn btn-outline btn-default" data-dismiss="modal">取消</a>
                    <a type="button" id="js-update-btn" class="btn btn-outline btn-primary">提交修改申请</a>
                </div>
            </div>
        </div>
    </div>
    <!--店铺修改:结束 -->
    <jsp:include page="../head/scripts.jsp"></jsp:include>
    <script>
        seajs.use("supplier/record");
    </script>
</body>
</html>