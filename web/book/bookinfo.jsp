<%@ page import="model.Admin" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
            <input type="text" name="name" lay-verify="name" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">ISBN</label>
        <div class="layui-input-block">
            <input type="text" name="isbn" lay-verify="isbn" layui-disabledautocomplete="off"
                   class="layui-input layui-disabled"
                   readonly unselectable="on">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">作者</label>
        <div class="layui-input-block">
            <input type="text" id="author" name="author" lay-verify="author" placeholder="请输入作者"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">书籍类型</label>
        <div class="layui-input-inline">
            <select name="type" lay-filter="type" id="type">
                <option value=""></option>
            </select>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">出版社</label>
        <div class="layui-input-block">
            <input type="text" name="publisher" lay-verify="publisher" placeholder="请输入出版社" autocomplete="off"
                   class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">出版日期</label>
        <div class="layui-input-block">
            <input type="text" id="publishdate" name="publishdate" lay-verify="publishdate" placeholder="请输入出版时间"
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


        laydate.render({
            elem: '#publishdate'
        });


        // 提交 只有在验证全部通过后才会进入
        form.on('submit(submit)', function (data) {
            $.ajax({
                url: '<%=request.getContextPath()%>/BookServlet?action=updateBook',
                type: 'POST',
                data: {
                    account:"<%=((Admin)session.getAttribute("Admin")).getNo()%>"
                    , password:"<%=((Admin)session.getAttribute("Admin")).getPassword()%>"
                    , name: data.field.name
                    , isbn: data.field.isbn
                    , author: data.field.author
                    , type: data.field.type
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

        // 验证密码
        form.verify({
            name: [/\S/, '书名不能为空'],
            author:[/\S/, '作者不能为空'],
            publisher:[/\S/, '出版社不能为空'],
            re_password: function (value) {
                var repassvalue = $('#password').val();
                if (value.length < 6 || value.length > 18)
                    return '密码必须6到18位';
                if (value !== repassvalue) {
                    return '两次输入的密码不一致!';
                }
            }
        });


        $(function () {
            $.ajax({
                url: "<%=request.getContextPath()%>/BookServlet?action=getBookTypes",//请求地址
                type: "POST",//请求方式
                dataType: "json",//返回数据类型
                contentType: "application/json",
                async: false,//同步
                success: function (result) {
                    var data = result;
                    var proHtml = '';
                    for (var o in data) {
                        proHtml += '<option value="' + data[o] + '">' + data[o] + '</option>';
                    }
                    $("#type").html(proHtml);
                },
                error: function () {
                    alert("fail");
                }
            });
            form.render();
        });


        var parent_json = eval('(' + parent.json + ')');
        // 填充数据
        form.val("from", {
            "name": parent_json.Name
            , "isbn": parent_json.ISBN
            , "author": parent_json.Author
            , "type": parent_json.Type
            , "publisher": parent_json.Publisher
            , "publishdate": parent_json.PublishDate
            , "amount": parent_json.Amount
            , "available": parent_json.Available
        })
    });
</script>
</html>