package burukeyou.rpc.client;


import burukeyou.common.protocol.RpcRequest;
import burukeyou.common.protocol.RpcResponse;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// 默认每个handler对象只能使用一次（内部用add状态维护）,标记Sharable就可以使用多次，否则进行重连时抛出异常
@ChannelHandler.Sharable
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private static final int MAX_RETRY_TIMES = 2;  // 连接失败重连接次数
    private static final int MAX_RETRY_SEND_TIMES = 3; // 请求超时重发次数

    private Bootstrap bootstrap;
    private NioEventLoopGroup bossGroup;
    private final String ip;
    private final int port;
    private final String serverName;
    private Channel providerChannel;
    private RpcResponse rpcResponse;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public RpcClient(String ip, int port,String serverName) {
        this.ip = ip;
        this.port = port;
        this.serverName = serverName;
        initConnect();
    }

    //
    private void initConnect() {
        bossGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 .option(ChannelOption.TCP_NODELAY, true)
                 .handler(new RpcClientHandlerInitializer(RpcClient.this));
    }

    private void connect(int remainingConnectTimes)  {
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).addListener(future -> {
                if (future.isSuccess()) {
                    providerChannel = ((ChannelFuture) future).channel();
                    countDownLatch.countDown();
                } else if (remainingConnectTimes <= 0) {
                    logger.error("重试次数已用完，放弃连接！");
                } else {
                    int order = (MAX_RETRY_TIMES - remainingConnectTimes) + 1; //第几次
                    logger.error(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + ": 连接失败，第" + order + "次连接……");
                    bootstrap.config().group()
                            .schedule(() -> connect(remainingConnectTimes - 1), (1 << order)*200, TimeUnit.MILLISECONDS);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RpcResponse sendRequest(RpcRequest rpcRequest) throws Exception {
        try {
            connect(MAX_RETRY_TIMES);

            boolean await = countDownLatch.await(3, TimeUnit.SECONDS);
            if (!await){
                // 建立连接超时
                throw new RuntimeException(serverName + "服务连接异常");
            }

            send(MAX_RETRY_SEND_TIMES,rpcRequest);

        } finally {
            bossGroup.shutdownGracefully(); // 请求完成后
        }
        return this.rpcResponse;
    }

    private void send(int remainingTimes,RpcRequest rpcRequest) throws InterruptedException {
        System.out.println("第"+((MAX_RETRY_SEND_TIMES - remainingTimes)+1)+"次发送: ");
        if (remainingTimes <= 0){
            throw new RuntimeException("请求服务超时");
        }else {
            providerChannel.writeAndFlush(rpcRequest);
            //超时阻塞等待关闭与服务端的连接
            boolean status = providerChannel.closeFuture().await(2, TimeUnit.SECONDS);
            if (!status){
                send(remainingTimes - 1,rpcRequest);
            }
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.rpcResponse = msg;
        ctx.close(); // 关闭连接
    }
}
