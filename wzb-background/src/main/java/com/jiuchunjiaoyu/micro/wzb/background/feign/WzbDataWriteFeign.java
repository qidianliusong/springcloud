package com.jiuchunjiaoyu.micro.wzb.background.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.background.dto.FeeCategoryDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.SchoolDrawAuditDTO;
import com.jiuchunjiaoyu.micro.wzb.background.feign.config.AuthFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用微智宝写入服务的feign接口
 */
@FeignClient(value = "WZB-DATA-WRITE-SERVER", configuration = AuthFeignConfiguration.class)
public interface WzbDataWriteFeign {

    @RequestMapping(value = "/operate/feeCategory/add", method = RequestMethod.POST)
    BaseResult<FeeCategoryDTO> saveFeeCategory(FeeCategoryDTO feeCategory);

    @RequestMapping(value = "/operate/feeCategory/update", method = RequestMethod.POST)
    BaseResult<FeeCategoryDTO> updateFeeCategory(FeeCategoryDTO feeCategory);

    @RequestMapping(value = "/operate/schoolDrawRecord/audit", method = RequestMethod.POST)
    BaseResult<Void> auditSchoolDraw(SchoolDrawAuditDTO schoolDrawAuditDTO);

    @RequestMapping(value = "/operate/feeCategory/changePriority", method = RequestMethod.POST)
    BaseResult<Void> changeFeeCategoryPriority(@RequestParam(value = "feeCategoryId1", required = true) long feeCategoryId1,
                                               @RequestParam(value = "feeCategoryId2", required = true) long feeCategoryId2);

}
