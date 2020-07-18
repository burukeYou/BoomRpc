package burukeyou.common.coder;

import burukeyou.common.protocol.RpcProtocol;
import burukeyou.common.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 *  基于长度域的拆包处理器(ChannelInboundHandler)
 *          每次根据设置的长度读取该长度的数据包
 *
 */
public class UnpackerHandler extends LengthFieldBasedFrameDecoder {

    private static final int lengthFieldOffset = 7;

    private static  final int intlengthFieldLength = 4;

    /**
     *  设置此次拆包的长度
     *      从 lengthFieldOffset 位置 读取  intlengthFieldLength 个长度的数据就是此次拆包的长度
     *      关于lengthFieldOffset和lengthFieldLength偏移量设置
     *      {@link burukeyou.common.util.CodecUtil#encoder(ByteBuf, RpcProtocol)}
     */
    public UnpackerHandler() {
        super(Integer.MAX_VALUE, lengthFieldOffset, intlengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 请求消息报文携带的魔数与自定义的魔数不同就拒绝响应
        if (in.getInt(in.readerIndex()) != CodecUtil.MAGIC_NUMBER){
            ctx.channel().close(); // 关闭连接
            return null;
        }
        return  super.decode(ctx,in); //拆包
    }
}
