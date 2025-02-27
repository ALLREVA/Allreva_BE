package com.backend.allreva.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(MemberNotFoundException::new);

        return new PrincipalDetails(member);
    }
}