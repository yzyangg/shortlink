package org.yzy.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.admin.dao.entity.GroupDO;

/**
 * @author Lenovo
 * @description 针对表【t_group】的数据库操作Service
 * @createDate 2023-11-25 23:44:16
 */
public interface GroupService extends IService<GroupDO> {

    void saveGroup(String groupName);
}
