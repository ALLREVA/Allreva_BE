package com.backend.allreva.member.infra;

import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.member.exception.MemberNotFoundException;
import com.backend.allreva.member.query.application.MemberDetailRepository;
import com.backend.allreva.member.query.application.response.MemberDetailResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.allreva.artist.command.domain.QArtist.artist;
import static com.backend.allreva.member.command.domain.QMember.member;
import static com.backend.allreva.member.command.domain.QMemberArtist.memberArtist;
import static com.backend.allreva.member.query.application.response.MemberDetailResponse.*;

@Repository
@RequiredArgsConstructor
public class MemberDetailRepositoryImpl implements MemberDetailRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberDetailResponse findById(final Long id) {
        return queryFactory.select(memberDetailProjections())
                .from(member)
                .leftJoin(memberArtist).on(memberArtist.memberId.eq(member.id))
                .leftJoin(artist).on(artist.id.eq(memberArtist.artistId))
                .where(member.id.eq(id))
                .fetchFirst();
    }

    private ConstructorExpression<MemberDetailResponse> memberDetailProjections() {
        return Projections.constructor(MemberDetailResponse.class,
                member.email.email,
                member.memberInfo.nickname,
                member.memberInfo.introduce,
                member.memberInfo.profileImageUrl,
                Projections.list(Projections.constructor(MemberArtistDetail.class,
                        memberArtist.artistId,
                        artist.name
                )),
                member.refundAccount
        );
    }

    @Override
    public OtherMember findMemberSummary(Long memberId) {
        OtherMember otherMember = queryFactory
                .select(Projections.constructor(OtherMember.class,
                        member.id,
                        member.memberInfo.nickname,
                        member.memberInfo.profileImageUrl
                ))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchFirst();

        if (otherMember == null) {
            throw new MemberNotFoundException();
        }
        return otherMember;
    }
}
