package org.yzy.shortlink.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.dao.entity.GroupDO;
import org.yzy.shortlink.admin.dao.mapper.GroupMapper;
import org.yzy.shortlink.admin.service.GroupService;
import org.yzy.shortlink.admin.util.NumberUtil;

/**
 * @author Lenovo
 * @description 针对表【t_group】的数据库操作Service实现
 * @createDate 2023-11-25 23:44:16
 */
@Service
@AllArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(String groupName) {
        String GID = NumberUtil.generateRandomString();
        // 检查是否存在 GID
        while (lambdaQuery().eq(GroupDO::getGid, GID).count() > 0) {
            GID = NumberUtil.generateRandomString();
        }
        GroupDO groupDO = GroupDO.builder()
                .name(groupName)
                .gid(GID)
                .build();

        baseMapper.insert(groupDO);
    }

}




