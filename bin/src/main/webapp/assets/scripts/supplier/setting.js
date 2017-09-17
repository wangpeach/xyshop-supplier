define(function(require) {
    require.async(['jquery', 'contabs', 'ue'], function() {
    	var edit = UE.getEditor('editor');
        var common = require('common');
        var $common = new common();
        var alert = require('alertUtils')
        var $alert = new alert();
        require('jqueryUtils');
//        require('http://api.map.baidu.com/api?v=2.0&ak=470iOqChfp0CIm0xxroNFfQgT0e2jEem&s=1');
        require('http://api.map.baidu.com/getscript?v=2.0&ak=470iOqChfp0CIm0xxroNFfQgT0e2jEem');
        
        // 百度地图API功能
        var map = new BMap.Map("allmap");
        var geoc = new BMap.Geocoder();
        var top_left_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_TOP_LEFT }); // 左上角，添加比例尺
        var top_left_navigation = new BMap.NavigationControl(); //左上角，添加默认缩放平移控件
        var top_right_navigation = new BMap.NavigationControl({ anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL }); //右上角，仅包含平移和缩放按钮
        /*缩放控件type有四种类型:BMAP_NAVIGATION_CONTROL_SMALL：仅包含平移和缩放按钮；BMAP_NAVIGATION_CONTROL_PAN:仅包含平移按钮；BMAP_NAVIGATION_CONTROL_ZOOM：仅包含缩放按钮*/
        //添加控件和比例尺
        map.addControl(top_left_control);
        map.addControl(top_left_navigation);
        map.addControl(top_right_navigation);
        var insertInfoFuc = function(pt) {
            $('#js-store-lng').val(pt.lng);
            $('#js-store-lat').val(pt.lat);
            map.clearOverlays();
            map.addOverlay(new BMap.Marker(pt));
            map.centerAndZoom(pt, map.getZoom());

            geoc.getLocation(pt, function(rs) {
                var addComp = rs.addressComponents;
                $(".js-pro option:contains('" + addComp.province + "')").each(function() {
                    if ($(this).text() == addComp.province) $(this).attr('selected', true);
                });
                getCityFuc($(".js-pro").val());
                if (addComp.city == "北京市" || addComp.city == "天津市" || addComp.city == "重庆市" || addComp.city == '上海市') {
                    var cc = "市辖区";
                    if (addComp.district.indexOf("市") > 0) cc = "市";
                    if (addComp.district.indexOf("县") > 0) cc = "县";
                    $(".js-city option:contains('" + cc + "')").each(function() {
                        if ($(this).text() == cc) $(this).attr('selected', true);
                    });
                } else
                    $(".js-city option:contains('" + addComp.city + "')").each(function() {
                        if ($(this).text() == addComp.city) $(this).attr('selected', true);
                    });
                getAreaFuc($(".js-city").val())
                $(".js-area option:contains('" + addComp.district + "')").each(function() {
                    if ($(this).text() == addComp.district) $(this).attr('selected', true);
                });
                $('#js-store-address').val(addComp.street + addComp.streetNumber);
            });
        }
        map.addEventListener("click", function(e) {
            insertInfoFuc(e.point);
        });

        $('.js-pro').change(function(e) {
            geoc.getPoint($(".js-pro").find("option:selected").text().replace('-', ''), function(point) {
                if (point) {
                    insertInfoFuc(point);
                } else {
                    console.log("您选择地址没有解析到结果!");
                }
            }, $(".js-pro").find("option:selected").text().replace('-', ''));
            $('.js-city').val('-');
            $('.js-area').val('-');
        });
        $('.js-city').change(function(e) {
            geoc.getPoint($(".js-city").find("option:selected").text().replace('-', ''), function(point) {
                if (point) {
                    insertInfoFuc(point);
                } else {
                    console.log("您选择地址没有解析到结果!");
                }
            }, $(".js-city").find("option:selected").text().replace('-', ''));
            $('.js-area').val('-');
        });
        $('.js-area').change(function(e) {
            geoc.getPoint($(".js-area").find("option:selected").text().replace('-', ''), function(point) {
                if (point) {
                    insertInfoFuc(point);
                } else {
                    console.log("您选择地址没有解析到结果!");
                }
            }, $(".js-city").find("option:selected").text().replace('-', ''));
        });
        $('#js-store-address').change(function(e) {
            geoc.getPoint($(this).val(), function(point) {
                if (point) {
                    insertInfoFuc(point);
                } else {
                    console.console.log("您选择地址没有解析到结果!");
                }
            }, $(".js-pro").find("option:selected").text().replace('-', ''));
        });
        $(".js-jingweidu").change(function(e) {
            if ($('#js-store-lng').val() != '' && $('#js-store-lat').val() != '')
                insertInfoFuc(new BMap.Point($('#js-store-lng').val(), $('#js-store-lat').val()));
        });
        /*获取省市县*/
        $(document).ready(function(e) {
            $.ajax({
                url: "/shop-supplier/area/ajax/provincelist",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                async: false,
                success: function(data) {
                    for (var key in data) {
                        $(".js-pro").append('<option value="' + key + '">' + data[key] + '</option>');
                    }
                },
                error: function() {
                    $alert._alert("城市列表加载失败");
                }
            });
        });
        var getCityFuc = function(pro) {
            $.ajax({
                url: "/shop-supplier/area/ajax/citylist",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    provinceid: pro
                },
                async: false,
                success: function(data) {
                    $(".js-city").html('');
                    $(".js-city").append('<option value="-">-</option>');
                    for (var key in data) {
                        $(".js-city").append('<option value="' + key + '">' + data[key] + '</option>');
                    }
                },
                error: function() {
                    $alert._alert("城市列表加载失败");
                }
            });
        }
        $(document).on("change", ".js-pro", function(e) {
            getCityFuc($(this).val());
        });
        var getAreaFuc = function(city) {
            $.ajax({
                url: "/shop-supplier/area/ajax/areaslist",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    cityid: city
                },
                async: false,
                success: function(data) {
                    $(".js-area").html('');
                    $(".js-area").append('<option value="-">-</option>');
                    for (var key in data) {
                        $(".js-area").append('<option value="' + key + '">' + data[key] + '</option>');
                    }
                },
                error: function() {
                    $alert._alert("县列表加载失败");
                }
            });
        }
        $(document).on("change", ".js-city", function(e) {
            getAreaFuc($(this).val());
        });
        /*读取店铺基础信息*/
        $(document).ready(function(e) {
            $.ajax({
                url: "/shop-supplier/shop/admininfo",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                async: false,
                success: function(shop) {
                    $('#js-store-name').val(shop.name);
                    $('#js-store-phone').val(shop.shopPhone);
                    var areasArray = shop.areaid.split(',');
                    $("#js-add-pro").val(areasArray[0]);
                    $.ajax({
                        url: "/shop-supplier/area/ajax/citylist",
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        type: "post",
                        dataType: "json",
                        data: {
                            provinceid: areasArray[0]
                        },
                        async: true,
                        success: function(data) {
                            $(".js-city").html('');
                            $(".js-city").append('<option value="-">-</option>');
                            for (var key in data) {
                                $(".js-city").append('<option value="' + key + '">' + data[key] + '</option>');
                            }
                            $("#js-add-city").val(areasArray[1]);
                            $.ajax({
                                url: "/shop-supplier/area/ajax/areaslist",
                                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                                type: "post",
                                dataType: "json",
                                data: {
                                    cityid: areasArray[1]
                                },
                                async: true,
                                success: function(data) {
                                    $(".js-area").html('');
                                    $(".js-area").append('<option value="-">-</option>');
                                    for (var key in data) {
                                        $(".js-area").append('<option value="' + key + '">' + data[key] + '</option>');
                                    }
                                    $("#js-add-area").val(areasArray[2]);
                                },
                                error: function() {
                                    $alert._alert("县列表加载失败");
                                }
                            });
                        },
                        error: function() {
                            $alert._alert("城市列表加载失败");
                        }
                    });
                    $('#js-store-address').val(shop.address);
                    $('#js-store-lng').val(shop.longitude);
                    $('#js-store-lat').val(shop.latitude);
                    
                    var point = new BMap.Point(shop.longitude, shop.latitude);
                    map.centerAndZoom(point, 12);
                    map.addOverlay(new BMap.Marker(point));

                    edit.addListener("ready", function () {
                    	$("#edui1").css("z-index", 9);
						edit.setContent(shop.shopDetail);
					});
                },
                error: function() {
                    $alert._alert("店铺数据加载失败");
                }
            });
        });

        $("#js-save-btn").click(function(e) {
            var name = $('#js-store-name').val(),
                pro = $('#js-add-pro').val(),
                city = $('#js-add-city').val(),
                area = $('#js-add-area').val(),
                _detail = UE.getEditor('editor').getContent();
            var phone = $('#js-store-phone').val();
            if (!$common._noEmpty(name)) {
                $alert._alert("店铺名字不能为空");
                return;
            }
            if (!$common._noEmpty(phone)) {
                $alert._alert("手机号不能为空");
                return;
            }
            if (phone.length != 11) {
                $alert._alert("手机号格式错误");
                return;
            }
            if (!$common._noEmpty(pro) || pro == '-') {
                $alert._alert("省份不能为空");
                return;
            }
            if (!$common._noEmpty(city) || city == '-') {
                $alert._alert("城市不能为空");
                return;
            }
            if (!$common._noEmpty(area) || area == '-') {
                $alert._alert("区县不能为空");
                return;
            }
            var areasid = pro + "," + city + "," + area;
            $.ajax({
                url: "/shop-supplier/shop/adminupdate",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    name: $('#js-store-name').val(),
                    shopPhone: $('#js-store-phone').val(),
                    address: $('#js-store-address').val(),
                    lng: $('#js-store-lng').val(),
                    lat: $('#js-store-lat').val(),
                    areaId: areasid,
                    areaName: $("#js-add-pro").find("option:selected").text() + "," + $("#js-add-city").find("option:selected").text() + "," + $("#js-add-area").find("option:selected").text(),
                    detail: _detail,
                },
                async: false,
                success: function(data) {
                    if (data == 1) {
                        $alert._alert("店铺数据保存成功!");
                    } else {
                        $alert._alert("店铺数据保存失败");
                    }
                },
                error: function() {
                    $alert._alert("店铺数据保存失败");
                }
            });
        });



    });
});
