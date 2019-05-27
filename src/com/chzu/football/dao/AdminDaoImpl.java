package com.chzu.football.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.User;
import com.chzu.football.util.MD5Utils;
@Repository(value="adminDaoImpl")
public class AdminDaoImpl extends HibernateDaoSupport implements IAdminDao
{

    @Override
    public List<User> findAllUser(Integer pageIndex, Integer pageSize) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        return (List<User>)this.getHibernateTemplate().findByCriteria(criteria, pageIndex*pageSize, pageSize);
    }

    @Override
    public Long findTotalUser() {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.setProjection(Projections.rowCount());
        List<Long> count = (List<Long>)this.getHibernateTemplate().findByCriteria(criteria);
        return count.get(0);
    }

    @Override
    public int addUser(User user) {
        // TODO Auto-generated method stub
        user.setPassword(MD5Utils.md5(user.getPassword()));
        return (int)this.getHibernateTemplate().save(user);
    }

    @Override
    public void deleteUser(String[] idArray) {
        // TODO Auto-generated method stub
        List<User> listUser = new ArrayList<>();
        for(String idStr:idArray){
            Integer id = Integer.valueOf(idStr);
            listUser.add(this.getHibernateTemplate().get(User.class, id));
        }
        this.getHibernateTemplate().deleteAll(listUser);
    }

    @Override
    public User initUser(Integer id) {
        // TODO Auto-generated method stub
       return this.getHibernateTemplate().get(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        // TODO Auto-generated method stub
        user.setPassword(MD5Utils.md5(user.getPassword()));
        this.getHibernateTemplate().update(user);   
    }

    @Override
    public List<Player> findAllPlayerByName(Integer pageIndex, Integer pageSize, String key) {
        // TODO Auto-generated method stub
        //String hql="from Player limit pageIndex,pageSize where name like '%"+key+"%'";
        DetachedCriteria criteria = DetachedCriteria.forClass(Player.class);
        if(key!=null && !key.trim().equals("")){
            //criteria.add(Restrictions.ilike("name", key));
            criteria.add(Restrictions.like("name", key,MatchMode.ANYWHERE));
        }
        
        return (List<Player>)this.getHibernateTemplate().findByCriteria(criteria, pageIndex*pageSize, pageSize);
    }

    @Override
    public Long findTotalPlayerByName(String key) {
        // TODO Auto-generated method stub
        String hql = "select count(p.id) from Player p";
        if(key!=null && !key.trim().equals("")){
            hql=hql+" where p.name like '%"+key+"%'";      
        }
        List<Long> count = (List<Long>)this.getHibernateTemplate().find(hql);
        return count.get(0);
    }

    @Override
    public void deletePlayer(String[] idArray) {
        // TODO Auto-generated method stub
        List<Player> listPlayer = new ArrayList<>();
        for(String idStr:idArray){
            Integer id = Integer.valueOf(idStr);
            listPlayer.add(this.getHibernateTemplate().get(Player.class, id));
        }
        this.getHibernateTemplate().deleteAll(listPlayer);
    }

    @Override
    public int addPlayer(Player player) {
        // TODO Auto-generated method stub
        return (int)this.getHibernateTemplate().save(player);
    }

    @Override
    public Player initPlayer(Integer id) {
        // TODO Auto-generated method stub
        return this.getHibernateTemplate().get(Player.class, id);
    }

    @Override
    public void updatePlayer(Player player) {
        // TODO Auto-generated method stub
        this.getHibernateTemplate().update(player);
    }

    @Override
    public List<Goods> findAllGoodsByName(Integer pageIndex, Integer pageSize, String key) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Goods.class);
        if(key!=null && !key.trim().equals("")){
            criteria.add(Restrictions.like("name", key, MatchMode.ANYWHERE));
        }
        return (List<Goods>)this.getHibernateTemplate().findByCriteria(criteria, pageIndex*pageSize, pageSize);
    }

    @Override
    public Long findTotalGoodsByName(String key) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Goods.class);
        if(key!=null && !key.trim().equals("")){
            criteria.add(Restrictions.like("name", key, MatchMode.ANYWHERE));
        }
        criteria.setProjection(Projections.rowCount());
        List<Long> total = (List<Long>)this.getHibernateTemplate().findByCriteria(criteria);
        return total.get(0);
    }

    @Override
    public void deleteGood(String[] idArray) {
        // TODO Auto-generated method stub
        List<Goods> listGoods = new ArrayList<>();
        for(String idStr:idArray){
            Integer id = Integer.valueOf(idStr);
            listGoods.add(this.getHibernateTemplate().get(Goods.class, id));
        }
        this.getHibernateTemplate().deleteAll(listGoods);
    }

    @Override
    public int addGood(Goods goods) {
        // TODO Auto-generated method stub
        return (int)this.getHibernateTemplate().save(goods);
    }

    @Override
    public Goods initGood(Integer id) {
        // TODO Auto-generated method stub
        return this.getHibernateTemplate().get(Goods.class, id);
    }

    @Override
    public void updateGood(Goods good) {
        // TODO Auto-generated method stub
        this.getHibernateTemplate().update(good);
    }

    @Override
    public String findAllOrderByName(Integer pageIndex, Integer pageSize, String key) {
        // TODO Auto-generated method stub
        //注意hql不支持limit，且this.getHibernateTemplete不支持sql，所以以下必须使用criteria进行子查询
       /* String hql = "from Order o limit "+pageIndex+","+pageSize;
        if(key!=null && !key.trim().equals("")){
            hql = hql+" where o.userId = (select u.id from User u where u.userName="+key+")";
        }*/
        DetachedCriteria orderCriteria = DetachedCriteria.forClass(Order.class);
        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class);
        if(key!=null && !key.trim().equals("")){
            userCriteria.add(Restrictions.like("userName", key,MatchMode.ANYWHERE));
          //setProjection除了可以用于聚合函数查询外，还有一种用处是用于子查询，前者参数是Projections调用聚合函数，后者参数是指定某列，指定某列用Property.forName()
            userCriteria.setProjection(Property.forName("id"));
            //模糊查询是多条用in 如果根据用户名精确查找一个用户名就一个id用eq
            orderCriteria.add(Property.forName("userId").in(userCriteria));
        }
       
        List<Order> listOrders = (List<Order>)this.getHibernateTemplate().findByCriteria(orderCriteria, pageIndex*pageSize, pageSize);
        List<JSONObject> listObject = new ArrayList<>();
        for(Order order:listOrders){
            String jsonString = JSON.toJSONStringWithDateFormat(order, "yyyy-MM-dd");
            JSONObject jsonObject = (JSONObject)JSON.parse(jsonString);
            User user = this.getHibernateTemplate().get(User.class, order.getUserId());
            Goods good =  this.getHibernateTemplate().get(Goods.class, order.getGoodsId());
            jsonObject.put("userName", user.getUserName());
            jsonObject.put("goodName", good.getName());
            listObject.add(jsonObject);
        }
        return JSON.toJSONStringWithDateFormat(listObject,"yyyy-MM-dd");
    }

    @Override
    public Long findTotalOrderByName(String key) {
        // TODO Auto-generated method stub
        String hql = "select count(o.id) from Order o";
        if(key!=null && !key.trim().equals("")){
            hql = hql+" where o.userId in (select u.id from User u where u.userName like '%"+key+"%')";
        }
        List<Long> count = (List<Long>)this.getHibernateTemplate().find(hql);
        return count.get(0);
    }

    @Override
    public String initOrder(Integer id) {
        // TODO Auto-generated method stub
        Order order = this.getHibernateTemplate().get(Order.class, id);
        User user = this.getHibernateTemplate().get(User.class, order.getUserId());
        Goods good = this.getHibernateTemplate().get(Goods.class, order.getGoodsId());
        String jsonString = JSON.toJSONStringWithDateFormat(order,"yyyy-MM-dd");
        JSONObject jsonObject = (JSONObject)JSON.parse(jsonString);
        jsonObject.put("username", user.getUserName());
        jsonObject.put("goodname", good.getName());
        return JSON.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd");
    }

    @Override
    public void updateOrder(Order order) {
        // TODO Auto-generated method stub
        
        this.getHibernateTemplate().update(order);
    }

    @Override
    public void deleteOrder(String[] idArray) {
        // TODO Auto-generated method stub
        List<Order> listOrder = new ArrayList<>();
        for(String idStr:idArray){
            Integer id = Integer.valueOf(idStr);
            listOrder.add(this.getHibernateTemplate().get(Order.class, id));
        }
        this.getHibernateTemplate().deleteAll(listOrder);
    }

    @Override
    public List<List> countGoods() {
        // TODO Auto-generated method stub
        List<List> countGoods = new ArrayList<>();
        List<Long> countFrock = new ArrayList<>();//球衣各颜色的数量
        List<Long> countPants = new ArrayList<>();//球裤各颜色的数量
        List<Long> countShots = new ArrayList<>();//球鞋各颜色的数量
        List<String> colorOfGoods = new ArrayList<>();//商品的颜色
        colorOfGoods.add("黑色");
        colorOfGoods.add("红色");
        colorOfGoods.add("粉红色");
        colorOfGoods.add("绿色");
        colorOfGoods.add("白色");
        colorOfGoods.add("蓝色");
        colorOfGoods.add("橙色");
        colorOfGoods.add("紫色");
        colorOfGoods.add("黄色");
        for(String color:colorOfGoods){
            DetachedCriteria criteriaOfFrock = DetachedCriteria.forClass(Goods.class);
            //criteria.add(Restrictions.eq("color", "黑色"));
            //criteria.add(Restrictions.eq("breed", "球衣"));
            criteriaOfFrock.add(Restrictions.and(Restrictions.eq("color", color),Restrictions.eq("breed", "球衣")));
            criteriaOfFrock.setProjection(Projections.rowCount());
            List<Long> frock = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfFrock);
            countFrock.add(frock.get(0));
            
            DetachedCriteria criteriaOfPants = DetachedCriteria.forClass(Goods.class);
            criteriaOfPants.add(Restrictions.and(Restrictions.eq("color", color),Restrictions.eq("breed", "球裤")));
            criteriaOfPants.setProjection(Projections.rowCount());
            List<Long> pants = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfPants);
            countPants.add(pants.get(0));
            
            DetachedCriteria criteriaOfShots = DetachedCriteria.forClass(Goods.class);
            criteriaOfShots.add(Restrictions.and(Restrictions.eq("color", color),Restrictions.eq("breed", "球鞋")));
            criteriaOfShots.setProjection(Projections.rowCount());
            List<Long> shots = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfShots);
            countShots.add(shots.get(0));
        }
        countGoods.add(countFrock);
        countGoods.add(countPants);
        countGoods.add(countShots);
        return countGoods;
    }

    @Override
    public List<List> countPlayer() {
        // TODO Auto-generated method stub
        List<List> countPlayer = new ArrayList<>();
        List<Long> countForward = new ArrayList<>();//前锋各年龄段的人数
        List<Long> countFullback = new ArrayList<>();//后卫各年龄段的人数
        List<Long> countMidfied = new ArrayList<>();//中场各年龄段的人数
        List<Long> countKeeper = new ArrayList<>();//守门员各年龄段的人数
        List<Integer> ageOfPlayer = new ArrayList<>();//球员的年龄
        for(int i=16;i<=47;i++){
            ageOfPlayer.add(i);
        }
        if(ageOfPlayer!=null && ageOfPlayer.size()>0){
            for(int i=0;i<ageOfPlayer.size();i=i+4){
                DetachedCriteria criteriaOfForward = DetachedCriteria.forClass(Player.class);
                criteriaOfForward.add(Restrictions.and(Restrictions.in("age", new Integer[]{ageOfPlayer.get(i),ageOfPlayer.get(i+1),ageOfPlayer.get(i+2),ageOfPlayer.get(i+3)}),Restrictions.eq("role", "前锋")));
                criteriaOfForward.setProjection(Projections.rowCount());
                List<Long> countOfForward = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfForward);
                countForward.add(countOfForward.get(0));
                
                DetachedCriteria criteriaOfFullback = DetachedCriteria.forClass(Player.class);
                criteriaOfFullback.add(Restrictions.and(Restrictions.in("age", new Integer[]{ageOfPlayer.get(i),ageOfPlayer.get(i+1),ageOfPlayer.get(i+2),ageOfPlayer.get(i+3)}),Restrictions.eq("role", "后卫")));
                criteriaOfFullback.setProjection(Projections.rowCount());
                List<Long> countOfFullback = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfFullback);
                countFullback.add(countOfFullback.get(0));
                
                DetachedCriteria criteriaOfMidfied = DetachedCriteria.forClass(Player.class);
                criteriaOfMidfied.add(Restrictions.and(Restrictions.in("age", new Integer[]{ageOfPlayer.get(i),ageOfPlayer.get(i+1),ageOfPlayer.get(i+2),ageOfPlayer.get(i+3)}),Restrictions.eq("role", "中场")));
                criteriaOfMidfied.setProjection(Projections.rowCount());
                List<Long> countOfMidfied = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfMidfied);
                countMidfied.add(countOfMidfied.get(0));
                
                DetachedCriteria criteriaOfKeeper = DetachedCriteria.forClass(Player.class);
                criteriaOfKeeper.add(Restrictions.and(Restrictions.in("age", new Integer[]{ageOfPlayer.get(i),ageOfPlayer.get(i+1),ageOfPlayer.get(i+2),ageOfPlayer.get(i+3)}),Restrictions.eq("role", "守门员")));
                criteriaOfKeeper.setProjection(Projections.rowCount());
                List<Long> countOfKeeper = (List<Long>)this.getHibernateTemplate().findByCriteria(criteriaOfKeeper);
                countKeeper.add(countOfKeeper.get(0));
            }
        }
        countPlayer.add(countForward);
        countPlayer.add(countFullback);
        countPlayer.add(countMidfied);
        countPlayer.add(countKeeper);
        return countPlayer;
    }

    @Override
    public List<Forum> findAllForum(Integer pageIndex, Integer pageSize) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Forum.class);
        return (List<Forum>)this.getHibernateTemplate().findByCriteria(criteria, pageIndex*pageSize, pageSize);
    }

    @Override
    public Long findTotalForum() {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Forum.class);
        criteria.setProjection(Projections.rowCount());
        List<Long> total = (List<Long>)this.getHibernateTemplate().findByCriteria(criteria);
        return total.get(0);
    }

    @Override
    public void deleteForum(String[] idArray) {
        // TODO Auto-generated method stub
        List<Forum> listForum = new ArrayList<>();
        for(String idStr:idArray){
            Integer id = Integer.valueOf(idStr);
            listForum.add(this.getHibernateTemplate().get(Forum.class, id));
        }
        this.getHibernateTemplate().deleteAll(listForum);
    }

    @Override
    public int addForum(Forum forum) {
        // TODO Auto-generated method stub
        return (int)this.getHibernateTemplate().save(forum);
    }

    @Override
    public Forum initForum(Integer id) {
        // TODO Auto-generated method stub
        return this.getHibernateTemplate().get(Forum.class, id);
    }

    @Override
    public void updateForum(Forum forum) {
        // TODO Auto-generated method stub
        this.getHibernateTemplate().update(forum);
    }

}
