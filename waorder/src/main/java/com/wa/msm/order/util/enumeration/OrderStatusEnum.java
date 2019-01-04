package com.wa.msm.order.util.enumeration;

public enum OrderStatusEnum {
    FINALIZED("FINALIZED"),
    NOT_PAID("NOT_PAID"),
    UPDATE_DEMAND("UPDATE_DEMAND"),
    DELETE_DEMAND("DELETE_DEMAND"),
    CANCELED("CANCELED");

    private final String name;

    private OrderStatusEnum(String pName ){
        name=pName;
    }

    public boolean equalsName(String otherName){
        return name.equals(otherName);
    }

    public String toString(){
        return this.name;
    }
}
