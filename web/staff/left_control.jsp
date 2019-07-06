<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/layui-v2.5.4/layui/css/layui.css">
</head>
<body class="layui-layout-body">
<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <ul class="layui-nav layui-nav-tree" lay-filter="test">
            <li class="layui-nav-item">
                <a href="javascript:;" target="table">申请<span class="layui-badge-dot layui-bg-red"></span></a>
                <dl class="layui-nav-child">
                    <dd><a href="${pageContext.request.contextPath}/staff/borrow_apply_message.jsp" target="table" id="borrow">借书申请<span class="layui-badge layui-bg-gray">2</span></a></dd>
                    <dd><a href="${pageContext.request.contextPath}/staff/return_apply_message.jsp" target="table" id="return">还书申请<span class="layui-badge layui-bg-gray">3</span></a></dd>
                    <dd><a href="${pageContext.request.contextPath}/staff/reader_request_apply.jsp" target="table" id="request">读者荐购<span class="layui-badge layui-bg-gray">5</span></a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="operate_book.jsp" target="table">书架</a>
            </li>
            <li class="layui-nav-item">
                <a href="operate_book_history.jsp" target="table">操作历史</a>
            </li>
        </ul>
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