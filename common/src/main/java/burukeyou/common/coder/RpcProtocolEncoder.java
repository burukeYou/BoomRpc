package burukeyou.common.coder;

import burukeyou.common.protocol.RpcProtocol;
import burukeyou.common.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *   编码 处理器
 */
@ChannelHandler.Sharable
public class RpcProtocolEncoder extends MessageToByteEncoder<RpcProtocol> {

    protected RpcProtocolEncoder(){}

    public static final RpcProtocolEncoder INSTANCE = new RpcProtocolEncoder();

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol msg, ByteBuf out) throws Exception {
        CodecUtil.encoder(out,msg);
    }
}
