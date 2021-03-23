package com.boaz.adv_Backend;

import com.boaz.adv_Backend.repository.MemberRepository;
import com.boaz.adv_Backend.vo.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;


@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void create() {
        Member member = new Member();
        member.setId("minyoung");
        member.setEmail("kminyoung0.kim@gmail.com");
        member.setPwd("1234");
        member.setNickname("민영");
        Member newMember = memberRepository.save(member);
        System.out.println(newMember);
    }

    @Test
    public void read() {
        Optional<Member> member = memberRepository.findById(0L);
        member.ifPresent(selectMember -> {
            System.out.println("member: " + selectMember);
        });
    }

    @Test
    public void update() {
        Optional<Member> member = memberRepository.findById(9L);

        member.ifPresent(selectMember -> {
            selectMember.setId("minyoung1");
            selectMember.setNickname("민영1");
            Member newMember = memberRepository.save(selectMember);
            System.out.println("user : " + newMember);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Optional<Member> member = memberRepository.findById(7L);

        System.out.println(member.isPresent());
        member.ifPresent(selectMember -> {
            memberRepository.delete(selectMember);
        });

        Optional<Member> deleteMember = memberRepository.findById(7L);
        System.out.println(deleteMember.isPresent());
    }
}
