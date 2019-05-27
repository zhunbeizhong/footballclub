package com.chzu.football.entity;
/*
 * CREATE TABLE `admintogoods`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `admin_id` int(255) NOT NULL,
  `goods_id` int(255) NOT NULL,
 */
public class AdminAndGoods
{
    private Integer id;
    private Integer adminId;
    private Integer goodsId;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAdminId() {
        return adminId;
    }
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    public Integer getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    
}
