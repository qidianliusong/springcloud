package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.ClassDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultList;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.ClassFeign;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 普通controller基类，提供部分基础功能
 */
public class CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(CommonBaseController.class);

    @Resource
    protected ClassFeign classFeign;

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    BaseResult<Void> exceptionHandler(Exception exception) {
        BaseResult<Void> baseResult = new BaseResult<>();
        if (exception instanceof WzbAggregateException) {
            WzbAggregateException we = (WzbAggregateException) exception;
            baseResult.setCode(we.getCode());
            baseResult.setMessage(we.getMessage());
            logger.error(exception.getMessage(), exception);
            return baseResult;
        }
        baseResult.setCode(SystemCode.systemErr.getCode());
        baseResult.setMessage(SystemCode.systemErr.getMessage() + ":" + exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return baseResult;
    }

    protected boolean validateArgs(BindingResult bindingResult, BaseResult<?> baseResult) {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            String fieldName = "";
            if (objectError instanceof FieldError) {
                fieldName = ((FieldError) objectError).getField();
            }
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage(fieldName + objectError.getDefaultMessage());
            return false;
        }
        return true;
    }

    /**
     * 验证feign接口返回数据
     *
     * @param result
     * @param feignResult
     * @param feignStr
     * @return
     */
    protected boolean validateFeignResult(BaseResult<?> result, BaseResult<?> feignResult, String feignStr,
                                          Logger logger) {
        if (SystemCode.success.getCode() != feignResult.getCode()) {
            logger.warn("调用" + feignStr + "失败，原因:" + feignResult.getMessage());
            result.setCode(feignResult.getCode());
            result.setMessage("调用" + feignStr + "失败，原因:" + feignResult.getMessage());
            return false;
        }
        return true;
    }

    protected ClassDTO getClassDTO(Long classId, String token, BaseResult<?> baseResult) {
        PhpResult<ResultList<ClassDTO>> classDetailResult = classFeign.getClassDetail(token, classId);
        if (SystemCode.success.getCode() != classDetailResult.getCode()) {
            logger.warn("调用classFeign.getClassDetail出错,原因：" + classDetailResult.getMsg());
            baseResult.setCode(classDetailResult.getCode());
            baseResult.setMessage("调用classFeign.getClassDetail出错,原因：" + classDetailResult.getMsg());
            return null;
        }
        ResultList<ClassDTO> classDetailList = classDetailResult.getData();
        if (classDetailList == null || classDetailList.getList() == null || classDetailList.getList().isEmpty()
                || classDetailList.getList().get(0) == null) {
            baseResult.setCode(WzbAggregateErrEnum.class_not_exists.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.class_not_exists.getMessage());
            return null;
        }

        return classDetailList.getList().get(0);
    }


    /**
     * 获取班级信息
     *
     * @param token
     * @param classId
     * @return
     */
    protected ClassDTO getClassDTO(String token, Long classId) throws WzbAggregateException {
        //调用php接口获取班级详细信息
        PhpResult<ResultList<ClassDTO>> classDetailResult = classFeign.getClassDetail(token, classId);
        if (SystemCode.success.getCode() != classDetailResult.getCode()) {
            logger.warn("调用classFeign.getClassDetail出错,原因：" + classDetailResult.getMsg());
            throw new WzbAggregateException(classDetailResult.getCode(), "调用classFeign.getClassDetail出错,原因：" + classDetailResult.getMsg());
        }
        ResultList<ClassDTO> classDetailList = classDetailResult.getData();
        if (classDetailList == null || classDetailList.getList() == null || classDetailList.getList().isEmpty()) {
            throw new WzbAggregateException(WzbAggregateErrEnum.class_not_exists.getCode(), WzbAggregateErrEnum.class_not_exists.getMessage());
        }
        return classDetailList.getList().get(0);
    }

    /**
     * 获取班委会成员
     *
     * @param token
     * @param classId
     * @return
     */
    protected List<UserDTO> getCommitteeUsers(String token, Long classId) {
        try {
            PhpResult<ResultList<UserDTO>> orgClassParents = classFeign.getOrgClassParents(token, classId);
            if (SystemCode.success.getCode() != orgClassParents.getCode()) {
                logger.warn("调用classFeign.getOrgClassParents出错,原因：" + orgClassParents.getMsg());
                return null;
            }
            List<UserDTO> committeeUsers = orgClassParents.getData().getList();
            return committeeUsers;
        } catch (Exception e) {
            logger.error("调用调用classFeign.getOrgClassParents出错", e);
            return null;
        }
    }

    /**
     * 是否班委会成员
     *
     * @return
     */
    protected boolean isCommittee(Long classId, Long userId, String token) {
        UserDTO master;
        try {
            ClassDTO classDTO = getClassDTO(token, classId);
            master = classDTO.getMaster();
        } catch (WzbAggregateException e) {
            logger.error("获取班级信息出错", e);
            return false;
        }
        if (master == null)
            return false;
        if (userId.equals(master.getId()))
            return true;
        List<UserDTO> committeeUsers = getCommitteeUsers(token, classId);
        if (committeeUsers != null && !committeeUsers.isEmpty()) {
            return committeeUsers.stream().anyMatch(dto -> userId.equals(dto.getId()));
        }
        return false;
    }

    /**
     * 获取XX爸爸或者XX老师称谓
     * @param classId
     * @param userId
     * @param token
     * @return
     */
    protected String getKinsfolk(Long classId, Long userId, String token) {
        UserDTO master;
        try {
            ClassDTO classDTO = getClassDTO(token, classId);
            master = classDTO.getMaster();
        } catch (WzbAggregateException e) {
            logger.error("获取班级信息出错", e);
            return null;
        }
        if (master == null)
            return null;
        if (userId.equals(master.getId()))
            return master.getRealname() + "老师";
        List<UserDTO> committeeUsers = getCommitteeUsers(token, classId);
        if (committeeUsers != null && !committeeUsers.isEmpty()) {
            for (UserDTO user : committeeUsers) {
                if (userId.equals(user.getId()))
                    return user.getKinsfolk();
            }
        }
        return null;
    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    protected String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PROXY_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        return ip;

    }
}
