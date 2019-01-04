package com.wa.msm.order.util.enumeration;

public enum OrderDemandEnum {
    VALIDATED_DEMAND("VALIDATED_DEMAND"),
    REJECTED_DEMAND("REJECTED_DEMAND"),
    OPENED_DEMAND("OPENED_DEMAND");


    private final String name;

    private OrderDemandEnum(String pName ){
        name=pName;
    }

    public boolean equalsName(String otherName){
        return name.equals(otherName);
    }

    public String toString(){
        return this.name;
    }
}
