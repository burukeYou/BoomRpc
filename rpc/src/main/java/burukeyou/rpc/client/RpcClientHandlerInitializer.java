package burukeyou.rpc.client;

import burukeyou.common.coder.RpcProtocolDecoder;
import burukeyou.common.coder.RpcProtocolEncoder;
import burukeyou.common.coder.UnpackerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class RpcClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private RpcClient rpcClient;

    public RpcClientHandlerInitializer(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    // 每次连接都会重新构建pipleLine
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new UnpackerHandler());
        pipeline.addLast(RpcProtocolDecoder.INSTANCE);
        pipeline.addLast(rpcClient);
        pipeline.addLast(RpcProtocolEncoder.INSTANCE);
    }
}
