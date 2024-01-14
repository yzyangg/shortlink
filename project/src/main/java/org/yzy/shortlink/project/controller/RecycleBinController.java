package org.yzy.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yzy.shortlink.common.convention.result.Result;
import org.yzy.shortlink.common.convention.result.Results;
import org.yzy.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.yzy.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.yzy.shortlink.project.service.RecycleBinService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2024/1/14 13:58
 */
@RestController()
@RequestMapping("/api/shortLink/project/v1/recycle-bin")
@RequiredArgsConstructor
public class RecycleBinController {


    public final RecycleBinService recycleBinService;

    /**
     * 短链接移入回收站
     *
     * @param recycleBinSaveReqDTO 短链接信息
     * @return 移入成功或者抛出异常
     */
    @PostMapping("/save")
    public Result<Void> saveRecycle(@RequestBody RecycleBinSaveReqDTO recycleBinSaveReqDTO) {
        recycleBinService.saveRecycleBin(recycleBinSaveReqDTO);
        return Results.success("放入回收站成功");
    }


    /**
     * 回收站分页查询
     * @param shortLinkPageReqDTO 分页参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBin(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        return Results.success(recycleBinService.pageRecycleBin(shortLinkPageReqDTO));
    }

    /**
     * 回收站恢复
     * @param recycleBinSaveReqDTO 短链接信息
     * @return 恢复成功或者抛出异常
     */
    @PostMapping("/restore")
    public Result<Void> restoreRecycleBin(@RequestBody RecycleBinRecoverReqDTO recycleBinSaveReqDTO) {
        recycleBinService.restoreRecycleBin(recycleBinSaveReqDTO);
        return Results.success("恢复成功");
    }
}


