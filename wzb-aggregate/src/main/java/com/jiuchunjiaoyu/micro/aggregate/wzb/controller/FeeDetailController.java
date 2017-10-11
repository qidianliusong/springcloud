package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.annotation.TokenAnnotation;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultList;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 记账controller
 */
@Api(value = "记账相关接口")
@RestController
@RequestMapping("/wzb/feeDetail")
public class FeeDetailController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeDetailController.class);

    @Resource
    private WzbDataWriteFeign writeFeign;

    @Resource
    private WzbDataReadFeign readFeign;

    @ApiOperation(value = "发布收费信息", notes = "发布收费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "防止重复提交token", name = "validateToken", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @TokenAnnotation
    public BaseResult<FeeDetailDTO> saveOrUpdate(@Validated FeeDetailDTO feeDetailDTO, BindingResult bindingResult,
                                                 @RequestParam(required = true) String token, HttpSession session) throws WzbAggregateException {

        BaseResult<FeeDetailDTO> baseResult = new BaseResult<>();

        if (!validateArgs(bindingResult, baseResult)) {
            return baseResult;
        }
        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        // 判断用户权限
        if (currentUser == null || !isCommittee(feeDetailDTO.getClassId(), currentUser.getId(), token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }

        ClassDTO classDTO = getClassDTO(feeDetailDTO.getClassId(), token, baseResult);
        if (classDTO == null) {
            return baseResult;
        }
        try {
            if (feeDetailDTO.getFeeCategoryId() != null) {
                BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeDetailDTO.getFeeCategoryId());
                FeignUtil.validateFeignResult(feeCategoryById, "readFeign.findFeeCategoryById", logger);
                FeeCategoryDTO feeCategoryDTO = feeCategoryById.getData();
                if (feeCategoryDTO.getType() == FeeCategoryDTO.TYPE_BACKGROUND) {
                    feeDetailDTO.setAmount(feeCategoryDTO.getAmount());
                    feeDetailDTO.setFeeCategoryName(feeCategoryDTO.getFeeCategoryName());
                    feeDetailDTO.setFeeDetailDesc(feeCategoryDTO.getFeeCategoryDesc());
                }
            }
        } catch (Exception e) {
            logger.error("获取收费类目信息出错,feeCategoryId=" + feeDetailDTO.getFeeCategoryId());
        }

        List<FeePayDTO> feePayDTOs = initFeePays(feeDetailDTO, token);
        feeDetailDTO.setFeePays(feePayDTOs);
        feeDetailDTO.setStudentCount(classDTO.getStudentCount());
        feeDetailDTO.setStatus(0);
        feeDetailDTO.setReleaseUserId(currentUser.getId());
        feeDetailDTO.setSchoolId(classDTO.getSchoolId());
        feeDetailDTO.setClassName(classDTO.getName());

        String kinsfolk = currentUser.getKinsfolk();
        if (StringUtils.isBlank(kinsfolk)) {
            kinsfolk = getKinsfolk(feeDetailDTO.getClassId(), currentUser.getId(), token);
            currentUser.setKinsfolk(kinsfolk);
            session.setAttribute(WzbConstant.SESSION_USER_NAME, currentUser);
        }
        feeDetailDTO.setReleaseUserName(StringUtils.isBlank(kinsfolk) ? currentUser.getRealname() : kinsfolk);
        //设置头像
        feeDetailDTO.setReleaseUserImg(currentUser.getHeadimgurl());
        if (feeDetailDTO.getFeeDetailId() == null) {
            BaseResult<FeeDetailDTO> saveFeeDetail = writeFeign.saveFeeDetail(feeDetailDTO);
            if (!validateFeignResult(baseResult, saveFeeDetail, "writeFeign.saveFeeDetail", logger)) {
                return baseResult;
            }
            try {
                FeeDetailDTO f = saveFeeDetail.getData();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                classFeign.publishFeeDetail(token, f.getFeeDetailId(), f.getFeeCategoryName(), f.getFeeDetailDesc(), f.getAmount().toString(), f.getSchoolId(),
                        f.getClassId(), f.getReleaseUserId().toString(), f.getReleaseUserName(), dateFormat.format(f.getCreateTime()), "0");
            } catch (Exception e) {
                logger.error("发布收费信息到班级圈出错", e);
            }

            baseResult.setData(saveFeeDetail.getData());
        } else {
            BaseResult<FeeDetailDTO> updateFeeDetail = writeFeign.updateFeeDetail(feeDetailDTO);
            try {
                FeeDetailDTO f = updateFeeDetail.getData();
                classFeign.editFeeDetail(token, f.getFeeDetailId(), f.getFeeCategoryName(), f.getFeeDetailDesc(), f.getAmount().toString(), f.getStatus().toString());
            } catch (Exception e) {
                logger.error("修改班级圈收费信息出错", e);
            }
            if (!validateFeignResult(baseResult, updateFeeDetail, "writeFeign.updateFeeDetail", logger)) {
                return baseResult;
            }
            baseResult.setData(updateFeeDetail.getData());
        }

        return baseResult;

    }

    private List<FeePayDTO> initFeePays(FeeDetailDTO feeDetailDTO, String token) throws WzbAggregateException {
        List<StudentDTO> studentDTOs = getStudentsByClassId(feeDetailDTO.getClassId(), token);
        List<FeePayDTO> feePayDTOs = new ArrayList<>();
        for (StudentDTO studentDTO : studentDTOs) {
            FeePayDTO feePay = new FeePayDTO();
            feePay.setAmount(feeDetailDTO.getAmount());
            feePay.setChildId(studentDTO.getId());
            feePay.setChildName(studentDTO.getName());
            feePay.setStatus(FeePayDTO.STATUS_NOT_PAY);
            feePay.setAvatarUrl(studentDTO.getAvatarUrl());
            feePayDTOs.add(feePay);
        }
        return feePayDTOs;
    }

    @ApiOperation(value = "获取收费信息", notes = "获取收费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "当前孩子id，没有孩子不需要传该参数", name = "childId", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public BaseResult<FeeDetailDTO> getFeeDetail(@RequestParam(required = true) Long feeDetailId, @RequestParam(required = false) Long childId) throws WzbAggregateException {
        BaseResult<FeeDetailDTO> baseResult = new BaseResult<>();
        BaseResult<FeeDetailDTO> feeDetailById = readFeign.getFeeDetailById(feeDetailId);
        if (!validateFeignResult(baseResult, feeDetailById, "readFeign.getFeeDetailById", logger))
            return baseResult;
        FeeDetailDTO feeDetailDTO = feeDetailById.getData();
        baseResult.setData(feeDetailDTO);
        if (childId == null)
            return baseResult;
        try {
            BaseResult<FeePayDTO> feePayDTOBaseResult = readFeign.getFeePay(feeDetailId, childId);
            FeignUtil.validateFeignResult(feePayDTOBaseResult, "readFeign.getFeePay", logger);
            if (FeePayDTO.STATUS_PAY == feePayDTOBaseResult.getData().getStatus()) {
                feeDetailDTO.setPaid(true);
            } else {
                feeDetailDTO.setPaid(false);
            }
        } catch (Exception e) {
            logger.error("获取当前用户的交费记录出错", e);
        }

        return baseResult;
    }

    @ApiOperation(value = "获取收费详情分页信息", notes = "获取收费详情分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "孩子id", name = "childId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "状态，0为开启，1为关闭", name = "status", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getPageInfo", method = RequestMethod.GET)
    public BaseResult<PageDTO<FeeDetailDTO>> getPage(@RequestParam(required = true) Long classId, @RequestParam(required = false) Integer status,
                                                     @RequestParam(required = false) Long childId,
                                                     @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        BaseResult<PageDTO<FeeDetailDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<FeeDetailDTO>> feeDetailPage = readFeign.getFeeDetailPage(classId, childId, status, pageNo - 1, pageSize);
        if (!validateFeignResult(baseResult, feeDetailPage, "readFeign.getFeeDetailPage", logger))
            return baseResult;
        baseResult.setData(feeDetailPage.getData());
        return baseResult;
    }

    @ApiOperation(value = "收费详情开启或关闭", notes = "收费详情开启或关闭")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public BaseResult<Void> changeStatus(Long feeDetailId, String token, HttpSession session) throws WzbAggregateException {
        BaseResult<Void> baseResult = new BaseResult<>();

        BaseResult<FeeDetailDTO> feeDetailDTOBaseResult = readFeign.getFeeDetailById(feeDetailId);
        FeignUtil.validateFeignResult(feeDetailDTOBaseResult, "readFeign.getFeeDetailById", logger);
        FeeDetailDTO feeDetailDTO = feeDetailDTOBaseResult.getData();
        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        // 判断用户权限
        if (currentUser == null || !isCommittee(feeDetailDTO.getClassId(), currentUser.getId(), token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }


        BaseResult<Void> changeResult = writeFeign.changeFeeDetailStatus(feeDetailId);
        FeignUtil.validateFeignResult(changeResult, "writeFeign.changeFeeDetailStatus", logger);

        try {
            classFeign.editFeeDetail(token, feeDetailDTO.getFeeDetailId(), feeDetailDTO.getFeeCategoryName(), feeDetailDTO.getFeeDetailDesc(),
                    feeDetailDTO.getAmount().toString(), feeDetailDTO.getStatus() == 0 ? "1" : "0");
        } catch (Exception e) {
            logger.error("修改班级圈收费信息出错", e);
        }

        return baseResult;
    }

    /**
     * 获取孩子
     *
     * @param classId
     * @param token
     * @return
     * @throws WzbAggregateException
     */
    private List<StudentDTO> getStudentsByClassId(Long classId, String token) throws WzbAggregateException {
        PhpResult<ResultList<StudentDTO>> studentsByClassid = classFeign.getStudentsByClassid(token, classId);
        if (SystemCode.success.getCode() != studentsByClassid.getCode()) {
            logger.warn("调用classFeign.getStudentsByClassid出错,原因：" + studentsByClassid.getMsg());
            throw new WzbAggregateException(studentsByClassid.getCode(),
                    "调用classFeign.getStudentsByClassid出错,原因：" + studentsByClassid.getMsg());
        }
        ResultList<StudentDTO> resultList = studentsByClassid.getData();
        if (resultList == null || resultList.getList() == null)
            return new ArrayList<>();
        return resultList.getList();
    }
}
