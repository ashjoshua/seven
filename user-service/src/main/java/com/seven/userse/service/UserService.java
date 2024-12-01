package com.seven.userse.service;

import com.seven.userse.model.RegistrationRequest;
import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.utils.LocationValidator;
import com.seven.userse.utils.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PhotoService photoService;
    private final LocationValidator locationValidator;

    public void registerUser(RegistrationRequest request) {
        if (!locationValidator.isWithinIndia(request.getLocation())) {
            throw new IllegalArgumentException("Location not within allowed region (India)");
        }

        photoService.validatePhotos(request.getPhotoUrls());

        User user = new User();
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setSex(request.getSex());
        user.setOrientation(request.getOrientation());
        user.setAge(request.getAge());
        user.setHeight(request.getHeight());
        user.setPitch(request.getPitch());
        user.setIsPremium(false);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        redisTemplate.opsForValue().set("user:" + user.getId(), user, Duration.ofHours(1));
    }
}
