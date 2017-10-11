package com.jiuchunjiaoyu.micro.wzb.task.dto;

public class QueryResultDTO {

    private WzbOrderDTO wzbOrderDTO;

    private WxPayQueryResponseDTO wxPayQueryResponseDTO;

    public WzbOrderDTO getWzbOrderDTO() {
        return wzbOrderDTO;
    }

    public void setWzbOrderDTO(WzbOrderDTO wzbOrderDTO) {
        this.wzbOrderDTO = wzbOrderDTO;
    }

    public WxPayQueryResponseDTO getWxPayQueryResponseDTO() {
        return wxPayQueryResponseDTO;
    }

    public void setWxPayQueryResponseDTO(WxPayQueryResponseDTO wxPayQueryResponseDTO) {
        this.wxPayQueryResponseDTO = wxPayQueryResponseDTO;
    }
}
