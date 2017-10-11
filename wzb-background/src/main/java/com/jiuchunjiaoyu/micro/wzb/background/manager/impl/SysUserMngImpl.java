package com.jiuchunjiaoyu.micro.wzb.background.manager.impl;

import com.jiuchunjiaoyu.micro.wzb.background.entity.SysUser;
import com.jiuchunjiaoyu.micro.wzb.background.manager.SysUserMng;
import com.jiuchunjiaoyu.micro.wzb.background.repository.SysUserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserMngImpl implements SysUserMng {

    @Resource
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}
