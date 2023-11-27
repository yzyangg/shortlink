package org.yzy.shortlink.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yzy.shortlink.admin.common.convention.result.Result;
import org.yzy.shortlink.admin.common.convention.result.Results;
import org.yzy.shortlink.admin.dto.req.LinkGroupUpdateReq;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupListRespDTO;
import org.yzy.shortlink.admin.service.GroupService;

import java.util.List;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/25 23:55
 */
@RestController("/api/short-link/admin/group")
@AllArgsConstructor
public class GroupController {

    public final GroupService groupService;

    /**
     * 保存分组
     *
     * @param groupName
     */
    @PostMapping("/save")
    public Result<Void> saveGroup(@RequestBody String groupName) {
        groupService.saveGroup(groupName);
        return Results.success();
    }


    /**
     * 获取分组列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShortLinkGroupListRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

    @PostMapping("/update")
    public Result<Void> updateGroup(@RequestBody LinkGroupUpdateReq LinkGroupUpdateReq) {
        return Results.success("功能待开发");
    }


}
