package adapters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by xingr on 2017/5/6.
 */

public class MyYAxisValueFormatter implements IAxisValueFormatter {
    private DecimalFormat mFormat;

    public MyYAxisValueFormatter(){
        mFormat = new DecimalFormat("0.000");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) ;
    }

}
