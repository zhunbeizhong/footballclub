package com.chzu.football.interceptor;

import org.apache.struts2.ServletActionContext;

import com.chzu.football.entity.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class AdminInterceptor extends MethodFilterInterceptor
{

    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        // TODO Auto-generated method stub
        User user=(User)ServletActionContext.getRequest().getSession().getAttribute("admin");
        if(user==null){
           return "login"; 
        }
        return invocation.invoke();
    }

}
