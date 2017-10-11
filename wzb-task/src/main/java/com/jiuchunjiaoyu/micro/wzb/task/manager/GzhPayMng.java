package com.jiuchunjiaoyu.micro.wzb.task.manager;

import com.jiuchunjiaoyu.micro.wzb.task.dto.CompanyPayQueryRpDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.QueryResultDTO;
import com.jiuchunjiaoyu.micro.wzb.task.exception.TaskException;

/**
 * h5支付相关
 */
public interface GzhPayMng {

    QueryResultDTO query(String getOutTradeNo) throws TaskException;

    CompanyPayQueryRpDTO queryCompanyPay(String partnerTradeNo) throws TaskException;

}
