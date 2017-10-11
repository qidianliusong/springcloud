package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import javax.annotation.Resource;

import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeDrawRecordMng;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

/**
 * 取款记录
 *
 */
@RestController
@RequestMapping("/operate/feeDrawRecord")
public class FeeDrawRecordController extends CommonBaseController{
	
	@Resource
	private FeeDrawRecordMng feeDrawRecordMng;
	
	@ApiOperation(value = "取款记录修改", notes = "取款记录新增或者修改处理")
	@ApiImplicitParams({})
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BaseResult<FeeDrawRecord> saveOrUpdate(@RequestBody FeeDrawRecord feeDrawRecord) throws WzbDataException {
		BaseResult<FeeDrawRecord> baseResult = new BaseResult<>();
		if(feeDrawRecord.getFeeDrawRecordId() == null){
		    feeDrawRecord.setCreateTime(new Date());
		    feeDrawRecord.setStatus(FeeDrawRecord.STATUS_NOT_PAY);
        }
		baseResult.setData(feeDrawRecordMng.saveFeeDrawRecord(feeDrawRecord));
		return baseResult;
	}


	@ApiOperation(value = "支付成功调用", notes = "支付成功调用")
	@ApiImplicitParams({
            @ApiImplicitParam(value="取款记录id",name="feeDrawRecordId",required=true,dataType="Long",paramType="query")
    })
	@RequestMapping(value = "/afterPay", method = RequestMethod.POST)
	public BaseResult<Void> afterPay(Long feeDrawRecordId) throws WzbDataException {
	    BaseResult<Void> baseResult = new BaseResult<>();
	    feeDrawRecordMng.afterPay(feeDrawRecordId);
	    return baseResult;
    }

}
