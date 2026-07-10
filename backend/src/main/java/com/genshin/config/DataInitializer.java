package com.genshin.config;

import com.genshin.modules.user.entity.SysUser;
import com.genshin.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String encodedPw = passwordEncoder.encode("Zq010722.");
        if (!userRepository.existsByUsername("qiuyingmu")) {
            var user = new SysUser();
            user.setUsername("qiuyingmu");
            user.setPassword(encodedPw);
            user.setNickname("qiuyingmu");
            user.setRole("admin");
            userRepository.insert(user);
            log.info("Default user created: qiuyingmu");
        } else {
            // Update password in case it changed
            var user = userRepository.findByUsername("qiuyingmu").get();
            user.setPassword(encodedPw);
            userRepository.updateById(user);
            log.info("Default user password updated");
        }
    }
}
