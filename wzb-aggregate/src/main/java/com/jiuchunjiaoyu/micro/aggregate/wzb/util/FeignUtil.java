package com.jiuchunjiaoyu.micro.aggregate.wzb.util;

import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import org.slf4j.Logger;

/**
 * 调用feign工具类
 */
public class FeignUtil {

    public static void validateFeignResult( BaseResult<?> feignResult, String feignStr,
                                           Logger logger) throws WzbAggregateException {
        if (SystemCode.success.getCode() != feignResult.getCode()) {
            logger.warn("调用" + feignStr + "失败，原因:" + feignResult.getMessage());
            throw new WzbAggregateException(feignResult.getCode(),"调用" + feignStr + "失败，原因:" + feignResult.getMessage());
        }
    }

}
