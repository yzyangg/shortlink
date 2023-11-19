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
 * @date 2023/11/19 23:01
 */
public class IdCardDesensitizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String idCard, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s = DesensitizedUtil.idCardNum(idCard, 4, 4);
        jsonGenerator.writeString(s);
    }
}
