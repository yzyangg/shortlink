package org.yzy.shortlink.admin.controller;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yzy.shortlink.admin.dto.req.UserLoginReqDTO;
import org.yzy.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.yzy.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;
import org.yzy.shortlink.admin.service.UserService;
import org.yzy.shortlink.common.convention.result.Result;
import org.yzy.shortlink.common.convention.result.Results;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/30 23:03
 */
@RequestMapping("/api/shortLink/admin/v1/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;


    /**
     * 根据用户名查询用户脱敏信息
     *
     * @param username 用户名
     * @return 返回用户信息
     */
    @GetMapping("/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 查询用户名是否已存在
     *
     * @param username 用户户名
     * @return 是否存在
     */
    @GetMapping("/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册用户
     *
     * @param requestParam 用户参数
     * @return 注册成功或者抛出异常
     */
    @PostMapping("")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success("注册成功");
    }

    /**
     * 修改用户
     *
     * @param requestParam 用户参数
     * @return 修改成功或者抛出异常
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success("修改成功");
    }

    /**
     * 登录
     *
     * @param requestParam 登录参数
     * @return 登录成功或者抛出异常
     */
    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        return Results.success(userService.login(requestParam));
    }

    /**
     * 退出登录
     *
     * @param token token
     * @return 退出成功或者抛出异常
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestParam("token") String token) {
        userService.logout(token);
        return Results.success("退出成功");
    }

}
