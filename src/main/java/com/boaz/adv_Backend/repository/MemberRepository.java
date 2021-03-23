package com.boaz.adv_Backend.repository;

import com.boaz.adv_Backend.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
