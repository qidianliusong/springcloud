package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeDetailMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeeDetailRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeePayBatchRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FeeDetailMngImpl extends BaseMngImpl<FeeDetail> implements FeeDetailMng {

    @Resource
    private FeeDetailRepository feeDetailRepository;

    @Resource
    private FeePayBatchRepository feePayBatchRepository;

    @Override
    protected CrudRepository<FeeDetail, Long> getRepository() {
        return feeDetailRepository;
    }

    @CachePut(value = "wzb:data:fee_detail", key = "'wzb:data:fee_detail_keys'+#p0.feeDetailId")
    @Override
    public FeeDetail save(FeeDetail entity) {
        return super.save(entity);
    }

    @CachePut(value = "wzb:data:fee_detail", key = "'wzb:data:fee_detail_keys'+#p0.feeDetailId")
    @Override
    public FeeDetail update(FeeDetail entity) {
        return super.update(entity);
    }

    @Caching(evict = {@CacheEvict(value = {"wzb:data:fee_detail"}, key = "'wzb:data:fee_detail_keys'+#p0"),
            @CacheEvict(value = {"wzb:data:fee_detail_exists"}, key = "'wzb:data:fee_detail_exists_keys'+#p0")
    })
    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public FeeDetail saveFeeDetail(FeeDetail feeDetail) {
        this.save(feeDetail);
        List<FeePay> feePays = feeDetail.getFeePays();
        if (feePays != null && !feePays.isEmpty()) {
            feePays.stream().forEach(feePay -> {
                feePay.setFeeDetailId(feeDetail.getFeeDetailId());
            });
            feePayBatchRepository.batchSave(feePays);
        }
        return feeDetail;
    }

    // @Resource
    // private FeeDetailRepository feeDetailRepository;
    //
    // @Override
    // public FeeDetail saveFeeDetail(FeeDetail feeDetail) {
    // return feeDetailRepository.save(feeDetail);
    // }
    //
    // @Override
    // public FeeDetail updateFeeDetail(FeeDetail feeDetail) {
    // return feeDetailRepository.save(feeDetail);
    // }
    //
    // @Override
    // public void deleteFeeDetailById(Long feeDetailId) {
    // feeDetailRepository.delete(feeDetailId);
    // }

}
