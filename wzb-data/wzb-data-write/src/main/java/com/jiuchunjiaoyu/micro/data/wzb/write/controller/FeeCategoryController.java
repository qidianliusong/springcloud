package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeCategoryMng;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;

/**
 * 收费类目控制器
 */
@RestController
@RequestMapping("/operate/feeCategory")
public class FeeCategoryController extends CommonBaseController {

    @Resource
    private FeeCategoryMng feeCategoryMng;

    @ApiOperation(value = "新增收费类目", notes = "新增收费类目")
    @ApiImplicitParams({
            // @ApiImplicitParam(value="收费详情id",name="feeDetailId",required=true,dataType="Long",paramType="query")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult<FeeCategory> save(@RequestBody @Validated FeeCategory feeCategory, BindingResult bindingResult) {
        BaseResult<FeeCategory> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult)) {
            return baseResult;
        }

        if (!validateFeeCategory(feeCategory, baseResult)) {
            return baseResult;
        }

        if (feeCategory.getClassId() == null && feeCategory.getSchoolId() == null && feeCategoryMng.existByFeeCategoryName(feeCategory.getFeeCategoryName())) {
            baseResult.setCode(WzbDataWriteErr.fee_category_name_exist.getCode());
            baseResult.setMessage(WzbDataWriteErr.fee_category_name_exist.getMessage());
            return baseResult;
        }

        feeCategory.setFeeCategoryId(null);
        if (feeCategory.getStatus() == null)
            feeCategory.setStatus(0);
        Date now = new Date();
        feeCategory.setCreateDate(now);
        feeCategory.setUpdateDate(now);
        if (feeCategory.getAllGrade() == null)
            feeCategory.setAllGrade(false);
        if (!feeCategory.getAllGrade() && feeCategory.getFeeCategoryGrades() != null && !feeCategory.getFeeCategoryGrades().isEmpty()) {
            if (feeCategory.getFeeCategoryGradeSet() == null)
                feeCategory.setFeeCategoryGradeSet(new HashSet<>());
            feeCategory.getFeeCategoryGrades().forEach(feeCategoryGrade -> {
                feeCategoryGrade.setFeeCategory(feeCategory);
                feeCategory.getFeeCategoryGradeSet().add(feeCategoryGrade);
            });
        }
        FeeCategory save = feeCategoryMng.saveFeeCategory(feeCategory);
        baseResult.setData(save);
        return baseResult;
    }


    @ApiOperation(value = "修改收费类目", notes = "修改收费类目")
    @ApiImplicitParams({
            // @ApiImplicitParam(value="收费详情id",name="feeDetailId",required=true,dataType="Long",paramType="query")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResult<FeeCategory> update(@RequestBody @Validated FeeCategory feeCategory, BindingResult bindingResult) {
        BaseResult<FeeCategory> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult)) {
            return baseResult;
        }
        if (feeCategory.getFeeCategoryId() == null) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("feeCategoryId不能为空");
            return baseResult;
        }

        FeeCategory feeCategoryFromDb = feeCategoryMng.findOne(feeCategory.getFeeCategoryId());

        if (feeCategoryFromDb == null) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("不存在的类目id");
            return baseResult;
        }

        if (!validateFeeCategory(feeCategory, baseResult)) {
            return baseResult;
        }

        if (feeCategory.getSchoolId() == null
                && feeCategory.getClassId() == null
                && !feeCategory.getFeeCategoryName().equals(feeCategoryFromDb.getFeeCategoryName())
                && feeCategoryMng.existByFeeCategoryName(feeCategory.getFeeCategoryName())) {
            baseResult.setCode(WzbDataWriteErr.fee_category_name_exist.getCode());
            baseResult.setMessage(WzbDataWriteErr.fee_category_name_exist.getMessage());
            return baseResult;
        }

        if (feeCategory.getStatus() == null)
            feeCategory.setStatus(0);

        if (feeCategory.getAllGrade() == null)
            feeCategory.setAllGrade(false);

        feeCategory.setPriority(feeCategoryFromDb.getPriority());

        if (!feeCategory.getAllGrade() && feeCategory.getFeeCategoryGrades() != null && !feeCategory.getFeeCategoryGrades().isEmpty()) {
            if (feeCategory.getFeeCategoryGradeSet() == null)
                feeCategory.setFeeCategoryGradeSet(new HashSet<>());
            feeCategory.getFeeCategoryGradeSet().clear();
            feeCategory.getFeeCategoryGrades().forEach(feeCategoryGrade -> {
                feeCategoryGrade.setFeeCategory(feeCategory);
                feeCategory.getFeeCategoryGradeSet().add(feeCategoryGrade);
            });
        } else {
            if (feeCategory.getFeeCategoryGradeSet() != null && !feeCategory.getFeeCategoryGradeSet().isEmpty()) {
                feeCategory.getFeeCategoryGradeSet().clear();
            }
        }

        Date now = new Date();
        feeCategory.setUpdateDate(now);
        FeeCategory update = feeCategoryMng.update(feeCategory);
        baseResult.setData(update);
        return baseResult;
    }

    /**
     * 验证合法性
     *
     * @param feeCategory
     * @param baseResult
     * @return
     */
    private boolean validateFeeCategory(FeeCategory feeCategory, BaseResult<FeeCategory> baseResult) {

        switch (feeCategory.getType()) {
            case FeeCategory.TYPE_COMMON:
                return true;
            case FeeCategory.TYPE_BACKGROUND:
                if (feeCategory.getAmount() == null || feeCategory.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    baseResult.setCode(SystemCode.validateFail.getCode());
                    baseResult.setMessage("amount数据错误");
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @ApiOperation(value = "交换优先级", notes = "交换优先级")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id1", name = "feeCategoryId1", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "收费类目id2", name = "feeCategoryId2", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/changePriority", method = RequestMethod.POST)
    public BaseResult<Void> changePriority(@RequestParam long feeCategoryId1, @RequestParam long feeCategoryId2) {
        BaseResult<Void> baseResult = new BaseResult<>();
        FeeCategory feeCategory1 = feeCategoryMng.findOne(feeCategoryId1);
        FeeCategory feeCategory2 = feeCategoryMng.findOne(feeCategoryId2);
        if (feeCategory1 == null || feeCategory2 == null) {
            baseResult.setCode(WzbDataWriteErr.entity_not_exist.getCode());
            baseResult.setMessage("不存在的收费类目");
            return baseResult;
        }
        Integer temp = feeCategory1.getPriority();
        feeCategory1.setPriority(feeCategory2.getPriority());
        feeCategory2.setPriority(temp);
        feeCategoryMng.save(feeCategory1);
        feeCategoryMng.save(feeCategory2);
        return baseResult;
    }
}
