package com.petm.property.model;

/**
 * Created by Mr.liu
 * On 2016/9/26
 * At 11:26
 * PetM
 */
public class Gift {
    public long giftid;
    public long businessid;
    public String title;
    public String description;
    public double sum;
    public double defaultvalue;
    public String unit;
    public boolean isvalid;
    public boolean isshow;
    public String btime;
    public String etime;
    //发卡后，有效期天数。（-1：表示与卡片的ETime一致）
    public int numberofdays;
}
