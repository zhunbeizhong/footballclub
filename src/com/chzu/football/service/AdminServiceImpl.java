package com.chzu.football.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.chzu.football.dao.IAdminDao;
import com.chzu.football.dao.UserDaoImpl;
import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.User;
import com.chzu.football.util.FastJsonUtil;

@Transactional
@Service(value="adminServiceImpl")
public class AdminServiceImpl implements IAdminService
{
    @Autowired
    @Qualifier(value="adminDaoImpl")
    private IAdminDao adminDao;

    @Override
    public String findAllUser(Integer pageIndex, Integer pageSize) {
        // TODO Auto-generated method stub
        List<User> findAllUser = adminDao.findAllUser(pageIndex,pageSize);
        return FastJsonUtil.toJSONString(findAllUser);
    }

    @Override
    public Long findTotalUser() {
        // TODO Auto-generated method stub
        return adminDao.findTotalUser();
    }

    @Override
    public int addUser(User user) {
        // TODO Auto-generated method stub
        return adminDao.addUser(user);
    }

    @Override
    public void deleteUser(String[] idArray) {
        // TODO Auto-generated method stub
        adminDao.deleteUser(idArray);
    }

    @Override
    public User initUser(Integer id) {
        // TODO Auto-generated method stub
        return adminDao.initUser(id);
    }

    @Override
    public void updateUser(User user) {
        // TODO Auto-generated method stub
        adminDao.updateUser(user);
    }

    @Override
    public String findAllPlayerByName(Integer pageIndex, Integer pageSize, String key) {
        // TODO Auto-generated method stub
        List<Player> listPlayers = adminDao.findAllPlayerByName(pageIndex,pageSize,key);
       // return FastJsonUtil.toJSONString(listPlayers);
        return JSON.toJSONStringWithDateFormat(listPlayers, "yyyy-MM-dd");
    }

    @Override
    public Long findTotalPlayerByName(String key) {
        // TODO Auto-generated method stub
        return adminDao.findTotalPlayerByName(key);
    }

    @Override
    public void deletePlayer(String[] idArray) {
        // TODO Auto-generated method stub
        adminDao.deletePlayer(idArray);
    }

    @Override
    public int addPlayer(Player player) {
        // TODO Auto-generated method stub
        return adminDao.addPlayer(player);
    }

    @Override
    public Player initPlayer(Integer id) {
        // TODO Auto-generated method stub
        return adminDao.initPlayer(id);
    }

    @Override
    public void updatePlayer(Player player) {
        // TODO Auto-generated method stub
        adminDao.updatePlayer(player);
    }

    @Override
    public String findAllGoodsByName(Integer pageIndex, Integer pageSize, String key) {
        // TODO Auto-generated method stub
        List<Goods> listGoods = adminDao.findAllGoodsByName(pageIndex,pageSize,key);
        return JSON.toJSONString(listGoods);
    }

    @Override
    public Long findTotalGoodsByName(String key) {
        // TODO Auto-generated method stub
        return adminDao.findTotalGoodsByName(key);
    }

    @Override
    public void deleteGood(String[] idArray) {
        // TODO Auto-generated method stub
        adminDao.deleteGood(idArray);
    }

    @Override
    public int addGood(Goods goods) {
        // TODO Auto-generated method stub
        return adminDao.addGood(goods);
    }

    @Override
    public Goods initGood(Integer id) {
        // TODO Auto-generated method stub
        return adminDao.initGood(id);
    }

    @Override
    public void updateGood(Goods good) {
        // TODO Auto-generated method stub
        adminDao.updateGood(good);
    }

    @Override
    public String findAllOrderByName(Integer pageIndex, Integer pageSize, String key) {
        // TODO Auto-generated method stub
        return adminDao.findAllOrderByName(pageIndex,pageSize,key);
    }

    @Override
    public Long findTotalOrderByName(String key) {
        // TODO Auto-generated method stub
        return adminDao.findTotalOrderByName(key);
    }

    @Override
    public String initOrder(Integer id) {
        // TODO Auto-generated method stub
        return adminDao.initOrder(id);
    }

    @Override
    public void updateOrder(Order order) {
        // TODO Auto-generated method stub
        adminDao.updateOrder(order);
    }

    @Override
    public void deleteOrder(String[] idArray) {
        // TODO Auto-generated method stub
        adminDao.deleteOrder(idArray);
    }

    @Override
    public String countGoods() {
        // TODO Auto-generated method stub
        List<List> countGoods=adminDao.countGoods();
        return FastJsonUtil.toJSONString(countGoods);
    }

    @Override
    public String countPlayer() {
        // TODO Auto-generated method stub
        List<List> countPlayer = adminDao.countPlayer();
        return FastJsonUtil.toJSONString(countPlayer);
    }

    @Override
    public String findAllForum(Integer pageIndex, Integer pageSize) {
        // TODO Auto-generated method stub
        List<Forum> listForums = adminDao.findAllForum(pageIndex,pageSize);
        return JSON.toJSONStringWithDateFormat(listForums, "yyyy-MM-dd");
    }

    @Override
    public Long findTotalForum() {
        // TODO Auto-generated method stub
        return adminDao.findTotalForum();
    }

    @Override
    public void deleteForum(String[] idArray) {
        // TODO Auto-generated method stub
        adminDao.deleteForum(idArray);
    }

    @Override
    public int addForum(Forum forum) {
        // TODO Auto-generated method stub
        return adminDao.addForum(forum);
    }

    @Override
    public Forum initForum(Integer id) {
        // TODO Auto-generated method stub
        return adminDao.initForum(id);
    }

    @Override
    public void updateForum(Forum forum) {
        // TODO Auto-generated method stub
        adminDao.updateForum(forum);
    }
}
