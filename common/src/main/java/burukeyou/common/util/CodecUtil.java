package burukeyou.common.util;

import burukeyou.common.entity.enums.RequestTypeEnum;
import burukeyou.common.protocol.RpcProtocol;
import burukeyou.common.protocol.RpcRequest;
import burukeyou.common.protocol.RpcResponse;
import burukeyou.common.serialize.Serializer;
import burukeyou.common.serialize.impl.ProtostuffSerializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 *      编解码工具
 */
public class CodecUtil {


    /**
     *  魔数
     */
    public static final int MAGIC_NUMBER = 0x65321487;

    /**
     *  协议版本
     */
    public static final int PROTOCOL_VERSION = 1;

    private static final Map<Byte, Serializer> serializerMap ;

    private static final Map<Byte,Class<? extends RpcProtocol>> requestTypeMap;

    static {
        //
        serializerMap = new HashMap<>();
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);

        //
        requestTypeMap = new HashMap<>();
        requestTypeMap.put(RequestTypeEnum.REQUEST.getType(), RpcRequest.class);
        requestTypeMap.put(RequestTypeEnum.RESPONSE.getType(), RpcResponse.class);
    }

    /**
     *  编码
     */
    public static void encoder(ByteBuf out, RpcProtocol protocol){
        byte[] data = Serializer.DEFAULT.serialize(protocol);
        out.writeInt(MAGIC_NUMBER); // 4
        out.writeByte(PROTOCOL_VERSION); //1
        out.writeByte(Serializer.DEFAULT.getSerializerAlgorithm()); // 1
        out.writeByte(protocol.getRequestType()); // 1
        out.writeInt(data.length); // 4
        out.writeBytes(data);
    }

    /**
     *  解码
     */
    public static RpcProtocol decoder(ByteBuf byteBuf){
        byteBuf.skipBytes(4); //跳过魔数
        byteBuf.skipBytes(1); // 跳过协议版本
        byte serializeAlgorithm = byteBuf.readByte();
        byte requestType = byteBuf.readByte();
        int dataLength = byteBuf.readInt();

        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        Serializer serializer = serializerMap.get(serializeAlgorithm);
        return serializer.deserialize(requestTypeMap.get(requestType), data);
    }

}
