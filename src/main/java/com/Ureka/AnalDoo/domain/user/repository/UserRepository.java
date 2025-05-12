package com.Ureka.AnalDoo.domain.user.repository;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    default User getById(Long id){
        return findById(id).orElseThrow(()->new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    default User getByEmail(String email){
        return findByEmail(email).orElseThrow(()-> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);


}
