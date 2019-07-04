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
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title></title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/layui-v2.5.4/layui/css/layui.css" media="all">
    <script src="<%=request.getContextPath()%>/layui-v2.5.4/layui/layui.js"></script>
</head>
<body style="margin-top: 10px">
<form class="layui-form" action="">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">ID搜索</label>
            <div class="layui-input-block">
                <input type="text" id="id" lay-verify="required" placeholder="请输入读者ID" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <a id="searchId" class="layui-btn">查询</a>
        </div>
    </div>
</form>

<table class="layui-hide" id="readerTable" lay-filter="readerTable" name="readerTable"></table>
<script type="text/html" id="toolBar">
    <a class="layui-btn layui-btn-xs" lay-event="edit">重置密码</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
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
            elem: '#readerTable'
            , id: 'readerTable'
            , height: 'full-200'
            , url: '<%=request.getContextPath()%>/AdminServlet?action=getReaderList' //数据接口
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度
            , title: '读者表'
            , page: true //开启分页
            , limit: 15
            , limits: [15, 30, 45, 60]
            , curr: 1 //设定初始在第 1 页
            , toolbar: ['print', 'filter', 'exports'] //开启工具栏，此处显示默认图标
            , cols: [[ //表头
                {field: 'No', title: 'ID', fixed: 'left', sort: true}
                , {field: 'Name', title: '姓名'}
                , {field: 'Gender', title: '性别'}
                , {field: 'Fine', title: '罚款'}
                , {field: 'TakeEffectDate', title: '开始日期'}
                , {field: 'LoseEffectDate', title: '截至日期'}
                , {fixed: 'right', align: 'center', toolbar: '#toolBar'}
            ]]
        });
        //监听行工具事件
        table.on('tool(readerTable)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                , layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'del') {
                layer.confirm('真的删除?', function (index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'get',
                        url: '<%=request.getContextPath()%>/AdminServlet?action=deleteReader',
                        data: {
                            no: data.No//传向后端的数据
                        },
                        contentType: 'application/json',
                        success: function (result) {
                            if (result.status === 'ok') {
                                layer.msg('删除成功', {icon: 1}, {time: 2000});
                            } else if (result.status === 'error') {
                                layer.msg(result.content, {icon: 2}, {time: 2000});

                            }
                            flushTab();
                        },
                        error: function (result) {
                            layer.msg('网络错误', {icon: 2}, {time: 2000});
                        }
                    });
                });
            } else if (layEvent === 'edit') {
                layer.confirm('确定重置？', function (index) {
                    json = JSON.stringify(data);
                    $.ajax({
                        type: 'get',
                        url: '<%=request.getContextPath()%>/AdminServlet?action=resetReaderPassword',
                        data: {
                            no: data.No//传向后端的数据
                        },
                        contentType: 'application/json',
                        success: function (result) {
                            if (result.status === 'ok') {
                                layer.msg('重置成功,重置密码为当前管理员账号', {icon: 1}, {time: 2000});
                            } else if (result.status === 'error') {
                                layer.msg(result.content, {icon: 2}, {time: 2000});

                            }
                            flushTab();
                        },
                        error: function (result) {
                            layer.msg('网络错误', {icon: 2}, {time: 2000});
                        }
                    });
                });
            }
        });
        // 单击搜索
        $("#searchId").click(function () {
            // 注意参数(myTable为表格id)
            var studentId = $("#id").val();
            table.reload('readerTable', {
                url: '<%=request.getContextPath()%>/AdminServlet?action=getReaderList&keyWord=' + studentId
            });
        });

        // 刷新表格
        function flushTab() {
            table.reload('readerTable', {
                url: '<%=request.getContextPath()%>/AdminServlet?action=getReaderList'
            });
        }

    });
</script>
</body>
</html>
