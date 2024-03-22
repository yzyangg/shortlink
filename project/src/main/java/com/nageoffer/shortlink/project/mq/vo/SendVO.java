package com.nageoffer.shortlink.project.mq.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yzy
 * @date 2024/3/21 21:17
 */
@Data
public class SendVO implements Serializable {
    private static final long serialVersionUID = 5905249092659173678L;

    Map<String, String> infoMap;
}
