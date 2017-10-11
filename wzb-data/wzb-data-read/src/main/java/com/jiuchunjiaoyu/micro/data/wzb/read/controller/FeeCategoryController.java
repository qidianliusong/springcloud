package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.WzbDataReadErr;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.ClassAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeCategoryMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/query/feeCategory")
@Api("班费类别查询")
public class FeeCategoryController extends CommonBaseController {

    @Resource
    private FeeCategoryMng feeCategoryMng;

    @Resource
    private ClassAccountMng classAccountMng;

    @ApiOperation(value = "前台系统获取班费类别列表", notes = "前台系统获取班费类别列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "年级id", name = "gradeId", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<List<FeeCategory>> getList(@RequestParam(required = false) Long schoolId, @RequestParam(required = false) Long classId,
                                                 @RequestParam(required = false) Long gradeId) {
        BaseResult<List<FeeCategory>> baseResult = new BaseResult<>();

        List<FeeCategory> list = new ArrayList<>();
        List<FeeCategory> basicCategory = feeCategoryMng.getBasicCategory();
        if (basicCategory != null && !basicCategory.isEmpty())
            list.addAll(basicCategory);
        if (classId != null) {
            List<FeeCategory> categoryByClassId = feeCategoryMng.getCategoryByClassId(classId);
            if (categoryByClassId != null && !categoryByClassId.isEmpty())
                list.addAll(categoryByClassId);
        }
        if (schoolId != null) {
            if (gradeId == null && classId != null) {
                ClassAccount classAccount = classAccountMng.findByClassId(classId);
                gradeId = classAccount.getGradeId();
            }
            if (gradeId != null) {
                List<FeeCategory> categoryBySchoolIdAndGradeId = feeCategoryMng.getCategoryBySchoolIdAndGradeId(schoolId, gradeId);
                if (categoryBySchoolIdAndGradeId != null && !categoryBySchoolIdAndGradeId.isEmpty())
                    list.addAll(categoryBySchoolIdAndGradeId);
            } else {
                List<FeeCategory> categoryBySchoolId = feeCategoryMng.getOpenCategoryBySchoolId(schoolId);
                if (categoryBySchoolId != null && !categoryBySchoolId.isEmpty())
                    list.addAll(categoryBySchoolId);
            }
        }

        baseResult.setData(list);
//        baseResult.setData(feeCategoryMng.getCategoryList(schoolId, classId));
        return baseResult;
    }

    @ApiOperation(value = "前台系统获取班费类别列表", notes = "前台系统获取班费类别列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "状态", name = "status", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/listForBack", method = RequestMethod.GET)
    public BaseResult<List<FeeCategory>> getList(@RequestParam(required = false) Long schoolId, @RequestParam(required = false) Long classId,
                                                 @RequestParam(required = false) Integer status) {
        BaseResult<List<FeeCategory>> baseResult = new BaseResult<>();
        baseResult.setData(feeCategoryMng.getCategoryList(schoolId, classId, status));
        return baseResult;
    }

    @ApiOperation(value = "根据id获取班费类目信息", notes = "根据id获取班费类目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班费类目id", name = "feeCategoryId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public BaseResult<FeeCategory> findById(Long feeCategoryId) {
        BaseResult<FeeCategory> baseResult = new BaseResult<>();

        FeeCategory feeCategory = feeCategoryMng.findOne(feeCategoryId);
        if (feeCategory == null) {
            baseResult.setCode(WzbDataReadErr.entity_not_exist.getCode());
            baseResult.setMessage(WzbDataReadErr.entity_not_exist.getMessage());
            return baseResult;
        }
        if (feeCategory.getFeeCategoryGradeSet() != null && feeCategory.getFeeCategoryGradeSet().size() > 0) {
            if (feeCategory.getFeeCategoryGrades() == null)
                feeCategory.setFeeCategoryGrades(new ArrayList<>());
            feeCategory.getFeeCategoryGrades().addAll(feeCategory.getFeeCategoryGradeSet());
        }
        baseResult.setData(feeCategory);
        return baseResult;
    }

    @ApiOperation(value = "根据学校id获取班费类别列表", notes = "供学校后台使用的班费类别列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "开始日期", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期", name = "endDate", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getPageBySchoolId", method = RequestMethod.GET)
    public BaseResult<Page<FeeCategory>> getPageBySchoolId(@RequestParam Long schoolId, @RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize, Date startDate, Date endDate) {
        if (pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<FeeCategory>> baseResult = new BaseResult<>();
        if (startDate == null) {
            baseResult.setData(feeCategoryMng.getCategoryPageBySchoolId(schoolId, pageNo, pageSize, true));
            return baseResult;
        }
        if (endDate == null) {
            endDate = new Date();
        }
        baseResult.setData(feeCategoryMng.getCategoryPageBySchoolId(schoolId, startDate, endDate, pageNo, pageSize, true));
        return baseResult;
    }
}
