package com.Ureka.AnalDoo.fixture;

import com.Ureka.AnalDoo.domain.entity.SocialType;
import com.Ureka.AnalDoo.domain.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static User createUser(String email) {
        User user = User.of(email, "user1", SocialType.KAKAO);
        return user;
    }

    public static User createUserWithId(Long id, String email) {
        User user = User.of(email, "user", SocialType.KAKAO);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
