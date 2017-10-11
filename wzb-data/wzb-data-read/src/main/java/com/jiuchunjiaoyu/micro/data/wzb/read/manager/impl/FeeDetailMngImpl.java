package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeDetailMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.FeeDetailRepository;

@Service
public class FeeDetailMngImpl extends BaseMngImpl<FeeDetail> implements FeeDetailMng {

    @Resource
    private FeeDetailRepository feeDetailRepository;

    @Override
    protected JpaRepository<FeeDetail, Long> getRepository() {
        return feeDetailRepository;
    }

    @Override
    public List<FeeDetail> findByClassId(Long classId) {
        return feeDetailRepository.findByClassId(classId);
    }

    @Cacheable(value = "wzb:data:fee_detail_exists", key = "'wzb:data:fee_detail_exists_keys'+#p0")
    @Override
    public boolean exists(Long id) {
        return super.exists(id);
    }

    @Cacheable(value = "wzb:data:fee_detail", key = "'wzb:data:fee_detail_keys'+#p0")
    @Override
    public FeeDetail findOne(Long id) {
        return super.findOne(id);
    }

    @Override
    public Page<FeeDetail> getPageInfo(Long classId, Integer status, int pageNo, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, new Sort(Sort.Direction.DESC, "createTime"));
        if (status == null) {
            return feeDetailRepository.getPageInfoByClassId(classId, pageRequest);
        }
        return feeDetailRepository.getPageInfoByClassIdAndStatus(classId, status, pageRequest);

    }

    @Override
    public Page<FeeDetail> getPageInfoByCategory(Long categoryId, int pageNo, int pageSize) {

        Pageable page = new PageRequest(pageNo, pageSize, new Sort(Sort.Direction.DESC, "createTime"));

        return feeDetailRepository.getPageInfoByCategoryId(categoryId,page);
    }

    @Override
    public BigDecimal getSumByCategory(Long categoryId) {
        return feeDetailRepository.getSumByCategoryId(categoryId);
    }


}
