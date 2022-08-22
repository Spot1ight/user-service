package com.example.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserResource {

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity addUser(@RequestBody UserEntity user) {
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll() {
        List<UserEntity> users = userRepository.findAll();
        users.sort(Comparator.comparing(UserEntity::getId));
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getById(
            @PathVariable(name = "userId") Long userId
    ) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserEntity user
    ) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setAge(user.getAge());
            userEntity.setGender(user.getGender());
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            UserEntity updatedUser = userRepository.save(userEntity);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteById(@PathVariable(name = "userId") Long userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.accepted().build();
    }
}
