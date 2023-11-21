package com.psp.TimeManager.services;

import com.psp.TimeManager.dtos.CredentialsDto;
import com.psp.TimeManager.dtos.SignUpDto;
import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.exceptions.AppException;
import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

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
        User user = userRepository.findByEmail(credentialsDto.email())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword()))
        {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid login or password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto)
    {
        Optional<User> oUser = userRepository.findByEmail(signUpDto.email());

        if(oUser.isPresent())
        {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public User addUser(User user)
    {
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(user.getPassword())));
        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserByEmail(String email)
    {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User was not found"));
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
