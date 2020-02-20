$(function () {

    $("#menu_datagrid").datagrid({
        url:"/menuList",
        columns:[[
            {field:'text',title:'名称',width:1,align:'center'},
            {field:'url',title:'跳转地址',width:1,align:'center'},
            {field:'parent',title:'父菜单',width:1,align:'center',formatter:function(value,row,index){
                    return value?value.text:'';
                }
            }
        ]],
        fit:true,
        rownumbers:true,
        singleSelect:true,
        striped:true,
        pagination:true,
        fitColumns:true,
        toolbar:'#menu_toolbar'
    });

    /*
     * 初始化新增/编辑对话框
     */
    $("#menu_dialog").dialog({
        width:300,
        height:300,
        closed:true,
        buttons:'#menu_dialog_bt'
    });
    
    /*下拉框父类菜单选项初始化*/
    $('#parentMenu').combobox({
        widtd:150,
        panelHeight:'auto',
        editable: false,
        url:'parentList',
        valueField:'id',
        textField:'text',
        onLoadSuccess:function () {/*数据加载完毕之后*/
            $("#parentMenu").each(function(i){
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
    })
    
    /*选择添加按钮*/
    $("#add").click(function () {
        $('#menu_form').form("clear");
        $("#menu_dialog").dialog("setTitle","添加菜单");
        $("#menu_dialog").dialog("open");
    });
    /*监听编辑按钮*/
    $('#edit').click(function () {
        $('#menu_form').form("clear");
        $('#menu_dialog').dialog("setTitle","编辑菜单");
        /*过获取当前选择的行*/
        var rowData=$('#menu_datagrid').datagrid("getSelected");
        console.log(rowData);
        if(!rowData) {
            $.messager.alert("温馨提示", "请选择一条数据");
            return;
        }
        /*父菜单回显*/
        if(rowData.parent){
            rowData["parent.id"]=rowData.parent.id;
        }else {
            $("#parentMenu").each(function(i){
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
        $('#menu_form').form("load",rowData);
        /*打开对话框*/
        $('#menu_dialog').dialog("open");
    })

    /*点击保存按钮*/
    $('#save').click(function () {
        /*判断当前是保存还是编辑*/
        var id=$("[name='id']").val();
        var url;
        if(id){
            if(id==$("[name='parent.id']").val()){
                $.messager.alert("温馨提示","不能选择自己为父菜单");
                return;
            }
            /*编辑*/
            url="updateMenu";
        }else {
            /*保存*/
            url="saveMenu";
        }
        $('#menu_form').form("submit",{
            url:url,
            success:function (data) {
                console.log(data);
                data=$.parseJSON(data);
                if(data.success){
                    $.messager.alert("温馨提示",data.msg);
                    /*关闭对话框*/
                    $('#menu_dialog').dialog("close");
                    /*下拉列表更新*/
                    $('#parentMenu').combobox("reload");
                    /*重新加载数据表格*/
                    $('#menu_datagrid').datagrid("reload");
                }else {
                    $.messager.alert("温馨提示",data.msg);
                }
            }
        })
    })


    /*取消按钮*/
    $('#cancel').click(function () {
        $('#menu_dialog').dialog("close");
    });

    /*删除*/
    $('#del').click(function () {
        var rowData=$('#menu_datagrid').datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("温馨提示","请选择一条数据");
            return;
        }
        /*提醒用户是否做删除操作*/
        $.messager.confirm("确认","是否做删除操作",function (res) {
            if(res){
                /*做删除操作*/
                $.get("/deleteMenu?id="+rowData.id,function (data) {
                    if(data.success){
                        /*下拉列表更新*/
                        $('#parentMenu').combobox("reload");
                        $.messager.alert("温馨提示",data.msg);
                        /*重新加载数据表格*/
                        $('#menu_datagrid').datagrid("reload");
                    }else {
                        $.messager.alert("温馨提示", data.msg);
                    }
                })
            }
        })
    })
});