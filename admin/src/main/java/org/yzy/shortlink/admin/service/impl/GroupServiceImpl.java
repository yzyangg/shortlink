package org.yzy.shortlink.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.dao.entity.GroupDO;
import org.yzy.shortlink.admin.dao.mapper.GroupMapper;
import org.yzy.shortlink.admin.service.GroupService;

/**
 * @author Lenovo
 * @description 针对表【t_group】的数据库操作Service实现
 * @createDate 2023-11-25 23:44:16
 */
@Service
@AllArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

}




