package com.wuyong.service.Impl;

import com.wuyong.domain.Department;
import com.wuyong.mapper.DepartmentMapper;
import com.wuyong.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public List<Department> getDepartmentList() {
        List<Department> departments = departmentMapper.selectAll();
        return departments;
    }
}
