package com.wuyong.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyong.domain.AjaxRes;
import com.wuyong.domain.Employee;
import com.wuyong.domain.PageListRes;
import com.wuyong.domain.QueryVo;
import com.wuyong.service.EmployeeService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/employee")
    @RequiresPermissions("employee:index")
    public String employee(){
        return "employee";
    }

    @RequestMapping("/employeeList")
    @ResponseBody
    /*员工列表  带分页*/
    public PageListRes employeeList(QueryVo vo, HttpSession session){
        System.out.println(vo);
        PageListRes pageListRes = employeeService.getEmployee(vo);
        session.setAttribute("employeeList",pageListRes);
        return pageListRes;
    }

    @RequestMapping("saveEmployee")
    @ResponseBody
    @RequiresPermissions("employee:add")
    /*接收员工添加的表单*/
    public AjaxRes saveEmployee(Employee employee){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            System.out.println("提交表单成功");
            System.out.println(employee);
            employee.setState(true);
            employeeService.saveEmployee(employee);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;
    }

    @RequestMapping("updateEmployee")
    @ResponseBody
    @RequiresPermissions("employee:edit")
    /*更新员工*/
    public AjaxRes updateEmployee(Employee employee){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            System.out.println("更新员工-----");
            System.out.println(employee);
            employeeService.updateEmployee(employee);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }

    @RequestMapping("updateState")
    @ResponseBody
    @RequiresPermissions("employee:delete")
    /*接收离职操作请求*/
    public AjaxRes updateState(long id){
        return employeeService.updateState(id);
    }
    @ExceptionHandler(AuthorizationException.class)
    public void handleShiroException(HandlerMethod method, HttpServletResponse response) throws IOException {/*发生异常的方法*/
        /*跳转到一个界面  界面提示没有权限*/
        /*判断当前这个请求是不是json请求 如果是  返回json给浏览器让他自己跳转*/
        /*获取方法上的注解*/
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if(responseBody!=null){
            //ajax
            AjaxRes ajaxRes = new AjaxRes();
            ajaxRes.setMsg("你没有权限操作");
            ajaxRes.setSuccess(false);
            String s = new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(s);
        }else {
            response.sendRedirect("/nopermission.jsp");
        }
    }

    /*导出文件到Excel*/
    @RequestMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response,HttpSession session){
        try {
            System.out.println("---------downloadExcel----------");
//        1从数据库中取出数据
        QueryVo queryVo = new QueryVo();
        queryVo.setPage(1);
        queryVo.setRows(5);
            PageListRes pageListRes = (PageListRes) session.getAttribute("employeeList");
            List<Employee> employees = (List<Employee>) pageListRes.getRows();
//            PageListRes plr = employeeService.getEmployee(queryVo);
//        List<Employee> employees = (List<Employee>) plr.getRows();
//        2创建Excel 写到excel中
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("员工数据");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("用户名");
        row.createCell(2).setCellValue("入职日期");
        row.createCell(3).setCellValue("电话");
        row.createCell(4).setCellValue("邮件");
        HSSFRow employeeRow=null;
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            employeeRow = sheet.createRow(i + 1);
            employeeRow.createCell(0).setCellValue(employee.getId());
            employeeRow.createCell(1).setCellValue(employee.getUsername());
            if(employee.getInputtime()!=null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(employee.getInputtime());
                employeeRow.createCell(2).setCellValue(format);
            }else {
                employeeRow.createCell(2).setCellValue("");
            }
            employeeRow.createCell(3).setCellValue(employee.getTel());
            employeeRow.createCell(4).setCellValue(employee.getEmail());
        }
//        3相应给浏览器

            String filename = new String("员工数据.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;filename="+filename);
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
/*下载上传模板*/
    @RequestMapping("downloadExcelTpl")
    @ResponseBody
    public void downloadExcelTpl(HttpServletRequest request,HttpServletResponse response){
        FileInputStream is=null;
        try {
            String filename = new String("EmployeeTpl.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;filename="+filename);

            /*获取文件路径*/
            String realPath = request.getSession().getServletContext().getRealPath("static/ExcelTpl.xls");

            is=new FileInputStream(realPath);
            IOUtils.copy(is,response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




    }

    /*文件上传     要配置mvc解析器*/
    @RequestMapping("uploadExcelFile")
    @ResponseBody
    public AjaxRes uploadExcelFile(MultipartFile excel){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            ajaxRes.setMsg("导入成功");
            ajaxRes.setSuccess(true);
            HSSFWorkbook wb = new HSSFWorkbook(excel.getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            /*获取最大的行号*/
            int lastRowNum = sheet.getLastRowNum();

            Row employeeRow=null;
            for (int i = 1; i <=lastRowNum ; i++) {
                employeeRow=sheet.getRow(i);
                Employee employee=new Employee();
                getCellValue(employeeRow.getCell(0));
                employee.setUsername((String) getCellValue(employeeRow.getCell(1)));

                Cell cell=employeeRow.getCell(2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
                Date date = simpleDateFormat.parse(cell.getRichStringCellValue().toString());
                employee.setInputtime(date);
                employee.setTel((String) getCellValue(employeeRow.getCell(3)));
                employee.setEmail((String) getCellValue(employeeRow.getCell(4)));
                employee.setPassword("1234");
                employeeService.saveEmployee(employee);
            }
        }catch (Exception e){
            ajaxRes.setMsg("导入失败");
            ajaxRes.setSuccess(true);
            e.printStackTrace();
        }


        return ajaxRes;
    }

    private Object getCellValue(Cell cell){
        switch (cell.getCellType()) {
            case STRING:
                    return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
        }
        return cell;
    }

}
