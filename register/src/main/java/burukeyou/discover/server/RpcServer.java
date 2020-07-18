package burukeyou.discover.server;

import burukeyou.common.coder.RpcProtocolDecoder;
import burukeyou.common.coder.RpcProtocolEncoder;
import burukeyou.common.coder.UnpackerHandler;
import burukeyou.common.config.BoomRpcProperties;
import burukeyou.discover.server.handler.ServerRequesthandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RpcServer {

    @Autowired
    private BoomRpcProperties properties;

    public void start()  {
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel ch) {
                            //  UnpackerHandler(in) ->
                            //      RpcProtocolDecoder(in) -> ServerRequesthandler(in) -> RpcProtocolEncoder(out)
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new UnpackerHandler());
                            pipeline.addLast(RpcProtocolDecoder.INSTANCE);
                            pipeline.addLast(ServerRequesthandler.INSTACNCE);
                            pipeline.addLast(RpcProtocolEncoder.INSTANCE);
                        }
                    });

            int port = Integer.parseInt(properties.getServerPort());
            ChannelFuture channelFuture = serverBootstrap.bind(port).addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                }
            });

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
