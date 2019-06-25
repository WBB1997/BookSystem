<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>管理员后台</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">管理员后台</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item"  ><a href="admin_book.jsp" target="mainFrame">图书管理</a></li>
            <li class="layui-nav-item"  ><a href="admin_reader.jsp" target="mainFrame">读者管理</a></li>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="/img/head.jpeg" class="layui-nav-img">
                    贤心
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="">基本资料</a></dd>
                    <dd><a href="">安全设置</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="">退了</a></li>
        </ul>
    </div>
    <%-- 左边区域 --%>
    <div class="layui-side layui-bg-black">
        <iframe name="mainFrame" src="admin_book.jsp" style="border: none ;width: 100%; height: 400px;">
        </iframe>
    </div>
    <%--中间区域--%>
    <div class="layui-body">
        <iframe name="table" src="BookTable.jsp" style="border: none;width: 100%;height: 100%;">
        </iframe>
    </div>
</div>
<script src="/layui-v2.5.4/layui/layui.js"></script>
<script>
    //JavaScript代码区域
    layui.use('element', function () {
        var element = layui.element;

    });
</script>
</body>
</html>