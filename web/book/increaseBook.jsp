<%@ page import="model.Admin" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body>
<form class="layui-form" action="" style="margin: 30px 60px 30px 5px;" lay-filter="from">
    <div class="layui-form-item">
        <label class="layui-form-label">馆藏数</label>
        <div class="layui-input-block">
            <input type="text" name="quantity" lay-verify="quantity" autocomplete="off" class="layui-input">
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


        // 提交 只有在验证全部通过后才会进入
        form.on('submit(submit)', function (data) {
            $.ajax({
                url: '<%=request.getContextPath()%>/BookServlet?action=increaseBook',
                type: 'POST',
                data: {
                    account:"<%=((Admin)session.getAttribute("Admin")).getNo()%>"
                    , password:"<%=((Admin)session.getAttribute("Admin")).getPassword()%>"
                    , isbn: parent_json.ISBN
                    , quantity: data.field.quantity
                },
                dataType: 'json',
                async: false,
                success: function (msg) {
                    if (msg.status === "ok") {
                        layer.msg(msg.content, {icon: 6});
                    } else {
                        layer.msg(msg.content, {icon: 5});
                    }
                }
            });
            return false;
        });
        var parent_json = eval('(' + parent.json + ')');
        // 填充数据
        form.val("from", {
            "quantity": parent_json.Amounts
        })
    });
</script>
</html>