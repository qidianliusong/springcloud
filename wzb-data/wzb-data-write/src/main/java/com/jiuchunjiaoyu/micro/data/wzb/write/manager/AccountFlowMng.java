package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.AccountFlow;

public interface AccountFlowMng extends BaseMng<AccountFlow> {

    AccountFlow saveAccountFlow(AccountFlow accountFlow);

    boolean existByTradeNo(String tradeNo);
}
