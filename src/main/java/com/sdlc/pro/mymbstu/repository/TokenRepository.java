package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByStudentIdAndMealDate(String studentId, String mealDate);

    List<Token> findByStudentId(String studentId);

    boolean existsByTokenNumber(String tokenNumber);
    List<Token> findByTokenNumberStartingWithIgnoreCase(String tokenPrefix);
    Optional<Token> findByTokenNumber(String tokenNumber);
    List<Token> findByVerifiedFalseOrderByMealDateDesc();

}
