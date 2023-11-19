package org.yzy.shortlink.admin.serialize;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 23:03
 */
public class PhoneDesensitizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String phone, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s = DesensitizedUtil.mobilePhone(phone);
        jsonGenerator.writeString(s);
    }
}
