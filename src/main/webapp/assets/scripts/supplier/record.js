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
            walletInfo();
            csinfo();
        });

        var walletInfo = function() {
            $.ajax({
                url: "shop/wallet-info",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                async: false,
                success: function(data) {
                    if (data) {
                        $('#js-balance').text(data.wallet.money);
                        var temp = [];
                        if (!$.isEmptyObject(data.upwallet)) {
                            review = data.upwallet.status;
                            $("#js-update-uuid").val(data.upwallet.uuid);
                            $('#js-cartId').text(data.upwallet.cartId);
                            $('#js-cartName').text(data.upwallet.cartName);
                            $('#js-cartUName').text(data.upwallet.cartUName);
                            if (review === "wait") {
                                temp.push('您的结算信息正在审核中，请耐心等待，请勿重复修改。');
                                $('#js-wait-update-alert').addClass('alert-success').removeClass('hide');
                            } else if (review === "fail") {
                                temp.push('您提交的提现信息审核失败, 原因：(' + data.upwallet.rebut + ')，请您根据反馈正确填写');
                                $('#js-wait-update-alert').addClass('alert-danger').removeClass('hide');
                            }
                        } else {
                            temp.push('您还没有补充提现信息，账户余额将无法提现，请点击修改补充提现信息!');
                            $('#js-wait-update-alert').addClass('alert-danger').removeClass('hide');
                        }
                        $('#js-wait-update-alert-span').text(temp.join(' '));
                    } else {
                        $alert._alert("店铺钱包信息加载失败");
                    }
                },
                error: function() {
                    $alert._alert("店铺钱包信息加载失败");
                }
            });
        }

        var csinfo = function() {
            $.post('money-record/csinfo', {
                shopUuid: $('#js-shop-uuid').val()
            }, function(data, textStatus, xhr) {
                /*optional stuff to do after success */
                $("#js-getmoney").text(data.money);
                $("#js-consumption-pep").text(data.buyedNum);
                $("#js-consumption-money").text(data.withdrawNum);
                $("#every_money").text(data.everyMoney);
            }, "json");
        }


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
        }, {
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
        }];
        coin._setTool('#coin-toolbar');
        coin._setSort('operateTime', 'desc');
        var $coin = coin._init("cointable", "money-record/list", c_columns, function(d) {
            d.shopUuid = $('#js-shop-uuid').val();
            d.type = $("#js-choise-type").val();
            d.hideCover = 'yes';
        }, false);

        $("#js-choise-type").change(function() {
            $coin._refresh();
        });

        $(document).on("click", '#js-update-btn', function() {
            var _cartId = $('#js-update-cartId').val();
            var _cartUname = $("#js-update-username").val();
            var _cartName = $('#js-update-cartName').val();
            var _cartOpen = $("#js-update-cartOpanAddr").val();
            if (!_cartId) {
                $alert._alert("银行卡帐号不能都为空");
                return;
            }
            if (!_cartName) {
                $alert._alert("银行卡开户行不能为空");
                return;
            }
            if (!_cartUname) {
                $alert._alert("银行卡卡主姓名不能为空");
                return;
            }
            if (!_cartOpen) {
                $alert._alert("开户行详细地址不能为空");
                return;
            }
            $.ajax({
                url: "updateWallet/update",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    uuid: $("#js-update-uuid").val(),
                    shopUuid: $('#js-shop-uuid').val(),
                    shopName: $('#js-shop-name').val(),
                    cartId: _cartId,
                    cartName: _cartName,
                    cartUName: _cartUname,
                    cartOpenAddr: _cartOpen
                },
                async: false,
                success: function(data) {
                    if (parseInt(data) > 0) {
                        $alert._alert("店铺支付信息修改申请提交成功,请等待管理员处理");
                        $("#updateModal").modal('hide');
                        var temp = [];
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


        /*申请结算*/
        $("#js-jiesuan").click(function(e) {
            if (!review) {
                $alert._alert("请先修改提现信息");
                return false;
            }
            if (review == "wait") {
                $alert._alert("提现信息审核中，请耐心等待");
                return false;
            }
            if (review == "fail") {
                $alert._alert("提现信息审核失败，请重新填写");
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
            $("#js-order-price").text('--');
            $("#js-pay-price").text('--');
            $("#js-favourable-price").text('--');
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
                $.post("order/search-consume", {
                        cardcode: $(this).val()
                    },
                    function(data, textStatus, jqXHR) {
                        if (!data) {
                            $alert._alert("序列码无效，请重新输入");
                            return false;
                        }

                        $("#js-order-no").text(data.orderNo);
                        $("#js-buy-user").text(data.userName);
                        $("#js-order-price").text(data.totalPrice);
                        $("#js-pay-price").text(data.payPrice);
                        // $("#js-favourable-price").text(data.);
                        $("#js-pay-why").text(function() {
                            switch (data.payWay) {
                                case 'coin':
                                    return '金币支付';
                                case 'wxpay':
                                    return '微信支付';
                                case 'alipay':
                                    return '支付宝支付';
                            }
                        });
                        $("#js-goods-name").text(data.good.name);
                        $("#js-buy-num").text(data.goodsNum);
                        $("#js-buy-time").text(data.addTime);
                        $("#js-action").html(function() {
                            return '<a type="button" id="js-consume" data-uid="' + data.uuid + '" class="btn btn-outline btn-info ">核销</a>';
                        });
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
                $.post("order/consume", {
                    "uuid": uuid
                }, function(data, textStatus, jqXHR) {
                    if (data === "success") {
                        $alert._alert("核销成功");
                        walletInfo();
                        csinfo();
                        $coin._refresh();
                    } else {
                        $alert._alert("核销失败");
                    }
                    layer.close(inx);
                });
            });
        });


    });
});