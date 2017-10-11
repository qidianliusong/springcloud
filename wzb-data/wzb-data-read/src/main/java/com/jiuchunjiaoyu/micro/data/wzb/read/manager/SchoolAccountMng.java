package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;

public interface SchoolAccountMng extends BaseMng<SchoolAccount> {

    SchoolAccount findBySchoolId(Long schoolId);

    boolean existsBySchoolId(Long schoolId);

}
