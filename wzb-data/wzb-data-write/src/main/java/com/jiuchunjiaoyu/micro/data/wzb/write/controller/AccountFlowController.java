package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.AccountFlow;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.AccountFlowMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 班级账户控制器
 */
@RestController
@RequestMapping("/operate/accountFlow")
@Api(tags = "账户流水相关接口")
public class AccountFlowController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(AccountFlowController.class);

    @Resource
    private AccountFlowMng accountFlowMng;


    @ApiOperation(value = "记录账户流水", notes = "记录账户流水")
    @ApiImplicitParams({
//            @ApiImplicitParam(value = "账户id", name = "accountId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<AccountFlow> save(@RequestBody AccountFlow accountFlow) {
        BaseResult<AccountFlow> baseResult = new BaseResult<>();
        boolean exist = accountFlowMng.existByTradeNo(accountFlow.getTradeNo());
        if (exist) {
            logger.warn("已经存在的订单号，tradeNo=" + accountFlow.getTradeNo());
            return baseResult;
        }
        baseResult.setData(accountFlowMng.saveAccountFlow(accountFlow));
        return baseResult;
    }

}
