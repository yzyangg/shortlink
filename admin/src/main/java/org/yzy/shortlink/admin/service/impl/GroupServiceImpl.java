package org.yzy.shortlink.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.common.biz.user.UserContext;
import org.yzy.shortlink.admin.dao.entity.GroupDO;
import org.yzy.shortlink.admin.dao.mapper.GroupMapper;
import org.yzy.shortlink.admin.dto.req.ShortLinkUpdateReq;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupListRespDTO;
import org.yzy.shortlink.admin.service.GroupService;
import org.yzy.shortlink.admin.util.NumberUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                .sortOrder(0)
                .gid(GID)
                .username(UserContext.getUsername())
                .build();

        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupListRespDTO> listGroup() {
        // TODO 获取用户名

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GroupDO::getUsername, UserContext.getUsername());
        wrapper.eq(GroupDO::getDelFlag, 0);
        wrapper.orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);


        return baseMapper.selectList(wrapper).stream().map(groupDO -> ShortLinkGroupListRespDTO.builder()
                .gid(groupDO.getGid())
                .name(groupDO.getName())
                .sortOrder(groupDO.getSortOrder())
                .username(groupDO.getUsername())
                .build()).collect(Collectors.toList());

    }

    @Override
    public void updateGroup(ShortLinkUpdateReq shortLinkUpdateReq) {
        LambdaUpdateWrapper<GroupDO> wrapper = new LambdaUpdateWrapper<GroupDO>()
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(!Objects.isNull(shortLinkUpdateReq.getId()), GroupDO::getId, shortLinkUpdateReq.getId())
                .eq(GroupDO::getDelFlag, 0);

        GroupDO groupDO = new GroupDO();
        groupDO.setName(shortLinkUpdateReq.getName());
        baseMapper.update(groupDO, wrapper);
    }

}




