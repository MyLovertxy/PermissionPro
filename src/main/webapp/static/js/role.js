$(function () {
    /*角色数据列表*/
    $('#role_dg').datagrid({
        url:"/getRoles",
        columns:[[
            {field:'rnum',title:'角色编号',width:100,align:'center'},
            {field:'rname',title:'角色名称',width:100,align:'center'},
        ]],
        fit:true,
        fitColumns:true,
        rownumbers:true,
        pagination:true,
        // toolbar:'#tb',
        singleSelect:true,
        striped:true,
    })
    /*添加编辑对话框*/
    $('#dialog').dialog({
        width:600,
        height:450,
        buttons:[{
            text:'保存',
            handler:function () {
                /*判断是保存还是编辑*/
                var rid=$("[name='rid']").val();
                var url;
                if(rid){
                    /*编辑*/
                    url="updateRole";
                }else {
                    /*保存*/
                    url='saveRole';
                }

                /*提交表单*/
                $('#myform').form("submit",{
                    url:url,
                    /*获取额外数据*/
                    onSubmit:function(param){
                        /*获取已选权限*/
                        var allrows = $('#role_data2').datagrid("getRows");
                        /*遍历出每一个权限*/
                        for(var i=0;i<allrows.length;i++){
                           var row= allrows[i];
                           param["permissions["+i+"].pid"]=row.pid;
                        }
                    },
                    success:function (data) {
                        data=$.parseJSON(data);
                        if(data.success){
                            $.messager.alert("温馨提示",data.msg);
                            /*关闭对话框*/
                            $('#dialog').dialog("close");
                            /*重新加载数据表格*/
                            $('#role_dg').datagrid("reload");
                        }else {
                            $.messager.alert("温馨提示",data.msg);
                        }
                    }
                })
            }
        },{
            text:'关闭',
            handler:function () {
                $('#dialog').dialog("close");
            }
        }],
        closed:true
    })
    /*添加角色*/
    $('#add').click(function () {
        /*清空表单*/
        $('#myform').form("clear");
        /*清空已选权限*/
        $('#role_data2').datagrid("loadData",{rows:[]});
        $('#dialog').dialog("setTitle","添加角色");
        $('#dialog').dialog("open");
    })
    /*权限列表*/
    $('#role_data1').datagrid({
        title:"所有的权限",
        width:250,
        height:400,
        fitColumns:true,
        url:'/permissionList',
        columns:[[
            {field:'pname',title:'权限名称',width:100,align:'center'}
        ]],
        singleSelect: true,
        /*监听行的点击*/
        onClickRow:function (rowIndex,rowData) {
            /*取出所有的已选权限*/
            var allRows = $('#role_data2').datagrid("getRows");
            /*取出每一个进行判断*/
            for (var i=0;i<allRows.length;i++){
               var row= allRows[i];
               if(rowData.pid==row.pid){ /*已经存在了该权限*/
                  /*让已经存在的权限成为选中状态*/
                   /*获取已经成为选中状态的角标*/
                   var index=$('#role_data2').datagrid("getRowIndex",row);
                   /*让该行成为选中状态*/
                   $('#role_data2').datagrid("selectRow",index);
                   return;
               }
            }
            /*把当前选中的，添加到已选权限*/
            $('#role_data2').datagrid("appendRow",rowData);
        }
    })
    /*选中权限列表*/
    $('#role_data2').datagrid({
        title:"已选权限",
        width:250,
        height:400,
        fitColumns:true,
        columns:[[
            {field:'pname',title:'权限名称',width:100,align:'center'}
        ]],
        singleSelect:true,
        onClickRow:function (rowIndex,rowData) {
            /*删除当前选中的一行*/
            $('#role_data2').datagrid("deleteRow",rowIndex);
        }
    })
    /*监听编辑按钮*/
    $('#edit').click(function () {
        $('#dialog').dialog("setTitle","编辑角色");
        /*过去当前选择的行*/
        var rowData=$('#role_dg').datagrid("getSelected");
        console.log(rowData);
        if(!rowData) {
            $.messager.alert("温馨提示", "请选择一条数据");
            return;
        }
        /*加载当前角色下的权限*/
        var options = $('#role_data2').datagrid("options");
        options.url="getPermissionByRid?rid="+rowData.rid;
        /*重新加载数据*/
        $('#role_data2').datagrid("load");
        /*回显表单*/
        $('#myform').form("load",rowData);
        /*打开对话框*/
        $('#dialog').dialog("open");
    })
    /*监听删除点击*/
    $('#remove').click(function () {
        /*获取当前选中行*/
        var rowData=$('#role_dg').datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("温馨提示","请选择一条数据");
            return;
        }
        /*提醒是否做删除操作*/
        $.messager.confirm("确认","是否做删除操作",function (res) {
            if(res){
                /*做删除操作*/
                $.get("/deleteRole?rid="+rowData.rid,function (data) {
                    /*get请求ajax不需要再转json*/
                    /*data=$.parseJSON(data);*/
                    if(data.success){
                        $.messager.alert("温馨提示",data.msg);
                        /*重新加载数据表格*/
                        $('#role_dg').datagrid("reload");
                    }else {
                        $.messager.alert("温馨提示", data.msg);
                    }
                })
            }
        })
    })
})