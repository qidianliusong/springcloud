package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultData;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config.ClassFeignConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用php班级相关接口调用
 */
@FeignClient(name = "USER", url = "${wzxy.url.prefix:http://dev-lumen.jcweixiaoyuan.cn}", configuration = ClassFeignConfiguration.class)
public interface UserFeign {

    /**
     * 获取用户信息
     */
    @Cacheable(value = "wzb:aggregate:getUserInfo", key = "'wzb:aggregate:getUserInfo_key_'+#p0+'_'+#p1")
    @RequestMapping(value = "peony/wx/user/getuserinfo", method = RequestMethod.GET)
    PhpResult<ResultData<UserDTO>> getUserInfo(@RequestParam(value = "token", required = true) String token, @RequestParam(value = "user_id", required = true) Long userId, @RequestParam(value = "verfiy_orgclass", required = true) Integer verfiy0rgclass);
}
