package com.jiuchunjiaoyu.micro.wzb.background.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzb.background.dto.FeeCategoryDTO;
import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;
import com.jiuchunjiaoyu.micro.wzb.background.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.background.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.wzb.background.util.FeignUtil;
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
import java.util.List;

/**
 * 收费类目接口
 */
@Api(tags = "收费类目相关接口")
@RestController
@RequestMapping("/wzb-background/feeCategory")
public class FeeCategoryController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeCategoryController.class);

    private static final int FEE_CATRGORY_NAME_EXIST_CODE = 1009;

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @ApiOperation(value = "获取收费类目列表", notes = "获取收费类目列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResult<List<FeeCategoryDTO>> getList() throws WzbBackgroundException {
        BaseResult<List<FeeCategoryDTO>> baseResult = new BaseResult<>();
        BaseResult<List<FeeCategoryDTO>> feeCategoryListResult = readFeign.getFeeCategoryList();
        FeignUtil.validateFeignResult(feeCategoryListResult, "readFeign.getFeeCategoryList", logger);
        baseResult.setData(feeCategoryListResult.getData());
        return baseResult;
    }

    @ApiOperation(value = "新增收费类目", notes = "新增收费类目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目名称", name = "feeCategoryName", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<FeeCategoryDTO> save(@RequestParam String feeCategoryName) throws WzbBackgroundException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        FeeCategoryDTO feeCategoryDTO = new FeeCategoryDTO();
        feeCategoryDTO.setFeeCategoryName(feeCategoryName);
        feeCategoryDTO.setType(FeeCategoryDTO.TYPE_COMMON);
        BaseResult<FeeCategoryDTO> feeCategoryDTOBaseResult = writeFeign.saveFeeCategory(feeCategoryDTO);
        if (feeCategoryDTOBaseResult.getCode() == FEE_CATRGORY_NAME_EXIST_CODE) {
            baseResult.setCode(FEE_CATRGORY_NAME_EXIST_CODE);
            baseResult.setMessage("该类目名称已存在");
            return baseResult;
        }
        FeignUtil.validateFeignResult(feeCategoryDTOBaseResult, "writeFeign.saveFeeCategory", logger);
        baseResult.setData(feeCategoryDTOBaseResult.getData());
        return baseResult;
    }

    @ApiOperation(value = "修改收费类目状态", notes = "修改收费类目状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id", name = "feeCategoryId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "状态，0为开启，1为关闭", name = "status", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public BaseResult<FeeCategoryDTO> changeStatus(@RequestParam long feeCategoryId, @RequestParam Integer status) throws WzbBackgroundException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        if (status < 0 || status > 1) {
            baseResult.setCode(SystemCode.illegal_paramter.getCode());
            baseResult.setMessage("status参数有误");
        }
        BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeCategoryId);
        FeignUtil.validateFeignResult(feeCategoryById, "readFeign.findFeeCategoryById", logger);
        FeeCategoryDTO feeCategoryDTO = feeCategoryById.getData();
        if (feeCategoryDTO.getSchoolId() != null || feeCategoryDTO.getClassId() != null) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("权限不足");
            return baseResult;
        }
        feeCategoryDTO.setStatus(status);
        BaseResult<FeeCategoryDTO> updateFeeCategory = writeFeign.updateFeeCategory(feeCategoryDTO);
        FeignUtil.validateFeignResult(updateFeeCategory, "writeFeign.updateFeeCategory", logger);
        baseResult.setData(updateFeeCategory.getData());
        return baseResult;
    }

    @ApiOperation(value = "交换优先级", notes = "交换优先级")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id1", name = "feeCategoryId1", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "收费类目id2", name = "feeCategoryId2", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/changePriority", method = RequestMethod.POST)
    public BaseResult<Void> changePriority(@RequestParam long feeCategoryId1, @RequestParam long feeCategoryId2) throws WzbBackgroundException {
        BaseResult<Void> baseResult = new BaseResult<>();
        BaseResult<Void> baseResult1 = writeFeign.changeFeeCategoryPriority(feeCategoryId1, feeCategoryId2);
        FeignUtil.validateFeignResult(baseResult1, "writeFeign.changeFeeCategoryPriority", logger);
        return baseResult;
    }
}
