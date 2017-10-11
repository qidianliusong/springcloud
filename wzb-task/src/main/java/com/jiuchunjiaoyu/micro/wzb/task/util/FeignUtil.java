package com.jiuchunjiaoyu.micro.wzb.task.util;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzb.task.exception.TaskException;
import org.slf4j.Logger;

/**
 * 调用feign工具类
 */
public class FeignUtil {

    public static void validateFeignResult(BaseResult<?> feignResult, String feignStr,
                                           Logger logger) throws TaskException {
        if (SystemCode.success.getCode() != feignResult.getCode()) {
            logger.warn("调用" + feignStr + "失败，原因:" + feignResult.getMessage());
            throw new TaskException(feignResult.getCode(), "调用" + feignStr + "失败，原因:" + feignResult.getMessage());
        }
    }

}
