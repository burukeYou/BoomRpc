package burukeyou.common.serialize.impl;

import burukeyou.common.entity.enums.SerializerAlgorithmEnum;
import burukeyou.common.serialize.Serializer;
import com.alibaba.fastjson.JSON;


public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithmEnum.JSON.getType();
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
