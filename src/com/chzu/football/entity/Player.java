package com.chzu.football.entity;
/**
 * `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `age` int(50) NOT NULL,
  `birth` datetime(6) NULL DEFAULT NULL,
  `height` double(50, 0) NOT NULL,
  `weight` double(50, 0) NOT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entryTime` datetime(6) NOT NULL,
  `careerAge` int(50) NOT NULL,
  `photoAdd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
 */
import java.util.Date;
public class Player
{
    private Integer id;
    private String name;
    private Integer age;
    private Date birth;
    private Double height;
    private Double weight;
    //球员的场上位置
    private String role;
    //球员入队时间
    private Date entryTime;
    //球员的职业生涯年龄
    private Integer careerAge;
    //球员的照片
    private String photoAdd;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Date getBirth() {
        return birth;
    }
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    public Double getHeight() {
        return height;
    }
    public void setHeight(Double height) {
        this.height = height;
    }
    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Date getEntryTime() {
        return entryTime;
    }
    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }
    public Integer getCareerAge() {
        return careerAge;
    }
    public void setCareerAge(Integer careerAge) {
        this.careerAge = careerAge;
    }
    public String getPhotoAdd() {
        return photoAdd;
    }
    public void setPhotoAdd(String photoAdd) {
        this.photoAdd = photoAdd;
    }
    
}
