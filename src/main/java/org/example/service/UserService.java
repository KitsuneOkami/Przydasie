package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return true;
        }

        userRepository.deleteById(id);
        return true;
    }
}
