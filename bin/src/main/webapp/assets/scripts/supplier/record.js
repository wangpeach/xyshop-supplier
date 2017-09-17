define(function(require) {
    require.async(['jquery', 'contabs'], function() {
        var imgs = require("imgsUtils");

        var $imgs = new imgs();
        // var common = require('common');
        // var $common = new common();
        // require('jqueryUtils')
        var alert = require('alertUtils');
        var $alert = new alert();

        var layer = require("layer");

        var review = false;
        /* -- 加载店铺钱包的基本信息 -- */
        $(document).ready(function(e) {
            var url = $('#js-wx-domain').val();
            $.ajax({
                async: false,
                url: url + 'shop/qr.html?shopUuid=' + $('#js-shop-uuid').val(),
                type: "get",
                dataType: 'jsonp',
                jsonp: 'callback',
                jsonpCallback: "success_jsonpCallback",
                timeout: 5000,
                success: function(json) {
                    if (json.status == 'true') {
                        $('#qrimg').attr('src', json.msg);
                        $('#js-qr-download').attr('href', json.zip);
                    }
                },
                error: function(xhr) {
                    console.log(xhr);
                }
            });

            $.ajax({
                url: "/shop-supplier/wallet/ajax/shopInfo",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    shopUuid: $('#js-shop-uuid').val()
                },
                async: false,
                success: function(data) {
                    if (data[0] == 'success') {
                        // $('#js-alipay').text(data[1]['alipayAccount']);
                        // $('#js-alipayName').text(data[1]['alipayAccountName']);
                        $('#js-cartId').text(data[1]['cartId']);
                        $('#js-cartName').text(data[1]['cartName']);
                        $('#js-balance').text(data[1]['money']);
                        $('#js-score').text(data[1]['score']);
                        var temp = [];
                        if (!$.isEmptyObject(data[2])) {
                            if (data[2].status === "wait") {
                                // temp.push('当前有一个修改申请正在等待处理...  ')
                                // temp.push('用于结算的支付宝账户:' + data[2]["alipay"]);
                                // temp.push('绑定的支付宝结算账户名:' + data[2]["alipayName"]);
                                // temp.push('。用于结算的银行卡账户:' + data[2]["cartId"]);
                                // temp.push('，用于结算的卡主姓名:' + data[2]["cartUName"]);
                                // temp.push('，用于结算的银行卡所在银行:' + data[2]["cartName"]);
                                temp.push('您的结算信息正在审核中，请耐心等待，请勿重复修改。');
                                $('#js-wait-update-alert').addClass('alert-success').removeClass('hide');
                            } else if (data[2].status === "fail") {
                                temp.push('您提交的提现信息审核失败, 原因：(' + data[2].rebut + ')，请您根据反馈正确填写');
                                $('#js-wait-update-alert').addClass('alert-danger').removeClass('hide');
                            } else {
                                $('#js-cartUName').text(data[2]['cartUName']);
                                review = true;
                            }
                        } else {
                            temp.push('您还没有补充提现信息，账户余额将无法提现，请点击修改补充提现信息!');
                            $('#js-wait-update-alert').addClass('alert-danger').removeClass('hide');
                        }
                        $('#js-wait-update-alert-span').text(temp.join(' '));

                        $("#shopName").text(data[4] || '--');
                        $("#endTime").html(data[5] || '--');

                        if (!$.isEmptyObject(data[3])) {
                            $("#lastTime").text(data[3].addTime || '--');
                            $("#lastIp").text(data[3].ip || '--');
                        }
                    } else {
                        $alert._alert("店铺钱包信息加载失败");
                    }
                },
                error: function() {
                    $alert._alert("店铺钱包信息加载失败");
                }
            });

            $.post('moneyrecord/ajax/cs', { uuid: $('#js-shop-uuid').val() }, function(data, textStatus, xhr) {
                /*optional stuff to do after success */
                $("#js-getmoney").text(data.getmoney);
                $("#js-consumption-pep").text(data.con_pep);
                $("#js-consumption-money").text(data.con_money);
                $("#every_money").text(data.every_money);

            }, "json");
        });



        var table = require('tableUtils');
        /*商户收支记录表*/
        var coin = new table();
        var c_columns = [{
                field: 'money',
                title: '支付金额',
                align: 'center',
                formatter: function(value, row, index) {
                    return value + " 元";
                }
            }, {
                field: 'leftMoney',
                title: '收支后余额',
                align: 'center',
                formatter: function(value, row, index) {
                    return value + " 元";
                }
            }, {
                field: 'username',
                title: '用户名',
                align: 'center',
                formatter: function(value, row, index) {
                    if (row.type === "expend") {
                        return row.shopName;
                    }
                    return value ? value : '--';
                }
            }, {
                field: 'type',
                title: '类型',
                align: 'center',
                formatter: function(value, row, index) {
                    switch (value) {
                        case 'income':
                            return "<span class='badge badge-info'>收入</span>";
                        case 'expend':
                            return "<span class='badge badge-danger'>支出</span>";
                    }
                    return "-";
                }
            }, {
                field: 'remarks',
                title: '备注',
                align: 'center',
                formatter: function(value, row, index) {
                    var text = value;
                    switch (row.buyType) {
                        case 'custom':
                            text += ' (整单买单)';
                            break;
                        case 'assign':
                            text += ' (套餐买单)';
                            break;
                    }
                    return text;
                }
            }
            // , {
            //     field: 'addTime',
            //     title: '录入/申请时间',
            //     align: 'center',
            // }
            , {
                field: 'status',
                title: '状态',
                align: 'center',
                formatter: function(value, row, index) {
                    switch (value) {
                        case 'wait':
                            return "<span class='badge badge-info'>等待处理</span>";
                        case 'success':
                            if (row.type == "expend") {
                                return "<span class='badge badge-success'>成功</span>";
                            } else {
                                var _showKey = (row.buyType == "custom" ? "消费成功" : "已使用");
                                return "<span class='badge badge-success'>" + _showKey + "</span>";
                            }
                        case 'fail':
                            return "<span class='badge badge-danger'>失败</span>";
                    }
                    return "-";
                }
            }, {
                field: 'payType',
                title: '支付类型',
                align: 'center',
                formatter: function(value, row, inx) {
                    switch (value) {
                        case 'weixin':
                            return '微信支付';
                        case 'wallet':
                            return '钱包支付';
                        case 'coin':
                            return '金币支付';
                        case 'alipay':
                            return '支付宝';
                        case 'unionPay':
                            return '银联支付';
                    }
                    return "-";
                }
            }, {
                field: 'operateTime',
                title: '处理时间',
                align: 'center',
            }, {
                field: 'failReason',
                title: '详情',
                align: 'center',
            }
        ];
        coin._setTool('#coin-toolbar');
        coin._setSort('operateTime', 'desc');
        var $coin = coin._init("cointable", "/shop-supplier/moneyrecord/ajax/shoplist", c_columns, function(d) {
            d.shopUuid = $('#js-shop-uuid').val();
            d.type = $("#js-choise-type").val();
            d.hideCover = 'yes';
        }, false);

        $("#js-choise-type").change(function() {
            $coin._refresh();
        });

        /*
         * 自动刷新
         */
        setInterval(function() {
            $coin._refresh();
        }, 2000);



        /*商户积分记录表*/
        // var score = new table();
        // var s_columns = [{
        //     field: 'score',
        //     title: '涉及积分',
        //     align: 'center',
        // }, {
        //     field: 'type',
        //     title: '类型',
        //     align: 'center',
        //     formatter: function(value, row, index) {
        //         switch (value) {
        //             case 'income':
        //                 return "<span class='badge badge-info'>收入</span>";
        //             case 'expend':
        //                 return "<span class='badge badge-danger'>支出</span>";
        //         }
        //         return "-";
        //     }
        // }, {
        //     field: 'leftScore',
        //     title: '收支后剩余积分',
        //     align: 'center',
        // }, {
        //     field: 'remarks',
        //     title: '备注',
        //     align: 'center',
        // }, {
        //     field: 'addTime',
        //     title: '录入时间',
        //     align: 'center',
        // }];
        // score._setTool('#score-toolbar');
        // score._setSort('addTime', 'desc');
        // var $score = score._init("scoretable", "/shop-supplier/shopsocrerecord/list ", s_columns, function(d) {
        //     d.shopUuid = $('#js-shop-uuid').val();
        // });

        /*修改支付结算信息申请*/
        $("#js-update").click(function(e) {
            if (review) {
                $alert._alert("提现信息属于敏感操作，请联系客服修改。");
                return false;
            }
            // $('#js-update-alipay').val($('#js-alipay').text());
            // $('#js-update-alipayName').val($('#js-alipayName').text());
            $('#js-update-cartId').val($('#js-cartId').text());
            $('#js-update-cartName').val($('#js-cartName').text());
            $("#updateModal").modal({
                backdrop: 'static'
            });
        });

        //        $(document).on('click', '#js-qr-download', function(){
        //        	
        //        });

        $(document).on("click", '#js-update-btn', function() {
            // var alipay = $('#js-update-alipay').val();
            // var alipayName = $('#js-update-alipayName').val();
            var _cartId = $('#js-update-cartId').val();
            var _cartUname = $("#js-update-username").val();
            var _cartName = $('#js-update-cartName').val();
            if (_cartId == '') {
                $alert._alert("银行卡帐号不能都为空");
                return;
            } else {
                // if (alipay != '' && alipayName == '') {
                //     $alert._alert("支付宝昵称不能为空");
                //     return;
                // }
                if (_cartId != '' && _cartName == '') {
                    $alert._alert("银行卡所属银行不能为空");
                    return;
                }
                if (_cartId != '' && _cartUname == '') {
                    $alert._alert("银行卡卡主姓名不能为空");
                    return;
                }
            }
            $.ajax({
                url: "/shop-supplier/walletupdate/ajax/save",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    shopUuid: $('#js-shop-uuid').val(),
                    shopName: $('#js-shop-name').val(),
                    // alipay: alipay,
                    // alipayName: alipayName,
                    cartId: _cartId,
                    cartName: _cartName,
                    cartUname: _cartUname
                },
                async: false,
                success: function(data) {
                    if (data[0] == "success") {
                        $alert._alert("店铺支付信息修改申请提交成功,请等待管理员处理");
                        $("#updateModal").modal('hide');
                        var temp = [];
                        // temp.push('用于结算的支付宝账户:' + alipay);
                        // temp.push('绑定的支付宝结算账户名:' + alipayName);
                        // temp.push('。用于结算的银行卡账户:' + _cartId);
                        // temp.push('，用于结算的卡主姓名:' + _cartUname);
                        // temp.push('，用于结算的银行卡所在银行:' + _cartName);
                        temp.push('您的结算信息正在审核中，请耐心等待，请勿重复修改。');
                        $('#js-wait-update-alert-span').text(temp.join(' '));
                        $('#js-wait-update-alert').removeClass('hide alert-danger').addClass('alert-success');
                    } else {
                        $alert._alert("店铺支付信息修改失败");
                    }
                },
                error: function() {
                    $alert._alert("店铺支付信息修改失败");
                }
            });
        });
        /*积分兑换金币*/
        $("#js-change-score").click(function(e) {
            $('#js-max-score').text($('#js-score').text());
            $('#js-change-score-number').val('0');
            $("#scoreModal").modal({
                backdrop: 'static'
            });
        });
        $(document).on("click", '#js-score-btn', function(e) {
            try {
                var maxScore = parseInt($('#js-score').text());
                var changeScore = parseInt($('#js-change-score-number').val());
                if (changeScore <= 0) {
                    $alert._alert('兑换积分必须大于0');
                    return;
                }
                if (changeScore > maxScore) {
                    $alert._alert('进行兑换的积分不能超过店铺的积分余额');
                    return;
                }
                $.ajax({
                    url: "/shop-supplier/wallet/ajax/changescore",
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    type: "post",
                    dataType: "json",
                    data: {
                        shopUuid: $('#js-shop-uuid').val(),
                        score: changeScore,
                    },
                    async: false,
                    success: function(data) {
                        if (data[0] == "success") {
                            $alert._alert("积分兑换金币成功");
                            $('#js-score').text((maxScore - changeScore));
                            $('#js-score').text((maxScore - changeScore));
                            var b = parseFloat($('#js-balance').text());
                            $('#js-balance').text((b + changeScore * 0.85));
                            $coin._refresh();
                            $score._refresh();
                        } else if (data[0] == 'scorebiger') {
                            $alert._alert("进行兑换的积分不能超过店铺的积分余额");
                        } else {
                            $alert._alert("积分兑换金币失败");
                        }
                        $("#scoreModal").modal('hide');
                    },
                    error: function() {
                        $alert._alert("店铺支付信息修改失败");
                    }
                });
            } catch (e) {
                $alert._alert('积分兑换金币出错啦~');
            }
        });

        /*申请结算*/
        $("#js-jiesuan").click(function(e) {
            if (!review) {
                $alert._alert("请先修改提现信息");
                return false;
            }

            if (parseInt($('#js-balance').text()) < 100) {
                $alert._alert('余额必须大于等于100才能提现');
                return false;
            }
            $('#js-max-coin').text($('#js-balance').text());
            $('#js-change-coin-number').val('0');
            $("#jiesuanModal").modal({
                backdrop: 'static'
            });
        });
        var lockApply = false;
        $(document).on("click", '#js-jiesuan-btn', function(e) {
            try {
                if (lockApply) return false;
                var maxCoin = parseFloat($('#js-balance').text());
                var changeCoin = parseFloat($('#js-change-coin-number').val());
                if (changeCoin < 100) {
                    $alert._alert('提现金额必须大于等于100');
                    return;
                }

                if (changeCoin > maxCoin) {
                    $alert._alert('提现金额不能超过店铺的余额');
                    return;
                }
                lockApply = true;
                $.ajax({
                    url: "/shop-supplier/moneyrecord/ajax/addApply",
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    type: "post",
                    dataType: "json",
                    data: {
                        shopUuid: $('#js-shop-uuid').val(),
                        money: changeCoin,
                    },
                    async: false,
                    success: function(data) {
                        if (data[0] == "success") {
                            $alert._alert("提现申请成功,请等待管理员进行处理....");
                            $('#js-balance').text(parseFloat(maxCoin - changeCoin).toFixed(3));
                            $coin._refresh();
                        } else if (data[0] == 'moneybig') {
                            $alert._alert("提现的金额不能超过店铺的余额");
                        } else {
                            $alert._alert("提现申请失败");
                        }
                        $("#jiesuanModal").modal('hide');
                    },
                    error: function() {
                        $alert._alert("店铺提现申请失败");
                    },
                    complete: function() {
                        lockApply = false;
                    }
                });
            } catch (e) {
                lockApply = false;
                $alert._alert('店铺提现申请失败出错啦~');
            }
        });

        /*所有数字类型input的数必须大于0*/
        $(document).on("change", "input[type='number']", function(e) {
            if (parseInt($(this).val()) < 1) {
                $alert._alert('数值必须大于0');
                $(this).val('1');
            }
        });


        /**
         * 核销 搜索订单
         */

        var clear_write_off = function() {
            $("#js-order-no").text('--');
            $("#js-buy-user").text('--');
            $("#js-pay-why").text('--');
            $("#js-buy-type").text('--');
            $("#js-buy-price").text('--');
            $("#js-goods-name").text('--');
            $("#js-buy-num").text('--');
            $("#js-buy-time").text('--');
            $("#js-action").html('--');
        }

        var lock = false;
        $('#search-write-off').keydown(function(e) {
            if ($(this).val().length >= 12 && e.keyCode != 8) {
                return false;
            } else {
                clear_write_off();
                lock = false;
            }
        }).keyup(function(e) {
            var _code = $(this).val(),
                cardCode = '',
                cardCodeArray = '';

            if (_code) {
                cardCodeArray = _code.split('');
            }
            for (var i = 0; i < cardCodeArray.length; i++) {
                cardCode += cardCodeArray[i];
                if ((i + 1) % 4 == 0 && (i + 1) < 12) {
                    cardCode += "  ";
                }
            }
            $("#formatCard").text(cardCode);


            if (_code.length == 12 && !lock) {
                lock = true;
                $.post("union-orders/ajax/search-consume", { cardcode: $(this).val() },
                    function(data, textStatus, jqXHR) {
                        console.log(data);
                        if (data.status && data.status == "no_data") {
                            $alert._alert("核销码无效，请重新输入");
                            return false;
                        }

                        if (data) {
                            $("#js-order-no").text(data.orderNo);
                            $("#js-buy-user").text(data.userName);
                            $("#js-buy-price").text(data.totalPrice);
                            $("#js-pay-why").text(function() {
                                switch (data.payWay) {
                                    case 'coin':
                                        return '金币支付';
                                    case 'weixin':
                                        return '微信支付';
                                    case 'wallet':
                                        return '钱包支付';
                                }
                            });
                            $("#js-buy-type").text(function() {
                                if (data.buyType == 'custom')
                                    return '整单买单';
                                else
                                    return '套餐买单';
                            });
                            $("#js-goods-name").text(data.goodsName);
                            $("#js-buy-num").text(data.goodsNum);
                            $("#js-buy-time").text(data.addTime);
                            $("#js-action").html(function() {
                                return '<a type="button" id="js-consume" data-uid="' + data.uuid + '" class="btn btn-outline btn-info ">核销</a>';
                            });
                        }
                    },
                    "json"
                );
            }
        });
        /**
         * 核销
         */
        $(document).delegate('#js-consume', 'click', function(e) {
            var uuid = $(this).data('uid');
            $alert._warning("确认核销该订单吗？", "", function() {
                var inx = layer.msg("核销中,请稍后...");

                $.post("union-orders/ajax/consume", { "uuid": uuid },
                    function(data, textStatus, jqXHR) {
                        if (data.orderno) {
                            $.ajax({
                                async: false,
                                url: $('#js-wx-domain').val() + "/notify/consumenotify.html",
                                type: "get",
                                data: { "orderuuid": data.orderuuid, "backscore": data.backscore },
                                dataType: 'jsonp',
                                jsonp: 'callback',
                                jsonpCallback: "success_jsonpCallback",
                                timeout: 5000,
                                success: function(json) {
                                    console.log('模板消息发送成功');
                                    console.log(json.status);

                                    $.ajax({
                                        async: false,
                                        url: $('#js-wx-domain').val() + "/shop/subcomm.html",
                                        type: "get",
                                        data: { "orderuuid": data.orderuuid },
                                        dataType: 'jsonp',
                                        jsonp: 'callback',
                                        jsonpCallback: "success_jsonpCallback",
                                        timeout: 5000,
                                        success: function(json) {
                                            layer.close(inx);
                                            $alert._alert("核销成功");
                                            clear_write_off();
                                            $coin._refresh();
                                            console.log('启动分佣');
                                            console.log(json.status);
                                        },
                                        error: function(xhr) {
                                            console.log(xhr);
                                        }
                                    });
                                },
                                error: function(xhr) {
                                    console.log(xhr);
                                }
                            });

                        }
                    },
                    "json"
                );
            });
        });


    });
});