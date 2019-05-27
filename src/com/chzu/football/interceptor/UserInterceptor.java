package com.chzu.football.interceptor;

import org.apache.struts2.ServletActionContext;

import com.chzu.football.entity.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
/*
 * 登录权限，对于未登录的用户，执行页面中的方法会拦截跳转到登录页面
 * 拦截器对ajax并不拦截
 */
public class UserInterceptor extends MethodFilterInterceptor
{

    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        // TODO Auto-generated method stub
        User user=(User)ServletActionContext.getRequest().getSession().getAttribute("user");
        //System.out.println(ServletActionContext.getRequest().getRequestURL());
        //当url中出现了jssessionid也可以通过拦截去去除URL末尾出现的jssessionid
        //未登录会拦截
        if(user == null){
            //返回字符串表示后面的方法会被拦截
            return "login";
        }
        //已登录不会拦截，后面方法正常执行即放行
        return invocation.invoke();
    }

}
