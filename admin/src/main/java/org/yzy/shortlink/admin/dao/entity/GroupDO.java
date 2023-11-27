package org.yzy.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @TableName t_group
 */
@TableName(value = "t_group")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDO extends BaseDO {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}