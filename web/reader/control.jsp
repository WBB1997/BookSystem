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
            <li class="layui-nav-item"  ><a href="message.jsp" target="mainFrame">消息<span class="layui-badge-dot layui-bg-red"></span></a></li>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="/img/head.jpeg" class="layui-nav-img">
                    <%=((Reader)request.getSession().getAttribute("Reader")).getName()%>
                </a>
                <dl class="layui-nav-child">
                    <dd><a id="info">基本资料</a></dd>
                </dl>
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
        <iframe name="table" src="book_table.jsp" style="border: none; width: 100%; height: 99%;">
        </iframe>
    </div>
</div>
<script src="/layui-v2.5.4/layui/layui.js"></script>
<script>
    layui.use(['layer','element','jquery'], function () {
        var element = layui.element
            , $ = layui.jquery
            ,layer = layui.layer;
        // // 单击添加
        $(document).on('click', '#info' , function () {
            layer.open({
                title: '修改个人信息',
                type: 2,
                skin: 'layui-layer-lan',
                closeBtn: 2,
                // skin: 'layui-layer-rim', // 加上边框
                area: ["740px", "550px"], // 宽高
                // maxmin: true, //开启最大化最小化按钮
                content: '/reader/reader_info.jsp',
                end: function () {
                }
            });
        });
    });
</script>
</body>
</html>