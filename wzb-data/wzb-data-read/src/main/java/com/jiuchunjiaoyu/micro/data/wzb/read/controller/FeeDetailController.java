package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.WzbDataReadErr;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeDetailMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeePayMng;
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
import java.math.BigDecimal;

@RestController
@RequestMapping("/query/feeDetail")
@Api("收费详情查询")
public class FeeDetailController extends CommonBaseController {

    @Resource
    private FeeDetailMng feeDetailMng;

    @Resource
    private FeePayMng feePayMng;

    @ApiOperation(value = "收费详情", notes = "收费详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代缴id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public BaseResult<FeeDetail> findById(@RequestParam(required = true) Long feeDetailId) {
        BaseResult<FeeDetail> baseResult = new BaseResult<>();
        FeeDetail feeDetail = feeDetailMng.findOne(feeDetailId);
        if (feeDetail == null) {
            baseResult.setCode(WzbDataReadErr.entity_not_exist.getCode());
            baseResult.setMessage(WzbDataReadErr.entity_not_exist.getMessage());
            return baseResult;
        }
        baseResult.setData(feeDetail);
        return baseResult;
    }

    @ApiOperation(value = "获取收费分页信息", notes = "获取收费分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "孩子id", name = "childId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "状态，0为开启，1为关闭", name = "status", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getPageInfo", method = RequestMethod.GET)
    public BaseResult<Page<FeeDetail>> getPage(@RequestParam(required = true) Long classId, @RequestParam(required = false) Long childId,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        if(pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<FeeDetail>> baseResult = new BaseResult<>();
        Page<FeeDetail> pageInfo = feeDetailMng.getPageInfo(classId, status, pageNo, pageSize);
        baseResult.setData(pageInfo);
        if (childId == null)
            return baseResult;
        if (pageInfo != null && pageInfo.getContent() != null && !pageInfo.getContent().isEmpty()) {
            pageInfo.getContent().parallelStream().forEach(feeDetail -> {
                if (feeDetail.getStatus() == FeeDetail.STATUS_OPEN) {
                    FeePay feePay = feePayMng.findByFeeDetailIdAndChildId(feeDetail.getFeeDetailId(), childId);
                    if (feePay != null && feePay.getStatus() == FeePay.STATUS_PAY) {
                        feeDetail.setPaid(true);
                    } else {
                        feeDetail.setPaid(false);
                    }
                }
            });
        }
        return baseResult;
    }

    @ApiOperation(value = "根据收费类目id获取收费详情信息", notes = "根据收费类目id获取收费详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id", name = "categoryId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getPageByCategoryId", method = RequestMethod.POST)
    public BaseResult<Page<FeeDetail>> getPageByCategoryId(@RequestParam(required = true) Long categoryId, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        if(pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<FeeDetail>> baseResult = new BaseResult<>();
        Page<FeeDetail> pageInfoByCategory = feeDetailMng.getPageInfoByCategory(categoryId, pageNo, pageSize);
        baseResult.setData(pageInfoByCategory);
        return baseResult;
    }

    @ApiOperation(value = "根据收费类目id获取收费总额", notes = "根据收费类目id获取收费总额")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费类目id", name = "categoryId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getSumByCategoryId", method = RequestMethod.GET)
    public BaseResult<BigDecimal> getSumByCategoryId(@RequestParam(required = true) Long categoryId) {
        BaseResult<BigDecimal> baseResult = new BaseResult<>();
        baseResult.setData(feeDetailMng.getSumByCategory(categoryId));
        return baseResult;
    }
}
