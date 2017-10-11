package com.jiuchunjiaoyu.micro.wzb.background.manager;

import com.jiuchunjiaoyu.micro.wzb.background.entity.SysUser;

public interface SysUserMng {

    SysUser findByUsername(String username);

}
