package com.backend.allreva.member.query.application;

import com.backend.allreva.member.query.application.response.MemberDetailResponse;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.member.query.application.response.NicknameDuplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberDetailRepository memberDetailRepository;
    private final MemberRepository memberRepository;

    public MemberDetailResponse getById(final Long id) {
        return memberDetailRepository.findById(id);
    }

    public NicknameDuplication isDuplicatedNickname(final String nickname) {
        boolean exists = memberRepository.existsByMemberInfoNickname(nickname);
        return new NicknameDuplication(exists);
    }
}
