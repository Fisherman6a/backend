package com.movie_back.backend.service;

import com.movie_back.backend.dto.user.UserDTO;
import com.movie_back.backend.dto.user.UserRegistrationRequest;
import com.movie_back.backend.entity.Role;
import com.movie_back.backend.entity.User;
import com.movie_back.backend.exception.ResourceNotFoundException;
import com.movie_back.backend.repository.UserRepository;
// import lombok.RequiredArgsConstructor; // 移除这行
import org.springframework.context.annotation.Lazy; // 导入 @Lazy
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// @RequiredArgsConstructor // 移除此注解，因为我们将手动创建构造函数
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 手动创建构造函数，并对PasswordEncoder使用@Lazy注解
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Spring Security 会调用此方法来加载用户信息
    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // 用户注册
    @Transactional
    public UserDTO registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // 默认注册为普通用户
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // 根据用户名查找用户并转换为DTO
    @Transactional(readOnly = true)
    public UserDTO findUserByUsername(String username) {
        User user = loadUserByUsername(username);
        return convertToDTO(user);
    }

    // 根据ID查找用户并转换为DTO
    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}