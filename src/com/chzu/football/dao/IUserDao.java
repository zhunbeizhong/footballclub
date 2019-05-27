package com.chzu.football.dao;

import java.util.List;

import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.Post;
import com.chzu.football.entity.User;

public interface IUserDao
{
    //注册
    public void regist(User user);
    //检查用户名是否已存在
    public List<User> checkUser(String username);
    //登录
    public List<User> login(User user);
    //分页查询球员信息
    public List<Player> findPlayers(int currentIndex);
    //查询球员总记录数
    public int findTotal();
    //编辑个人信息页面渲染值
    public User findUser(Integer id);
    public void updateUser(User user);
    public List<Goods> findGoods(int index);
    public Goods initGood(int id);
    public int saveOrder(Order order);
    public List<Order> findOrders(Integer userId,Integer pageIndex,Integer pageSize);
    public Goods findGoodByGoodId(Integer goodId);
    public Integer countTotal(Integer userId);
    public int findTotalGoods();
    public List<Forum> findForum(int currentIndex);
    public int findTotalForum();
    public List<List> findForumAndPost(int forumId);
    public void addPost(Post post);
}
