package com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result;
/**
 * 接收返回的list
 */

public class ResultData<T> {

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
