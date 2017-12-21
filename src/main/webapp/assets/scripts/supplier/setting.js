define(function(require) {
    require.async(['jquery', 'contabs', 'ueditor', 'ueconfig'], function() {
        var edit = UE.getEditor('editor');
        var common = require('common');
        var $common = new common();
        var alert = require('alertUtils')
        var $alert = new alert();
        require('jqueryUtils');

        require('AMap');


        var map = new AMap.Map('allmap', {
            resizeEnable: true,
            zoom: 13,
            center: [116.39, 39.9],
            keyboardEnable: true
        });

        AMap.plugin(['AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Geocoder'], function() {
            var autoOptions = {
                city: "",
                input: "keyword" //使用联想输入的input的id
            };
            autocomplete = new AMap.Autocomplete(autoOptions);
            var placeSearch = new AMap.PlaceSearch({
                city: "",
                map: map
            })
            AMap.event.addListener(autocomplete, "select", function(e) {
                //TODO 针对选中的poi实现自己的功能
                placeSearch.search(e.poi.name)
            });


            var geocoder = new AMap.Geocoder({
                city: "010" //城市，默认：“全国”
            });

            map.on('click', function(e) {
                map.clearMap();
                document.getElementById("js-add-jingweidu").value = (e.lnglat.getLng() + ',' + e.lnglat.getLat());
                var marker = new AMap.Marker({
                    map: map,
                    bubble: true
                })
                marker.setPosition(e.lnglat);
                geocoder.getAddress(e.lnglat, function(status, result) {
                    if (status == 'complete') {
                        console.log(result);
                        document.getElementById('keyword').value = result.regeocode.formattedAddress
                    }
                })
            })
        });
        /**
         * end
         */


        /*读取店铺基础信息*/
        /* $(document).ready(function(e) {
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

                    $('#js-store-address').val(shop.address);
                    $('#js-store-lng').val(shop.longitude);
                    $('#js-store-lat').val(shop.latitude);

                    map.clearMap();
                    var lnglat = new AMap.LngLat(shop.longitude, shop.latitude);
                    var mark = new AMap.Marker({
                        position: lnglat
                    });
                    map.setZoom(13);
                    map.setCenter(lnglat);
                    map.panTo(lnglat);
                    map.add(mark);

                    edit.addListener("ready", function () {
                    	$("#edui1").css("z-index", 9);
						edit.setContent(shop.shopDetail);
					});
                },
                error: function() {
                    $alert._alert("店铺数据加载失败");
                }
            });
        });*/


        /**
         * 加载城市列表
         *
         * @param {any} seletive 填充目标元素
         * @param {any} area 选择的城市id
         */
        var getAreaFuc = function(seletive, area) {
            var def = $.Deferred();
            $.ajax({
                url: "/xyshop/district/list",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                dataType: "json",
                data: {
                    areaid: area
                },
                async: false,
                success: function(data) {
                    $(seletive).html('');
                    $(seletive).append('<option value="-">-</option>');
                    for (var key in data) {
                        $(seletive).append('<option value="' + data[key].id + '">' + data[key].name + '</option>');
                    }
                    def.resolve(data);
                },
                error: function(xhr) {
                    def.reject(xhr);
                    $alert._alert("城市列表加载失败");
                }
            });
            return def.promise();
        }



        jQuery(document).ready(function($) {

            var areaIds = $("#areaIds").val().split(",");

            getAreaFuc("#js-add-pro", 0).then(function() {
                $("#js-add-pro").val(areaIds[0]);

                getAreaFuc("#js-add-city", areaIds[0]).then(function() {
                    $("#js-add-city").val(areaIds[1]);
                    getAreaFuc("#js-add-area", areaIds[1]).then(function() {
                        $("#js-add-area").val(areaIds[2]);
                    });
                });
            });

            $(document).on("change", ".js-pro", function(e) {
                getAreaFuc("#js-add-city", $(this).val());
            });
            $(document).on("change", ".js-city", function(e) {
                getAreaFuc("#js-add-area", $(this).val());
            });



            map.clearMap();
            var lng = $("#js-store-lng").val(),
                lat = $("#js-store-lat").val();
            var lnglat = new AMap.LngLat(lng, lat);
            var mark = new AMap.Marker({
                position: lnglat
            });
            map.setZoom(13);
            map.setCenter(lnglat);
            map.panTo(lnglat);
            map.add(mark);


            edit.addListener("ready", function() {
                $("#edui1").css("z-index", 9);
                $.get($("#shopDetail").val(), {}, function(data) {
                    /*optional stuff to do after success */
                    edit.setContent(data);
                });

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