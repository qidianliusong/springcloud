package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.TestUserEntity;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.TestUserMng;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/test")
@Api(value="测试控制器")
public class TestController extends CommonBaseController{

	@Resource
	private TestUserMng testUserMng;
	
	@ApiOperation(value="测试接口",notes="测试接口")
	@ApiImplicitParams({
		@ApiImplicitParam(value="用户ID",name="userId",required=false,dataType="Long",paramType="query")
	})
	@RequestMapping(value="/11",method=RequestMethod.GET)
	public BaseResult<String> test1(){
		BaseResult<String> result = new BaseResult<>();
		result.setData("lalaalal");
		if(1==1)
			throw new RuntimeException("自定测试错误");
		return result;
	}
	
	@ApiOperation(value="测试保存",notes="测试保存")
	@RequestMapping(value="/save",method={RequestMethod.POST})
	public BaseResult<TestUserEntity> saveUser(TestUserEntity user,String bb){
		System.out.println(bb);
		BaseResult<TestUserEntity> result = new BaseResult<>();
		result.setData(testUserMng.save(user));
		return result;
	}
}
