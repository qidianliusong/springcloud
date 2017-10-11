package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.RoleEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 微智宝
 */
@Api(value = "微智宝班级账户详情接口")
@RestController
@RequestMapping("/wzb/classAccount")
public class ClassAccountController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(ClassAccountController.class);

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @ApiOperation(value = "获取班级账户信息", notes = "获取班级账户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "/getBaseInfo", method = RequestMethod.GET)
    public BaseResult<WzbFullInfoDTO> getWzbFullInfo(@RequestParam String token, @RequestParam Long classId, HttpSession session) throws WzbAggregateException {

        BaseResult<WzbFullInfoDTO> baseResult = new BaseResult<>();

        if (classId == null) {
            baseResult.setCode(WzbAggregateErrEnum.request_params_err.getCode());
            baseResult.setMessage("classId不能为空");
            return baseResult;
        }

        WzbFullInfoDTO wzbFullInfoDTO = new WzbFullInfoDTO();
        baseResult.setData(wzbFullInfoDTO);

        UserDTO currentUser = (UserDTO) session.getAttribute(WzbConstant.SESSION_USER_NAME);
        if (currentUser == null) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }

        //调用php接口获取班级详细信息
        PhpResult<ResultList<ClassDTO>> classDetailResult = classFeign.getClassDetail(token, classId);
        if (SystemCode.success.getCode() != classDetailResult.getCode()) {
            logger.warn("调用classFeign.getClassDetail出错,原因：" + classDetailResult.getMsg());
            baseResult.setCode(classDetailResult.getCode());
            baseResult.setMessage("调用classFeign.getClassDetail出错,原因：" + classDetailResult.getMsg());
            return baseResult;
        }
        ResultList<ClassDTO> classDetailList = classDetailResult.getData();
        if (classDetailList == null || classDetailList.getList() == null || classDetailList.getList().isEmpty()) {
            baseResult.setCode(WzbAggregateErrEnum.class_not_exists.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.class_not_exists.getMessage());
            return baseResult;
        }

        ClassDTO classDTO = classDetailList.getList().get(0);

        wzbFullInfoDTO.setClassId(classId);
        wzbFullInfoDTO.setClassName(classDTO.getName());
        if (classDTO.getGrade() != null)
            wzbFullInfoDTO.setGradeName(classDTO.getGrade().getName());
        wzbFullInfoDTO.setStudentCount(classDTO.getStudentCount());

        String kinsfolk = getKinsfolk(classId, currentUser.getId(), token);
        if(!StringUtils.isBlank(kinsfolk)){
            currentUser.setKinsfolk(kinsfolk);
            session.setAttribute(WzbConstant.SESSION_USER_NAME, currentUser);
        }

        //班主任信息
        UserDTO master = classDTO.getMaster();
        master.setKinsfolk(master.getRealname() + "老师");
        if (wzbFullInfoDTO.getCommitteeUsers() == null) {
            wzbFullInfoDTO.setCommitteeUsers(new ArrayList<>());
        }
        wzbFullInfoDTO.getCommitteeUsers().add(master);

        List<UserDTO> committeeUsers = getCommitteeUsers(token, classId);
        if (committeeUsers != null && !committeeUsers.isEmpty()) {
            wzbFullInfoDTO.getCommitteeUsers().addAll(committeeUsers);
        }

        if (UserDTO.COME_FORM_TEACHER == currentUser.getComeFrom()) {
            if (currentUser.getId().equals(master.getId())) {
                wzbFullInfoDTO.setRoleEnum(RoleEnum.class_master.getId());
            } else {
                wzbFullInfoDTO.setRoleEnum(RoleEnum.teacher.getId());
            }
        } else {
            if (isCommittee(classId, currentUser.getId(), token))
                wzbFullInfoDTO.setRoleEnum(RoleEnum.committee.getId());
            else
                wzbFullInfoDTO.setRoleEnum(RoleEnum.parent.getId());
        }

        wzbFullInfoDTO.setSchoolId(classDTO.getSchoolId());

        initClassAccount(classId, baseResult, classDTO);

        initSchoolAccount(classDTO);

        BaseResult<ClassAccountDTO> classAccountResult = readFeign.getClassAccountByClassId(classId);
        if (SystemCode.success.getCode() != classAccountResult.getCode()) {
            logger.warn("调用readFeign.getClassAccountByClassId失败，原因:" + classAccountResult.getMessage());
            baseResult.setCode(classAccountResult.getCode());
            baseResult.setMessage("调用readFeign.getClassAccountByClassId失败，原因:" + classAccountResult.getMessage());
            return baseResult;
        }
        ClassAccountDTO classAccountDTO = classAccountResult.getData();

        wzbFullInfoDTO.setAccountingAmount(classAccountDTO.getAccountingAmount());
        wzbFullInfoDTO.setAmount(classAccountDTO.getAmount());
        wzbFullInfoDTO.setUsedAmount(classAccountDTO.getUsedAmount());
        return baseResult;
    }

    private void initSchoolAccount(ClassDTO classDTO) throws WzbAggregateException {
        BaseResult<Boolean> existsSchoolAccount = readFeign.existsSchoolAccount(classDTO.getSchoolId());
        FeignUtil.validateFeignResult(existsSchoolAccount, "readFeign.existsSchoolAccount", logger);
        if (!existsSchoolAccount.getData()) {
            SchoolAccountDTO schoolAccountDTO = new SchoolAccountDTO();
            schoolAccountDTO.setSchoolAmount(new BigDecimal(0));
            schoolAccountDTO.setSchoolId(classDTO.getSchoolId());
            if (classDTO.getSchool() != null){
                schoolAccountDTO.setSchoolName(classDTO.getSchool().getName());
                schoolAccountDTO.setProvince(classDTO.getSchool().getProvince());
                schoolAccountDTO.setCity(classDTO.getSchool().getCity());
                schoolAccountDTO.setCounty(classDTO.getSchool().getCounty());
                schoolAccountDTO.setTown(classDTO.getSchool().getTown());
            }
            BaseResult<SchoolAccountDTO> schoolAccountDTOBaseResult = writeFeign.saveSchoolAccount(schoolAccountDTO);
            FeignUtil.validateFeignResult(schoolAccountDTOBaseResult,"writeFeign.saveSchoolAccount",logger);
        }
    }

    private void initClassAccount(@RequestParam Long classId, BaseResult<WzbFullInfoDTO> baseResult, ClassDTO classDTO) throws WzbAggregateException {
        BaseResult<Boolean> classAccountExistsResult = readFeign.classAccountExistsByClassId(classId);

        FeignUtil.validateFeignResult(classAccountExistsResult, "readFeign.classAccountExistsByClassId", logger);
        //班级账户不存在，创建账户
        if (!classAccountExistsResult.getData()) {
            ClassAccountDTO classAccountDTO = new ClassAccountDTO();
            classAccountDTO.setClassId(classId);
            classAccountDTO.setClassName(classDTO.getName());
            classAccountDTO.setSchoolId(classDTO.getSchoolId());
            classAccountDTO.setGradeId(classDTO.getGradeId());
            if(classDTO.getGrade()!=null)
                classAccountDTO.setGradeName(classDTO.getGrade().getName());
            if (classDTO.getSchool() != null)
                classAccountDTO.setSchoolName(classDTO.getSchool().getName());
            classAccountDTO.setAccountingAmount(new BigDecimal(0));
            classAccountDTO.setAmount(new BigDecimal(0));
            classAccountDTO.setUsedAmount(new BigDecimal(0));
            classAccountDTO.setUsedAmount(new BigDecimal(0));
            BaseResult<Void> saveClassAccount = writeFeign.saveClassAccount(classAccountDTO);
            FeignUtil.validateFeignResult(saveClassAccount, "writeFeign.saveClassAccount", logger);
        }
    }

}
