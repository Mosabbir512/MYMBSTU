package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.Token;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Token createToken(User user, String hallName, String mealType) {
        // Generate token for today's meal
        Token token = new Token(
                user.getId(),
                hallName,
                mealType,
                LocalDate.now().toString()
        );

        return tokenRepository.save(token);
    }

    public Token findByTokenNumber(String tokenNumber) {
        return tokenRepository.findByTokenNumber(tokenNumber)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public Token verifyToken(Long tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        token.setVerified(true);
        return tokenRepository.save(token);
    }

    public Token validateToken(String tokenNumber) {
        return tokenRepository.findByTokenNumber(tokenNumber).orElseThrow(() -> new RuntimeException("Token not valided "));

    }
    public List<Token> findTokensStartingWith(String query) {
        return tokenRepository.findByTokenNumberStartingWithIgnoreCase(query);
    }
}