<%@ page import="model.Reader" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2019/6/26
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body>
<form class="layui-form" action="" style="margin-top: 30px" lay-filter="from">
    <div class="layui-form-item">
        <label class="layui-form-label">账号</label>
        <div class="layui-input-inline">
            <input type="text" name="no" lay-verify="no" placeholder="请输入账号" autocomplete="off"
                   class="layui-input layui-disabled" readonly unselectable="on">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">旧密码</label>
        <div class="layui-input-inline">
            <input type="password" id="old_password" name="old_password" lay-verify="old_password" placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">新密码</label>
        <div class="layui-input-inline">
            <input type="password" id="new_password" name="new_password" lay-verify="new_password" placeholder="请输入密码"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">确认新密码</label>
        <div class="layui-input-inline">
            <input type="password" id="re_new_password" name="re_new_password" lay-verify="re_new_password" placeholder="请输入密码"
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
        <div class="layui-input-block" lay-verify="sex">
            <input type="radio" name="sex" value="男" title="男">
            <input type="radio" name="sex" value="女" title="女">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">账号类型</label>
        <div class="layui-input-inline">
            <select name="type" lay-filter="type" id="type">
                <option value=""></option>
            </select>
        </div>
        <div class="layui-input-inline">
            <select name="college" id="college">
                <option value=""></option>
            </select>
        </div>
    </div>


    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">生效日期</label>
            <div class="layui-input-block">
                <input type="text" name="date1" id="date1" autocomplete="off" lay-filter="date1"
                       class="layui-input layui-disabled" disabled>
            </div>
        </div>

        <div class="layui-inline">
            <label class="layui-form-label">失效日期</label>
            <div class="layui-input-block">
                <input type="text" name="date2" id="date2" autocomplete="off" lay-filter="date2"
                       class="layui-input layui-disabled" disabled>
            </div>
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

        //日期
        laydate.render({
            elem: '#date1'
        });
        laydate.render({
            elem: '#date2'
        });

        // 验证密码
        form.verify({
            old_password: [/(.+){6,18}$/, '密码必须6到18位'],
            new_password: function (value) {
                var repassvalue = $('#re_new_password').val();
                if (value.length < 6 || value.length > 18)
                    return '密码必须6到18位';
                if (value !== repassvalue) {
                    return '两次输入的密码不一致!';
                }
            },
            name: [/\S/, '姓名不能为空'],
        });

        // 提交 只有在验证全部通过后才会进入
        form.on('submit(submit)', function (data) {
            // console.log(data.elem)//被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form)//被执行提交的form对象，一般在存在form标签时才会返回
            // console.log(data.field)//当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                url: '<%=request.getContextPath()%>/ReaderServlet?action=updateReader',
                type: 'POST',
                data: {
                    no: data.field.no
                    , old_password: data.field.old_password
                    , new_password: data.field.new_password
                    , name: data.field.name
                    , gender: data.field.sex
                    , type: data.field.type
                    , college: data.field.college
                },
                dataType: 'json',
                async: false,
                success: function (msg) {
                    console.log(msg.valueOf());
                    if (msg.status === "ok") {
                        layer.msg(msg.content, {icon: 6}, 2000);
                    } else {
                        layer.msg(msg.content, {icon: 5});
                    }
                }
            });
            return false;
        });

        // 获取select 值
        $(function () {
            $.ajax({
                url: "<%=request.getContextPath()%>/ReaderServlet?action=getCollegeTypes",//请求地址
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
                    $("#college").html(proHtml);
                },
                error: function () {
                    alert("fail");
                }
            });

            $.ajax({
                url: "<%=request.getContextPath()%>/ReaderServlet?action=getReaderTypes",//请求地址
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

        // 初始值
        <%
            SimpleDateFormat sdf_input = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");//输入格式
            SimpleDateFormat sdf_target =new SimpleDateFormat("yyyy-MM-dd"); //转化成为的目标格式
        %>

        // 刷新表格
        form.val("from", {
            "no": "<%=((Reader)session.getAttribute("Reader")).getNo()%>",
            "name": "<%=((Reader)session.getAttribute("Reader")).getName()%>"
            ,
            "sex": "<%=((Reader)session.getAttribute("Reader")).getGender()%>"
            ,
            "college": "<%=((Reader)session.getAttribute("Reader")).getCollege()%>"
            ,
            "date1": "<%=sdf_target.format(sdf_input.parse(((Reader)session.getAttribute("Reader")).getTakeEffectDate()))%>"
            ,
            "date2": "<%=sdf_target.format(sdf_input.parse(((Reader)session.getAttribute("Reader")).getLoseEffectDate()))%>"
            ,
            "type": "<%=((Reader)session.getAttribute("Reader")).getType()%>"
        });
    });
</script>
</html>
