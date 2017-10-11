package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeCategoryMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeeCategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Transactional
@Service
public class FeeCategoryMngImpl extends BaseMngImpl<FeeCategory> implements FeeCategoryMng {

    @Resource
    private FeeCategoryRepository feeCategoryRepository;

    @Override
    protected CrudRepository<FeeCategory, Long> getRepository() {
        return feeCategoryRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = "wzb:data:fee_category_by_school", key = "'wzb:data:fee_category_by_school_key'+#p0.schoolId", condition = "#p0.schoolId!=null"),
            @CacheEvict(value = "wzb:data:fee_category_by_class", key = "'wzb:data:fee_category_by_class_key'+#p0.classId", condition = "#p0.classId!=null"),
            @CacheEvict(value = "wzb:data:fee_category_basic", allEntries = true, condition = "#p0.schoolId==null and #p0.classId==null")})
    @Override
    public FeeCategory save(FeeCategory entity) {
        return super.save(entity);
    }

    @Caching(evict = {
            @CacheEvict(value = "wzb:data:fee_category_by_school", key = "'wzb:data:fee_category_by_school_key'+#p0.schoolId", condition = "#p0.schoolId!=null"),
            @CacheEvict(value = "wzb:data:fee_category_by_class", key = "'wzb:data:fee_category_by_class_key'+#p0.classId", condition = "#p0.classId!=null"),
            @CacheEvict(value = "wzb:data:fee_category_basic", allEntries = true, condition = "#p0.schoolId==null and #p0.classId==null")})
    @Override
    public FeeCategory update(FeeCategory entity) {
        return super.update(entity);
    }

    @Caching(evict = {
            @CacheEvict(value = "wzb:data:fee_category_by_school", key = "'wzb:data:fee_category_by_school_key'+#p0.schoolId", condition = "#p0.schoolId!=null"),
            @CacheEvict(value = "wzb:data:fee_category_by_class", key = "'wzb:data:fee_category_by_class_key'+#p0.classId", condition = "#p0.classId!=null"),
            @CacheEvict(value = "wzb:data:fee_category_basic", allEntries = true, condition = "#p0.schoolId==null and #p0.classId==null")})
    @Override
    public FeeCategory saveFeeCategory(FeeCategory feeCategory) {
        Integer maxPriority = feeCategoryRepository.getMaxPriority();
        maxPriority = maxPriority == null ? 0 : maxPriority;
        feeCategory.setPriority(maxPriority+1);
        return save(feeCategory);
    }

    @Override
    public boolean existByFeeCategoryName(String feeCategoryName) {
        return feeCategoryRepository.existByFeeCategoryName(feeCategoryName);
    }
}
