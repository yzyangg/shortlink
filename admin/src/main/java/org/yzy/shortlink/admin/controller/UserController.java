package org.yzy.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.yzy.shortlink.admin.common.convention.result.Result;
import org.yzy.shortlink.admin.common.convention.result.Results;
import org.yzy.shortlink.admin.dto.req.UserLoginReqDTO;
import org.yzy.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.yzy.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;
import org.yzy.shortlink.admin.service.UserService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 17:53
 */
@RestController
@RequestMapping("/api/short-link/admin/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    public final UserService userService;


    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO user = userService.getUserByUsername(username);
        return Results.success(user);
    }


    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @GetMapping("/has-username/{username}")
    public Result<Boolean> hasUsername(@PathVariable("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 用户注册
     *
     * @param userRegisterReqDTO 用户注册信息
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody UserRegisterReqDTO userRegisterReqDTO) {
        userService.register(userRegisterReqDTO);
        return Results.success("注册成功");
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateReqDTO
     * @return
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody UserUpdateReqDTO userUpdateReqDTO) {
        userService.update(userUpdateReqDTO);
        return Results.success("更新成功");
    }

    /**
     * 用户登录
     *
     * @param userLoginReqDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO userLoginReqDTO) {
        UserLoginRespDTO loginResp = userService.login(userLoginReqDTO);
        return Results.success(loginResp);
    }

    /**
     * 校验用户是否登录
     *
     * @param token
     * @return
     */
    @GetMapping("/check-login")
    public Result<Boolean> checkLogin(@RequestParam("token") String token, @RequestParam("username") String username) {
        log.info("token:{},username:{}", token, username);
        return Results.success(userService.checkLogin(token, username));
    }

    /**
     * 退出登录
     *
     * @param token
     * @param username
     * @return
     */
    @DeleteMapping("/logout")
    public Result<Void> logout(@RequestParam("token") String token, @RequestParam("username") String username) {
        log.info("token:{},username:{}", token, username);
        userService.logout(token, username);
        return Results.success("退出成功");
    }

}
