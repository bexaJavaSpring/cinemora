package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.config.CustomAuthenticationProvider;
import bekhruz.com.cinemora.config.CustomUserDetails;
import bekhruz.com.cinemora.config.UserSession;
import bekhruz.com.cinemora.dto.auth.AuthResponse;
import bekhruz.com.cinemora.dto.auth.LoginResponse;
import bekhruz.com.cinemora.dto.auth.MeResponse;
import bekhruz.com.cinemora.dto.auth.RegisterRequest;
import bekhruz.com.cinemora.entity.SessionUser;
import bekhruz.com.cinemora.entity.User;
import bekhruz.com.cinemora.entity.enums.Status;
import bekhruz.com.cinemora.exception.AlreadyExistsException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.SessionUserRepository;
import bekhruz.com.cinemora.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionUserRepository sessionUserRepository;
    private final JwtTokenService jwtTokenService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;

    public AuthService(UserRepository userRepository, SessionUserRepository sessionUserRepository, JwtTokenService jwtTokenService, CustomAuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.userRepository = userRepository;
        this.sessionUserRepository = sessionUserRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationProvider = authenticationProvider;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
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

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        // Username yoki email allaqachon mavjudmi?
        if (userRepository.existsByUsername(req.getUsername()))
            throw new AlreadyExistsException("Bu username band: " + req.getUsername());
        if (userRepository.existsByEmail(req.getEmail()))
            throw new AlreadyExistsException("Bu email band: " + req.getEmail());

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(User.UserRole.USER)       // default USER
                .isActive(true)
                .build();

        userRepository.save(user);
        String token = jwtTokenService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public MeResponse getMe() {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        if (user == null)
            throw new GenericNotFoundException("Token noto'g'ri yoki muddati o'tgan");

        MeResponse res = new MeResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setAvatarUrl(user.getAvatarUrl());
        res.setRole(user.getRole().name());
        return res;
    }
}
