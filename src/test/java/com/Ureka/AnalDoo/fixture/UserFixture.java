package com.Ureka.AnalDoo.fixture;

import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.Role;
import com.Ureka.AnalDoo.domain.entity.enums.SocialType;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static User createUser(String email) {
        User user = User.of(email, "user1", "111", SocialType.KAKAO, Role.PARTICIPANT, "1111");
        return user;
    }

    public static User createUserWithId(Long id, String email) {
        User user = User.of(email, "user1", "111", SocialType.KAKAO, Role.PARTICIPANT, "1111");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
