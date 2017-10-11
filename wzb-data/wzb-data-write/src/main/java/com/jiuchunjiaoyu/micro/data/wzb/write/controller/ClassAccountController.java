package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.ClassAccountMng;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 班级账户控制器
 *
 */
@RestController
@RequestMapping("/operate/classAccount")
public class ClassAccountController extends CommonBaseController{
	
	@Resource
	private ClassAccountMng classAccountMng;

	@ApiOperation(value = "新建班级账户", notes = "新建班级账户")
	@ApiImplicitParams({})
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BaseResult<ClassAccount> save( @RequestBody ClassAccount classAccount) {
		BaseResult<ClassAccount> baseResult = new BaseResult<>();
		classAccount.setClassAccountId(null);
		ClassAccount save = classAccountMng.save(classAccount);
		baseResult.setData(save);
		return baseResult;
	}
	
	@ApiOperation(value = "修改班级账户", notes = "修改班级账户")
	@ApiImplicitParams({})
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public BaseResult<ClassAccount> update( @RequestBody ClassAccount classAccount) {
		BaseResult<ClassAccount> baseResult = new BaseResult<>();
		if(classAccount.getClassAccountId() == null){
			baseResult.setCode(SystemCode.validateFail.getCode());
			baseResult.setMessage("classAccountId不能为空");
			return baseResult;
		}
		if(!classAccountMng.exists(classAccount.getClassAccountId())){
			baseResult.setCode(SystemCode.validateFail.getCode());
			baseResult.setMessage("不存在的班级账户id"+classAccount.getClassAccountId());
			return baseResult;
		}
		ClassAccount update = classAccountMng.update(classAccount);
		baseResult.setData(update);
		return baseResult;
	}
	
	@ApiOperation(value = "删除班级账户", notes = "删除班级账户")
	@ApiImplicitParams({
		@ApiImplicitParam(value="账户id",name="accountId",required=true,dataType="Long",paramType="query")
	})
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public BaseResult<Void> deleteFeeDetail(@RequestParam(required=true) Long accountId) {
		BaseResult<Void> baseResult = new BaseResult<>();
		classAccountMng.delete(accountId);
		return baseResult;
	}
	
}
