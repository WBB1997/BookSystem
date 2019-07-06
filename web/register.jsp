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
            <input type="text" name="no" lay-verify="no" placeholder="请输入账号" autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block">
            <input type="password" id="password" name="password" lay-verify="password" placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">确认密码</label>
        <div class="layui-input-block">
            <input type="password" id="re_password" name="re_password" lay-verify="re_password" placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">姓名</label>
        <div class="layui-input-block">
            <input type="text" name="name" lay-verify="name" placeholder="请输入姓名" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">性别</label>
        <div class="layui-input-block" lay-verify="sex">
            <input type="radio" checked name="sex" value="男" title="男">
            <input type="radio" name="sex" value="女" title="女">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">院系</label>
        <div class="layui-input-block">
            <select name="college" id="college">
                <option value=""></option>
            </select>
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
            password: [/(.+){6,18}$/, '密码必须6到18位'],
            re_password: function (value) {
                var repassvalue = $('#password').val();
                if (value.length < 6 || value.length > 18)
                    return '密码必须6到18位';
                if (value !== repassvalue) {
                    return '两次输入的密码不一致!';
                }
            },
            name: function(value){
                if(value.length < 1){
                    return '姓名不能为空';
                }
            }
        });

        <%--// 获取select 值--%>
        <%--$(function () {--%>
        <%--    $.ajax({--%>
        <%--        url: "<%=request.getContextPath()%>/ReaderServlet?action=getCollegeTypes",//请求地址--%>
        <%--        type: "POST",//请求方式--%>
        <%--        dataType: "json",//返回数据类型--%>
        <%--        contentType: "application/json",--%>
        <%--        async: false,//同步--%>
        <%--        success: function (result) {--%>
        <%--            var data = result;--%>
        <%--            var proHtml = '';--%>
        <%--            for (var o in data) {--%>
        <%--                proHtml += '<option value="' + data[o] + '">' + data[o] + '</option>';--%>
        <%--            }--%>
        <%--            console.log(result);--%>
        <%--            $("#college").html(proHtml);--%>
        <%--        },--%>
        <%--        error: function () {--%>
        <%--            alert("fail");--%>
        <%--        }--%>
        <%--    });--%>
        <%--    form.render();--%>
        <%--});--%>


        // 提交 只有在验证全部通过后才会进入
        form.on('submit(submit)', function (data) {
            $.ajax({
                url: '<%=request.getContextPath()%>/ReaderServlet?action=registerAccount',
                type: 'POST',
                data: {
                    no: data.field.no
                    , password: data.field.password
                    , name: data.field.name
                    , gender: data.field.sex
                    , college: data.field.college
                },
                dataType: 'json',
                async: false,
                success: function (msg) {
                    console.log(msg.valueOf());
                    if (msg.status === "ok") {
                        layer.msg(msg.content, {icon: 6});
                    } else {
                        layer.msg(msg.content, {icon: 5});
                    }
                }
            });
            return false;
        });
    });
</script>
</html>