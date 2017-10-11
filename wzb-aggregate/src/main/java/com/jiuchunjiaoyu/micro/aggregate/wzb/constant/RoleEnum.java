package com.jiuchunjiaoyu.micro.aggregate.wzb.constant;

/**
 * 角色
 */
public enum RoleEnum {
    class_master(1,"班主任"),
    teacher(2,"老师"),
    parent(3,"家长"),
    committee(4,"家委会成员");

    private Integer id;
    private String message;

    private RoleEnum(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
