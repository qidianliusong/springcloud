package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.AccountFlow;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ProvinceFlowAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolFlowAccount;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountFlowMng extends BaseMng<AccountFlow> {

    Page<AccountFlow> getPage(String province, Long schoolId, Long classId, Integer type, Date startDate, Date endDate, Integer pageNo, Integer pageSize);

    BigDecimal getStatistics(String province, Long schoolId, Long classId,Integer type ,Date startDate, Date endDate);

    GeneralAccount getGeneralAccount();

    SchoolFlowAccount getSchoolFlowAccount(Long schoolId);

    ProvinceFlowAccount getProvinceFlowAccount(String provinceName);

    List<ProvinceFlowAccount> getProvinceFlowAccountList();

    Page<SchoolFlowAccount> getSchoolFlowAccountPage(String province, String schoolName, Integer pageNo, Integer pageSize);
}
