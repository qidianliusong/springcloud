package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralAccountRepository extends JpaRepository<GeneralAccount, Long> {

}
