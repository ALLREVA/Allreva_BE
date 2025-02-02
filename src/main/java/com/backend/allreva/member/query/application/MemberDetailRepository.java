package com.backend.allreva.member.query.application;

import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.member.query.application.response.MemberDetailResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailRepository {
    MemberDetailResponse findById(final Long id);

    OtherMember findMemberSummary(Long memberId);
}
