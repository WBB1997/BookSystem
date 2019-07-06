<%--
  Created by IntelliJ IDEA.
  User: FJ
  Date: 2019/6/24
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="referrer" content="no-referrer"/>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title></title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/layui-v2.5.4/layui/css/layui.css" media="all">
    <script src="<%=request.getContextPath()%>/layui-v2.5.4/layui/layui.js"></script>
</head>

<style>
    .layui-table-cell {
        height: auto;
        white-space: normal;
    }
</style>

<body style="margin-top: 10px">
<form class="layui-form" action="">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">ISBN</label>
            <div class="layui-input-block">
                <input type="text" id="input" lay-verify="required" placeholder="请输入ISBN" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <a id="searchId" class="layui-btn">查询</a>
        </div>
    </div>
</form>

<table class="layui-hide" id="historyTable" lay-filter="history"></table>

<%--<script type="text/html" id="historyBar">--%>
<%--    &lt;%&ndash;<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>&ndash;%&gt;--%>
<%--    &lt;%&ndash;    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>&ndash;%&gt;--%>
<%--</script>--%>

<script type="text/html" id="imgTpl">
    <div align="center">
        <img src="{{ d.Cover }}">
    </div>
</script>

<script>
    layui.use(['form', 'laypage', 'layer', 'table', 'upload', 'element'], function () {
        var form = layui.form
            , laypage = layui.laypage //分页
            , layer = layui.layer //弹层
            , table = layui.table //表格
            , upload = layui.upload //上传
            , element = layui.element //元素操作
            , $ = layui.jquery;

        //执行一个 table 实例
        table.render({
            elem: '#historyTable'
            , id: 'historyTable'
            , height: 'full-100'
            , url: '<%=request.getContextPath()%>/StaffServlet?action=getHistory' //数据接口
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度
            , title: '历史表'
            , page: true //开启分页
            , limit: 15
            , skin: 'line'
            , even: true
            , limits: [15, 30, 45, 60]
            , curr: 1 //设定初始在第 1 页
            , toolbar: ['print', 'filter', 'exports'] //开启工具栏，此处显示默认图标
            , cols: [[ //表头
                // {field: 'Cover', title: '封面', fixed: 'left', templet: '#imgTpl'}
                {field: 'Name', align: 'center', title: '书名'}
                , {field: 'ISBN', align: 'center',title: 'ISBN'}
                , {field: 'PurchaseDate', align: 'center',title: '时间', sort: true}
                , {field: 'PurchaseAmount', align: 'center',title: '操作数量'}
            ]]
        });

        // 单击搜索
        $("#searchId").click(function () {
            var isbn = $("#input").val();
            table.reload('historyTable', {
                url: '<%=request.getContextPath()%>/StaffServlet?action=getHistory&keyword=' + isbn
            });
        });

    });
</script>
</body>
</html>
