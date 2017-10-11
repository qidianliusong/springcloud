package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxPayQueryResponseDTO;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
/**
 * 调用微智宝写入服务的feign接口
 */
@FeignClient("WXA-PAY")
public interface WxaPayFeign {
	
//	@RequestMapping(value="/wxa/pay/prePay",method=RequestMethod.POST)
//	public BaseResult<PrePayResutlDTO> prePay(PrePayRequestDTO prePayRequestDTO);
//
//	@RequestMapping(value="/wxa/pay/query",method=RequestMethod.GET)
//	public BaseResult<WxPayQueryResponseDTO> query(@RequestParam(required = true,value="outTradeNo") String outTradeNo);
	
}
