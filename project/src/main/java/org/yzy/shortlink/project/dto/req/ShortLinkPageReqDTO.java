package org.yzy.shortlink.project.dto.req;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;

/**
 * 短链接分页请求参数
 */
@Data
public class ShortLinkPageReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    private Long size;
    private Long current;

    public IPage<ShortLinkDO> convert() {

        return new Page<>(current, size);
    }
}
