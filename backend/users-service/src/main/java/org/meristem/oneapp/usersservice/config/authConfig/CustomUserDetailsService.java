package org.meristem.oneapp.usersservice.config.authConfig;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.domains.enums.Status;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.models.Roles;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.PermissionsRepository;
import org.meristem.oneapp.usersservice.repositories.RolesRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PermissionsRepository permissionsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = usersRepository.findByEmailAndStatus(email, Status.ACTIVE.getValue()).orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
        List<Roles> usersRoles = rolesRepository.findAllByUsersId(user.getId());
        List<String> rolesPermissions = permissionsRepository.findAllByRolesIds(usersRoles.stream().map(Roles::getId).collect(Collectors.toList()));
        rolesPermissions.addAll(usersRoles.stream().map(Roles::getName).toList());
        List<GrantedAuthority> authorities = rolesPermissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new AuthenticatedUser(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getPassword(), user.getPhoneNumber(), user.getReferralCode(), user.getOnboardingCompleted(), authorities);
    }
}
