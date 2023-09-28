package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.UserRoleStatisticsUseCase;
import getawaygo_project.getawaygo_backend.domain.UserStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleStatisticsUseCaseImpl implements UserRoleStatisticsUseCase {
    private UserRepository userRepository;

    @Override
    public UserStatisticsDTO getRoleStatistics() {
        List<UserEntity> guests = userRepository.findAll().stream().filter(u -> u.getAuthority().getRole().equals("GUEST")).toList();
        List<UserEntity> hosts = userRepository.findAll().stream().filter(u -> u.getAuthority().getRole().equals("HOST")).toList();

        long guestsCount = guests.stream().count();
        long hostsCount = hosts.stream().count();

        return UserStatisticsDTO.builder()
                .hostCount(hostsCount)
                .guestCount(guestsCount).build();

    }
}
