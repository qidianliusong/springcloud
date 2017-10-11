package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.PrePayDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WzbOrderDTO;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * H5支付接口
 */
@FeignClient("WX-H5-PAY")
public interface H5PayFeign {

    @RequestMapping(value = "/h5/pay/prePay", method = RequestMethod.POST)
    BaseResult<PrePayDTO> prePay(WzbOrderDTO wzbOrderDTO);

}
