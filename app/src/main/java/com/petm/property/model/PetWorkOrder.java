package com.petm.property.model;

import com.petm.property.enums.EnumWorkOrderStatus;

/**
 * Created by Mr.liu
 * On 2016/9/26
 * At 14:13
 * PetM
 */
public class PetWorkOrder {
    public long petworkorderid;
    public long beauticianid;
    public long petid;
    public long petserviceid;
    public EnumWorkOrderStatus petworkorderstatusid;
    public long createby;
    public String ctime;
    public String btime;
    public String wtime;
    public String etime;
    public String comment;
    public long rootid;
    public Beautician beautician;
    public PetService petService;
    public Pet pet;
}
