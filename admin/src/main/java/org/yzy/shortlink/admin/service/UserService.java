package org.yzy.shortlink.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.admin.dao.entity.UserDO;
import org.yzy.shortlink.admin.dto.req.UserLoginReqDTO;
import org.yzy.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.yzy.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 返回用户信息
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 查询用户名是否已存在
     *
     * @param username 用户名
     * @return username是否存在
     */
    Boolean hasUsername(String username);

    /**
     * 注册用户
     *
     * @param requestParam 用户注册请求参数
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 修改用户
     *
     * @param requestParam 用户修改请求参数
     */
    void update(UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     *
     * @param requestParam 用户登录请求参数
     * @return 用户登录返回参数
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查用是否登录
     *
     * @param token 用户登录token
     * @return 用户是否登录
     */
    Boolean checkLogin(String token);

    /**
     * 用户退出登录
     *
     * @param token token
     */
    void logout(String token);
}
