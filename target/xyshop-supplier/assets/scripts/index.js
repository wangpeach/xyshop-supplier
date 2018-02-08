define(function(require) {
    require('jquery');
    require('bootstrap');
    require('metisMenu');
    require('slimscroll');

    require('hplus');
    require('contabs_index');
    require('pace');
    var alert = require('alertUtils')
    var $alert = new alert();
    $(document).on("click", ".js-index-update-pwd", function(e) {
        $("#pwdIndexModal").modal();
    });
    /*修改登录密码*/
    $(document).on("click", "#js-index-pwd-btn", function(e) {
        var o = $('#js-index-old-pwd').val(),
            n = $("#js-index-new-pwd").val(),
            r = $("#js-index-replace-pwd").val();
        if (n != r) {
            $alert._alert("两次输入的密码不一致！");
            return;
        }
        if (n.length < 6) {
            $alert._alert("密码的长度必须大于6位！");
            return;
        }
        $.ajax({
            url: "shop/ajax/updatepwd",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: "post",
            dataType: "json",
            data: {
                oldpwd: $('#js-index-old-pwd').val(),
                newpwd: $("#js-index-new-pwd").val()
            },
            async: true,
            success: function(data) {
                if (data[0] == "pwderror") {
                    $alert._alert('原始密码不对！');
                } else if (data[0] == 'success') {
                    $alert._alert("修改成功！");
                    $("#pwdIndexModal").modal('hide');
                } else if (data[0] == 'noagent') {
                    $alert._alert("账户不存在！");
                }
            },
            error: function() {
                $alert._alert("修改失败！");
            }
        });
    });

    jQuery(document).ready(function($) {
        $(".shopMenus").show('400');
        /**
         * 官方联系方式
         */
        $.post('/xyshop/params/search', {
            'type': 'contact_us'
        }, function(data, textStatus, xhr) {
            /*optional stuff to do after success */
            if (data && data.length > 0) {
                var eles = new Array();
                eles.push('<span class="block m-t-xs" style="margin-bottom: 15px;"><strong class="font-bold">官方联系方式</strong></span>');
                for (var i = 0; i < data.length; i++) {
                    eles.push('<p>' + data[i].name + '：<span id="lx_phone">' + data[i].paramValue + '</span></p>');
                }
                $(".contactUs").append(eles.join(' '));
                $(".contactUsLi").show();
            }
        }, 'json');
    });
});