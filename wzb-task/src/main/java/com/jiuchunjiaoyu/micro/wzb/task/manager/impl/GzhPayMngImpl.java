package com.jiuchunjiaoyu.micro.wzb.task.manager.impl;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.constant.Constant;
import com.jiuchunjiaoyu.micro.wzb.task.constant.WxTradeStaus;
import com.jiuchunjiaoyu.micro.wzb.task.dto.*;
import com.jiuchunjiaoyu.micro.wzb.task.exception.TaskException;
import com.jiuchunjiaoyu.micro.wzb.task.feign.GzhPayFeign;
import com.jiuchunjiaoyu.micro.wzb.task.feign.WzbOrderFeign;
import com.jiuchunjiaoyu.micro.wzb.task.manager.GzhPayMng;
import com.jiuchunjiaoyu.micro.wzb.task.util.FeignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GzhPayMngImpl implements GzhPayMng {

    private static Logger logger = LoggerFactory.getLogger(GzhPayMngImpl.class);
    /**
     * 订单过期时间10分钟
     */
    private static final Integer ORDER_EXPIRE_TIME = 600;

    @Resource
    private GzhPayFeign gzhPayFeign;

    @Resource
    private WzbOrderFeign wzbOrderFeign;

    @Override
    public QueryResultDTO query(String outTradeNo) throws TaskException {
        BaseResult<WxPayQueryResponseDTO> wxPayQueryResponseDTOBaseResult = gzhPayFeign.query(outTradeNo);
        FeignUtil.validateFeignResult(wxPayQueryResponseDTOBaseResult, "gzhPayFeign.query", logger);
        BaseResult<WzbOrderDTO> wzbOrderDTOBaseResult = wzbOrderFeign.findByOutTradeNo(outTradeNo);
        FeignUtil.validateFeignResult(wzbOrderDTOBaseResult, "wzbOrderFeign.findByOutTradeNo", logger);
        WxPayQueryResponseDTO wxPayQueryResponseDTO = wxPayQueryResponseDTOBaseResult.getData();
        WzbOrderDTO wzbOrderDTO = wzbOrderDTOBaseResult.getData();
//        if (WzbOrderDTO.STATUS_PAY == wzbOrderDTO.getStatus()) {
//            return wxPayQueryResponseDTO;
//        }

        QueryResultDTO queryResultDTO = new QueryResultDTO();
        queryResultDTO.setWxPayQueryResponseDTO(wxPayQueryResponseDTO);
        queryResultDTO.setWzbOrderDTO(wzbOrderDTO);

        if (!"SUCCESS".equals(wxPayQueryResponseDTO.getResult_code())) {
            if(Constant.WX_TRADE_QUERY_ORDERNOTEXIST.equals(wxPayQueryResponseDTO.getErr_code())){
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(wxPayQueryResponseDTO.getErr_code()+":"+wxPayQueryResponseDTO.getErr_code_des());
                BaseResult<WzbOrderDTO> wzbOrderDTOUpdateResult = wzbOrderFeign.saveOrUpdate(wzbOrderDTO);
                FeignUtil.validateFeignResult(wzbOrderDTOUpdateResult, "wzbOrderFeign.saveOrUpdate", logger);
            }
            return queryResultDTO;
        }

        wzbOrderDTO.setTransactionId(wxPayQueryResponseDTO.getTransaction_id());

        switch (wxPayQueryResponseDTO.getTrade_state()){
            case Constant.WX_TRADE_SUCCESS:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_PAY);
                wzbOrderDTO.setMessage(WxTradeStaus.success.getCode()+"_"+WxTradeStaus.success.getMesssage());
                break;
            case Constant.WX_TRADE_USERPAYING:
                return queryResultDTO;
            case Constant.WX_TRADE_NOTPAY:
                if(System.currentTimeMillis() <  wzbOrderDTO.getExpireTime().getTime())
                    return queryResultDTO;
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.notpay.getCode()+"_"+WxTradeStaus.notpay.getMesssage());
                break;
            case Constant.WX_TRADE_CLOSED:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.close.getCode()+"_"+WxTradeStaus.close.getMesssage());
                break;
            case Constant.WX_TRADE_PAYERROR:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.payerror.getCode()+"_"+WxTradeStaus.payerror.getMesssage());
                break;
            case Constant.WX_TRADE_REFUND:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.refund.getCode()+"_"+WxTradeStaus.refund.getMesssage());
                break;
            case Constant.WX_TRADE_REVOKED:
                //此处支付异常
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.revoked.getCode()+"_"+WxTradeStaus.revoked.getMesssage());
                break;
            default:
                break;
        }
        BaseResult<WzbOrderDTO> wzbOrderDTOUpdateResult = wzbOrderFeign.saveOrUpdate(wzbOrderDTO);
        FeignUtil.validateFeignResult(wzbOrderDTOUpdateResult, "wzbOrderFeign.saveOrUpdate", logger);

        return queryResultDTO;
    }

    @Override
    public CompanyPayQueryRpDTO queryCompanyPay(String partnerTradeNo) throws TaskException {
        BaseResult<CompanyPayQueryRpDTO> companyPayQueryRpDTOBaseResult = gzhPayFeign.companyPayQuery(partnerTradeNo, Constant.GZH_APP_ID);
        FeignUtil.validateFeignResult(companyPayQueryRpDTOBaseResult,"gzhPayFeign.companyPayQuery",logger);
        BaseResult<CompanyPayOrderDTO> companyPayOrderDTOBaseResult = wzbOrderFeign.findByPartnerTradeNo(partnerTradeNo);
        FeignUtil.validateFeignResult(companyPayOrderDTOBaseResult,"wzbOrderFeign.findByPartnerTradeNo",logger);

        CompanyPayQueryRpDTO companyPayQueryRpDTO = companyPayQueryRpDTOBaseResult.getData();
        CompanyPayOrderDTO companyPayOrderDTO = companyPayOrderDTOBaseResult.getData();

        if(!Constant.WX_TRADE_SUCCESS.equals(companyPayQueryRpDTO.getResultCode())){

            if( Constant.WX_COMPANY_PAY_QUERY_NOT_FOUND.equals(companyPayQueryRpDTO.getErrCode())){
                companyPayOrderDTO.setStatus(CompanyPayOrderDTO.STATUS_PAY_FAIL);
                companyPayOrderDTO.setMessage(companyPayQueryRpDTO.getErrCode()+":"+companyPayQueryRpDTO.getErrCodeDes());
                BaseResult<CompanyPayOrderDTO> saveOrUpdateCompanyOrder = wzbOrderFeign.saveOrUpdateCompanyOrder(companyPayOrderDTO);
                FeignUtil.validateFeignResult(saveOrUpdateCompanyOrder,"wzbOrderFeign.saveOrUpdateCompanyOrder",logger);
            }

            return companyPayQueryRpDTO;
        }

        switch (companyPayQueryRpDTO.getStatus()){
            case "SUCCESS":
                companyPayOrderDTO.setMessage(Constant.WX_TRADE_SUCCESS);
                companyPayOrderDTO.setStatus(CompanyPayOrderDTO.STATUS_PAY);
                break;
            case "FAILED":
                companyPayOrderDTO.setMessage(companyPayQueryRpDTO.getStatus()+":"+companyPayQueryRpDTO.getReason());
                companyPayOrderDTO.setStatus(CompanyPayOrderDTO.STATUS_PAY_FAIL);
                break;

            case "PROCESSING":
                return companyPayQueryRpDTO;
            default:
                return companyPayQueryRpDTO;

        }
        BaseResult<CompanyPayOrderDTO> saveOrUpdateCompanyOrder = wzbOrderFeign.saveOrUpdateCompanyOrder(companyPayOrderDTO);
        FeignUtil.validateFeignResult(saveOrUpdateCompanyOrder,"wzbOrderFeign.saveOrUpdateCompanyOrder",logger);

        return companyPayQueryRpDTO;
    }

}
