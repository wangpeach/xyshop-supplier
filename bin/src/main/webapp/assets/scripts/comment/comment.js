define(function(require) {
    require.async(['jquery', 'contabs'], function() {
        var common = require('common');
        var $common = new common();
        require('jqueryUtils');
        require("imgTip");
        var alert = require('alertUtils');
        var $alert = new alert();
        var table = require('tableUtils');
        /*用户表表*/
        var comment = new table();
        var columns = [{
            field: 'userHead',
            title: '用户头像',
            align: 'center',
            formatter: function(value, row, index) {
                if ($common._noEmpty(value)) {
                    return "<img class='js-img-open' src='" + value + "' />";
                }
                return "-";
            }
        }, {
            field: 'userName',
            title: '用户名',
            align: 'center',
        }, {
            field: 'shopName',
            title: '商铺名',
            align: 'center',
        }, {
            field: 'goodsName',
            title: '商品名',
            align: 'center',
            formatter: function(value, row, index) {
                if(!value) {
                    return '-';
                }
                return value;
            }
        }, {
            field: 'judgeLevel',
            title: '评分',
            align: 'center',
        }, {
            field: 'judgeContent',
            title: '评价内容',
            align: 'center',
        }, {
            field: 'imgs',
            title: '图片',
            align: 'center',
            formatter: function(value, row, index) {
                if(value) {
                    var imgs = value.split(';'), array = new Array();
                    for (var i = 0; i < imgs.length; i++) {
                        array.push('<img class="js-img-open" src="'+ imgs[i] +'" alt="" />');
                    }
                    return array.join(" ");
                }
                return '-';
            }
        }, {
            field: 'replyContent',
            title: '回复内容',
            align: 'center',
        }, {
            field: 'addTime',
            title: '创建日期',
            align: 'center',
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            formatter: function(value, row, index) {
                if(!row.replyContent) {
                    var opt = [];
                    opt.push('<a class="btn btn-outline btn-success reply">回复</a>');
                    return opt.join(" ");    
                }
                return '-';
            },
            events: {
                'click .reply': function(e, value, row, index) {
                    $("#cmt-uuid").val(row.uuid);
                    $("#cmt-msg").text("评价内容：" + row.judgeContent);
                    $("#replyctx").val('');
                    $("#replyModal").modal({
                        backdrop: 'static'
                    });
                }
            }
        }];
        comment._setSort('addTime', 'desc');
        var $comment = comment._init("table", "/shop-goods/goodsjudge/list", columns, function(d) {
            d.status = "pass";
            d.replyed = $("#js-choise-reply").val();
            d.shopUuid = $('#js-shop-uuid').val();
        });

        $(".js-change-table-params").change(function() {
            $comment._refresh();
        });
        
        $(".btn-reply").click(function() {
            var ctx = $("#replyctx").val();
            if(!ctx) {
                $alert._alert("请填写回复内容");
                return false;
            }
            $.post('/shop-goods/goodsjudge/reply', {"uuid": $("#cmt-uuid").val(), "replyctx": ctx}, function(data, textStatus, xhr) {
                /*optional stuff to do after success */
                if(data === "success") {
                    $alert._strSuc("回复成功");
                    $comment._refresh();
                    $("#replyModal").modal('hide');
                } else {
                    $alert._alert("回复失败");
                }
            });
        });

    });
});
