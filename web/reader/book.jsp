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
                <a href="book_table.jsp" target="table">书架</a>
            </li>
            <li class="layui-nav-item">
                <a href="borrow_return_history.jsp" target="table">借书记录</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;" target="table">申请记录<span class="layui-badge-dot layui-bg-red"></span></a>
                <dl class="layui-nav-child">
                    <dd><a href="${pageContext.request.contextPath}/reader/borrow_apply_history.jsp" target="table">我的借书<span class="layui-badge layui-bg-gray">1</span></a></dd>
                    <dd><a href="${pageContext.request.contextPath}/reader/return_apply_history.jsp" target="table">我的还书<span class="layui-badge layui-bg-gray">2</span></a></dd>
                    <dd><a href="${pageContext.request.contextPath}/reader/book_request.jsp" target="table">我的荐购<span class="layui-badge layui-bg-gray">2</span></a></dd>
                    <dd><a href="${pageContext.request.contextPath}/reader/reader_subscribe.jsp" target="table">我的预约<span class="layui-badge layui-bg-gray">5</span></a></dd>
                </dl>
            </li>s
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