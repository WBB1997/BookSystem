<%@ page import="model.Reader" %>
<%@ page import="model.Staff" %>
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
<body style="margin-top: 10px" scroll="no">
<form class="layui-form">
    <div class="layui-inline">
        <label class="layui-form-label">ISBN</label>
    </div>
    <div class="layui-inline">
        <input type="text" id="searchWord" lay-verify="required" placeholder="输入ISBN" autocomplete="off"
               class="layui-input">
    </div>
    <div class="layui-inline">
        <a id="searchId" class="layui-btn">查询</a>
    </div>
</form>

<table class="layui-hide" id="bookTable" lay-filter="bookTable" lay-even></table>

<script type="text/html" id="bookBar">
    {{#  if(d.Status === '待处理'){ }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="cancel">拒绝</a>
    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="ok">同意</a>
    {{#  }}}
</script>

<script type="text/html" id="imgTpl">
    <div align="center">
        <img src="{{ d.Cover }}">
    </div>
</script>

<script type="text/html" id="statusTpl">
    {{#  if(d.Status === '待处理'){ }}
    <div align="center">
        <lable style="color: #fb830e">待处理</lable>
    </div>
    {{#  }else if (d.Status === '申请失败'){ }}
    <div align="center">
        <lable style="color: red">申请失败</lable>
    </div>
    {{# }else if (d.Status === '申请成功'){ }}
    <div align="center">
        <lable style="color: green">申请成功</lable>
    </div>
    {{#  }else if (d.Status === '已取消'){ }}
    <div align="center">
        <lable style="color: green">已取消</lable>
    </div>
    {{#  }}}
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
            elem: '#bookTable'
            , id: 'bookTable'
            , height: 'full-100'
            , url: '<%=request.getContextPath()%>/StaffServlet?action=getReturnApplyHistory' //数据接口
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度
            , title: '书籍表'
            , page: true //开启分页
            , limit: 15
            , skin: 'line'
            , even: true
            , limits: [15, 30, 45, 60]
            , curr: 1 //设定初始在第 1 页
            , toolbar: ['print', 'filter', 'exports'] //开启工具栏，此处显示默认图标
            , cols: [[ //表头
                {field: 'Reader_No', align: 'center', title: '读者账号'}
                // , {field: 'Cover', title: '封面', templet: '#imgTpl'}
                , {field: 'Name', align: 'center', title: '书名'}
                , {field: 'ISBN', align: 'center', title: 'ISBN'}
                , {field: 'Time', align: 'center', title: '时间'}
                , {field: 'Status', align: 'center', title: '状态', templet: '#statusTpl'}
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bookBar'}
            ]]
        });
        //监听行工具事件
        table.on('tool(bookTable)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                , layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'ok') {
                layer.confirm('请仔细审核读者信息，是否同意？', function (index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'get',
                        url: '<%=request.getContextPath()%>/StaffServlet?action=dealWithReturnApply&keyword="同意"',
                        data: {
                            isbn: data.ISBN//传向后端的数据
                            , account: data.Reader_No//传向后端的数据
                        },
                        contentType: 'application/json',
                        success: function (result) {
                            if (result.status === "ok") {
                                layer.msg(result.content, {icon: 6});
                            } else {
                                layer.msg(result.content, {icon: 5});
                            }
                            flushTab();
                        },
                        error: function (result) {
                            layer.msg('网络错误', {icon: 2}, {time: 2000});
                            flushTab();
                        }
                    });
                });
            }else if (layEvent === 'cancel'){
                layer.confirm('是否拒绝？', function (index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'get',
                        url: '<%=request.getContextPath()%>/StaffServlet?action=dealWithReturnApply&keyword="拒绝"',
                        data: {
                            isbn: data.ISBN//传向后端的数据
                            , account: data.Reader_No//传向后端的数据
                        },
                        contentType: 'application/json',
                        success: function (result) {
                            if (result.status === "ok") {
                                layer.msg(result.content, {icon: 6});
                            } else {
                                layer.msg(result.content, {icon: 5});
                            }
                            flushTab();
                        },
                        error: function (result) {
                            layer.msg('网络错误', {icon: 2}, {time: 2000});
                            flushTab();
                        }
                    });
                });
            }
        });

        // 单击搜索
        $("#searchId").click(function () {
            // 注意参数(bookTable为表格id)
            // type为select选择框
            // searchWord为输入框
            var searchWord = $('#searchWord').val();
            table.reload('bookTable', {
                url: '<%=request.getContextPath()%>/StaffServlet?action=getReturnApplyHistory&keyword=' + searchWord
            });
        });

        // 刷新表格
        function flushTab() {
            // $(".layui-laypage-btn")[0].click();
            table.reload('bookTable', {
                url: '<%=request.getContextPath()%>/StaffServlet?action=getReturnApplyHistory'
            });
        }

    });
</script>
</body>
</html>
