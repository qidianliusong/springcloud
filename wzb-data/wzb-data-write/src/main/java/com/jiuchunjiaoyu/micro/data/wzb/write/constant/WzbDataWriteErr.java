package com.jiuchunjiaoyu.micro.data.wzb.write.constant;

/**
 * 写入数据报错信息
 */
public enum WzbDataWriteErr {

    fee_detail_not_exists(1001, "不存在的收费详情"),
    classaccount_not_exists(1002, "不存在的班级账户"),

    fee_agent_id_not_be_null(1003, "代缴id不存在或为空"),

    id_not_be_null(1004, "id不能为空"),

    amount_not_enough(1005, "余额不足"),

    fee_draw_record_not_exist(1006, "取款记录不存在"),
    entity_not_exist(1007, "不存在的实体"),

    operation_not_permitted(1008, "操作不允许"),

    fee_category_name_exist(1009, "收费类目名称已存在");

    private int code;
    private String message;

    private WzbDataWriteErr(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
