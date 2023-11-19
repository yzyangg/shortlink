package org.yzy.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.admin.dao.entity.UserDO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 17:49
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    UserRespDTO getUserByUsername(String username);
}
