package com.jiuchunjiaoyu.micro.aggregate.wzb.manager.impl;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.CompanyPayOrderDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.CompanyPayResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.CompanyPayFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbOrderFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.manager.CompanyPayMng;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CompanyPayMngImpl implements CompanyPayMng {

    private static Logger logger = LoggerFactory.getLogger(CompanyPayMngImpl.class);


    @Resource
    private CompanyPayFeign companyPayFeign;

    @Resource
    private WzbOrderFeign wzbOrderFeign;

    @Override
    public CompanyPayResponseDTO draw(CompanyPayOrderDTO companyPayOrderDTO) throws WzbAggregateException {
        companyPayOrderDTO.setDesc("取款");
        companyPayOrderDTO.setMchAppid(WzbConstant.GZH_APP_ID);
        companyPayOrderDTO.setStatus(CompanyPayOrderDTO.STATUS_NOT_PAY);
        BaseResult<CompanyPayOrderDTO> baseResult = wzbOrderFeign.saveOrUpdateCompanyPayOrder(companyPayOrderDTO);
        FeignUtil.validateFeignResult(baseResult,"wzbOrderFeign.saveOrUpdateCompanyPayOrder",logger);
        CompanyPayOrderDTO resultDTO = baseResult.getData();
        BaseResult<CompanyPayResponseDTO> companyPayResponseDTOBaseResult = companyPayFeign.pay(resultDTO);
        FeignUtil.validateFeignResult(companyPayResponseDTOBaseResult,"companyPayFeign.pay",logger);
        CompanyPayResponseDTO responseDTO = companyPayResponseDTOBaseResult.getData();
        if(!"SUCCESS".equals(responseDTO.getReturnCode())){
            resultDTO.setStatus(CompanyPayOrderDTO.STATUS_PAY_FAIL);
            resultDTO.setMessage(responseDTO.getReturnMsg());
            wzbOrderFeign.saveOrUpdateCompanyPayOrder(resultDTO);
            return responseDTO;
        }
        if(!"SUCCESS".equals(responseDTO.getResultCode())){
            resultDTO.setStatus(CompanyPayOrderDTO.STATUS_PAY_FAIL);
            resultDTO.setMessage(responseDTO.getErrCodeDes());
            wzbOrderFeign.saveOrUpdateCompanyPayOrder(resultDTO);
            return responseDTO;
        }
        resultDTO.setPaymentTime(companyPayOrderDTO.getPaymentTime());
        resultDTO.setStatus(CompanyPayOrderDTO.STATUS_PAY);
        resultDTO.setMessage("SUCCESS");
        wzbOrderFeign.saveOrUpdateCompanyPayOrder(resultDTO);
        return responseDTO;
    }
}
