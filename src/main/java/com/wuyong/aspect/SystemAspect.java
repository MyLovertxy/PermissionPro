package com.wuyong.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyong.domain.Systemlog;
import com.wuyong.mapper.SystemlogMapper;
import com.wuyong.util.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class SystemAspect {
    @Autowired
    private SystemlogMapper systemlogMapper;

    public void writeLog(JoinPoint joinPoint) throws JsonProcessingException {
        System.out.println("记录日志------------");
        /*设置时间*/
        Systemlog systemlog = new Systemlog();
        systemlog.setOptime(new Date());
        /*设置ip地址*/
        /*requst对象可以拿到IP地址  没有request 添加拦截器获取请求对象*/
        HttpServletRequest request = RequestUtil.getRequest();
        if(request!=null){
            String ip = request.getRemoteAddr();
            System.out.println(ip);
            systemlog.setIp(ip);
        }
        /*获取目标方法的全路径*/
        String name = joinPoint.getTarget().getClass().getName();
        /*获取方法的名字*/
        String signature = joinPoint.getSignature().getName();
        String func=name+":"+signature;
        systemlog.setFunction(func);

        /*获取方法的参数*/
        String param = new ObjectMapper().writeValueAsString(joinPoint.getArgs());
        systemlog.setParams(param);
        systemlogMapper.insert(systemlog);
    }
}
