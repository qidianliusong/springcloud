package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackRequestDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.GzhPayFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.manager.GzhPayMng;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信回调接口
 */
@RestController
@RequestMapping("/wx")
public class WxCallBackController {

    private static Logger logger = LoggerFactory.getLogger(WxCallBackController.class);

    @Resource
    private GzhPayFeign gzhPayFeign;

    @Resource
    private GzhPayMng gzhPayMng;

    @RequestMapping(value = "callBack", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)
    public PayCallBackResponseDTO callBack(@RequestBody PayCallBackRequestDTO payCallBackRequestDTO) {
        System.out.println("收到微信回调" + payCallBackRequestDTO.getOut_trade_no() + "      " + payCallBackRequestDTO.getReturn_msg());
        PayCallBackResponseDTO payCallBackResponseDTO = new PayCallBackResponseDTO();
        if (!"SUCCESS".equals(payCallBackRequestDTO.getReturn_code())) {
            logger.warn("微信回调告知操作失败，失败原因:" + payCallBackRequestDTO.getReturn_msg());
            payCallBackResponseDTO.setReturn_code("SUCCESS");
            return payCallBackResponseDTO;
        }
        try {
            gzhPayMng.callBack(payCallBackRequestDTO);
            payCallBackResponseDTO.setReturn_code("SUCCESS");
            return payCallBackResponseDTO;
        } catch (Exception e) {
            logger.error("处理微信回调失败", e);
            payCallBackResponseDTO.setReturn_code("FAIL");
            return payCallBackResponseDTO;
        }


    }

}
