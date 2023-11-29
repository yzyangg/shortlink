package org.yzy.shortlink.project.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.yzy.shortlink.project.common.convention.result.Result;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yzy
 * @version 1.0
 * @description 短链接远程服务
 * @date 2023/11/29 9:11
 */
public class ShortLinkRemoteService {

    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        requestMap.put("gid", requestParam.getGid());

        String jsonStr = HttpUtil.get("http://localhost:8080/api/short-link/link/page", requestMap);

        return JSON.parseObject(jsonStr, new TypeReference<>(){});
    }
}
