package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.ClassDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.FeeCategoryDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultList;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.ClassFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 班费类别接口
 */
@Api(value = "班费类别相关接口")
@RestController
@RequestMapping("/wzb/feeCategory")
public class FeeCategoryController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeCategoryController.class);

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @ApiOperation(value = "获取班费类别列表", notes = "获取班费类别列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<List<FeeCategoryDTO>> getList(Long schoolId,@RequestParam(required = false) Long classId) {
        BaseResult<List<FeeCategoryDTO>> baseResult = new BaseResult<>();
        BaseResult<List<FeeCategoryDTO>> feeCategoryList = readFeign.getFeeCategoryList(schoolId,classId);
        if (!validateFeignResult(baseResult, feeCategoryList, "readFeign.getFeeCategoryList", logger))
            return baseResult;
        baseResult.setData(feeCategoryList.getData());
        return baseResult;
    }

    @ApiOperation(value = "根据id获取班费类目信息", notes = "根据id获取班费类目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "班费类目id", name = "feeCategoryId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public BaseResult<FeeCategoryDTO> findById(@RequestParam Long feeCategoryId) throws WzbAggregateException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeCategoryId);
        FeignUtil.validateFeignResult(feeCategoryById,"readFeign.findFeeCategoryById",logger);
        baseResult.setData(feeCategoryById.getData());
        return baseResult;
    }

    @ApiOperation(value = "自定义收费类目", notes = "自定义收费类目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "收费类目名称", name = "feeCategoryName", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "收费类目简介", name = "feeCategoryDesc", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/addFeeCategory", method = RequestMethod.POST)
    public BaseResult<FeeCategoryDTO> addFeeCategory(Long classId, String feeCategoryName, @RequestParam(required = false) String feeCategoryDesc, String token, HttpSession session) throws WzbAggregateException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        UserDTO currentUser = (UserDTO) session.getAttribute(WzbConstant.SESSION_USER_NAME);
        if(currentUser == null || !isCommittee(classId,currentUser.getId(),token)){
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }
        FeeCategoryDTO feeCategoryDTO = new FeeCategoryDTO();
        feeCategoryDTO.setClassId(classId);
        feeCategoryDTO.setCreateUserId(currentUser.getId());
        feeCategoryDTO.setFeeCategoryName(feeCategoryName);
        feeCategoryDTO.setFeeCategoryDesc(feeCategoryDesc);
        feeCategoryDTO.setType(FeeCategoryDTO.TYPE_COMMON);
        BaseResult<FeeCategoryDTO> feeCategoryDTOBaseResult = writeFeign.saveFeeCategory(feeCategoryDTO);
        FeignUtil.validateFeignResult(feeCategoryDTOBaseResult,"writeFeign.saveFeeCategory",logger);
        baseResult.setData(feeCategoryDTOBaseResult.getData());
        return baseResult;
    }

}
