package org.yzy.shortlink.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
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

    
}
