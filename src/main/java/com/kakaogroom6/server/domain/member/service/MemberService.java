package com.kakaogroom6.server.domain.member.service;

import com.kakaogroom6.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private  final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public boolean cheackLoginIdDuplicate(String loginId) {
        return memberRepository.findByEmail(loginId).isPresent();
    }

    public boolean cheackNicknmaeDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void join(JoinRequest request) {
        memberRepository.save(request.toEntity());
    }

    public void join2(JoinRequest request) {
        memberRepository.save(request.toEntity(encoder.encode(request.getPassword())));
    }

    public User login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByLoginId(request.getLoginId());

        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if(!user.getPassword().equals(request.getPassword())) {
            return null;
        }

        return user;
    }
    public User login2(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByLoginId(request.getLoginId());

        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        // 암호는 암호화할때마다 값이 달라지므로 내부 함수 사용하여 비교
        if(!encoder.matches(request.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

}
