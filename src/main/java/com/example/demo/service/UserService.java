package com.example.demo.service;

import com.example.demo.dto.req.UserReq;
import com.example.demo.dto.res.UserRes;
import com.example.demo.entity.User;
import com.example.demo.entity.User.Role;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserRes> getAllUsers() {
        return userRepository.findAll().stream().map(UserRes::toJson).toList();
    }

    public UserRes findById(Long id) {
        return UserRes.toJson(userRepository.findById(id).get());
    }

    public UserRes create(UserReq req) {
        try {
            User user = new User();
            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setPassword(req.getPassword());
            user.setPhone(req.getPhone());
            user.setRole(Role.valueOf(req.getRole().toLowerCase().trim()));
            user.setAvatar(req.getAvatar());
            return UserRes.toJson(userRepository.save(user));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserRes update(Long id, UserReq req) {
        try {
            User user = userRepository.findById(id).get();
            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setPhone(req.getPhone());
            user.setRole(Role.valueOf(req.getRole().toLowerCase().trim()));
            user.setAvatar(req.getAvatar());
            return UserRes.toJson(userRepository.save(user));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}