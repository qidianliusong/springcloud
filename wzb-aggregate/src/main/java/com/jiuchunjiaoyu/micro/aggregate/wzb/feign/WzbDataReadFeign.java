package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 调用微智宝读服务的feign接口
 */
@FeignClient("WZB-DATA-READ-SERVER")
public interface WzbDataReadFeign {

    @RequestMapping(value = "/test/11", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public BaseResult<String> test1();

    @RequestMapping(value = "/query/feeAgent/findById", method = RequestMethod.GET)
    public BaseResult<FeeAgentDTO> findFeeAgentById(
            @RequestParam(required = true, value = "feeAgentId") Long feeAgentId);

    @RequestMapping(value = "/query/classAccount/getByClassId", method = RequestMethod.GET)
    public BaseResult<ClassAccountDTO> getClassAccountByClassId(
            @RequestParam(required = true, value = "classId") Long classId);

    @RequestMapping(value = "/query/classAccount/existsByClassId", method = RequestMethod.GET)
    public BaseResult<Boolean> classAccountExistsByClassId(
            @RequestParam(required = true, value = "classId") Long classId);

    @RequestMapping(value = "/query/feeTake/getPageInfo", method = RequestMethod.POST)
    public BaseResult<PageDTO<FeeTakeDTO>> getFeeTakePageInfo(
            @RequestParam(required = true, value = "classId") Long classId,
            @RequestParam(required = true, value = "pageNo") Integer pageNo,
            @RequestParam(required = true, value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/query/feeDetail/findById", method = RequestMethod.GET)
    public BaseResult<FeeDetailDTO> getFeeDetailById(
            @RequestParam(required = true, value = "feeDetailId") Long feeDetailId);

    @RequestMapping(value = "/query/feeCategory/list", method = RequestMethod.GET)
    public BaseResult<List<FeeCategoryDTO>> getFeeCategoryList(
            @RequestParam(required = true, value = "schoolId") Long schoolId, @RequestParam(required = true, value = "classId") Long classId);

    @RequestMapping(value = "/query/feeDetail/getPageInfo", method = RequestMethod.GET)
    public BaseResult<PageDTO<FeeDetailDTO>> getFeeDetailPage(
            @RequestParam(required = true, value = "classId") Long classId,
            @RequestParam(required = true, value = "childId") Long childId,
            @RequestParam(required = true, value = "status") Integer status,
            @RequestParam(required = true, value = "pageNo") Integer pageNo,
            @RequestParam(required = true, value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/query/feeAgent/getPageInfo", method = RequestMethod.POST)
    public BaseResult<PageDTO<FeeAgentDTO>> getFeeAgentPageInfo(
            @RequestParam(required = true, value = "classId") Long classId,
            @RequestParam(required = true, value = "pageNo") Integer pageNo,
            @RequestParam(required = true, value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/query/feePay/list", method = RequestMethod.GET)
    public BaseResult<List<FeePayDTO>> getFeePayList(
            @RequestParam(required = true, value = "feeDetailId") Long feeDetailId,
            @RequestParam(required = true, value = "payStatus") Integer payStatus);

    @RequestMapping(value = "/query/feeAgent/list", method = RequestMethod.GET)
    public BaseResult<List<FeeAgentDTO>> getFeeAgentlist(
            @RequestParam(required = true, value = "feeDetailId") Long feeDetailId);

    @RequestMapping(value = "/query/feePay/get", method = RequestMethod.GET)
    public BaseResult<FeePayDTO> getFeePay(@RequestParam(required = true, value = "feeDetailId") Long feeDetailId,
                                           @RequestParam(required = true, value = "childId") Long childId);

    @RequestMapping(value = "/query/feePay/getById", method = RequestMethod.GET)
    public BaseResult<FeePayDTO> getFeePayById(@RequestParam(required = true, value = "feePayId") Long feePayId);

    @RequestMapping(value = "/query/feeDrawRecord/getPage", method = RequestMethod.POST)
    public BaseResult<PageDTO<FeeDrawRecordDTO>> getFeeDrawRecordPage(
            @RequestParam(required = true, value = "classId") Long classId,
            @RequestParam(value = "pageNo") Integer pageNo, @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 根据id获取记账信息
     *
     * @param feeTakeId
     * @return
     */
    @RequestMapping(value = "/query/feeTake/getById", method = RequestMethod.GET)
    BaseResult<FeeTakeDTO> getFeeTakeById(@RequestParam(value = "feeTakeId", required = true) Long feeTakeId);

    /**
     * 根据id获取收费类目
     *
     * @param feeCategoryId
     * @return
     */
    @RequestMapping(value = "/query/feeCategory/findById", method = RequestMethod.GET)
    BaseResult<FeeCategoryDTO> findFeeCategoryById(@RequestParam(value = "feeCategoryId", required = true) Long feeCategoryId);

    /**
     * 判断学校账户是否存在
     *
     * @param schoolId
     * @return
     */
    @RequestMapping(value = "/query/schoolAccount/existsBySchoolId", method = RequestMethod.GET)
    BaseResult<Boolean> existsSchoolAccount(@RequestParam(value = "schoolId", required = true) Long schoolId);

    /**
     * 获取
     * @param startTime
     * @return
     */
    @RequestMapping(value = "/query/feeDrawRecord/prePayRecordsCount", method = RequestMethod.GET)
    BaseResult<Integer> getprePayFeeDrawRecordsCount(@RequestParam(required = true,value = "startTime") Date startTime,@RequestParam(required = true,value = "classId") Long classId);
}
