package com.Ureka.AnalDoo.domain.user.repository;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User getById(Long id){
        return findById(id).orElseThrow(()->new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }
}
