package burukeyou.common.serialize;

import burukeyou.common.serialize.impl.ProtostuffSerializer;

public interface Serializer {

    /**
     *  默认使用 Protostuff 序列化
     */
    Serializer DEFAULT = new ProtostuffSerializer();

    /**
     *  序列化
     */
    <T> byte[] serialize(T  object);

    /**
     *  反序列化
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);


    /**
     *   序列化算法
     */
    byte getSerializerAlgorithm();
}
