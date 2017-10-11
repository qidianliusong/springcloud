package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.FeeTakeDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
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

/**
 * 记账controller
 */
@Api(value = "记账相关接口")
@RestController
@RequestMapping("/wzb/feeTake")
public class FeeTakeController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeTakeController.class);

    @Resource
    private WzbDataWriteFeign writeFeign;

    @Resource
    private WzbDataReadFeign readFeign;

    @ApiOperation(value = "新建或修改记账", notes = "新建或修改记账信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),})
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public BaseResult<FeeTakeDTO> saveOrUpdate(@Validated FeeTakeDTO feeTakeDTO, BindingResult bindingResult, String token, HttpSession session) {
        BaseResult<FeeTakeDTO> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult)) {
            return baseResult;
        }
        UserDTO currentUser = (UserDTO) session.getAttribute("user");

        // 判断用户权限
        if (currentUser == null || !isCommittee(feeTakeDTO.getClassId(), currentUser.getId(), token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }
        feeTakeDTO.setUserId(currentUser.getId());

        String kinsfolk = currentUser.getKinsfolk();
        if (StringUtils.isBlank(kinsfolk)) {
            kinsfolk = getKinsfolk(feeTakeDTO.getClassId(), currentUser.getId(), token);
            currentUser.setKinsfolk(kinsfolk);
            session.setAttribute(WzbConstant.SESSION_USER_NAME, currentUser);
        }
        feeTakeDTO.setUserName(StringUtils.isBlank(kinsfolk) ? currentUser.getRealname() : kinsfolk);
        BaseResult<FeeTakeDTO> saveOrUpdateFeeTake = writeFeign.saveOrUpdateFeeTake(feeTakeDTO);
        if (!validateFeignResult(baseResult, saveOrUpdateFeeTake, "writeFeign.saveOrUpdateFeeTake", logger)) {
            return baseResult;
        }
        baseResult.setData(saveOrUpdateFeeTake.getData());
        return baseResult;
    }

    @ApiOperation(value = "记账分页数据", notes = "记账分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query")})
    @RequestMapping(value = "/pageInfo", method = RequestMethod.POST)
    public BaseResult<PageDTO<FeeTakeDTO>> getPageInfo(@RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize, Long classId) {
        BaseResult<PageDTO<FeeTakeDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<FeeTakeDTO>> feeTakePageInfo = readFeign.getFeeTakePageInfo(classId, pageNo, pageSize);
        if (!validateFeignResult(baseResult, feeTakePageInfo, "readFeign.getFeeTakePageInfo", logger)) {
            return baseResult;
        }
        baseResult.setData(feeTakePageInfo.getData());
        return baseResult;
    }

    @ApiOperation(value = "删除记账", notes = "删除记账信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "记账id", name = "feeTakeId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public BaseResult<Void> delete(Long feeTakeId, String token, HttpSession session) throws WzbAggregateException {
        BaseResult<Void> baseResult = new BaseResult<>();
        BaseResult<FeeTakeDTO> feeTakeDTOBaseResult = readFeign.getFeeTakeById(feeTakeId);
        FeignUtil.validateFeignResult(feeTakeDTOBaseResult, "readFeign.getFeeTakeById", logger);
        FeeTakeDTO feeTakeDTO = feeTakeDTOBaseResult.getData();
        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        // 判断用户权限
        if (currentUser == null || !isCommittee(feeTakeDTO.getClassId(), currentUser.getId(), token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }
        BaseResult<Void> deleteResult = writeFeign.deleteFeeTake(feeTakeId);
        FeignUtil.validateFeignResult(deleteResult, "writeFeign.deleteFeeTake", logger);
        return baseResult;
    }
}
