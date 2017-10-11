package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import org.springframework.data.domain.Page;

public interface ClassAccountMng extends BaseMng<ClassAccount>{

	ClassAccount findByClassId(Long classId);

	boolean existsByClassId(Long classId);

    Page<ClassAccount> getClassAccountPage(Long schoolId, String className, Integer pageNo, Integer pageSize);
}
