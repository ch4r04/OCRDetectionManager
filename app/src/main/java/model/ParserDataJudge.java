package model;

import android.util.Log;

import utils.ByteDisposeUtil;

/**
 * Created by ch4r0n on 2017/5/13.
 */

public class ParserDataJudge {


    /**
     * 判断需要解析的数据是否合法 以及他是什么类型
     * @param data
     * @return
     * -1 : 数据不合法
     * 1 : 数据为获取模板数据
     * 2 : 数据为线路追踪数据
     */
    public static int parserData(byte []data){
        String header = ByteDisposeUtil.toHex(data,0,4).toString();
        String trail = ByteDisposeUtil.toHex(data,data.length - 4,data.length).toString();
        Log.e("VAILDDATA->",header+trail);
        if (header.equals("ffffeeee") == false || trail.equals("eeeeffff") == false) {
            Log.e("JUDGE", "this is a unvaild data");
            return -1;
        }
        String cmdCode = ByteDisposeUtil.toHex(data,4,8).toString();
        Log.e("CMDCODE-->",cmdCode);
        if (cmdCode.equals("81000006")){
            //获取模板数据
            Log.e("TEMPLATE-->",cmdCode);
            return 1;
        }else if (cmdCode.equals("81000009")) {
            //获取线路追踪数据
            Log.e("TRACEFAULT-->",cmdCode);
            return 2;
        }else {
            return -2;
        }

    }
}
