package org.yzy.shortlink.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import org.yzy.shortlink.admin.dto.ShortLinkRemoteService;
import org.yzy.shortlink.admin.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.yzy.shortlink.common.convention.result.Result;
import org.yzy.shortlink.common.convention.result.Results;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

/**
 * 短链接后管控制层
 */
@RestController
@RequestMapping("/api/shortLink/admin/v1/shortlink")
public class ShortLinkController {
    /**
     * /后续重构为SpringCloud Feign调用
     */
    private final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {

    };

    /**
     * 创建短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数
     */
    @PostMapping("/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 修改短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数
     */
    @PutMapping("/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数
     */
    @GetMapping("/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }

    /**
     * 查询短链接分组内数量
     *
     * @param requestParam 请求参数
     * @return 返回参数
     */
    @GetMapping("/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam) {
        return shortLinkRemoteService.listGroupShortLinkCount(requestParam);
    }

}
