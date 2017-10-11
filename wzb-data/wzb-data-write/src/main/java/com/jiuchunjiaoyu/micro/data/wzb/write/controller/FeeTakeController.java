package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import javax.annotation.Resource;

import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeTakeMng;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 *记账controller 
 */
@RestController
@RequestMapping("/operate/feeTake")
public class FeeTakeController extends CommonBaseController{
	
	@Resource
	private FeeTakeMng feeTakeMng;

	@ApiOperation(value = "新建或修改记账", notes = "新建或修改记账信息")
	@ApiImplicitParams({
			// @ApiImplicitParam(value="收费详情id",name="feeDetailId",required=true,dataType="Long",paramType="query")
	})
	@RequestMapping(value="saveOrUpdate",method=RequestMethod.POST)
	public BaseResult<FeeTake> saveOrUpdate(@RequestBody FeeTake feeTake){
		BaseResult<FeeTake> baseResult = new BaseResult<>();
		baseResult.setData(feeTakeMng.saveFeeTake(feeTake));
		return baseResult;
	}

	@ApiOperation(value = "删除记账", notes = "删除记账信息")
	@ApiImplicitParams({
			 @ApiImplicitParam(value="收费详情id",name="feeDetailId",required=true,dataType="Long",paramType="query")
	})
	@RequestMapping(value="delete",method=RequestMethod.DELETE)
	public BaseResult<Void> delete(Long feeTakeId){
		BaseResult<Void> baseResult = new BaseResult<>();
		FeeTake feeTake = feeTakeMng.findOne(feeTakeId);
		if(feeTake == null){
		    baseResult.setCode(WzbDataWriteErr.entity_not_exist.getCode());
		    baseResult.setMessage("不存在的记账信息,id="+feeTakeId);
		    return baseResult;
        }
		feeTakeMng.deleteFeeTake(feeTake);
		return baseResult;
	}
	
}
