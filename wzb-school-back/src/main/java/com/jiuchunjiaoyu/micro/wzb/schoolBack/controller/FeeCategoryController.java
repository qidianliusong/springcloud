package com.jiuchunjiaoyu.micro.wzb.schoolBack.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.constant.WzbSchoolBackErr;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.FeeCategoryDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.FeeDetailDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.FeePayDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.exception.WzbBackgroundException;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.util.FeignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收费类目接口
 */
@Api(tags = "收费类目相关接口")
@RestController
@RequestMapping("/wzb-school/feeCategory")
public class FeeCategoryController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeCategoryController.class);

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @ApiOperation(value = "获取收费类目列表", notes = "获取收费类目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "开始日期(yyyy-MM-dd HH:mm:ss)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:mm:ss)", name = "endDate", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResult<PageDTO<FeeCategoryDTO>> getList(@RequestParam Long schoolId, @RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize, String startDate, String endDate) throws WzbBackgroundException {
        BaseResult<PageDTO<FeeCategoryDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<FeeCategoryDTO>> pageBySchoolId = readFeign.getFeeCategoryPageBySchoolId(schoolId, pageNo - 1, pageSize, startDate, endDate);
        FeignUtil.validateFeignResult(pageBySchoolId, "readFeign.getFeeCategoryPageBySchoolId", logger);
        baseResult.setData(pageBySchoolId.getData());
        return baseResult;
    }

    @ApiOperation(value = "根据id获取收费类目", notes = "根据id获取收费类目")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分类id", name = "feeCategoryId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public BaseResult<FeeCategoryDTO> findById(Long feeCategoryId) throws WzbBackgroundException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeCategoryId);
        FeignUtil.validateFeignResult(feeCategoryById, "readFeign.findFeeCategoryById", logger);
        baseResult.setData(feeCategoryById.getData());
        return baseResult;
    }

    @ApiOperation(value = "修改收费类目状态", notes = "修改收费类目状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id", name = "feeCategoryId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "状态，0为开启，1为关闭", name = "status", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public BaseResult<FeeCategoryDTO> changeStatus(@RequestParam Long feeCategoryId, @RequestParam Long schoolId, @RequestParam Integer status) throws WzbBackgroundException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        if (status < 0 || status > 1) {
            baseResult.setCode(WzbSchoolBackErr.request_params_err.getCode());
            baseResult.setMessage("status参数有误");
        }
        BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeCategoryId);
        FeignUtil.validateFeignResult(feeCategoryById, "readFeign.findFeeCategoryById", logger);
        FeeCategoryDTO feeCategoryDTO = feeCategoryById.getData();
        if (!schoolId.equals(feeCategoryDTO.getSchoolId())) {
            baseResult.setCode(WzbSchoolBackErr.user_permission_denied.getCode());
            baseResult.setMessage(WzbSchoolBackErr.user_permission_denied.getMessage());
            return baseResult;
        }
        feeCategoryDTO.setStatus(status);
        BaseResult<FeeCategoryDTO> updateFeeCategory = writeFeign.updateFeeCategory(feeCategoryDTO);
        FeignUtil.validateFeignResult(updateFeeCategory, "writeFeign.updateFeeCategory", logger);
        baseResult.setData(updateFeeCategory.getData());
        return baseResult;
    }

    @ApiOperation(value = "新建收费类目", notes = "新建收费类目")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<FeeCategoryDTO> save(@RequestBody @Validated FeeCategoryDTO feeCategoryDTO, BindingResult bindingResult) throws WzbBackgroundException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult))
            return baseResult;
        feeCategoryDTO.setType(FeeCategoryDTO.TYPE_BACKGROUND);
        BaseResult<FeeCategoryDTO> feeCategoryDTOBaseResult = writeFeign.saveFeeCategory(feeCategoryDTO);
        FeignUtil.validateFeignResult(feeCategoryDTOBaseResult, "writeFeign.saveFeeCategory", logger);
        baseResult.setData(feeCategoryDTOBaseResult.getData());
        return baseResult;
    }

    @ApiOperation(value = "修改收费类目", notes = "修改收费类目")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResult<FeeCategoryDTO> update(@RequestBody @Validated FeeCategoryDTO feeCategoryDTO, BindingResult bindingResult) throws WzbBackgroundException {
        BaseResult<FeeCategoryDTO> baseResult = new BaseResult<>();

        if (!validateArgs(bindingResult, baseResult))
            return baseResult;

        if (feeCategoryDTO.getFeeCategoryId() == null) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("feeCategoryId不能为空");
            return baseResult;
        }

        feeCategoryDTO.setType(FeeCategoryDTO.TYPE_BACKGROUND);

        BaseResult<FeeCategoryDTO> feeCategoryDTOBaseResult = writeFeign.updateFeeCategory(feeCategoryDTO);
        FeignUtil.validateFeignResult(feeCategoryDTOBaseResult, "writeFeign.updateFeeCategory", logger);
        baseResult.setData(feeCategoryDTOBaseResult.getData());
        return baseResult;
    }

    @ApiOperation(value = "根据收费类目id获取收费详情列表", notes = "根据收费类目id获取收费详情列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id", name = "feeCategoryId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getFeeDetailList", method = RequestMethod.POST)
    public BaseResult<Map<String, Object>> getFeeDetailList(@RequestParam Long feeCategoryId, @RequestParam(defaultValue = "1") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) throws WzbBackgroundException {

        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        Map<String, Object> resultMap = new HashMap<>();
        BaseResult<BigDecimal> feeDetailSumByCategoryId = readFeign.getFeeDetailSumByCategoryId(feeCategoryId);

        FeignUtil.validateFeignResult(feeDetailSumByCategoryId, "readFeign.getFeeDetailSumByCategoryId", logger);
        resultMap.put("totalAmount", feeDetailSumByCategoryId.getData());
        BaseResult<PageDTO<FeeDetailDTO>> feeDetailPageByCategoryId = readFeign.getFeeDetailPageByCategoryId(feeCategoryId, pageNo - 1, pageSize);
        FeignUtil.validateFeignResult(feeDetailPageByCategoryId, "readFeign.getFeeDetailPageByCategoryId", logger);
        resultMap.put("page", feeDetailPageByCategoryId.getData());

        baseResult.setData(resultMap);

        return baseResult;
    }

    @ApiOperation(value = "根据收费详情id获取缴费信息列表", notes = "根据收费详情id获取缴费信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "支付状态:0为未支付,1为已支付,不传递该值为查询全部", name = "payStatus", required = false, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/getFeePayList", method = RequestMethod.GET)
    public BaseResult<List<FeePayDTO>> getFeeDetailList(@RequestParam(required = true) Long feeDetailId, Integer payStatus) throws WzbBackgroundException {
        BaseResult<List<FeePayDTO>> baseResult = new BaseResult<>();
        BaseResult<List<FeePayDTO>> feePayList = readFeign.getFeePayList(feeDetailId, payStatus);
        FeignUtil.validateFeignResult(feePayList, "readFeign.getFeePayList", logger);
        baseResult.setData(feePayList.getData());
        return baseResult;
    }

    @ApiOperation(value = "获取收费信息", notes = "获取收费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findFeeDetailById", method = RequestMethod.GET)
    public BaseResult<FeeDetailDTO> getFeeDetail(@RequestParam(required = true) Long feeDetailId) throws WzbBackgroundException {
        BaseResult<FeeDetailDTO> baseResult = new BaseResult<>();
        BaseResult<FeeDetailDTO> feeDetailById = readFeign.getFeeDetailById(feeDetailId);
        FeignUtil.validateFeignResult(feeDetailById, "readFeign.getFeeDetailById", logger);
        FeeDetailDTO feeDetailDTO = feeDetailById.getData();
        baseResult.setData(feeDetailDTO);
        return baseResult;
    }
}
