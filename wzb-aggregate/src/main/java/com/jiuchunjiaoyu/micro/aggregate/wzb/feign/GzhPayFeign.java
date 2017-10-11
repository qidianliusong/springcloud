package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.PrePayDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxAuthDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxPayQueryResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WzbOrderDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackRequestDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config.GzhFeignConfiguration;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * H5支付接口
 */
@FeignClient(value="WX-GZH-PAY",configuration = GzhFeignConfiguration.class)
public interface GzhPayFeign {

    @RequestMapping(value = "/gzh/auth/getOpenId", method = RequestMethod.GET)
    BaseResult<WxAuthDTO> getOpenId(@RequestParam(value = "code", required = true) String code);

    @RequestMapping(value = "/gzh/pay/prePay", method = RequestMethod.POST)
    BaseResult<PrePayDTO> prePay(WzbOrderDTO wzbOrderDTO);

    @RequestMapping(value = "/gzh/pay/query", method = RequestMethod.GET)
    BaseResult<WxPayQueryResponseDTO> query(@RequestParam(required = true, value = "outTradeNo") String outTradeNo);

    @RequestMapping(value = "callBack", method = RequestMethod.POST)
    BaseResult<Void> callBack(@RequestBody PayCallBackRequestDTO payCallBackRequestDTO);

}
