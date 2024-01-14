package org.yzy.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.project.dao.entity.LinkAccessStatsDO;
import org.yzy.shortlink.project.dao.mapper.LinkAccessStatsMapper;
import org.yzy.shortlink.project.service.LinkAccessStatsService;

/**
* @author Lenovo
* @description 针对表【t_link_access_stats】的数据库操作Service实现
* @createDate 2024-01-14 18:25:44
*/
@Service
public class LinkAccessStatsServiceImpl extends ServiceImpl<LinkAccessStatsMapper, LinkAccessStatsDO>
    implements LinkAccessStatsService {

}




