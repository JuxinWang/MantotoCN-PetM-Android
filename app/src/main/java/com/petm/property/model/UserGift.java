package com.petm.property.model;

import com.petm.property.enums.EnumGiftStatus;

/**
 * Created by Mr.liu
 * On 2016/9/26
 * At 11:22
 * PetM
 */
public class UserGift {
    public Long usergiftid;
    public Long giftid;
    public Long userid;
    public String source;
    public EnumGiftStatus statusid;
    public String comment;
    public String ctime;
    public Long createby;
    public Double value;
    public String etime;
    public Gift gift;
    public Business business;
}
