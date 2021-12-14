layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(saveBtn)", function(data) {

        // 获取表单元素的内容
        var fieldData = data.field;
        console.log(fieldData)
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                oldPwd:fieldData.oldPwd,
                newPwd:fieldData.newPwd,
                confirmPwd:fieldData.confirmPwd
            },
            dataType:"json",
            success:function (data) {
                if (data.code==200){
                    layer.msg("用户密码修改成功，系统将在3秒种后退出",function (){
                        $.removeCookie("userIdStr", {domain:"localhost",path:"/crm"});
                        $.removeCookie("userName", {domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName", {domain:"localhost",path:"/crm"});
                        window.parent.location.href = ctx+"/index";
                    });
                }else {
                    layer.msg(data.msg);
                }
            }
        })


    });

});