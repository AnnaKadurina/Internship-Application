package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.User;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;

public class UserConverter {
    private UserConverter(){}
public static User convert(UserEntity user){
    return User.builder()
            .userId(user.getUserId())
            .address(user.getAddress())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .photo(user.getPhoto())
            .username(user.getUsername())
            .role(user.getAuthority().getRole())
            .active(user.getActive())
            .build();
}
}
