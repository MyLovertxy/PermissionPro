package com.wuyong.web;

import com.wuyong.domain.Department;
import com.wuyong.service.DepartmentService;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @RequestMapping("departList")
    @ResponseBody
    public  List<Department> departList(){
        List<Department> departmentList = departmentService.getDepartmentList();
        return departmentList;
    }
}
