define(function(require) {

    if (!String.prototype.includes) {
        String.prototype.includes = function(search, start) {
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

    require.async(['jquery', 'contabs', 'icheck', 'ue'], function() {
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
            field: 'name',
            title: '商品名',
            align: 'center',
        }, {
            field: 'thumbImg',
            title: '商品图片',
            align: 'center',
            formatter: function(value, row, index) {
                return imgUpStr(value);
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
            formatter: function(value, row, index) {
                $imgs.lunbo("lunbo-" + index);
                return imgUpStr(value, index);
            }
        }, {
            field: 'ifCoin',
            title: '能否金币兑换',
            align: 'center',
            sortable: true,
            formatter: function(value, row, index) {
                if (value == 0) {
                    return "不能";
                } else if (value == 1) {
                    return "能";
                }
            }
        }, {
            field: 'addTime',
            title: '添加时间',
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
            field: 'ifOnline',
            title: '状态',
            align: 'center',
            formatter: function(value, row, index) {
                switch (value) {
                    case "0":
                        return "<span class='badge badge-info'>待上架</span>";
                    case "1":
                        return "<span class='badge badge-success'>已上架</span>";
                    case "2":
                        return "<span class='badge badge-danger'>已下架</span>";
                    case "4":
                    	return "<span class='badge badge-danger'>已冻结</span>";
                    case "5":
                        return "<span class='badge badge-danger'>已驳回</span>";
                }
                return "-";
            }
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            formatter: function(value, row, index) {
                var opt = [];
                if(row.ifOnline != "4") {
                	opt.push('<a class="btn btn-outline btn-success js-update">修改信息</a>');
                }
                switch(row.ifOnline) {
                	case "1":
                		opt.push('<a class="btn btn-outline btn-danger js-goodown">下架</a>');
                		break;
                	case "2":
                		opt.push('<a class="btn btn-outline btn-warning js-goodup">上架</a>');
                		break;
                	case "5":
                		opt.push('<a class="btn btn-outline btn-danger js-rebut-reason">查看驳回理由</a>')
                }
                opt.push('<a href="goodsinfo.html?u=' + row.uuid + '" class="btn btn-outline btn-info J_menuItem">详情</a>');
                return opt.join(" ");
            },
            events: {
                'click .js-update': function(e, value, row, index) {
                    $('#updateuuid').val(row.uuid);
                    $("#js-add-goods-btn").addClass('hide');
                    $("#js-upate-goods-btn").removeClass('hide');
                    $.get("http://huifenshophtml-1253471825.file.myqcloud.com/" + row.desFile, function(data) {
                        var start = data.indexOf("<body>"),
                            end = data.indexOf('</body>');
                        edit.setContent(data.substring(start, end));
                    });
                    $("#name").val(row.name);
                    $("#shop-uuid").val(row.shopUuid);
                    $("#shop-name").val(row.shopName);
                    $("#price").val(row.price);
                    $("#balance").val(row.balance);
                    $("#stock").val(row.stock);
                    $("#cat-uuid").val(row.catId);
					$("#cat-name").val(row.catName);

                    $("#headImgShow").html("<img id='headImg'  data-value='" + row.thumbImg + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + row.thumbImg + "?x-oss-process=image/resize,w_200'/></div>");
                    if ($common._noEmpty(row.moreImg)) {
                        if (!row.moreImg.includes('#')) {
                            $("#otherheadImgShow").append("<img  class='js-img-open otherImg' data-value='" + row.moreImg + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + row.moreImg + "?x-oss-process=image/resize,w_200'/></div>");
                        } else {
                            var imagelist = row.moreImg.split('#');
                            for (var int = 0; int < imagelist.length; int++) {
                                if (imagelist[int]) {
                                    $("#otherheadImgShow").append("<img class='js-img-open otherImg' data-value='" + imagelist[int] + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + imagelist[int] + "?x-oss-process=image/resize,w_200'/>");
                                }
                            }
                        }
                    }
                    $("#myModal").modal({
                        backdrop: 'static'
                    });
                },
                'click .js-goodown': function(e, value, row, index) {
            		$.post('/shop-goods/uniongoods/update-unionGoods', {'good_uuid': row.uuid, 'ifOnline': "2", info: ""}, function(data, textStatus, xhr) {
            			/*optional stuff to do after success */
            			$alert._alert('下架成功');
            			$t._refresh();
            		});
                },
                'click .js-goodup': function(e, value, row, index) {
                	$.post('/shop-goods/uniongoods/update-unionGoods', {'good_uuid': row.uuid, 'ifOnline': "1", info: ""}, function(data, textStatus, xhr) {
            			/*optional stuff to do after success */
            			$alert._alert('上架成功');
            			$t._refresh();
            		});
                },
                'click .js-rebut-reason': function(e, value, row, index) {
                	if(row.rebut) {
                		$alert._alert(row.rebut);
                	} else {
                		$alert._alert("审核员未填写驳回理由!");
                	}
                }
            },
        }];
        t._setSort('updateTime', 'desc');
        var $t = t._init("table", "/shop-goods/uniongoods/list", columns, function(d) {
            d.shopUuid = $('#js-shop-uuid').val();
            d.online = $("#js-choise-online").val();
        });

        /**
         * [商品状态]
         */
        $(".js-change-table-params").change(function(e) {
            $t._refresh();
        });

        $('input.radio').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
            increaseArea: '20%' // optional
        });
        $(document).on('ifChecked', 'input.radio', function(event) {
            $("input[name='" + $(this).attr('name') + "']").removeAttr("checked");
            $(this).attr("checked", "checked");
        });

        var imgUpStr = function(src, index) {
            if ($common._noEmpty(src)) {
                if (src.indexOf('#') > 0) {
                    var imglist = src.split("#"),
                        temp = [];
                    for (var int = 0; int < imglist.length; int++) {
                        temp.push("<img class='js-img-open' data-value='" + imglist[int] + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + imglist[int] + "'/>");
                    }
                    return temp.join(' ');
                } else {
                    return "<img class='js-img-open' data-value='" + src + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + src + "'/>";
                }
            } else
                return "-";
        };

        var imgUP = null,
            imgOtherUP = null;
        $('#myModal').on('shown.bs.modal', function(e) {
            UE.getEditor('editor');
            imgUP = $upload._init(imgUP, '#imgDiv', function(file, response) {
                $("#headImgShow").html("<img  id='headImg'  data-value='" + response.url + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + response.url + "?x-oss-process=image/resize,w_200'/>");
            });
            imgOtherUP = $upload2._init(imgOtherUP, '#imgOtherDiv', function(file, response) {
                var appendOImagesFlag = $common._imgUP("otherImg", "" + response.url);
                if (appendOImagesFlag) {
                    $("#otherheadImgShow").append("<img class='otherImg' data-value='" + response.url + "' src='http://huifenshopimg-1253471825.file.myqcloud.com/" + response.url + "?x-oss-process=image/resize,w_200'/></div>");
                }
            });
        });
        $('#myModal').on('hidden.bs.modal', function(e) {
            $(".form-control").val('');
            $("#headImgShow").html('');
            $("#otherheadImgShow").html('');
        });
        /*添加商品*/
        $(document).on("click", "#js-add", function(e) {
            $("#js-add-goods-btn").removeClass('hide');
            $("#js-upate-goods-btn").addClass('hide');
            $("#otherheadImgShow").val('');
            $(".form-control").val('');
            edit.setContent('');
            $("#myModal").modal({
                backdrop: 'static'
            });
        });
        $(document).on("click", "#js-add-goods-btn", function(e) {
            var otherImages = '';

            var _price = $("#price").val();
            var _balance = $("#balance").val();
            if(parseFloat(_balance) > parseFloat(_price)) {
                $alert._alert('结算价输入错误');
                return false;
            }

            $('.otherImg').each(function() {
                otherImages = otherImages + "#" + $(this).data('value');
            });
            if (otherImages == '') {
                otherImages = "#";
            }
            $.ajax({
                url: "/shop-goods/uniongoods/addUnionGoods",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                data: {
                    name: $("#name").val(),
                    info: UE.getEditor('editor').getContent(),
                    thumbImg: $("#headImg").data('value'),
                    moreImg: otherImages,
                    price: _price,
                    balance: _balance,
                    shopUuid: $("#js-shop-uuid").val(),
                    shopName: $("#js-shop-name").val(),
                    stock: $("#stock").val(),
                    catId: $("#cat-uuid").val(),
                    catName: $("#cat-name").val(),
                    ifOnline: "0"
                },
                dataType: "json",
                async: true,
                success: function(data) {
                    $alert._strSuc('商品添加成功,请点击销量单位管理添加商品的售价等信息,以确保商品能正常销售');
                    $t._refresh();
                    $("#myModal").modal('hide');
                },
                error: function() {
                    $alert._alert('商品添加失败');
                },
                beforeSend: function(XMLHttpRequest, self) {
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
        /*修改商品基础信息*/
        $(document).on("click", "#js-upate-goods-btn", function(e) {
            var otherImages = '';
            $('.otherImg').each(function() {
                otherImages = otherImages + "#" + $(this).data('value');
            });
            if (otherImages == '') {
                otherImages = "#";
            }
            $.ajax({
                url: "/shop-goods/uniongoods/update-unionGoods",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                type: "post",
                data: {
                    good_uuid: $('#updateuuid').val(),
                    name: $("#name").val(),
                    info: UE.getEditor('editor').getContent(),
                    thumbImg: $("#headImg").data('value'),
                    moreImg: otherImages,
                    price: $("#price").val(),
                    balance: $("#balance").val(),
                    shopUuid: $("#js-shop-uuid").val(),
                    shopName: $("#js-shop-name").val(),
                    stock: $("#stock").val(),
                    ifOnline: "0",
                    catId: $("#cat-uuid").val(),
                    catName: $("#cat-name").val(),
                    // ifOnline: $("input[name='online'][checked]").val(),
                    // ifScore: $("input[name='ifscore'][checked]").val(),
                    // brandId: $("#brand-uuid").val(),
                    // brandName: $("#brand-name").val(),
                    
                },
                dataType: "json",
                async: true,
                success: function(data) {
                    $alert._alert('商品修改成功');
                    $t._refresh();
                    $("#myModal").modal('hide');
                },
                error: function() {
                    $alert._alert('商品修改失败');
                    $("#myModal").modal('hide');
                },
                beforeSend: function(XMLHttpRequest, self) {
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
        $(document).on("click", "#js-clean-images", function(e) {
            $("#otherheadImgShow").html('');
            imgOtherUP.reset();
        });
        $(document).on("click", ".otherImg", function(e) {
            var that = this;
            $alert._warning("确定删除该图片？", "该操作将删除所选图片", function() {
                $(that).remove();
                imgOtherUP.reset();
                $alert._alert('操作成功');
            });
        });


        /*选择商品分类*/
        $(document).on("click","#js-cat-select-btn",function(e){
         $("#js-cat-select").empty();
         $.ajax({
        	url:"/shop-goods/categroy/ajax/idlist",
        	contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        	type: "post",
        	dataType:"json",
        	data:{
        		name:$("#cat-name").val()
        	},
        	async:true,success:function(data){
        		for (var key in data) { 
        			$("#js-cat-select").append('<option value="'+key+'">'+data[key]+'</option>');
        		} 
        		$("#cat-selectBox").removeClass('hide');
        	}
          });
        }); 
        $(document).on("click","#js-cat-sure",function(e){
         $("#cat-selectBox").addClass('hide');
         $("#cat-uuid").val($("#js-cat-select").val().split('-')[0]);
         $("#cat-name").val($("#js-cat-select").find("option:selected").text());
        });

        /*选择品牌*/
        // $(document).on("click","#js-brand-select-btn",function(e){
        //  $("#js-brand-select").empty();
        //  $.ajax({
        // 	url:"/shop-goods/brand/ajax/idlist",
        // 	contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        // 	type: "post",
        // 	dataType:"json",
        // 	data:{
        // 		name:$("#brand-name").val()
        // 	},
        // 	async:true,success:function(data){
        // 		for (var key in data) { 
        // 			$("#js-brand-select").append('<option value="'+key+'">'+data[key]+'</option>');
        // 		} 
        // 		$("#brand-selectBox").removeClass('hide');
        // 	}
        //   });
        // }); 
        // $(document).on("click","#js-brand-sure",function(e){
        //  $("#brand-selectBox").addClass('hide');
        //  $("#brand-uuid").val($("#js-brand-select").val().split('-')[0]);
        //  $("#brand-name").val($("#js-brand-select").find("option:selected").text());
        // });

        /*选择商家*/
        // $(document).on("click","#js-shop-select-btn",function(e){
        //  $("#js-shop-select").empty();
        //  $.ajax({
        // 	url:"shop/ajax/idlist",
        // 	contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        // 	type: "post",
        // 	dataType:"json",
        // 	data:{
        // 		name:$("#shop-name").val()
        // 	},
        // 	async:true,success:function(data){
        // 		for (var key in data) { 
        // 			$("#js-shop-select").append('<option value="'+key+'">'+data[key]+'</option>');
        // 		} 
        // 		$("#shop-selectBox").removeClass('hide');
        // 	}
        //   });
        // }); 
        // $(document).on("click","#js-shop-sure",function(e){
        //  $("#shop-selectBox").addClass('hide');
        //  $("#shop-uuid").val($("#js-shop-select").val().split('-')[0]);
        //  $("#shop-name").val($("#js-shop-select").find("option:selected").text());
        // });

        /*所有数字类型input的数必须大于0*/
        $(document).on("change", "input[type='number']", function(e) {
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
