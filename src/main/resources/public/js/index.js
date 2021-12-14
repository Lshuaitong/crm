layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(login)", function(data){
        // 获取表单元素的值 （用户名 + 密码）
        var fieldData = data.field;

        //判断参数是否为空
        if (fieldData.username == "undefined" || fieldData.username == ""){
                layer.msg("用户名不能为空");
                return false;
        }
        if (fieldData.password ==  "undefined" || fieldData.password== ""){
                layer.msg("密码不能为空");
                return false;
        }
        // 发送 ajax 请求，请求用户登录
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:fieldData.username,
                userPwd:fieldData.password
            },
           dataType:"json",
            async:true,
           success:function(data){
                // 判断是否登录成功
                if (data.code == 200) {
                    layer.msg("登录成功！", function () {
                        // 将用户信息存到cookie中
                        var result = data.result;
                        $.cookie("userIdStr", result.userIdStr);
                        $.cookie("userName", result.userName);
                        $.cookie("trueName", result.trueName);
                        // 登录成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    });
                } else {
                    // 提示信息
                    layer.msg(data.msg);
                }
           },
           error:function (data){
                console.log(data);
           }
        });


        return false;
    });
    
    
});