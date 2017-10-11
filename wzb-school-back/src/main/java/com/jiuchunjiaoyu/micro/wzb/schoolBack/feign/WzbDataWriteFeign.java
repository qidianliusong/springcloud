package com.jiuchunjiaoyu.micro.wzb.schoolBack.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.FeeCategoryDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.SchoolDrawRecordDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.config.AuthFeignConfiguration;
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

    @RequestMapping(value = "/operate/schoolDrawRecord/save", method = RequestMethod.POST)
    BaseResult<SchoolDrawRecordDTO> saveSchoolDrawRecord(SchoolDrawRecordDTO schoolDrawRecord);

    @RequestMapping(value = "/operate/schoolDrawRecord/delete", method = RequestMethod.DELETE)
    BaseResult<Void> deleteSchoolDrawRecord(@RequestParam(value = "schoolDrawRecordId", required = true) long schoolDrawRecordId);

}
