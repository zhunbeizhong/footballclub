package com.chzu.football.entity;

import java.util.Date;

/*
 * `id` int(255) NOT NULL AUTO_INCREMENT,
  `telphone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time` datetime(6) NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remasks` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `user_id` int(255) NOT NULL,
  `goods_id` int(255) NOT NULL,
 */
public class Order
{
    private Integer id;
    private String telPhone;
    private String address;
    //下单的日期
    private Date time;
    private String email;
    //1表示男，0表示女，默认为男
    private String sex;
    private String remasks;
    //用户id
    private Integer userId;
    //物品id
    private Integer goodsId;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTelPhone() {
        return telPhone;
    }
    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getRemasks() {
        return remasks;
    }
    public void setRemasks(String remasks) {
        this.remasks = remasks;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    
}
