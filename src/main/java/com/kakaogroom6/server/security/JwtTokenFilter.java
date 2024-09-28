package com.kakaogroom6.server.security;

import com.kakaogroom6.server.domain.member.entity.MemberEntity;
import com.kakaogroom6.server.domain.member.repository.MemberRepository;
import com.kakaogroom6.server.global.errors.code.CommonErrorCode;
import com.kakaogroom6.server.global.errors.exception.RestApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws SecurityException, IOException, ServletException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더가 없는 경우 쿠키를 헤더로 사용 시도
        if(authorizationHeader == null) {

            if(request.getCookies() == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("jwtToken"))
                    .findFirst()
                    .orElse(null);

            if(jwtTokenCookie == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = jwtTokenCookie.getValue();
            authorizationHeader = "Bearer " + jwtToken;
        }

        // 토큰값이 이상한 경우
        if(!authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.split(" ")[1];

        // 토큰이 만료된 경우
        if(JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        String loginId = JwtTokenUtil.getLoginId(token, secretKey);

        MemberEntity loginUser = memberRepository.findByEmail(loginId).orElseThrow(() -> new RestApiException(CommonErrorCode.MEMBER_NOT_FOUND));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.getEmail(), null, List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
