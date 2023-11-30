package org.yzy.shortlink.admin.controller;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yzy.shortlink.admin.dto.response.UserRespDTO;
import org.yzy.shortlink.admin.service.UserService;
import org.yzy.shortlink.common.convention.result.Result;
import org.yzy.shortlink.common.convention.result.Results;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/30 23:03
 */
@RequestMapping("/api/shortLink/admin/v1")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;


    @GetMapping("/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

}
