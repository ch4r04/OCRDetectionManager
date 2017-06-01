package socket;

import android.util.Log;

import utils.NetworkUtils;

/**
 * Created by xingr on 2017/5/4.
 */

public class FrameDataSocket {

    public static byte[] HEADER_FRAME = NetworkUtils.intToBytes2(0xffffeeee);    //起始帧
    public static byte[] PKLEN = NetworkUtils.intToBytes2(0x00000000);            //帧总长
    public static byte[] REV = NetworkUtils.intToBytes2(0x001B0000);             //版本号
    public static byte[] SRC = NetworkUtils.intToBytes2( 0x0000004e);             //源地址   //这里是APP设备
    public static byte[] DST = NetworkUtils.intToBytes2(0xA000000e);             //目的地址 //这里是TF500设备
    public static byte[] PKTYP = NetworkUtils.intToBytes22(0x0000);               //帧类型 全填0
    public static byte[] PKID = NetworkUtils.intToBytes22(0x0000);                //流水号
    public static byte[] OTHER_CMD =NetworkUtils.intToBytes2(0x00000000);       //保留位
    public static byte[] CMDCODE =NetworkUtils.intToBytes2(0x00000000);          // 命令码
    public static byte[] DATLEN =NetworkUtils.intToBytes2(0x00000000);          //命令码+数据长度+数据区 的字节长度
    public static byte[] DATA;                         //数据区 n位
    public static byte[] END_FRAME = NetworkUtils.intToBytes2(0xeeeeffff);       //结尾帧


    /**
     * 发送请求连接设备到服务器
     * @return
     */
    public static byte[] sendToConnectRequest(String deviceID){
        //请求连接服务器 请求命令码

        CMDCODE = NetworkUtils.intToBytes2(0x82000000);

        if (deviceID.length() > 16 || deviceID.length() < 0){
            return new byte[0x00];
        }
        //计算总帧长 40+n
        PKLEN = NetworkUtils.intToBytes2(40 + deviceID.length());
        Log.e("tag",PKLEN.length + "");

        //数据区的长度
        DATLEN = NetworkUtils.intToBytes2( 4 + 4 + deviceID.length());

        DATA = deviceID.getBytes();
        Log.e("HEADER",HEADER_FRAME.length+"");
        Log.e("PKLEN",PKLEN.length+"");
        Log.e("REV",REV.length+"");
        Log.e("SRC",SRC.length+"");
        Log.e("DST",DST.length+"");
        Log.e("PKTYP",PKTYP.length+"");
        Log.e("PKID",PKID.length+"");
        Log.e("OTHER_CMD",OTHER_CMD.length+"");
        Log.e("CMD",CMDCODE.length+"");
        Log.e("DATALEN",DATLEN.length+"");
        Log.e("DATA",DATA.length+"");
        Log.e("ENDFRAME",END_FRAME.length+"");

        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,DATA,END_FRAME);
        Log.e("RESULT_BYTE",resultByte.length + "");
        //流水包自动添加
        autoAddPKID();
        return resultByte;

    }

    /**
     * 发送断开连接到服务器
     * @return
     */
    public static byte[] sendDisconnectRequest(){
        //定义状态码
        CMDCODE = NetworkUtils.intToBytes2(0x82000002);
        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;

    }


    /**
     * 构造发送的线路设置数据
     * @param range 距离范围
     * @param wl 激光波长
     * @param pw 脉冲宽度
     * @param time 测量时间
     * @param gi 群折射率
     * @return
     */
    public static byte[] sendLineSetupData(int range, int wl, int pw, int time, int gi){
        //定义状态码
        CMDCODE = NetworkUtils.intToBytes2(0x81000000);
        //填充数据区
        DATA = NetworkUtils.byteMergerAll(
                NetworkUtils.intToBytes2(range),
                NetworkUtils.intToBytes2(wl),
                NetworkUtils.intToBytes2(pw),
                NetworkUtils.intToBytes2(time),
                NetworkUtils.intToBytes2(gi));

        PKLEN = NetworkUtils.intToBytes2(40 + DATA.length);
        DATLEN = NetworkUtils.intToBytes2( 4 + 4 + DATA.length);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,DATA,END_FRAME);
        autoAddPKID();
        return resultByte;
    }

    /**
     * 自动添加PKID流水号
     */
    public static void autoAddPKID(){
        if (NetworkUtils.bytesToInt22(PKID,0) > 0xFFFF){
            PKID = NetworkUtils.intToBytes22(0x0000);
        }else {
            PKID = NetworkUtils.intToBytes22(NetworkUtils.bytesToInt22(PKID,0) + 1);
        }
    }

    /**
     * 停止线路设置
     * @return
     */
    public static byte[] sendStopLineSetupData(){
        //定义状态码
        CMDCODE = NetworkUtils.intToBytes2(0x81000003);
        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;
    }


    /**
     * 获取模板请求
     * @return
     */
    public static byte[] sendGetTemplate(){
        //定义状态码
        CMDCODE = NetworkUtils.intToBytes2(0x81000005);


        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;
    }

    /**
     * 停止获取模板请求
     * @return
     */
    public static byte[] sendStopTemplate(){
        //定义状态码
        CMDCODE = NetworkUtils.intToBytes2(0x81000007);

        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;
    }

    /**
     * 开始故障追踪
     * @return
     */
    public static byte[] sendStartTraceFault(){
        //定义状态码
        CMDCODE = NetworkUtils.intToBytes2(0x81000008);

        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;
    }

    /**
     * 停止故障追踪
     * @return
     */
    public static byte[] sendStopTraceFault(){
        CMDCODE = NetworkUtils.intToBytes2(0x8100000B);

        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;

    }

    /**
     * 发送开始普查测试操作
     * @return
     */
    public static byte[] sendStartCensusTest(){
        CMDCODE = NetworkUtils.intToBytes2(0x8100000C);

        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;
    }

    /**
     * 结束普查测试
     * @return
     */
    public static byte[] sendStopCensusTest(){
        CMDCODE = NetworkUtils.intToBytes2(0x8100000E);

        PKLEN = NetworkUtils.intToBytes2(40);
        DATLEN = NetworkUtils.intToBytes2(4 + 4);
        byte [] resultByte = NetworkUtils.byteMergerAll(HEADER_FRAME,PKLEN,REV,SRC,DST,PKTYP,PKID,OTHER_CMD,CMDCODE,DATLEN,END_FRAME);
        autoAddPKID();
        return resultByte;
    }

}
