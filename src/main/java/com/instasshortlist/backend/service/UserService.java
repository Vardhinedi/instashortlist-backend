package com.instashortlist.backend.service;

import com.instashortlist.backend.model.User;
import com.instashortlist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Flux<User> getAll() {
        return repo.findAll();
    }

    public Mono<User> getById(Long id) {
        return repo.findById(id);
    }

    public Mono<User> create(User user) {
        // ✅ Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public Mono<User> update(Long id, User updated) {
        return repo.findById(id)
                .flatMap(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    existing.setRole(updated.getRole());
                    existing.setUsername(updated.getUsername());

                    // ✅ Re-encrypt the updated password
                    existing.setPassword(passwordEncoder.encode(updated.getPassword()));

                    return repo.save(existing);
                });
    }

    public Mono<Void> delete(Long id) {
        return repo.findById(id).flatMap(repo::delete);
    }

    public Mono<Boolean> login(String username, String password) {
        return repo.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .defaultIfEmpty(false);
    }
}
