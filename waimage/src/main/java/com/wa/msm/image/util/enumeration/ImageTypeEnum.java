package com.wa.msm.image.util.enumeration;

public enum ImageTypeEnum {
    USER("USR"),
    ADVENTURE("ADV"),
    CATEGORY("CAT");

    private String code;

    ImageTypeEnum(String code){
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
