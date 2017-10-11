package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.WzbDataReadErr;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeAgentMng;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/query/feeAgent")
@Api("代缴查询")
public class FeeAgentController extends CommonBaseController{

	@Resource
	private FeeAgentMng feeAgentMng;

	@ApiOperation(value = "根据id获取交费记录", notes = "根据id获取交费记录")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "代缴id", name = "feeAgentId", required = true, dataType = "Long", paramType = "query") })
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	public BaseResult<FeeAgent> findById(@RequestParam(required = true) Long feeAgentId) {
		BaseResult<FeeAgent> baseResult = new BaseResult<>();
		FeeAgent feeAgent = feeAgentMng.findOne(feeAgentId);
		if(feeAgent == null){
			baseResult.setCode(WzbDataReadErr.entity_not_exist.getCode());
			baseResult.setMessage(WzbDataReadErr.entity_not_exist.getMessage());
			return baseResult;
		}
		baseResult.setData(feeAgent);
		return baseResult;
	}

	@ApiOperation(value = "代缴分页信息", notes = "代缴分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getPageInfo", method = RequestMethod.POST)
	public BaseResult<Page<FeeAgent>> getPageInfo(@RequestParam(required = true) Long classId,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		if(pageSize > Constant.MAX_PAGE_SIZE)
			pageSize = Constant.MAX_PAGE_SIZE;
		BaseResult<Page<FeeAgent>> baseResult = new BaseResult<>();
		baseResult.setData(feeAgentMng.getPageInfo(classId, pageNo, pageSize));
		return baseResult;
	}

	@ApiOperation(value = "根据收费详情id获取代缴信息", notes = "根据收费详情id获取代缴信息")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query") })
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public BaseResult<List<FeeAgent>> list(@RequestParam(required = true) Long feeDetailId) {
		BaseResult<List<FeeAgent>> baseResult = new BaseResult<>();
		baseResult.setData(feeAgentMng.getList(feeDetailId));
		return baseResult;
	}

	@ApiOperation(value = "获取等待支付状态的缴费信息", notes = "获取等待支付状态的缴费信息")
	@RequestMapping(value = "/getPreFeeAgents", method = RequestMethod.GET)
	public BaseResult<List<FeeAgent>> getPreFeeAgents(Date startTime) {
		BaseResult<List<FeeAgent>> baseResult = new BaseResult<>();
		if(startTime == null){
			startTime = new Date();
		}
		baseResult.setData(feeAgentMng.getPreFeeAgents(startTime));
		return baseResult;
	}
}
