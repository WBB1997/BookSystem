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
<div class="layui-side-scroll" id="reader" style="display: inline">
    <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
    <ul class="layui-nav layui-nav-tree">
        <li class="layui-nav-item">
            <a href="reader.jsp" target="table">读者列表</a>
        </li>
    </ul>
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