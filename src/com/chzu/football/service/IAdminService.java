package com.chzu.football.service;

import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.User;

public interface IAdminService{
   public String findAllUser(Integer pageIndex, Integer pageSize);
   public Long findTotalUser();
   public int addUser(User user);
   public void deleteUser(String[] idArray);
   public User initUser(Integer id);
   public void updateUser(User user);
   public String findAllPlayerByName(Integer pageIndex, Integer pageSize, String key);
   public Long findTotalPlayerByName(String key);
public void deletePlayer(String[] idArray);
public int addPlayer(Player player);
public Player initPlayer(Integer id);
public void updatePlayer(Player player);
public String findAllGoodsByName(Integer pageIndex, Integer pageSize, String key);
public Long findTotalGoodsByName(String key);
public void deleteGood(String[] idArray);
public int addGood(Goods goods);
public Goods initGood(Integer id);
public void updateGood(Goods good);
public String findAllOrderByName(Integer pageIndex, Integer pageSize, String key);
public Long findTotalOrderByName(String key);
public String initOrder(Integer id);
public void updateOrder(Order order);
public void deleteOrder(String[] idArray);
public String countGoods();
public String countPlayer();
public String findAllForum(Integer pageIndex, Integer pageSize);
public Long findTotalForum();
public void deleteForum(String[] idArray);
public int addForum(Forum forum);
public Forum initForum(Integer id);
public void updateForum(Forum forum);
   
}
