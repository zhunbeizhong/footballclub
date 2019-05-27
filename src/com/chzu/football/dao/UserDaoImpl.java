package com.chzu.football.dao;
/*package com.epoint.crm.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.epoint.crm.domain.User;

public class UserDaoImpl extends HibernateDaoSupport implements IUserDao
{

    @Override
    public void regist(User1 user) {
        this.getHibernateTemplate().save(user);
    }

    @Override
    public List<User1> checkCode(String user_code) {
        // TODO Auto-generated method stub
        List<User1> users=(List<User1>) this.getHibernateTemplate().find("from User where user_code=?",user_code);
        return users;
    }

    @Override
    public List<User1> login(User1 user) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(User1.class);
        //criteria此时相当于select *
        criteria.add(Restrictions.eq("user_code", user.getUser_code()));
        criteria.add(Restrictions.eq("user_password", user.getUser_password()));
        criteria.add(Restrictions.eq("user_state", user.getUser_state()));
        List<User1> users=(List<User1>) this.getHibernateTemplate().findByCriteria(criteria);
        return users;
    }

   
}
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.chzu.football.entity.Forum;
import com.chzu.football.entity.Goods;
import com.chzu.football.entity.Order;
import com.chzu.football.entity.Player;
import com.chzu.football.entity.Post;
import com.chzu.football.entity.User;
import com.chzu.football.util.MD5Utils;
import com.opensymphony.xwork2.security.DefaultAcceptedPatternsChecker;
@Repository(value="userDaoImpl")
public class UserDaoImpl extends HibernateDaoSupport implements IUserDao{
    //注册
    @Override
    public void regist(User user) {
        //对密码进行加密
       user.setPassword(MD5Utils.md5(user.getPassword()));
       user.setSex("1");
       Serializable serializable = this.getHibernateTemplate().save(user);
       //System.out.println(serializable);
        
    }
    //检查用户名是否已存在
    @Override
    public List<User> checkUser(String username) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        //hibernate除了sql外hql和Criteria都是设置属性
        criteria.add(Restrictions.eq("userName", username));
        return (List<User>)this.getHibernateTemplate().findByCriteria(criteria);
    }
    //登录
    public List<User> login(User user){
        //查询密码是加密的密码
         return (List<User>)this.getHibernateTemplate().find("from User u where u.userName=? and u.password=?", user.getUserName(),MD5Utils.md5(user.getPassword()));
    }
    //分页查询球员信息
    @Override
    public List<Player> findPlayers(int currentIndex) {
        // TODO Auto-generated method stub
        //一定要记得在applicationContext.xml中配置对应实体类的映射文件
        DetachedCriteria criteria = DetachedCriteria.forClass(Player.class);
        List<Player> listPlayers = (List<Player>)this.getHibernateTemplate().findByCriteria(criteria, (currentIndex-1)*9, 9);
        return listPlayers;
    }
    @Override
    public int findTotal() {
        // TODO Auto-generated method stub
        return ((List<Player>)this.getHibernateTemplate().find("from Player p")).size();
    }
    @Override
    public User findUser(Integer id) {
        // TODO Auto-generated method stub
        //延迟加载
        return this.getHibernateTemplate().get(User.class, id);
    }
    @Override
    public void updateUser(User user) {
        // TODO Auto-generated method stub
        user.setPassword(MD5Utils.md5(user.getPassword()));
        this.getHibernateTemplate().update(user);
    }
    @Override
    public List<Goods> findGoods(int index) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Goods.class);
        return (List<Goods>)this.getHibernateTemplate().findByCriteria(criteria, (index-1)*9, 9);
    }
    @Override
    public Goods initGood(int id) {
        // TODO Auto-generated method stub
        return this.getHibernateTemplate().get(Goods.class, id);
    }
    @Override
    public int saveOrder(Order order) {
        // TODO Auto-generated method stub
        int result = (int) this.getHibernateTemplate().save(order);
        System.out.println(result);
        return result;
    }
    @Override
    public List<Order> findOrders(Integer userId,Integer pageIndex,Integer pageSize) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
        criteria.add(Restrictions.eq("userId", userId));
        //pageIndex表示下标，注意从0开始，pageSize表示每页多少条
        return (List<Order>)this.getHibernateTemplate().findByCriteria(criteria, pageIndex*pageSize, pageSize);
    }
    @Override
    public Goods findGoodByGoodId(Integer goodId) {
        // TODO Auto-generated method stub
        return this.getHibernateTemplate().get(Goods.class,goodId);
    }
    @Override
    public Integer countTotal(Integer userId) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
        //注意要设置多个聚合函数，可以用ProjectionsList或者上一次设置完后重新设置时setProjection(null)
        criteria.setProjection(Projections.count("id"));//调用函数rowCount也行
        criteria.add(Restrictions.eq("userId", userId));
        List<Long> count = (List<Long>)this.getHibernateTemplate().findByCriteria(criteria);   
        String countStr = count.get(0).toString();
        return Integer.valueOf(countStr);
    }
    @Override
    public int findTotalGoods() {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Goods.class);
        criteria.setProjection(Projections.rowCount());
        List<Long> listTotal = (List<Long>)this.getHibernateTemplate().findByCriteria(criteria);
        return Integer.parseInt(listTotal.get(0).toString());
    }
    @Override
    public List<Forum> findForum(int currentIndex) {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Forum.class);
        List<Forum> list = (List<Forum>)this.getHibernateTemplate().findByCriteria(criteria, (currentIndex-1)*8, 8);
        return list;
    }
    @Override
    public int findTotalForum() {
        // TODO Auto-generated method stub
        DetachedCriteria criteria = DetachedCriteria.forClass(Forum.class);
        criteria.setProjection(Projections.rowCount());
        List<Long> listTotal = (List<Long>)this.getHibernateTemplate().findByCriteria(criteria);
        return Integer.parseInt(listTotal.get(0).toString());
    }
    @Override
    public List<List> findForumAndPost(int forumId) {
        // TODO Auto-generated method stub
        List<List> list = new ArrayList<>();
        List<Forum> listForum = new ArrayList<>();
        List<Post> listPost = new ArrayList<>();
        Forum forum = this.getHibernateTemplate().get(Forum.class, forumId);
        listForum.add(forum);
        DetachedCriteria criteria = DetachedCriteria.forClass(Post.class);
        criteria.add(Restrictions.eq("formId", forumId));
        listPost = (List<Post>)this.getHibernateTemplate().findByCriteria(criteria);
        list.add(listForum);
        list.add(listPost);
        return list;
    }
    @Override
    public void addPost(Post post) {
        // TODO Auto-generated method stub
      this.getHibernateTemplate().save(post);  
    } 
}
