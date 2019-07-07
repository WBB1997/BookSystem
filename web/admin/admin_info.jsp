<%@ page import="model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body>
<form class="layui-form" action="" style="margin: 30px 30px 30px 5px" lay-filter="from">
    <div class="layui-form-item">
        <label class="layui-form-label">账号</label>
        <div class="layui-input-block">
            <input type="text" name="no" lay-verify="no" autocomplete="off" class="layui-input"
                   disabled>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">旧密码</label>
        <div class="layui-input-block">
            <input type="password" id="old_password" name="old_password" lay-verify="old_password" placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">新密码</label>
        <div class="layui-input-block">
            <input type="password" id="new_password" name="new_password" lay-verify="new_password" placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">重复新密码</label>
        <div class="layui-input-block">
            <input type="password" id="re_new_password" name="re_new_password" lay-verify="re_new_password"
                   placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">姓名</label>
        <div class="layui-input-inline">
            <input type="text" name="name" lay-verify="name" placeholder="请输入姓名" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">性别</label>
        <div class="layui-input-inline" lay-verify="sex">
            <input type="radio" name="sex" value="男" title="男">
            <input type="radio" name="sex" value="女" title="女">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">手机号</label>
        <div class="layui-input-block">
            <input type="text" name="phone" lay-verify="phone|number" placeholder="请输入手机号" autocomplete="off"
                   class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="submit">提交</button>
        </div>
    </div>

</form>
</body>
<script src="/layui-v2.5.4/layui/layui.js"></script>
<script>
    layui.use(['form', 'laydate', 'jquery', 'element'], function () {
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit
            , laydate = layui.laydate
            , $ = layui.jquery
            , element = layui.element

        // 验证密码
        form.verify({
            old_password: [/(.+){6,18}$/, '密码必须6到18位'],
            re_new_password: function (value) {
                var repassvalue = $('#new_password').val();
                console.log(repassvalue);
                if (value.length < 6 || value.length > 18)
                    return '密码必须6到18位';
                if (value !== repassvalue) {
                    return '两次输入的密码不一致!';
                }
            },
            name: [/\S/, '姓名不能为空'],
            phone:[/\S/, '手机号不能为空'],
        });

        // 提交 只有在验证全部通过后才会进入
        form.on('submit(submit)', function (data) {
            $.ajax({
                url: '<%=request.getContextPath()%>/AdminServlet?action=updateAdmin',
                type: 'POST',
                data: {
                    no: data.field.no
                    , old_password: data.field.old_password
                    , new_password: data.field.new_password
                    , name: data.field.name
                    , gender: data.field.sex
                    , phone: data.field.phone
                },
                dataType: 'json',
                async: false,
                success: function (msg) {
                    if (msg.status === "ok")
                        layer.msg(msg.content, {icon: 6});
                    else{
                        layer.msg(msg.content, {icon: 5});
                    }
                }
            });
            return false;

        });


        form.val("from", {
            "no": "<%=((Admin)session.getAttribute("Admin")).getNo()%>"
            , "name": "<%=((Admin)session.getAttribute("Admin")).getName()%>"
            , "sex": "<%=((Admin)session.getAttribute("Admin")).getGender()%>"
            , "phone": "<%=((Admin)session.getAttribute("Admin")).getPhone()%>"
        })

    });
</script>
</html>
