define(function (require) {

    if (!String.prototype.includes) {
        String.prototype.includes = function (search, start) {
            'use strict';
            if (typeof start !== 'number') {
                start = 0;
            }

            if (start + search.length > this.length) {
                return false;
            } else {
                return this.indexOf(search, start) !== -1;
            }
        };
    }

    require.async(['jquery', 'contabs', 'icheck', 'ueditor', 'ueconfig'], function () {
        var imgs = require("imgsUtils");
        var $imgs = new imgs();
        var edit = UE.getEditor('editor');
        var common = require('common');
        var $common = new common();
        require('jqueryUtils');
        require("imgTip");
        var alert = require('alertUtils')
        var $alert = new alert();
        var upload = require('uploadUtils');
        var $upload = new upload(),
            $upload2 = new upload();

            
        var table = require('tableUtils');



        var t = new table();
        var columns = [{
            field: 'goodNo',
            title: '商品编号',
            align: 'center',
        }, {
            field: 'name',
            title: '商品名',
            align: 'center',
        }, {
            field: 'thumbImg',
            title: '商品图片',
            align: 'center',
            formatter: function (value, row, index) {
                return imgUpStr(row.thumbImgShow);
            }
        }, {
            field: 'shopName',
            title: '商家',
            align: 'center',
            sortable: true,
            visible: false,
        }, {
            field: 'moreImg',
            title: '更多图片',
            align: 'center',
            visible: false,
            formatter: function (value, row, index) {
                $imgs.lunbo("lunbo-" + index);
                return imgUpStr(row.moreImgShow, index);
            }
        }, {
            field: 'addTime',
            title: '添加时间',
            align: 'center',
            sortable: true,
        }, {
            field: 'orgPrice',
            title: '门市价',
            align: 'center',
            sortable: true,
        }, {
            field: 'price',
            title: '价格',
            align: 'center',
            sortable: true,
        }, {
            field: 'updateTime',
            title: '修改时间',
            align: 'center',
            sortable: true,
            visible: false,
        }, {
            field: 'saleNum',
            title: '销售量',
            align: 'center',
            sortable: true,
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            formatter: function (value, row, index) {
                switch (value) {
                    case "wait":
                        return "<span class='badge badge-info'>待上架</span>";
                    case "online":
                        return "<span class='badge badge-success'>已上架</span>";
                    case "offline":
                        return "<span class='badge badge-danger'>已下架</span>";
                    case "freeze":
                        return "<span class='badge badge-danger'>已冻结</span>";
                    case "reject":
                        return "<span class='badge badge-danger'>已驳回</span>";
                }
                return "-";
            }
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            formatter: function (value, row, index) {
                var opt = [];
                if(row.status != "freeze") {
                    opt.push('<a class="btn btn-outline btn-success js-update">修改</a>');
                }
                switch (row.status) {
                    case "online":
                        opt.push('<a class="btn btn-outline btn-danger js-goodown">下架</a>');
                        break;
                    case "offline":
                        opt.push('<a class="btn btn-outline btn-primary js-goodup">上架</a>');
                        break;
                }
                if(row.status != "freeze") {
                    opt.push('<a  class="btn btn-outline btn-danger js-delete">删除</a>');
                }
                opt.push('<a href="goodsinfo.html?u=' + row.uuid + '" class="btn btn-outline btn-info J_menuItem">详情</a>');
                return opt.join(" ");
            },
            events: {
                'click .js-update': function (e, value, row, index) {
                    $('#updateuuid').val(row.uuid);
                    $("#js-add-goods-btn").addClass('hide');
                    $("#js-upate-goods-btn").removeClass('hide');
                    $.get(row.desFile, function (data) {
                        edit.setContent(data);
                    });
                    $("#goodno").val(row.goodNo);
                    $("#name").val(row.name);
                    $("#shop-uuid").val(row.shopUuid);
                    $("#shop-name").val(row.shopName);
                    $("#orgprice").val(row.orgPrice);
                    $("#price").val(row.price);
                    //$("#stock").val(row.stock);
                    //$("#cat-uuid").val(row.catId);
                    //$("#cat-name").val(row.catName);
                    //$("#brand-uuid").val(row.brandId);
                    //$("#brand-name").val(row.brandName);

                    $("#headImgShow").html("<img id='headImg'  data-value='" + row.thumbImg + "' src='" + row.thumbImgShow + "?x-oss-process=image/resize,w_200'/></div>");
                    if ($common._noEmpty(row.moreImg)) {
                        if (!row.moreImg.includes('#')) {
                            $("#otherheadImgShow").append("<img  class='js-img-open otherImg' data-value='" + row.moreImg + "' src='" + row.moreImgShow + "?x-oss-process=image/resize,w_200'/></div>");
                        } else {
                            var imagelist = row.moreImg.split('#');
                            var imagelistShow = row.moreImgShow.split('#');
                            for (var int = 0; int < imagelistShow.length; int++) {
                                if (imagelistShow[int]) {
                                    $("#otherheadImgShow").append("<img class='js-img-open otherImg' data-value='" + imagelist[int] + "' src='" + imagelistShow[int] + "?x-oss-process=image/resize,w_200'/>");
                                }
                            }
                        }
                    }
                    $("#myModal").modal({
                        backdrop: 'static'
                    });
                },
                'click .js-goodown': function (e, value, row, index) {
                    execAction('goods/down', {'uuid': row.uuid}, "确定下架该商品吗？", "下架成功");
                },
                'click .js-goodup': function (e, value, row, index) {
                    execAction('goods/up', {'uuid': row.uuid}, "确定上架该商品吗？", "上架成功");
                },
                'click .js-delete': function (e, value, row, index) {
                    execAction('goods/delete', {'uuid': row.uuid}, "确定删除该商品吗？", "删除成功");
                }
            },
        }];
        t._setSort('addTime', 'desc');
        var $t = t._init("table", "goods/pagelist", columns, function (d) {
            d.uuid = $('#js-shop-uuid').val();
            d.status = $("#js-choise-online").val();
            d.shopUuid = $("#js-shop-uuid").val();
        });

        /**
         * [商品状态]
         */
        $(".js-change-table-params").change(function (e) {
            $t._refresh();
        });


        /**
         * 相关操作
         * @param action
         * @param args
         * @param tips
         * @param suctips
         */
        var execAction = function(action, args, tips, suctips) {
            $alert._warning(tips, "", function () {
                $.post(action, args, function (data, textStatus, xhr) {
                    /*optional stuff to do after success */
                    $alert._alert(suctips);
                    $t._refresh();
                });
            });
        }



        $('input.radio').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
            increaseArea: '20%' // optional
        });
        $(document).on('ifChecked', 'input.radio', function (event) {
            $("input[name='" + $(this).attr('name') + "']").removeAttr("checked");
            $(this).attr("checked", "checked");
        });

        var imgUpStr = function (src, index) {
            if ($common._noEmpty(src)) {
                if (src.indexOf('#') > 0) {
                    var imglist = src.split("#"),
                        temp = [];
                    for (var int = 0; int < imglist.length; int++) {
                        temp.push("<img class='js-img-open' data-value='" + imglist[int] + "' src='" + imglist[int] + "'/>");
                    }
                    return temp.join(' ');
                } else {
                    return "<img class='js-img-open' data-value='" + src + "' src='" + src + "'/>";
                }
            } else
                return "-";
        };


        /**
         * 上传 图片
         * @type {null}
         */
        var imgUP = null,
            imgOtherUP = null;
        $('#myModal').on('shown.bs.modal', function (e) {
            UE.getEditor('editor');
            imgUP = $upload._init(imgUP, '#imgDiv', function (file, response) {
                $("#headImgShow").html("<img  id='headImg'  data-value='" + response.value + "' src='" + response.url + "?x-oss-process=image/resize,w_200'/>");
            });
            imgOtherUP = $upload2._init(imgOtherUP, '#imgOtherDiv', function (file, response) {
                var appendOImagesFlag = $common._imgUP("otherImg", "" + response.url);

                if (appendOImagesFlag) {
                    $("#otherheadImgShow").append("<img class='otherImg' data-value='" + response.value + "' src='" + response.url + "?x-oss-process=image/resize,w_200'/></div>");
                }
            });
        });
        $('#myModal').on('hidden.bs.modal', function (e) {
            $(".form-control").val('');
            $("#headImgShow").html('');
            $("#otherheadImgShow").html('');
        });
        /*添加商品*/
        $(document).on("click", "#js-add", function (e) {
            $("#js-add-goods-btn").removeClass('hide');
            $("#js-upate-goods-btn").addClass('hide');
            $("#otherheadImgShow").val('');
            $(".form-control").val('');
            edit.setContent('');
            $("#myModal").modal({
                backdrop: 'static'
            });
        });


        $(document).on("click", "#js-add-goods-btn", function (e) {
            var otherImages = '';

            var vernull = false;
            $("#myModal .form-group input.required").each(function (inx, item) {
                if(!$(item).val()) {
                    $alert._alert($(item).prev().text() + "不能为空");
                    vernull = true;
                    return false;
                }
            });
            if(vernull) return false;

            var _price = $("#price").val();
            var _balance = $("#balance").val();
            var _headImg = $("#headImg").data('value');
            if (parseFloat(_balance) > parseFloat(_price)) {
                $alert._alert('结算价输入错误');
                return false;
            }

            if(!_headImg) {
                $alert._alert("必须上传商品图片");
                return false;
            }

            var array = new Array();
            $('.otherImg').each(function (inx, item) {
                array.push($(item).data('value'));
            })
            otherImages = array.join("#");

            if (otherImages == '') {
                $alert._alert("必须上传商品图片");
                return false;
            }
            $.ajax({
                url: "goods/save",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                data: {
                    thumbImg: $("#headImg").data('value'),
                    moreImg: otherImages,
                    goodNo: $("#goodno").val(),
                    name: $("#name").val(),
                    price: $("#price").val(),
                    orgPrice: $("#orgprice").val(),
                    //balance: $("#balance").val(),
                    //stock: $("#stock").val(),
                    //catId: $("#cat-uuid").val(),
                    //catName: $("#cat-name").val(),
                    //brandId: $("#brand-uuid").val(),
                    //brandName: $("#brand-name").val(),
                    desFile: UE.getEditor('editor').getContent(),
                    shopUuid: $("#js-shop-uuid").val(),
                    shopName: $("#js-shop-name").val(),
                },
                dataType: "json",
                async: true,
                success: function (data) {
                    $alert._strSuc('商品添加成功');
                    $t._refresh();
                    $("#myModal").modal('hide');
                },
                error: function () {
                    $alert._alert('商品添加失败');
                }
            });
        });
        /*修改商品基础信息*/
        $(document).on("click", "#js-upate-goods-btn", function (e) {
            var otherImages = '';

            var vernull = false;
            $("#myModal .form-group input.required").each(function (inx, item) {
                if(!$(item).val()) {
                    $alert._alert($(item).prev().text() + "不能为空");
                    vernull = true;
                    return false;
                }
            });
            if(vernull) return false;

            var _price = $("#price").val();
            var _balance = $("#balance").val();
            var _headImg = $("#headImg").data('value');
            if (parseFloat(_balance) > parseFloat(_price)) {
                $alert._alert('结算价输入错误');
                return false;
            }

            if(!_headImg) {
                $alert._alert("必须上传商品图片");
                return false;
            }

            var array = new Array();
            $('.otherImg').each(function (inx, item) {
                array.push($(item).data('value'));
            })
            otherImages = array.join("#");

            if (otherImages == '') {
                $alert._alert("必须上传商品图片");
                return false;
            }

            $.ajax({
                url: "goods/resave",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                data: {
                    uuid: $('#updateuuid').val(),
                    thumbImg: $("#headImg").data('value'),
                    moreImg: otherImages,
                    goodNo: $("#goodno").val(),
                    name: $("#name").val(),
                    price: $("#price").val(),
                    orgPrice: $("#orgprice").val(),
                    //balance: $("#balance").val(),
                    //stock: $("#stock").val(),
                    //catId: $("#cat-uuid").val(),
                    //catName: $("#cat-name").val(),
                    //brandId: $("#brand-uuid").val(),
                    //brandName: $("#brand-name").val(),
                    desFile: UE.getEditor('editor').getContent(),
                    shopUuid: $("#js-shop-uuid").val(),
                    shopName: $("#js-shop-name").val(),
                },
                dataType: "json",
                async: true,
                success: function (data) {
                    $alert._alert('商品修改成功');
                    $t._refresh();
                    $("#myModal").modal('hide');
                },
                error: function () {
                    $alert._alert('商品修改失败');
                    $("#myModal").modal('hide');
                },
                beforeSend: function (XMLHttpRequest, self) {
                    var array = {};
                    array.brandId = "";
                    array.brandName = "";
                    var temp = $common._paramsEmptyFill(self, array);
                    if (!temp.r) {
                        $alert._alert('请确认所有参数都填写完整!');
                        XMLHttpRequest.abort();
                    }
                    self.data = temp.data;
                }
            });
        });


        /*清空所有图片*/
        $(document).on("click", ".otherImg", function (e) {
            var that = this;
            $alert._warning("确定删除该图片？", "该操作将删除所选图片", function () {
                $(that).remove();
                imgOtherUP.reset();
            });
        });


        /*选择商品分类*/
        //$(document).on("click", "#js-cat-select-btn", function (e) {
        //    $("#js-cat-select").empty();
        //    $.ajax({
        //        url: "category/list",
        //        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        //        type: "post",
        //        dataType: "json",
        //        data: {
        //            key: $("#cat-name").val()
        //        },
        //        async: true, success: function (data) {
        //            for (var key in data) {
        //                $("#js-cat-select").append('<option value="' + data[key].uuid + '">' + data[key].name + '</option>');
        //            }
        //            $("#cat-selectBox").removeClass('hide');
        //        }
        //    });
        //});
        //$(document).on("click", "#js-cat-sure", function (e) {
        //    $("#cat-selectBox").addClass('hide');
        //    $("#cat-uuid").val($("#js-cat-select").val().split('-')[0]);
        //    $("#cat-name").val($("#js-cat-select").find("option:selected").text());
        //});

        /*选择品牌*/
        //$(document).on("click", "#js-brand-select-btn", function (e) {
        //    $("#js-brand-select").empty();
        //    $.ajax({
        //        url: "brand/list",
        //        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        //        type: "post",
        //        dataType: "json",
        //        data: {
        //            key: $("#brand-name").val()
        //        },
        //        async: true, success: function (data) {
        //            for (var key in data) {
        //                $("#js-brand-select").append('<option value="' + data[key].uuid + '">' + data[key].name + '</option>');
        //            }
        //            $("#brand-selectBox").removeClass('hide');
        //        }
        //    });
        //});
        //$(document).on("click", "#js-brand-sure", function (e) {
        //    $("#brand-selectBox").addClass('hide');
        //    $("#brand-uuid").val($("#js-brand-select").val().split('-')[0]);
        //    $("#brand-name").val($("#js-brand-select").find("option:selected").text());
        //});

        /*所有数字类型input的数必须大于0*/
        $(document).on("change", "input[type='number']", function (e) {
            if (parseInt($(this).val()) < 1) {
                $alert._alert('数值必须大于0');
                $(this).val('1');
            }
        });

        /*表单验证*/
        var validate = require('validateUtils');
        var $validate = new validate();
        $validate._init('form');
    });
});
