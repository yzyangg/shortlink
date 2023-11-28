package org.yzy.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
@RestController
@RequestMapping("/api/short-link/admin/group")
@RequiredArgsConstructor
@Slf4j
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
