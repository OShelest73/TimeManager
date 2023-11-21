package com.psp.TimeManager.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.exceptions.AppException;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.models.Permission;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostConstruct
    protected void init()
    {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto dto){
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);

        return JWT.create()
            .withIssuer(dto.getEmail())
            .withIssuedAt(now)
            .withExpiresAt(validity)
            .withClaim("fullName", dto.getFullName())
            .withClaim("usersPermissions", dto.getPermissions())
            .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token)
    {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto user = UserDto.builder()
                .email(decoded.getIssuer())
                .fullName(decoded.getClaim("fullName").asString())
                .build();
        Claim claim = decoded.getClaim("usersPermissions");

        String permissions = claim.toString();

        //String permissions = decoded.getClaim("usersPermissions").toString();
        permissions = permissions.replace("\"", "").replace("[", "").replace("]", "");
        String[] roleNames = permissions.split(",");

        List<String> bufPermissions = new ArrayList<>();
        for (String aRoleName : roleNames) {
            bufPermissions.add(aRoleName);
        }
        user.setPermissions(bufPermissions);

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        User user = userRepository.findByEmail(decoded.getIssuer())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return new UsernamePasswordAuthenticationToken(userMapper.toUserDto(user), null, Collections.emptyList());
    }
}
