package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.config.CustomAuthenticationProvider;
import bekhruz.com.cinemora.config.CustomUserDetails;
import bekhruz.com.cinemora.dto.auth.LoginResponse;
import bekhruz.com.cinemora.entity.SessionUser;
import bekhruz.com.cinemora.entity.User;
import bekhruz.com.cinemora.entity.enums.Status;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.SessionUserRepository;
import bekhruz.com.cinemora.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionUserRepository sessionUserRepository;
    private final JwtTokenService jwtTokenService;
    private final CustomAuthenticationProvider authenticationProvider;

    public AuthService(UserRepository userRepository, SessionUserRepository sessionUserRepository, JwtTokenService jwtTokenService, CustomAuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.sessionUserRepository = sessionUserRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationProvider = authenticationProvider;
    }

    public Object login(String username, String password) {
        Authentication authenticate = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                username, password
        ));
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        User user = userRepository.findById(userDetails.getUserId()).orElse(null);
        if (user == null)
            throw new GenericNotFoundException(userDetails.getUserId().toString(), "user.not.found");

        SessionUser sessionUser = sessionUserRepository.findByUserId(user.getId());
        if (sessionUser != null) {
            String accessToken = jwtTokenService.generateToken(userDetails.getUsername());
            String refreshToken = jwtTokenService.generateToken(userDetails.getUsername());
            sessionUser.setAccessToken(accessToken);
            sessionUser.setRefreshToken(refreshToken);
            sessionUser.setStatus(Status.ACTIVE);
            sessionUserRepository.save(sessionUser);
            return LoginResponse.builder()
                    .accessToken(sessionUser.getAccessToken())
                    .refreshToken(sessionUser.getRefreshToken())
                    .build();
        }
        String accessToken = jwtTokenService.generateToken(userDetails.getUsername());
        String refreshToken = jwtTokenService.generateToken(userDetails.getUsername());
        sessionUserRepository.save(SessionUser.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(Status.ACTIVE)
                .build());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
