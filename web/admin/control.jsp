<%@ page import="model.Admin" %>
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
        <div class="layui-logo">管理员后台-图书管理系统</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item" ><a href="staff_table.jsp" target="table">工作人员管理</a></li>
            <li class="layui-nav-item" ><a href="reader_table.jsp" target="table">读者管理</a></li>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="/img/head.jpeg" class="layui-nav-img">
                    <%=((Admin)request.getSession().getAttribute("Admin")).getName()%>
                </a>
                <dl class="layui-nav-child">
                    <dd><a id="info">个人资料</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="<%=request.getContextPath()%>/AdminServlet?action=loginOut">退出</a></li>
        </ul>
    </div>
</div>
<%--中间区域--%>
<div style="height: 1000px">
    <iframe name="table" src="staff_table.jsp" style="border: none;width: 100%;height: 99%;">
    </iframe>
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
                content: '/admin/admin_info.jsp',
                end: function () {
                }
            });
        });
    });
</script>
</body>
</html>