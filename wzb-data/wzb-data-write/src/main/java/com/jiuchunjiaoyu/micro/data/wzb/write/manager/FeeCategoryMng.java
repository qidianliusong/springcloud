package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;

/**
 * 收费类目manager 
 *
 */
public interface FeeCategoryMng extends BaseMng<FeeCategory>{

    FeeCategory saveFeeCategory(FeeCategory feeCategory);

    boolean existByFeeCategoryName(String feeCategoryName);

}
