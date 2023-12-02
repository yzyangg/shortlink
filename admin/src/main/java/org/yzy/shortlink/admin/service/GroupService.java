package org.yzy.shortlink.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.admin.dao.entity.GroupDO;
import org.yzy.shortlink.admin.dto.request.ShortLinkGroupSortReqDTO;
import org.yzy.shortlink.admin.dto.request.ShortLinkGroupUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {
    /**
     * 新增短链接分组
     *
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);
    /**
     * 新增短链接分组
     *
     * @param username 用户名
     * @param groupName 短链接分组名
     */
    void saveGroup(String username,String groupName);

    /**
     * 查询用户短链接分组集合
     *
     * @return 短链接分组集合
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     *
     * @param requestParam 短链接参数 gid  name
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);

    /**
     * 删除短链接分组
     *
     * @param gid 分组id
     */
    void deleteGroup(String gid);

    /**
     * 短链接分组排序
     *
     * @param requestParam 短链接分组排序参数
     */
    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
