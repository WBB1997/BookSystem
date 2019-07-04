<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录</title>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
    <c:if test="${!(empty message)}">
        <script type="text/javascript">
            alert('<c:out value="${message}"/>');
        </script>
        <c:remove var="message" scope="session"/>
    </c:if>

</head>
<body background="img/login.jpg">
<form class="form-horizontal" action="" method="post" role="form" id="from1"
      style="margin-top: 300px; width: 1500px; margin-left: 100px">
    <div class="form-group">
        <label for="account" class="col-sm-2 control-label" style="color: #ffffff">账号</label>
        <div class="col-sm-10" id="user">
            <input type="text" class="form-control" name="account" id="account" placeholder="请输入账号">
            <%--                        <span class="help-block">一个较长的帮助文本块，超过一行。</span>--%>
        </div>
    </div>
    <div class="form-group">
        <label for="password" class="col-sm-2 control-label" style="color: #ffffff">密码</label>
        <div class="col-sm-10" id="pass">
            <input type="password" class="form-control" name="password" id="password" placeholder="请输入密码">
            <%--            <span class="help-block ">一个较长的帮助文本块，超过一行。</span>--%>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-offset-2 col-sm-1">
            <select class="form-control" id="select">
                <option value="Reader">读者</option>
                <option value="Staff">工作人员</option>
                <option value="Admin">管理员</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default" onclick="checkForm()">登录</button>
            <button type="button" class="btn btn-default" id="register"> 注册</button>
        </div>
    </div>
</form>
<%--<script type="text/javascript" color="0,0,255" opacity='0.7' zIndex="-1" count="150" src="<%=request.getContextPath()%>/js/canvas-nest.js"></script>--%>
</body>
<script src="/layui-v2.5.4/layui/layui.js"></script>
<script>
    function checkForm() {
        switch (document.getElementById("select").value) {
            case 'Admin':
                document.getElementById("from1").setAttribute("action", "/AdminServlet?action=checkAccount");
                break;
            case 'Staff':
                document.getElementById("from1").setAttribute("action", "/StaffServlet?action=checkAccount");
                break;
            case 'Reader':
                document.getElementById("from1").setAttribute("action", "/ReaderServlet?action=checkAccount");
                break;
        }
    }

    layui.use(['form', 'laypage', 'layer', 'table', 'upload', 'element'], function () {
        var layer = layui.layer //弹层
            , $ = layui.jquery;


        $(document).on('click', '#register', function () {
            layer.open({
                title: '注册读者账号',
                type: 2,
                skin: 'layui-layer-lan',
                closeBtn: 2,
                // skin: 'layui-layer-rim', // 加上边框
                area: ["740px", "550px"], // 宽高
                // maxmin: true, //开启最大化最小化按钮
                content: 'register.jsp',
            });
        });

    });
</script>
</html>
