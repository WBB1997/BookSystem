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
                <a href="book_table.jsp" target="table">书籍资料</a>
            </li>
            <li class="layui-nav-item">
                <a href="borrow_return_history.jsp" target="table">借书记录</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;" target="table">申请记录</a>
                <dl class="layui-nav-child">
                    <dd><a href="${pageContext.request.contextPath}/reader/borrow_apply_history.jsp" target="table">借书申请</a></dd>
                    <dd><a href="${pageContext.request.contextPath}/reader/return_apply_history.jsp" target="table">还书申请</a></dd>
                </dl>
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