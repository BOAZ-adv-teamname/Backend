package com.boaz.news_service.repository;

import com.boaz.news_service.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndPwd(String id, String pwd);
    Optional<Member> findById(String id);
}
