<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>店铺后台 - 店铺设置</title>
        <jsp:include page="../head/baseHead.jsp"></jsp:include>
        <style>
            #tip {
                width: 180px;
                background-color: #1ab394;
                color: #333;
                border: 1px solid silver;
                box-shadow: 3px 4px 3px 0px silver;
                position: absolute;
                /* top: 43.6%; */
                margin-top: -405px;
                right: 10px;
                border-radius: 5px;
                overflow: hidden;
                line-height: 20px;
            }

            #tip input[type="text"] {
                height: 25px;
                border: 0;
                padding-left: 5px;
                width: 90%;
                margin: 0px 5%;
                border-radius: 3px;
                outline: none;
            }
        </style>
    </head>

    <body>
        <div class="row wrapper border-bottom white-bg page-heading">
            <div class="col-sm-4">
                <h2>商家-店铺基本信息设置</h2>
            </div>
        </div>
        <div class="wrapper wrapper-content" style="width: 50%;">
            <div class="row">
                <div class="col-sm-12">
                    <a class="list-group-item">
                        <label>店铺名称</label>
                        <input class="form-control" type="text" id="js-store-name" value="${self.name}" placeholder="店铺名称">
                    </a>
                    <a class="list-group-item">
                        <label>店铺手机</label>
                        <input class="form-control" type="number" id="js-store-phone" value="${self.shopPhone}" placeholder="店铺手机">
                    </a>
                    <a class="list-group-item">
                        <label>所在地区</label>
                        <div style="display: block;">
                            <input type="hidden" id="areaIds" value="${self.areaid}">
                            <select class="form-control js-pro js-areas" id="js-add-pro" style="width:31%;display: inline-block;" placeholder="省">
                                <option value="-" selected="selected">-</option>
                            </select>
                            <select class="form-control js-city js-areas" id="js-add-city" style="width:31%;display: inline-block;" placeholder="市">
                                <option value="-">-</option>
                            </select>
                            <select class="form-control js-area js-areas" id="js-add-area" style="width:31%;display: inline-block;" placeholder="县">
                                <option value="-">-</option>
                            </select>
                        </div>
                    </a>
                    <a class="list-group-item">
                        <label>店铺地址</label>
                        <input class="form-control" type="text" id="js-store-address" value="${self.address}" placeholder="店铺地址">
                    </a>
                    <a class="list-group-item">
                        <label>地图经度</label>
                        <input class="form-control js-jingweidu" type="text" id="js-store-lng" value="${self.longitude}" placeholder="地图经度">
                    </a>
                    <a class="list-group-item">
                        <label>地图纬度</label>
                        <input class="form-control js-jingweidu" type="text" id="js-store-lat" value="${self.latitude}" placeholder="地图纬度">
                    </a>
                    <div id="allmap" style="width:100%;height:400px;border: 1px solid #CCCFD3;margin: 10px"></div>
                    <div id="tip">
                        <input type="text" id="keyword" name="keyword" placeholder="请输入关键字：(选定后搜索)"/>
                    </div>
                    <a class="list-group-item" id="editorItem">
                        <label>商家详情 </label><small>(上传图片大小最好在2M以内)</small>
                        <input type="hidden" id="shopDetail" value="${self.shopDetail}">
                        <script id="editor" type="text/plain" style="height:500px;"></script>
                    </a>
                    <div class="modal-footer">
                        <a id="js-reset-btn" class="btn  btn-default" style="margin-left: 150px;">重置</a>
                        <a id="js-save-btn" class="btn  btn-primary">保存</a>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../head/scripts.jsp"></jsp:include>
        <script>
        seajs.use("supplier/setting.js");
        </script>
    </body>

    </html>
