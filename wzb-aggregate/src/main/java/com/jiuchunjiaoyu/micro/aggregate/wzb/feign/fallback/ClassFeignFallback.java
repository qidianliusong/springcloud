package com.jiuchunjiaoyu.micro.aggregate.wzb.feign.fallback;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.ClassDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.StudentDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultList;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.ClassFeign;

/**
 * 班级接口熔断
 * @author liusongsong
 *
 */
public class ClassFeignFallback implements ClassFeign{

	@Override
	public PhpResult<ResultList<UserDTO>> getOrgClassParents(String token, Long classId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PhpResult<ResultList<ClassDTO>> getTeacherClass(String token, Long teacherId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PhpResult<ResultList<ClassDTO>> getClassDetail(String token, Long classId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PhpResult<ResultList<StudentDTO>> getStudentsByClassid(String token, Long classId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String publishFeeDetail(String token, Long fee_id, String fee_type, String fee_content, String fee, Long school_id, Long class_id, String publisher_id, String publisher_name, String publish_time, String student_id) {
		return null;
	}

	@Override
	public String deleteFeeDetail(String token, Long fee_id) {
		return null;
	}

	@Override
	public String editFeeDetail(String token, Long fee_id, String fee_type, String fee_content, String fee, String status) {
		return null;
	}

}
