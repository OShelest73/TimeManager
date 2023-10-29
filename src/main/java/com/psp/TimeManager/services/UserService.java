package com.psp.TimeManager.services;

import PasswordConfig;
import com.psp.TimeManager.dto.CredentialsDto;
import com.psp.TimeManager.dto.UserDto;
import com.psp.TimeManager.exceptions.AppException;
import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDto login(CredentialsDto credentialsDto){
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword()))
        {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    private Collections<GrantedAuthority> mapRoleToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    public User addUser(User user)
    {
        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(User user)
    {
        return userRepository.save(user);
    }

    public User findUserById(int id)
    {
        return userRepository.findUserById(id).
                orElseThrow(() -> new UserNotFoundException("User was not found"));
    }

    public void deleteUser(int id){
        userRepository.deleteUserById(id);
    }
}
