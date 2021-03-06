package com.boaz.news_service.service;

import com.boaz.news_service.repository.MemberRepository;
import com.boaz.news_service.util.HashFunction;
import com.boaz.news_service.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Member> getList() {
        return memberRepository.findAll();
    }

    @Override
    public Member login(String id, String pwd) throws NoSuchAlgorithmException {
        Optional<Member> loginMember = memberRepository.findByIdAndPwd(id, HashFunction.sha256(pwd));

        if (!loginMember.isPresent()) {
            return null;
        }

        Member member = loginMember.get();
        memberRepository.save(member);

        return member;
    }

    @Override
    public boolean isDuplicate(String id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.isPresent();
    }

    @Override
    public Member join(String id, String pwd, String email, String nick) throws NoSuchAlgorithmException {
        return memberRepository.save(
                Member.builder()
                        .id(id)
                        .pwd(HashFunction.sha256(pwd))
                        .email(email)
                        .nickname(nick)
                        .build()
        );
    }
}
