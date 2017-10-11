package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.GeneralAccountMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 班级账户控制器
 */
@RestController
@RequestMapping("/operate/generalAccount")
@Api(tags = "总账户相关接口")
public class GeneralAccountController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(GeneralAccountController.class);

    @Resource
    private GeneralAccountMng generalAccountMng;


    @ApiOperation(value = "增加总账户学校后台金额", notes = "增加总账户学校后台金额")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "增加金额", name = "amount", required = true, dataType = "double", paramType = "query")
    })
    @RequestMapping(value = "/addSchoolAmount", method = RequestMethod.POST)
    public BaseResult<GeneralAccount> addSchoolAmount(@RequestParam BigDecimal amount) {
        BaseResult<GeneralAccount> baseResult = new BaseResult<>();
        baseResult.setData(generalAccountMng.addSchoolAmount(amount));
        return baseResult;
    }

}
