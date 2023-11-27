package org.yzy.shortlink.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yzy.shortlink.project.common.convention.result.Result;
import org.yzy.shortlink.project.common.convention.result.Results;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.service.ShortLinkService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/27 9:03
 */
@RestController
@RequestMapping("/api/short-link/link")
@RequiredArgsConstructor
public class ShortLinkController {
    public final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     * @param shortLinkCreateReqDTO 短链接创建请求参数
     * @return 短链接创建响应参数
     */
    @PostMapping("/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        return Results.success(shortLinkService.createShortLink(shortLinkCreateReqDTO));
    }

}
