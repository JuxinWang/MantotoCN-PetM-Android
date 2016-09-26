package com.petm.property.model;

import com.petm.property.enums.EnumBusinessType;

/**
 * Created by Mr.liu
 * On 2016/9/26
 * At 11:30
 * PetM
 */
public class Business {
    public long businessid;
    public long shopkeeperid;
    public String businessname;
    public String hours;
    public String address;
    public String contact;
    public EnumBusinessType businesstypeid;
    public double latitude;
    public double longitude;
    public int avgprice;
    public String description;
    public boolean isvalid;
    public boolean isvip;
    public String areaid;
    public String ctime;
    public String btime;
    public String etime;
    public long frontCoverId;
}
