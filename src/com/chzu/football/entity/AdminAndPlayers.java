package com.chzu.football.entity;
/*
 * REATE TABLE `admintoplayers`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `admin_id` int(255) NOT NULL,
  `players_id` int(255) NOT NULL,
 */
public class AdminAndPlayers
{
    private Integer id;
    private Integer adminId;
    private Integer playersId;
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
    public Integer getPlayersId() {
        return playersId;
    }
    public void setPlayersId(Integer playersId) {
        this.playersId = playersId;
    }
    
}
