package com.jiuchunjiaoyu.micro.wzb.task.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.dto.CompanyPayQueryRpDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.WxPayQueryResponseDTO;
import com.jiuchunjiaoyu.micro.wzb.task.feign.config.GzhFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * H5支付接口
 */
@FeignClient(value = "WX-GZH-PAY", configuration = GzhFeignConfiguration.class)
public interface GzhPayFeign {

    @RequestMapping(value = "/gzh/pay/query", method = RequestMethod.GET)
    BaseResult<WxPayQueryResponseDTO> query(@RequestParam(required = true, value = "outTradeNo") String outTradeNo);

    @RequestMapping(value = "/gzh/company/pay/query", method = RequestMethod.GET)
    BaseResult<CompanyPayQueryRpDTO> companyPayQuery(@RequestParam(required = true, value = "partnerTradeNo") String partnerTradeNo, @RequestParam(required = true, value = "appId") String appId);

}
