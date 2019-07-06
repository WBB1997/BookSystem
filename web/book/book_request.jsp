<%@ page import="model.Admin" %>
<%@ page import="model.Staff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body>
<form class="layui-form" action="" style="margin: 30px 30px 30px 5px;" lay-filter="from">

    <div class="layui-form-item">
        <label class="layui-form-label">书名</label>
        <div class="layui-input-block">
            <input type="text" name="name" lay-verify="required|name" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">ISBN</label>
        <div class="layui-input-block">
            <input type="text" name="isbn" lay-verify="required|isbn" layui-disabledautocomplete="off"
                   class="layui-input ">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">作者</label>
        <div class="layui-input-block">
            <input type="text" id="author" name="author" lay-verify="required|author" placeholder="请输入作者"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">出版社</label>
        <div class="layui-input-block">
            <input type="text" name="publisher" lay-verify="required|publisher" placeholder="请输入出版社" autocomplete="off"
                   class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">出版日期</label>
        <div class="layui-input-block">
            <input type="text" id="publishdate" name="publishdate" lay-verify="required|publishdate" placeholder="请输入出版时间"
                   autocomplete="off"
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
            , element = layui.element;

        form.render();

        $(this).removeAttr("lay-key");

        laydate.render({
            elem: '#publishdate'
            ,trigger : 'click'
        });

        // 提交 只有在验证全部通过后才会进入
        form.on('submit(submit)', function (data) {
            $.ajax({
                url: '<%=request.getContextPath()%>/ReaderServlet?action=readerBookRequest',
                type: 'POST',
                data: {
                    name: data.field.name
                    , isbn: data.field.isbn
                    , author: data.field.author
                    , publisher: data.field.publisher
                    , publishdate: data.field.publishdate
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
    });
</script>
</html>
