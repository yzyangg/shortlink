package org.yzy.shortlink.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yzy.shortlink.admin.common.convention.result.Result;
import org.yzy.shortlink.admin.common.convention.result.Results;
import org.yzy.shortlink.admin.service.GroupService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/25 23:55
 */
@RestController("/group")
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




}
