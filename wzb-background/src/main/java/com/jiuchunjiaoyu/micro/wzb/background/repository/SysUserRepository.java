package com.jiuchunjiaoyu.micro.wzb.background.repository;

import com.jiuchunjiaoyu.micro.wzb.background.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Integer> {

    SysUser findByUsername(String username);
}
