package com.wuyong.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyong.domain.AjaxRes;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyFormFilter extends FormAuthenticationFilter {
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
       response.setCharacterEncoding("utf-8");
        System.out.println("认证成功-----------");
        AjaxRes ajaxRes = new AjaxRes();
        ajaxRes.setMsg("登录成功");
        ajaxRes.setSuccess(true);
        /*把对象转成json格式的字符串*/
        String jsonString = new ObjectMapper().writeValueAsString(ajaxRes);
        response.getWriter().print(jsonString);
        return false;
    }

    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e,
                                     ServletRequest request,
                                     ServletResponse response) {
        response.setCharacterEncoding("utf-8");
        System.out.println("认证失败-------------");
        AjaxRes ajaxRes = new AjaxRes();
        ajaxRes.setSuccess(false);
        if(e!=null){
            /*获取异常的名称*/
            String name = e.getClass().getName();
            if(name.equals(UnknownAccountException.class.getName())){
                /*没有账号*/
                ajaxRes.setMsg("账号不存在");
            }else if(name.equals(IncorrectCredentialsException.class.getName())){
                /*密码错误*/
                ajaxRes.setMsg("密码错误");
            }else {
                /*位置异常*/
                ajaxRes.setMsg("位置错误");
            }
        }

        try {
            /*把对象转成json格式的字符串*/
            String jsonString = new ObjectMapper().writeValueAsString(ajaxRes);
            response.getWriter().print(jsonString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
