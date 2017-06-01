package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private static RxTxBytes getRxTxBytesSinceBoot(Context context) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {}

        if (applicationInfo != null) {
            int uid = applicationInfo.uid;
            long rxBytes = TrafficStats.getUidRxBytes(uid);
            long txBytes = TrafficStats.getUidTxBytes(uid);
            return new RxTxBytes(rxBytes, txBytes);
        } else {
            return null;
        }
    }

    public static void saveNetworkUsageOnShutDown(Context context) {
        RxTxBytes rxTxBytes = getTotalRxTxBytes(context);
        if (rxTxBytes != null) {
            SharedPreferences sharedPreferences = PreferenceUtils.getSharedPreferences(context);
            sharedPreferences.edit().putLong(PreferenceUtils.TRAFFIC_RECEIVED, rxTxBytes.rxBytes)
                    .putLong(PreferenceUtils.TRAFFIC_TRANSMITTED, rxTxBytes.txBytes).apply();
        }
    }

    public static RxTxBytes getTotalRxTxBytes(Context context) {
        RxTxBytes rxTxBytes = getRxTxBytesSinceBoot(context);
        if (rxTxBytes != null) {
            SharedPreferences sharedPreferences = PreferenceUtils.getSharedPreferences(context);
            long rxBytes = sharedPreferences.getLong(PreferenceUtils.TRAFFIC_RECEIVED, 0);
            long txBytes = sharedPreferences.getLong(PreferenceUtils.TRAFFIC_TRANSMITTED, 0);
            return new RxTxBytes(rxBytes + rxTxBytes.rxBytes, txBytes + rxTxBytes.txBytes);
        } else {
           return null;
        }
    }

    public static class RxTxBytes {
        public long rxBytes;
        public long txBytes;

        public RxTxBytes(long rxBytes, long txBytes) {
            this.rxBytes = rxBytes;
            this.txBytes = txBytes;
        }
    }


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     * @param value
     *            要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes( int value )
    {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }
    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    public static byte[] intToBytes22(int value)
    {
        byte[] src = new byte[2];
        src[0] = (byte)( (value >> 8) & 0xFF);
        src[1] = (byte)( value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }

    public static int bytesToInt22(byte[] src, int offset) {
        int value;
        value = (int)(((src[offset] & 0xFF)<<8)
                |(src[offset+1] & 0xFF));
        return value;
    }


    /**
     * 合并几个byte[]
     * @param values
     * @return
     */
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }
}