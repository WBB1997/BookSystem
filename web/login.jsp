<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <c:if test="${!(empty message)}">
        <script type="text/javascript">
            alert('<c:out value="${message}"/>');
        </script>
        <c:remove var="message" scope="session"/>
    </c:if>
</head>
<body background="img/login.jpg">
<form class="form-horizontal" action="AdminServlet?action=checkAccount" method="post" role="form" style="margin-top: 300px; width: 1500px; margin-left: 100px">
    <div class="form-group">
        <label for="account" class="col-sm-2 control-label" style="color: #ffffff">账号</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" name="account" id="account" placeholder="请输入账号">
        </div>
    </div>
    <div class="form-group">
        <label for="password" class="col-sm-2 control-label" style="color: #ffffff">密码</label>
        <div class="col-sm-10">
            <input type="password" class="form-control" name="password" id="password" placeholder="请输入密码">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
                <label style="color: #ffffff">
                    <input type="checkbox" name = "remember">请记住我
                </label>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">登录</button>
        </div>
    </div>
</form>
</body>
</html>
