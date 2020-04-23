package org.s.api.admin.core.consistant;

/**
 * 
 * @author WeiWei
 * 资源类型的枚举值
 */
public enum ResourceTypeEnum {

    API(0, "接口"),
    FUNC(1, "后台函数");

    private int value;
    private String title;
    private ResourceTypeEnum(int value, String title){
        this.value = value;
        this.title = title;
    }

    public int getValue() {
        return value;
    }
    public String getTitle() {
        return title;
    }

    public static ResourceTypeEnum match(int value){
        for (ResourceTypeEnum item: ResourceTypeEnum.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

}