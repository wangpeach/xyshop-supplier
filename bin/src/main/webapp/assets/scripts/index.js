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


    $.ajax({
        url: "/shop-agent/agent/ajax/agent-area",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        type: "post",
        dataType: "json",
        data: {
            area: $("#area").val()
        },
        async: true,
        success: function(data) {
        	console.log(data);
            $("#agent-name").text(data.name);
            $("#agent-tel").text(data.phoneNum);
            $("#agent-hottel").text(data.telPhoneNum);
        },
        error: function() {
            $alert._alert("获取代理商联系方式失败..");
        }
    });

    $.post('params/sysparam-type', {'_type': 'other'}, function(data, textStatus, xhr) {
    	/*optional stuff to do after success */
    	for (var i = 0; i < data.length; i++) {
    		$("#" + data[i].key).text(data[i].value);
    	}
    }, 'json');
});
