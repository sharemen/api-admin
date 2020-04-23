package org.s.api.admin.core.consistant;

/**
 * 
 * @author WeiWei
 * 资源类型的枚举值
 */
public enum OperateEnum {

    ADD("add", "添加"),
    UPDATE("update", "修改"),
	DEL("del", "删除");

    private String value;
    private String title;
    private OperateEnum(String value, String title){
        this.value = value;
        this.title = title;
    }

    public String getValue() {
        return value;
    }
    public String getTitle() {
        return title;
    }

    public static OperateEnum match(String value){
        for (OperateEnum item: OperateEnum.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

}