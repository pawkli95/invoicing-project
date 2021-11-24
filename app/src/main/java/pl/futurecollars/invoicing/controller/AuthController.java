package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.config.security.JwtUtil;
import pl.futurecollars.invoicing.dto.AuthParams;
import pl.futurecollars.invoicing.dto.AuthResponse;
import pl.futurecollars.invoicing.model.User;

@Api(tags = {"auth-controller"})
@CrossOrigin
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @ApiOperation(value = "Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthParams request) {
        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        AuthResponse authResponse = new AuthResponse(jwtUtil.generateAccessToken(user), user.getRole());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authResponse);
    }
}
