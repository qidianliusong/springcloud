package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.ClassDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.StudentDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultList;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config.ClassFeignConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用php班级相关接口调用
 */
@FeignClient(name = "class", url = "${wzxy.url.prefix:http://dev-lumen.jcweixiaoyuan.cn}", configuration = ClassFeignConfiguration.class)
public interface ClassFeign {

    /**
     * 获得班委会家长信息
     *
     * @param token
     * @param classId
     * @return
     */
    @Cacheable(value = "wzb:aggregate:getorgclassparents", key = "'wzb:aggregate:getorgclassparents_key'+#p1")
    @RequestMapping(value = "/peony/wx/class/getorgclassparents", method = RequestMethod.GET)
    PhpResult<ResultList<UserDTO>> getOrgClassParents(@RequestParam(value = "token", required = true) String token,
                                                      @RequestParam(value = "class_id", required = true) Long classId);

    @RequestMapping(value = "/peony/wx/teacher/getteacherclass", method = RequestMethod.GET)
    PhpResult<ResultList<ClassDTO>> getTeacherClass(@RequestParam(value = "token", required = true) String token,
                                                    @RequestParam(value = "teacher_id", required = true) Long teacherId);

    @Cacheable(value = "wzb:aggregate:classdetail", key = "'wzb:aggregate:classdetail_key'+#p1")
    @RequestMapping(value = "/peony/wx/teacher/getclassdetail", method = RequestMethod.GET)
    PhpResult<ResultList<ClassDTO>> getClassDetail(@RequestParam(value = "token", required = true) String token,
                                                   @RequestParam(value = "class_id", required = true) Long classId);

    @Cacheable(value = "wzb:aggregate:getstudentsbyclassid", key = "'wzb:aggregate:getstudentsbyclassid_key'+#p1")
    @RequestMapping(value = "/peony/wx/student/getstudentsbyclassid", method = RequestMethod.GET)
    PhpResult<ResultList<StudentDTO>> getStudentsByClassid(@RequestParam(value = "token", required = true) String token,
                                                           @RequestParam(value = "class_id", required = true) Long classId);

    /**
     * 发布收费信息到班级圈
     *
     * @return
     */
    @Async
    @RequestMapping(value = "/peony/wx/classpay/publish", method = RequestMethod.POST)
    String publishFeeDetail(@RequestParam(value = "token", required = true) String token,
                            @RequestParam(value = "fee_id", required = true) Long fee_id,
                            @RequestParam(value = "fee_type", required = true) String fee_type,
                            @RequestParam(value = "fee_content", required = true) String fee_content,
                            @RequestParam(value = "fee", required = true) String fee,
                            @RequestParam(value = "school_id", required = true) Long school_id,
                            @RequestParam(value = "class_id", required = true) Long class_id,
                            @RequestParam(value = "publisher_id", required = true) String publisher_id,
                            @RequestParam(value = "publisher_name", required = true) String publisher_name,
                            @RequestParam(value = "publish_time", required = true) String publish_time,
                            @RequestParam(value = "student_id", required = true) String student_id);

    /**
     * 删除朋友圈缴费信息
     *
     * @return
     */
    @Async
    @RequestMapping(value = "/peony/wx/classpay/delete", method = RequestMethod.POST)
    String deleteFeeDetail(@RequestParam(value = "token", required = true) String token,
                           @RequestParam(value = "fee_id", required = true) Long fee_id);

    /**
     * 修改朋友圈缴费信息
     *
     * @return
     */
    @Async
    @RequestMapping(value = "/peony/wx/classpay/edit", method = RequestMethod.POST)
    String editFeeDetail(@RequestParam(value = "token", required = true) String token,
                         @RequestParam(value = "fee_id", required = true) Long fee_id,
                         @RequestParam(value = "fee_type", required = true) String fee_type,
                         @RequestParam(value = "fee_content", required = true) String fee_content,
                         @RequestParam(value = "fee", required = true) String fee,
                         @RequestParam(value = "status", required = true) String status);
}
