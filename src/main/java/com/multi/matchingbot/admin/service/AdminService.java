package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserRepository userRepository;


}
