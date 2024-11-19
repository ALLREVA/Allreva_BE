package com.kwanse.allreva.auth.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kwanse.allreva.auth.application.dto.PrincipalDetails;
import com.kwanse.allreva.member.command.application.MemberRepository;
import com.kwanse.allreva.member.command.domain.Member;
import com.kwanse.allreva.member.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return new PrincipalDetails(member, null);
    }
}