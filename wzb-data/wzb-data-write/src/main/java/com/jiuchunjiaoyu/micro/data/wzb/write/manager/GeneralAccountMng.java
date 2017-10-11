package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;

import java.math.BigDecimal;

public interface GeneralAccountMng extends BaseMng<GeneralAccount> {

    GeneralAccount getGeneralAccount();

    GeneralAccount addSchoolAmount(BigDecimal amount);
}
