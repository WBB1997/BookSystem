<%@ page import="model.Reader" %>
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
        <label class="layui-form-label">类别</label>
        <div class="layui-inline">
            <select id="type" lay-filter="type">
                <option value="All">任意词</option>
                <option value="ISBN">ISBN</option>
                <option value="Name">书名</option>
                <option value="Author">作者</option>
                <option value="Type">类型</option>
                <option value="Publisher">出版社</option>
            </select>
        </div>
    </div>
    <div class="layui-inline">
        <input type="text" id="searchWord" lay-verify="required" placeholder="输入关键词" autocomplete="off"
               class="layui-input">
    </div>
    <div class="layui-inline">
        <a id="searchId" class="layui-btn">查询</a>
    </div>
    <div class="layui-inline">
        <a id="addId" class="layui-btn layui-btn-normal">荐购</a>
    </div>
</form>

<table class="layui-hide" id="bookTable" lay-filter="bookTable"></table>

<script type="text/html" id="bookBar">
    {{#  if(d.Available > 0){ }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="brow">借书</a>
    {{#  }else{ }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="subs">预约</a>
    {{#   } }}
</script>

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
            elem: '#bookTable'
            , id: 'bookTable'
            , skin: 'line'
            , even: true
            , height: 'full-100'
            , url: '<%=request.getContextPath()%>/BookServlet?action=getAllBooks' //数据接口
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度
            , title: '书籍表'
            , page: true //开启分页
            , limit: 15
            , limits: [15, 30, 45, 60]
            , curr: 1 //设定初始在第 1 页
            , toolbar: ['print', 'filter', 'exports'] //开启工具栏，此处显示默认图标
            , cols: [[ //表头
                {field: 'Cover', title: '封面', fixed: 'left', templet: '#imgTpl'}
                , {field: 'ISBN', title: 'ISBN', sort: true}
                , {field: 'Name', title: '书名'}
                , {field: 'Author', title: '作者'}
                , {field: 'Publisher', title: '出版社'}
                , {field: 'PublishDate', title: '出版日期'}
                , {field: 'Type', title: '类型'}
                , {field: 'Amount', title: '馆藏', sort: true}
                , {field: 'Available', title: '可借', sort: true}
                , {fixed: 'right', align: 'center', toolbar: '#bookBar'}
            ]]
        });
        //监听行工具事件
        table.on('tool(bookTable)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                , layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'brow') {
                layer.confirm('确定借书？', function (index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'get',
                        url: '<%=request.getContextPath()%>/ReaderServlet?action=borrowBook',
                        data: {
                            isbn: data.ISBN//传向后端的数据
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
            }else if (layEvent === 'subs'){
                layer.confirm('确定预约？', function (index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'get',
                        url: '<%=request.getContextPath()%>/ReaderServlet?action=BookSubscribe',
                        data: {
                            isbn: data.ISBN//传向后端的数据
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
            var keyword = $('#type').val();
            var searchWord = $('#searchWord').val();
            console.log(keyword + searchWord);
            table.reload('bookTable', {
                url: '<%=request.getContextPath()%>/BookServlet?action=getBooks&keyword=' + keyword + '&searchword=' + searchWord
            });
        });

        // 单击添加
        $("#addId").click(function () {
            layer.open({
                title: '推荐书籍',
                type: 2,
                skin: 'layui-layer-lan',
                closeBtn: 2,
                // skin: 'layui-layer-rim', // 加上边框
                area: ["700px", "470px"], // 宽高
                content: '/book/book_request.jsp',
                end: function () {
                    flushTab();
                }
            });

        });

        // 刷新表格
        function flushTab() {
            // $(".layui-laypage-btn")[0].click();
            table.reload('bookTable', {
                url: '<%=request.getContextPath()%>/BookServlet?action=getAllBooks'
            });
        }

    });
</script>
</body>
</html>
