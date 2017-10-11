package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface FeeDetailMng extends BaseMng<FeeDetail> {

    List<FeeDetail> findByClassId(Long classId);

    Page<FeeDetail> getPageInfo(Long classId, Integer status, int pageNo, int pageSize);

    Page<FeeDetail> getPageInfoByCategory(Long categoryId, int pageNo, int pageSize);

    BigDecimal getSumByCategory(Long categoryId);

}
