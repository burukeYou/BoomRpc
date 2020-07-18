package burukeyou.common.coder;

import burukeyou.common.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 *  解码器
 */
@ChannelHandler.Sharable //空注解起标记作用， 确保不能添加两个一样的handler到pipline中重复添加
public class RpcProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    protected RpcProtocolDecoder(){}

    public static final RpcProtocolDecoder INSTANCE = new RpcProtocolDecoder();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(CodecUtil.decoder(msg));
    }
}
