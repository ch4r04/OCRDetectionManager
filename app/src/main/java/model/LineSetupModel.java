package model;

/**
 * Created by ch4r0n on 2017/5/7.
 */

/**
 * @name 光缆监测管理系统
 * @package_name name：model
 * @class_name LineSetupModel
 * @description 获取模板得到的最终数据
 * @anthor ch4r0n QQ:609461975
 * @time 2017/5/7 11:30
 * @change
 * @chang 11:30
 */
public class LineSetupModel {

    private int waveLength;

    private float refractive;

    private int distanceRange;

    private int pulseWidth;

    private int mTime;

    private int tracePointLoc;

    private static LineSetupModel instance;

    private LineSetupModel(){};

    public static synchronized LineSetupModel getInstance(){
       if (instance == null){
           instance = new LineSetupModel();
       }
        return instance;
    }

    public static synchronized LineSetupModel initWithAllContribute(int waveLength, float refractive, int distanceRange, int pulseWidth, int mTime, int tracePointLoc){
        if (instance == null) {
            instance = new LineSetupModel();
        }
        instance.waveLength = waveLength;
        instance.refractive = refractive;
        instance.distanceRange = distanceRange;
        instance.pulseWidth = pulseWidth;
        instance.mTime = mTime;
        instance.tracePointLoc = tracePointLoc;
        return instance;
    }

    public int getWaveLength() {
        return waveLength;
    }

    public void setWaveLength(int waveLength) {
        this.waveLength = waveLength;
    }

    public float getRefractive() {
        return refractive;
    }

    public void setRefractive(float refractive) {
        this.refractive = refractive;
    }

    public int getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(int distanceRange) {
        this.distanceRange = distanceRange;
    }

    public int getPulseWidth() {
        return pulseWidth;
    }

    public void setPulseWidth(int pulseWidth) {
        this.pulseWidth = pulseWidth;
    }

    public int getmTime() {
        return mTime;
    }

    public void setmTime(int mTime) {
        this.mTime = mTime;
    }

    public int getTracePointLoc() {
        return tracePointLoc;
    }

    public void setTracePointLoc(int tracePointLoc) {
        this.tracePointLoc = tracePointLoc;
    }
}
