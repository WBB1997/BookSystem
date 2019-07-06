<%@ page import="model.Admin" %>
<%@ page import="model.Staff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>图书管理系统</title>
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">工作人员后台</div>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="${pageContext.request.contextPath}/img/head.jpeg" class="layui-nav-img">
                    <%=((Staff)request.getSession().getAttribute("Staff")).getName()%>
                </a>
                <dl class="layui-nav-child">
                    <dd><a id="info">个人资料</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="<%=request.getContextPath()%>/StaffServlet?action=loginOut">退出</a></li>
        </ul>
    </div>
    <%-- 左边区域 --%>
    <div class="layui-side layui-bg-black">
        <iframe name="mainFrame" src="left_control.jsp" style="border: none ;width: 100%; height: 99%;">
        </iframe>
    </div>
    <%--中间区域--%>
    <div class="layui-body">
        <iframe name="table" src="operate_book.jsp" style="border: none;width: 100%;height: 99%;">
        </iframe>
    </div>
</div>
<script src="${pageContext.request.contextPath}/layui-v2.5.4/layui/layui.js"></script>
<script>
    //JavaScript代码区域
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
                content: '/staff/staff_info.jsp'
            });
        });
    });
</script>
</body>
</html>