package com.chzu.football.controller;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.User;
import com.chzu.football.service.IAdminService;
import com.chzu.football.util.FastJsonUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller(value="adminAction")
//每发一个请求就有一个action，默认是单例，所有要配置成多例
@Scope("prototype")
public class AdminAction extends ActionSupport implements ModelDriven<Player>{
    //记录日志对象
    public static Logger log = Logger.getLogger(AdminAction.class);
    
    //引入service
    @Autowired
    @Qualifier(value="adminServiceImpl")
    private IAdminService adminService;
    /*ajax直接返回的到原页面，返回值不用写成String类型直接void即可，
    springmvc的ajax一样会返回到原页面，只不过springmvc返回一个实体然后通过responsebody注解前端可以直接拿到json，
            而struct2需要通过out.println返回json
    */
    private File upload;
    private String uploadFileName;
    private Player player = new Player();
    
    private File uploadGood;
    private String uploadGoodFileName;
    private Goods goods;
    private Order order;
    private Forum forum;
    @Override
    public Player getModel() {
        // TODO Auto-generated method stub
        return player;
    }
    public void findCurrentAdmin(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = null;
        User currentAdmin = (User)ServletActionContext.getRequest().getSession().getAttribute("admin");
        try {
            out = response.getWriter();
            //实体类转换成json对象，传回前端的是json对象
            String jsonString = FastJsonUtil.toJSONString(currentAdmin);
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
        ServletActionContext.getRequest().getSession().removeAttribute("admin");
        return "login";
    }
    //分页显示所有的用户
    public void findAllUser(){
        log.info("开始分页查询所有用户");
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
        String data = adminService.findAllUser(pageIndex,pageSize);
        Long count=adminService.findTotalUser();
        try {
            out = response.getWriter();
            out.println("{data:"+data+",total:"+count+"}");
            //out.println("total:"+count);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.debug(e);
        }
        log.info("查询结束");
    }
    //管理员添加用户
    public void addUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String jsonString = request.getParameter("formData");
        JSONObject json = (JSONObject)JSON.parse(jsonString);
        User user = JSON.toJavaObject(json, User.class);
        int result = adminService.addUser(user);
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
    //删除用户
    public void deleteUser(){
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = null;
            HttpServletRequest request = ServletActionContext.getRequest();
            String ids = request.getParameter("id");
            String[] idArray = ids.split(",");
            adminService.deleteUser(idArray);
            try {
                out = response.getWriter();
                out.println("{\"result\":\"yes\"}");
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    //初始化修改页面的值
    public void initUser(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String idStr = request.getParameter("userId");
        Integer id = null;
        if(idStr!=null && !idStr.trim().equals("")){
            id = Integer.valueOf(idStr);
        }
        User user = adminService.initUser(id);
        String jsonString =  FastJsonUtil.toJSONString(user);
        try {
            out = response.getWriter();
            out.println(jsonString);
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
        User user=JSON.toJavaObject(json, User.class);
        //System.out.println(user.getPassword()+user.getUserName());
        adminService.updateUser(user);
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
    //分页查询所有球员
    public void findAllPlayer(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
       
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
        //分页的数据
        String data = adminService.findAllPlayerByName(pageIndex,pageSize,null);
        //总记录数
        Long count = adminService.findTotalPlayerByName(null);
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
    //分页查询所有球员包括是否根据球员信息模糊查询（原生的分页查询是写一个pojo为pageBean）
    public void findAllPlayerByName(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String key = request.getParameter("key");
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
        //分页的数据
        String data = adminService.findAllPlayerByName(pageIndex,pageSize,key);
        //总记录数
        Long count = adminService.findTotalPlayerByName(key);
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
    //删除运动员
    public void deletePlayer(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String ids = request.getParameter("id");
        String[] idArray = ids.split(",");
        adminService.deletePlayer(idArray);
        try {
            out = response.getWriter();
            out.println("{\"result\":\"yes\"}");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //新增球员,此时已登录不需要排除拦截
    public String addPlayer(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
       
        /*String jsonString = request.getParameter("formData");
        JSONObject json = (JSONObject)JSON.parse(jsonString);
        Player player = JSON.toJavaObject(json, Player.class);*/
        //为点击上传按钮或未执行上传文件直接执行确定按钮到ajax请求，相当于未上传图片，所以可以不设置photoAdd，此字段允许为空
        if(uploadFileName!=null && !uploadFileName.trim().equals("")){
            player.setPhotoAdd("../images/"+uploadFileName);
            String realPath = "F:/workspaces/eclipse 4.6/footballClub/WebContent/images/"+uploadFileName;
            File file = new File(realPath);
           
            try {
                if(!file.exists()){
                    file.createNewFile();
                }
                FileUtils.copyFile(upload, file);
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        int result = adminService.addPlayer(player);
        return "managerPlayer";
    }
  //初始化球员修改页面的值
    public void initPlayer(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String idStr = request.getParameter("playerId");
        Integer id = null;
        if(idStr!=null && !idStr.trim().equals("")){
            id = Integer.valueOf(idStr);
        }
        Player player = adminService.initPlayer(id);
        String jsonString =  JSON.toJSONStringWithDateFormat(player, "yyyy-MM-dd");
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //修改球员信息
    public String updatePlayer(){
       
        /*前端页面已经设置了一个隐藏域name值为photoAdd对应图片地址属性的name值，防止修改时为上传图片而导致原图片地址修改后表单中没有对应的值而导致为null，
                            设置隐藏域为了修改后不上传图片仍能取到原图片的地址*/
        if(uploadFileName!=null && !uploadFileName.trim().equals("")){
            player.setPhotoAdd("../images/"+uploadFileName);
            String realPath = "F:/workspaces/eclipse 4.6/footballClub/WebContent/images/"+uploadFileName;
            File file = new File(realPath);
           
            try {
                if(!file.exists()){
                    file.createNewFile();
                }
                FileUtils.copyFile(upload, file);
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        //前端页面已经设置了id为隐藏域，没有id就不知道更新那条记录，根据id更新所以必须有id值，如果不想显示就设置为隐藏域   
        adminService.updatePlayer(player);
        return "managerPlayer";
    }
    
    //分页查询所有商品包括根据商品名称分页查询
    public void findAllGoodsByName(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String key = request.getParameter("key");
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
        //分页的数据
        String data = adminService.findAllGoodsByName(pageIndex,pageSize,key);
        //总记录数
        Long count = adminService.findTotalGoodsByName(key);
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
  //删除商品
    public void deleteGood(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String ids = request.getParameter("id");
        String[] idArray = ids.split(",");
        adminService.deleteGood(idArray);
        try {
            out = response.getWriter();
            out.println("{\"result\":\"yes\"}");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //新增商品,此时已登录不需要排除拦截
    public String addGood(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        //为点击上传按钮或未执行上传文件直接执行确定按钮到ajax请求，相当于未上传图片，所以可以不设置photoAdd，此字段允许为空
        if(uploadGoodFileName!=null && !uploadGoodFileName.trim().equals("")){
            goods.setPhotoAdd("../images/"+uploadGoodFileName);
            String realPath = "F:/workspaces/eclipse 4.6/footballClub/WebContent/images/"+uploadGoodFileName;
            File file = new File(realPath);
           
            try {
                if(!file.exists()){
                    file.createNewFile();
                }
                FileUtils.copyFile(uploadGood, file);
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        int result = adminService.addGood(goods);
        return "managerGoods";
    }
  //初始化球员修改页面的值
    public void initGood(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String idStr = request.getParameter("goodId");
        Integer id = null;
        if(idStr!=null && !idStr.trim().equals("")){
            id = Integer.valueOf(idStr);
        }
        Goods good = adminService.initGood(id);
        String jsonString =  JSON.toJSONString(good);
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //修改球员信息
    public String updateGood(){
       
        /*前端页面已经设置了一个隐藏域name值为photoAdd对应图片地址属性的name值，防止修改时为上传图片而导致原图片地址修改后表单中没有对应的值而导致为null，
                            设置隐藏域为了修改后不上传图片仍能取到原图片的地址*/
        if(uploadGoodFileName!=null && !uploadGoodFileName.trim().equals("")){
            goods.setPhotoAdd("../images/"+uploadGoodFileName);
            String realPath = "F:/workspaces/eclipse 4.6/footballClub/WebContent/images/"+uploadGoodFileName;
            File file = new File(realPath);
           
            try {
                if(!file.exists()){
                    file.createNewFile();
                }
                FileUtils.copyFile(uploadGood, file);
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        //前端页面已经设置了id为隐藏域，没有id就不知道更新那条记录，根据id更新所以必须有id值，如果不想显示就设置为隐藏域   
        adminService.updateGood(goods);
        return "managerGoods";
    }
    
    //分页查询所有订单包括根据用户名分页查询
    public void findAllOrderByName(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String key = request.getParameter("key");
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
        //分页的数据
        String data = adminService.findAllOrderByName(pageIndex,pageSize,key);
        //总记录数
        Long count = adminService.findTotalOrderByName(key);
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
  //删除订单
    public void deleteOrder(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String ids = request.getParameter("id");
        String[] idArray = ids.split(",");
        adminService.deleteOrder(idArray);
        try {
            out = response.getWriter();
            out.println("{\"result\":\"yes\"}");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //初始化球员修改页面的值
    public void initOrder(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String idStr = request.getParameter("orderId");
        Integer id = null;
        if(idStr!=null && !idStr.trim().equals("")){
            id = Integer.valueOf(idStr);
        }
        String jsonString = adminService.initOrder(id);
      
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //修改订单
    public String updateOrder(){
        System.out.println(order);
        adminService.updateOrder(order);
        return "managerOrder";
    }
    //商品信息统计，柱状图显示每个分类的数量
    public void countGoods(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String jsonString = adminService.countGoods();
      
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //柱状图显示不同球员位置的各个年龄段人数
    public String countPlayer(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String jsonString = adminService.countPlayer();
      
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    //分页查询所有论坛帖子
    public void findAllForum(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
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
        //分页的数据
        String data = adminService.findAllForum(pageIndex,pageSize);
        //总记录数
        Long count = adminService.findTotalForum();
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
  //删除论坛
    public void deleteForum(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String ids = request.getParameter("id");
        String[] idArray = ids.split(",");
        adminService.deleteForum(idArray);
        try {
            out = response.getWriter();
            out.println("{\"result\":\"yes\"}");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //新增发布论坛帖子
    public String addForum(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();

        int result = adminService.addForum(forum);
        return "managerCommunity";
    }
  //初始化球员修改页面的值
    public void initForum(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        String idStr = request.getParameter("forumId");
        Integer id = null;
        if(idStr!=null && !idStr.trim().equals("")){
            id = Integer.valueOf(idStr);
        }
        Forum forum = adminService.initForum(id);
        String jsonString =  JSON.toJSONStringWithDateFormat(forum, "yyyy-MM-dd");
        try {
            out = response.getWriter();
            out.println(jsonString);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //修改帖子信息
    public String updateForum(){
        //前端页面已经设置了id为隐藏域，没有id就不知道更新那条记录，根据id更新所以必须有id值，如果不想显示就设置为隐藏域   
        adminService.updateForum(forum);
        return "managerCommunity";
    }
    
    public File getUpload() {
        return upload;
    }
    public void setUpload(File upload) {
        this.upload = upload;
    }
    public String getUploadFileName() {
        return uploadFileName;
    }
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
    public File getUploadGood() {
        return uploadGood;
    }
    public void setUploadGood(File uploadGood) {
        this.uploadGood = uploadGood;
    }
    public String getUploadGoodFileName() {
        return uploadGoodFileName;
    }
    public void setUploadGoodFileName(String uploadGoodFileName) {
        this.uploadGoodFileName = uploadGoodFileName;
    }
    public Goods getGoods() {
        return goods;
    }
    public void setGoods(Goods goods) {
        this.goods = goods;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Forum getForum() {
        return forum;
    }
    public void setForum(Forum forum) {
        this.forum = forum;
    }
   
}