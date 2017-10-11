package com.jiuchunjiaoyu.micro.wzb.task.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.dto.CompanyPayOrderDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.WzbOrderDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用微智宝写入服务的feign接口
 */
@FeignClient("WZB-ORDER")
public interface WzbOrderFeign {

    @RequestMapping(value = "/wzbOrder/saveOrUpdate", method = RequestMethod.POST)
    BaseResult<WzbOrderDTO> saveOrUpdate(WzbOrderDTO wzbOrderDTO);

    @RequestMapping(value = "/wzbOrder/findByOutTradeNo", method = RequestMethod.GET)
    BaseResult<WzbOrderDTO> findByOutTradeNo(@RequestParam(required = true, value = "outTradeNo") String outTradeNo);

    @RequestMapping(value = "/wzbOrder/companyPay/findByPartnerTradeNo", method = RequestMethod.GET)
    BaseResult<CompanyPayOrderDTO> findByPartnerTradeNo(@RequestParam(required = true, value = "partnerTradeNo") String partnerTradeNo);

    @RequestMapping(value = "/wzbOrder/companyPay/saveOrUpdate", method = RequestMethod.POST)
    BaseResult<CompanyPayOrderDTO> saveOrUpdateCompanyOrder(CompanyPayOrderDTO commpanyPayOrder);

}
