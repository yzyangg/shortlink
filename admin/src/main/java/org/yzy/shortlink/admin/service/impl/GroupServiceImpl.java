package org.yzy.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.biz.user.UserContext;
import org.yzy.shortlink.admin.dao.entity.GroupDO;
import org.yzy.shortlink.admin.dao.mapper.GroupMapper;
import org.yzy.shortlink.admin.dto.ShortLinkRemoteService;
import org.yzy.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import org.yzy.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import org.yzy.shortlink.admin.service.GroupService;
import org.yzy.shortlink.admin.toolkit.RandomGenerator;

import java.util.List;
import java.util.Optional;

/**
 * 短链接接口分组实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    // TODO 后续重构为SpringCloud Feign调用
    private final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    @Override
    public void saveGroup(String groupName) {
        saveGroup(UserContext.getUsername(), groupName);
    }

    @Override
    public void saveGroup(String username, String groupName) {
        String gid;
        do {
            gid = RandomGenerator.generateRandom();
        } while (!hasGid(username, gid));
        GroupDO groupDO = GroupDO
                .builder()
                .gid(gid)
                .username(username)
                .name(Optional.ofNullable(groupName).orElse("默认分组"))
                .sortOrder(0)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class).
                eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .orderByAsc(GroupDO::getSortOrder);

        // 分组数据
        List<GroupDO> groupDOS = baseMapper.selectList(wrapper);
        // 分组下短链接数量
        List<ShortLinkGroupCountQueryRespDTO> groupCountQueryRespDTOS =
                shortLinkRemoteService.listGroupShortLinkCount(groupDOS.stream().map(GroupDO::getGid).toList()).getData();

        // 组装返回数据
        List<ShortLinkGroupRespDTO> shortLinkGroupRespDTOS = BeanUtil.copyToList(groupDOS, ShortLinkGroupRespDTO.class);
        shortLinkGroupRespDTOS.forEach(one -> {
            one.setShortLinkCount(
                    groupCountQueryRespDTOS.stream()
                            .filter(two -> one.getGid().equals(two.getGid()))
                            .findFirst()
                            .map(ShortLinkGroupCountQueryRespDTO::getShortLinkCount)
                            .orElse(0)
            );
        });
        return shortLinkGroupRespDTOS;
    }

    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {
        requestParam.forEach(param -> {
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(param.getSortOrder())
                    .build();
            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getGid, param.getGid())
                    .eq(GroupDO::getDelFlag, 0);
            baseMapper.update(groupDO, updateWrapper);
        });
    }

    private boolean hasGid(String username, String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, Optional.ofNullable(username).orElse(UserContext.getUsername()));
        GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag == null;
    }
}
