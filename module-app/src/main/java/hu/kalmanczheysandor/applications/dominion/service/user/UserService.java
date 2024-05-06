package hu.kalmanczheysandor.applications.dominion.service.user;

import hu.kalmanczheysandor.applications.dominion.entity.User;
import hu.kalmanczheysandor.applications.dominion.entity.UserRole;
import hu.kalmanczheysandor.applications.dominion.exception.UserNotFoundException;
import hu.kalmanczheysandor.applications.dominion.repository.UserRepository;
import hu.kalmanczheysandor.applications.dominion.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    public User saveAsPlayer(User player) {
        String encodedPassword = passwordEncoder.encode(player.getPassword());

        UserRole role = new UserRole("ROLE_PLAYER", player);


        player.setPassword(encodedPassword);
        player.getAuthorities().add(role);

        return userRepository.save(player);
    }


    public UserResponse saveUser(UserCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Set simple properties
        User userToSave = new User();
        userToSave.setIdentifier(request.getIdentifier());
        userToSave.setPassword(encodedPassword);
        userToSave.setName(request.getName());

        // Add roles
        Set<UserRole> authorities = new HashSet<>();
        if (request.getRolePlayer()) {
            authorities.add(new UserRole("ROLE_PLAYER", userToSave));
        }
        if (request.getRoleAdmin()) {
            authorities.add(new UserRole("ROLE_ADMIN", userToSave));
        }
        if (request.getRoleEngineer()) {
            authorities.add(new UserRole("ROLE_ENGINEER", userToSave));
        }
        userToSave.setAuthorities(authorities);

        // Save
        User userSaved = userRepository.save(userToSave);

        return convertEntityToResponse(userSaved);
    }



    public UserResponse updateUser(long userId, UserUpdateRequest request) {
        try {
            // Request mapping
            User newUser = convertRequestToEntity(request);

            // Find the existing record
            if(!userRepository.existsById(userId)) {
                throw new UserNotFoundException(userId);
            }
            User existingRecord = userRepository.findById(userId);

            // Creating and overwriting objects
            modelMapper.map(newUser,existingRecord);
            existingRecord.setId(userId);

            // Providing persistence update
            User modifiedRecord = userRepository.save(existingRecord);

            return convertEntityToResponse(modifiedRecord);
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }




    public List<UserResponse> listAllUser() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                       .map(item -> modelMapper.map(item, UserResponse.class))
                       .collect(Collectors.toList());
    }


    public UserResponse findUserById(long userId) {
        try {
            User user = userRepository.findById(userId);
            return convertEntityToResponse(user);
        } catch (EntityNotFoundException e) {

            throw new UserNotFoundException(userId);
        }
    }

    public void deleteUserById(long userId) {
        try {
            userRoleRepository.deleteAllByUserId(userId);
            userRepository.deleteById(userId);
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }

    private UserResponse convertEntityToResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setIdentifier(user.getIdentifier());
        response.setName(user.getName());

        for (UserRole role : user.getAuthorities()) {
            if ("ROLE_PLAYER".equals(role.getAuthority())) {
                response.setRolePlayer(true);
            }
            else if ("ROLE_ADMIN".equals(role.getAuthority())) {
                response.setRoleAdmin(true);
            }
            else if ("ROLE_ENGINEER".equals(role.getAuthority())) {
                response.setRoleEngineer(true);
            }
        }
        return response;
    }

    private User convertRequestToEntity(UserUpdateRequest request) {

        User user = new User();
        user.setId(request.getId());
        user.setIdentifier(request.getIdentifier());
        user.setName(request.getName());

        // Add roles
        Set<UserRole> authorities = new HashSet<>();
        if (request.getRolePlayer()) {
            authorities.add(new UserRole("ROLE_PLAYER", user));
        }
        if (request.getRoleAdmin()) {
            authorities.add(new UserRole("ROLE_ADMIN", user));
        }
        if (request.getRoleEngineer()) {
            authorities.add(new UserRole("ROLE_ENGINEER", user));
        }
        user.setAuthorities(authorities);

        return user;
    }

}
