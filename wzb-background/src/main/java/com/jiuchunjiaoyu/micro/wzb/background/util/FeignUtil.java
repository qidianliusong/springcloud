package com.jiuchunjiaoyu.micro.wzb.background.util;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;
import org.slf4j.Logger;

/**
 * 调用feign工具类
 */
public class FeignUtil {

    public static void validateFeignResult(BaseResult<?> feignResult, String feignStr,
                                           Logger logger) throws WzbBackgroundException {
        if (SystemCode.success.getCode() != feignResult.getCode()) {
            logger.warn("调用" + feignStr + "失败，原因:" + feignResult.getMessage());
            throw new WzbBackgroundException(feignResult.getCode(), "调用" + feignStr + "失败，原因:" + feignResult.getMessage());
        }
    }

}
