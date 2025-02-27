package com.backend.allreva.member.command.domain;

import com.backend.allreva.common.model.BaseEntity;
import com.backend.allreva.common.model.Email;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import com.backend.allreva.member.command.domain.value.MemberInfo;
import com.backend.allreva.member.command.domain.value.MemberRole;
import com.backend.allreva.member.command.domain.value.RefundAccount;
import com.backend.allreva.member.infra.RefundAccountConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private LoginProvider loginProvider;

    @Embedded
    private MemberInfo memberInfo;

    @Convert(converter = RefundAccountConverter.class)
    @Column(name = "refund_account", nullable = false)
    private RefundAccount refundAccount;

    @Builder
    private Member(
            final Email email,
            final LoginProvider loginProvider,
            final MemberRole memberRole,
            final String nickname,
            final String introduce,
            final String profileImageUrl
    ) {
        this.email = email;
        this.loginProvider = loginProvider;
        this.memberRole = memberRole;
        this.memberInfo = MemberInfo.builder()
                .nickname(nickname)
                .introduce(introduce)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public void setMemberInfo(
            final String nickname,
            final String introduce,
            final String profileImageUrl
    ) {
        this.memberInfo = MemberInfo.builder()
                .nickname(nickname)
                .introduce(introduce)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public void setRefundAccount(
            final String bank,
            final String number
    ) {
        this.refundAccount = RefundAccount.builder()
                .bank(bank)
                .number(number)
                .build();
    }

    public void setDefaultRefundAccount() {
        this.refundAccount = RefundAccount.builder()
                .bank("")
                .number("")
                .build();
    }
}
