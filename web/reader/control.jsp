<%@ page import="model.Reader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>读者后台</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">读者后台</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item"  ><a href="book.jsp" target="mainFrame">图书</a></li>
            <li class="layui-nav-item"  ><a href="personal.jsp" target="mainFrame">个人</a></li>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="/img/head.jpeg" class="layui-nav-img">
                    <%=((Reader)request.getSession().getAttribute("Reader")).getName()%>
                </a>
<%--                <dl class="layui-nav-child">--%>
<%--                    <dd><a href="">安全设置</a></dd>--%>
<%--                </dl>--%>
            </li>
            <li class="layui-nav-item"><a href="<%=request.getContextPath()%>/ReaderServlet?action=loginOut">退出</a></li>
        </ul>
    </div>
    <%-- 左边区域 --%>
    <div class="layui-side layui-bg-black">
        <iframe name="mainFrame" src="book.jsp" style="border: none ;width: 100%; height: 400px;">
        </iframe>
    </div>
    <%--中间区域--%>
    <div class="layui-body">
        <iframe name="table" src="BookTable.jsp" style="border: none; width: 100%; height: 99%;">
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