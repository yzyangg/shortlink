package org.yzy.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yzy.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import org.yzy.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import org.yzy.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import org.yzy.shortlink.admin.service.GroupService;
import org.yzy.shortlink.common.convention.result.Result;
import org.yzy.shortlink.common.convention.result.Results;

import java.util.List;


@RestController
@RequestMapping("/api/shortLink/admin/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;


    /**
     * 新增短链接分组
     *
     * @param requestParam 短链接分组参数
     * @return 新增成功或者抛出异常
     */
    @PostMapping("/")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success("新增成功");
    }

    /**
     * 查询分组
     *
     * @return 分组集合
     */
    @GetMapping("/")
    public Result<List<ShortLinkGroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }


    /**
     * 修改短链接分组名称
     *
     * @param requestParam 短链接分组参数
     * @return 修改成功或者抛出异常
     */
    @PutMapping("/")
    public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO requestParam) {
        groupService.updateGroup(requestParam);
        return Results.success();
    }


    /**
     * 删除短链接分组
     *
     * @param gid 分组id
     * @return 删除成功或者抛出异常
     */
    @DeleteMapping("/")
    public Result<Void> deleteGroup(@RequestParam("gid") String gid) {
        groupService.deleteGroup(gid);
        return Results.success();
    }

    /**
     * 短链接分组参排序
     *
     * @param requestParam 短链接分组排序参数
     * @return 排序成功或者抛出异常
     */
    @PostMapping("/sort")
    public Result<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success();
    }
}
