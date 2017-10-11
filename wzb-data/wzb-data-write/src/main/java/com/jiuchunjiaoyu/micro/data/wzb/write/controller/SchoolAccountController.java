package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.ClassAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.SchoolAccountMng;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 班级账户控制器
 *
 */
@RestController
@RequestMapping("/operate/schoolAccount")
public class SchoolAccountController extends CommonBaseController{
	
	@Resource
	private SchoolAccountMng schoolAccountMng;

	@ApiOperation(value = "新建学校账户", notes = "新建学校账户")
	@ApiImplicitParams({})
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BaseResult<SchoolAccount> save(@RequestBody SchoolAccount schoolAccount) {
		BaseResult<SchoolAccount> baseResult = new BaseResult<>();
		schoolAccount.setSchoolAccountId(null);
		SchoolAccount save = schoolAccountMng.save(schoolAccount);
		baseResult.setData(save);
		return baseResult;
	}

}
