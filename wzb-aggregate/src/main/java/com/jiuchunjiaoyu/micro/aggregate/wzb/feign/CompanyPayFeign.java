package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackRequestDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config.GzhFeignConfiguration;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * H5支付接口
 */
@FeignClient(value="WX-GZH-PAY",configuration = GzhFeignConfiguration.class)
public interface CompanyPayFeign {

    @RequestMapping(value = "/gzh/company/pay", method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResult<CompanyPayResponseDTO> pay(CompanyPayOrderDTO companyPayOrderDTO);

}
