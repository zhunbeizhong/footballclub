package com.chzu.football.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chzu.football.dao.IUserDao;
import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.Post;
import com.chzu.football.entity.User;
import com.chzu.football.util.FastJsonUtil;
//一般是在会出现异常 的方法上加事务注解(前提要在配置文件中配置事务管理)
@Transactional
@Service(value="userServiceImpl")
public class UserServiceImpl implements IUserService
{   
    @Autowired
    @Qualifier(value="userDaoImpl")
    private IUserDao userDao;
    //注册
    public void regist(User user){
        userDao.regist(user);
    }
    //检查用户名是否已存在
    public List<User> checkUser(String username){
        return userDao.checkUser(username);
    }
    //登录
    public List<User> login(User user){
        return userDao.login(user);
    }
    //分页查询球员信息
    @Override
    public String findPlayers(int currentIndex) {
        // TODO Auto-generated method stub
        List<Player> listPlayers = userDao.findPlayers(currentIndex);
        String jsonObject = JSON.toJSONStringWithDateFormat(listPlayers,"yyyy-MM-dd");
        //System.out.println(jsonObject);
        return jsonObject;
    }
    //查询球员总记录数
    @Override
    public int findTotal() {
        // TODO Auto-generated method stub
        return userDao.findTotal();
    }
    //编辑个人信息页面渲染值
    @Override
    public User findUser(Integer id) {
        // TODO Auto-generated method stub
        return userDao.findUser(id);
    }
    @Override
    public void updateUser(User user) {
        // TODO Auto-generated method stub
        userDao.updateUser(user);
    }
    @Override
    public List<Goods> findGoods(int index) {
        // TODO Auto-generated method stub
        return userDao.findGoods(index);
    }
    @Override
    public Goods initGood(int id) {
        // TODO Auto-generated method stub
        return userDao.initGood(id);
    }
    @Override
    public int saveOrder(Order order) {
        // TODO Auto-generated method stub
        //增加容错性
        if(order!=null && order.getTime()!=null){
        Timestamp time = new Timestamp(order.getTime().getTime());
        order.setTime(time);
        return userDao.saveOrder(order);
        }
        return 0;
    }
    @Override
    public String findOrders(Integer id,Integer pageIndex,Integer pageSize) {
        // TODO Auto-generated method stub
        List<Order> listOrders = userDao.findOrders(id,pageIndex,pageSize);
        User user = userDao.findUser(id);
        List<JSONObject> listObject = new ArrayList<JSONObject>();
        for(Order list:listOrders){
            String jsonString = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd");
            JSONObject jsonObject = (JSONObject)JSONObject.parse(jsonString);
            jsonObject.put("userName", user.getUserName());
            Goods good = userDao.findGoodByGoodId(list.getGoodsId());
            jsonObject.put("goodName", good.getName());
            listObject.add(jsonObject);
        }      
        //return FastJsonUtil.toJSONString(listObject);
        return JSON.toJSONStringWithDateFormat(listObject,"yyyy-MM-dd");
    }
    @Override
    public Integer countTotal(Integer userId) {
        // TODO Auto-generated method stub
        return userDao.countTotal(userId);
    }
    @Override
    public int findTotalGoods() {
        // TODO Auto-generated method stub
        return userDao.findTotalGoods();
    }
    @Override
    public String findForum(int currentIndex) {
        // TODO Auto-generated method stub
        List<Forum> listForum = userDao.findForum(currentIndex);
        String jsonObject = JSON.toJSONStringWithDateFormat(listForum,"yyyy-MM-dd");
        //System.out.println(jsonObject);
        return jsonObject;
    }
    @Override
    public int findTotalForum() {
        // TODO Auto-generated method stub
        return userDao.findTotalForum();
    }
    @Override
    public String findForumAndPost(int forumId) {
        // TODO Auto-generated method stub
        List<List> list = userDao.findForumAndPost(forumId);
        return JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd");
    }
    @Override
    public void addPost(Post post) {
        // TODO Auto-generated method stub
        userDao.addPost(post);
    }
    
}
