package com.cafehub.cafehubspring.service;

import com.cafehub.cafehubspring.domain.Member;
import com.cafehub.cafehubspring.exception.http.InternalServerErrorException;
import com.cafehub.cafehubspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Member 저장 |
     * 존재하는 멤버라면 null을 반환하고 새로운 멤버라면 Member를 반환합니다. 저장 중 디비에서 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Member save(String uuid) {
        log.info("IN PROGRESS | Member 저장 AT " + LocalDateTime.now() + " | " + uuid);

        Optional<Member> foundMember = memberRepository.findByUuid(uuid);
        if (foundMember.isPresent()) {
            return null;
        }

        Member member = Member.builder()
                .uuid(uuid)
                .build();
        try {
            Member newMember = memberRepository.save(member);
            log.info("COMPLETE | Member 저장 AT " + LocalDateTime.now() + " | " + uuid);
            return newMember;
        } catch (Exception e) {
            throw new InternalServerErrorException("Member 저장 중 에러 발생", e);
        }
    }
}
