package socket;

import com.vilyever.socketclient.SocketClient;

/**
 * Created by ch4r0n on 2017/5/9.
 */

/**
 * 框架封装 单例
 */
public class CHSocketClient {

    public static CHSocketClient instance = null;

    SocketClient socketClient;


    public synchronized static CHSocketClient getCHSocketClient(){
        if (instance == null) {
            instance = new CHSocketClient();
        }
        return instance;
    }

    private CHSocketClient(){
        socketClient = new SocketClient();
        socketClient.getAddress().setRemoteIP("192.168.199.97"); // 设置IP
        socketClient.getAddress().setRemotePort(9998); // 设置端口
        socketClient.getAddress().setConnectionTimeout(3 * 1000); // 设置连接超时时长
        socketClient.setCharsetName("UTF-8"); // 设置发送和接收String消息的默认编码
        socketClient.getHeartBeatHelper().setHeartBeatInterval(-2); // 设置自动发送心跳包的时间间隔，若值小于0则不发送心跳包
        socketClient.getHeartBeatHelper().setRemoteNoReplyAliveTimeout(-2); // 设置远程端多长时间内没有消息发送到本地就自动断开连接，若值小于0则不自动断开
        socketClient.getSocketPacketHelper().setReceiveTrailerString(null); // 设置接收消息时判断消息结束的尾部信息，用于解决粘包分包问题，若为null则每次读取InputStream直到其为空，可能出现粘包问题

    }

    public SocketClient getSocketClient() {
        return socketClient;
    }


    public void disConnectedSocket(){
        if (socketClient!=null){
            socketClient.sendData(FrameDataSocket.sendDisconnectRequest());
            socketClient.disconnect();
        }
    }


}
