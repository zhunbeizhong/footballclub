package com.chzu.football.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Post;
import com.chzu.football.entity.User;
import com.chzu.football.service.IUserService;
import com.chzu.football.util.FastJsonUtil;
import com.chzu.football.util.MD5Utils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller(value="userAction")
//每发一个请求就有一个action，默认是单例，所有要配置成多例
@Scope("prototype")
public class UserAction extends ActionSupport implements ModelDriven<User>{
    //记录日志的对象
    public static Logger log = Logger.getLogger(UserAction.class);
    //模型驱动，把前端页面传过来的值封装成对象要求前端name值和实体类属性值一致,且要new一个对象
    private User user = new User();
    //非注解方式需要set和get方法
    @Autowired
    @Qualifier(value="userServiceImpl")
    private IUserService userService;
    
    //判断是用户还是管理员登录
    private String role;
    @Override
    public User getModel() {
        // TODO Auto-generated method stub
        return user;
    }
    //struts2 方法是public且返回值为String或void(这种情况属于ajax，通过out.println会自动返回到原页面不需要返回string值)且不带参数
    //用户注册
    public String regist(){
        userService.regist(user);
        return "login";
    }
    //检查用户名是否已存在，若已存在是不能添加到数据库中
    public void checkUser(){
       log.info("检查用户名是否存在开始");
       HttpServletResponse response=ServletActionContext.getResponse();
       HttpServletRequest request = ServletActionContext.getRequest();
       String username=request.getParameter("username");
       response.setCharacterEncoding("utf-8");
       response.setContentType("text/html; charset=utf-8");
       PrintWriter out=null;
       List<User> listUser = userService.checkUser(username);
       System.out.println(listUser);
       //String jsonString = FastJsonUtil.toJSONString(listUser);
       if(listUser!=null && listUser.size()>0){
           log.info("用户名已存在");
           try {
               out=response.getWriter();
               //返回给前端的值就是json对象，不需要解析
               out.println("{\"result\":\"no\"}");
           }
           catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               log.debug(e);
           }
       }else{
           try {
               out=response.getWriter();
               out.println("{\"result\":\"yes\"}");
               log.info("用户名不存在，可以正常注册");
           }
           catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               log.debug(e);
           }
       }
       log.info("检查结束");
       //return NONE;
    }
    //登录
    public String login(){
        log.info("登录开始");
        //System.out.println("login");
        HttpSession session = ServletActionContext.getRequest().getSession();
        if("用户".equals(role)){
            List<User> listUser=userService.login(user);
            if(listUser!=null && listUser.size()>0){
                //登录成功后把信息保存到session中，方便权限验证，登出的时候用sessio.removeAttribute();
                //session.setAttribute("user", user.getUserName());
                //注意保存的一定时查询后的对象，否则登录失败session也有值，这样权限验证的错误账号也能访问后面的页面
                session.setAttribute("user", listUser.get(0));
                return "index";
            }
            return "login";
        } 
        if("管理员".equals(role)){
            if("admin".equals(user.getUserName())&&"admin".equals(user.getPassword())){
                user.setId(1);
                user.setUserName("admin");
                user.setPassword("admin");
                session.setAttribute("admin", user);
                return "index_admin";
            }
            if("root".equals(user.getUserName())&&"root".equals(user.getPassword())){
                user.setId(2);
                user.setUserName("root");
                user.setPassword("root");
                session.setAttribute("admin", user);
                return "index_admin";
            }      
            return "login";
        }
        log.info("登录结束");
        return "login";
    }
    //查询当前登录的用户名，在用户首页中展示用户名,ajax自动返回原页面不需要返回string值
    public void findCurrentUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = null;
        User currentUser = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
        try {
            out = response.getWriter();
            //实体类转换成json对象，传回前端的是json对象
            String jsonString = FastJsonUtil.toJSONString(currentUser);
            //System.out.println(jsonString);
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } 
    //退出登录
    public String exitLogin(){
        ServletActionContext.getRequest().getSession().removeAttribute("user");
        return "login";
    }
    //分页查看球员信息
    public void findPlayers(){
        String currentIndexStr = ServletActionContext.getRequest().getParameter("currentIndexOf");
        int currentIndex=0;
        //增加容错
        if(currentIndexStr!=null && !currentIndexStr.trim().isEmpty()){
            currentIndex=Integer.parseInt(currentIndexStr);
        }
        String jsonObject = userService.findPlayers(currentIndex);
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            out = response.getWriter();
            out.println(jsonObject);           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //查询球员总记录数
    public void findTotal(){
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int total = userService.findTotal();
        //System.out.println(total);
        try {
            out = response.getWriter();
            out.println("{\"total\":"+total+"}");           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //编辑个人信息页面渲染值
    //因为这个方法是登录后才会执行所以拦截器中不要排除这个方法
    public void findUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String userId = request.getParameter("userId");
        Integer id = Integer.parseInt(userId);
        User user = userService.findUser(id);
        String jsonObject = FastJsonUtil.toJSONString(user);
        try {
            out=response.getWriter();
            out.println(jsonObject);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //修改个人信息,拦截器不需要排除
    public void updateUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String jsonString = request.getParameter("formData");
        JSONObject json = (JSONObject)JSON.parse(jsonString);
        //json.put(key, value);
        user=JSON.toJavaObject(json, User.class);
        //System.out.println(user.getPassword()+user.getUserName());
        userService.updateUser(user);
        //return "index";
        try {
            out=response.getWriter();
            out.println("{\"msg\":\"ok\"}");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //查询球衣
    public void findGoods(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String currentIndex = request.getParameter("currentIndexOf");
        int index = Integer.parseInt(currentIndex);
        List<Goods> listGoods =  userService.findGoods(index);
        String jsonString = null;
        //增加容错性
        if(listGoods!=null && listGoods.size()>0){
            jsonString = FastJsonUtil.toJSONString(listGoods);
        }
        
        try {
            out=response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //初始化订单填写页面的商品名称,登录才能到订单填写页面，不要排除拦截
    public void initGood(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String goodId = request.getParameter("goodId");
        int id = Integer.parseInt(goodId);
        Goods good = userService.initGood(id);
        String jsonString = null;
        //增加容错性
        if(good!=null){
            jsonString = FastJsonUtil.toJSONString(good);
        }
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
    }
    //保存订单信息，不需要排除拦截
    public void saveOrder(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String jsonString = request.getParameter("formData");
        JSONObject json = (JSONObject)JSON.parse(jsonString);
        Order order = JSON.toJavaObject(json, Order.class);
        String time = request.getParameter("datetime");
        //time=time.replace("GMT", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);
        try {
            if(time!=null){
                order.setTime(format.parse(time));
            }
           
        }
        catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int result = userService.saveOrder(order);
        
        try {
            out = response.getWriter();
            if(result>0){
                out.println("{\"result\":\"yes\"}");
            }else{
                out.println("{\"result\":\"no\"}");
            }
           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    //查看订单信息
    public void findOrders(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String pageIndexStr = request.getParameter("pageIndex");
        Integer pageIndex = null;
        if(pageIndexStr!=null && !pageIndexStr.trim().equals("")){
            pageIndex = Integer.parseInt(pageIndexStr);
        }
        String pageSizeStr = request.getParameter("pageSize");
        Integer pageSize = null;
        if(pageSizeStr!=null && !pageSizeStr.trim().equals("")){
           pageSize  = Integer.parseInt(pageSizeStr);
        }
        User user = (User)session.getAttribute("user");
        //订单数量可能很大，所以后期更换成Long类型
        Integer userId = user.getId();
        //data表示分页数据
        String data = userService.findOrders(userId,pageIndex,pageSize);   
        //count表示总数
        Integer count = userService.countTotal(userId);
        try {
            out = response.getWriter();
            out.println("{data:"+data+",total:"+count+"}");
            //out.println("total:"+count);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //查询所有的商品数量，注意要排除拦截
    public void findTotalGoods(){
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int total = userService.findTotalGoods();
        //System.out.println(total);
        try {
            out = response.getWriter();
            out.println("{\"total\":"+total+"}");           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
  //分页查看论坛帖子信息
    public void findForum(){
        String currentIndexStr = ServletActionContext.getRequest().getParameter("currentIndexOf");
        int currentIndex=0;
        //增加容错
        if(currentIndexStr!=null && !currentIndexStr.trim().isEmpty()){
            currentIndex=Integer.parseInt(currentIndexStr);
        }
        String jsonObject = userService.findForum(currentIndex);
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            out = response.getWriter();
            out.println(jsonObject);           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //查询帖子总记录数
    public void findTotalForum(){
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int total = userService.findTotalForum();
        //System.out.println(total);
        try {
            out = response.getWriter();
            out.println("{\"total\":"+total+"}");           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //查询帖子和回复
    public void findForumAndPost(){
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int forumId = 0;
        String forumIdStr = ServletActionContext.getRequest().getParameter("forumId");
        if(forumIdStr!=null && !forumIdStr.trim().equals("")){
            forumId = Integer.parseInt(forumIdStr);
        }
        String jsonObject = userService.findForumAndPost(forumId);
        try {
            out = response.getWriter();
            out.println(jsonObject);           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //回复帖子的添加
    public String addPost(){
        PrintWriter out=null;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int forumId = 0;
        String forumIdStr = ServletActionContext.getRequest().getParameter("forumId");
        if(forumIdStr!=null && !forumIdStr.trim().equals("")){
            forumId = Integer.parseInt(forumIdStr);
        }
        String content = ServletActionContext.getRequest().getParameter("content");
        User user = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
        String username = user.getUserName();
        Post post = new Post();//一般规范不用new,习惯用IoC配置文件注入或者在实体类和action中加上注解，这里毕设为了方便 
        post.setContent(content);
        post.setFormId(forumId);
        post.setUsername(username);
        //id自增不需要设置
        userService.addPost(post);
        try {
            out = response.getWriter();
            out.println("{\"result\":\"yes\"}");           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}