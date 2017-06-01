package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.github.mikephil.charting.components.XYDESC;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by xingr on 2017/5/6.
 */

public class XYDESCRenderer extends Renderer {


    /**
     * paint for the XYDESCRenderer labels
     */
    protected Paint mXYDESCLabelPaint;
    protected XYDESC mXYDesc;

    public XYDESCRenderer(ViewPortHandler viewPortHandler, XYDESC xydesc) {
        super(viewPortHandler);
        mXYDesc = xydesc;
        //初始化画笔
        mXYDESCLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mXYDESCLabelPaint.setTextSize(Utils.convertDpToPixel(xydesc.getTextSize()));
        mXYDESCLabelPaint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * Returns the Paint object used for drawing the Legend labels.
     *
     * @return
     */
    public Paint getLabelPaint() {
        return mXYDESCLabelPaint;
    }

    /**
     * 在画布上绘制
     * @param c
     */
    public void renderLegend(Canvas c) {
        if (!mXYDesc.isEnabled())
            return;
        Typeface tf = mXYDESCLabelPaint.getTypeface();

        if (tf != null)
            mXYDESCLabelPaint.setTypeface(tf);

        mXYDESCLabelPaint.setTextSize(mXYDesc.getTextSize());
        mXYDESCLabelPaint.setColor(mXYDesc.getTextColor());
        //获取行高
        float labelLineHeight = Utils.getLineHeight(mXYDESCLabelPaint);
        float yoffset = mXYDesc.getYOffset();
        float xoffset = mXYDesc.getXOffset();
//        drawLabel(c, mViewPortHandler.contentLeft() + mXYDesc.getMaximumEntryWidth(mXYDESCLabelPaint, mXYDesc.getyDesc()), yoffset + labelLineHeight + mXYDesc.getyPadding(), mXYDesc.getyDesc());
        drawLabel(c, mViewPortHandler.contentLeft()
                , yoffset + labelLineHeight + mXYDesc.getyPadding(), mXYDesc.getyDesc());
        drawLabel(c, mViewPortHandler.contentRight() - mXYDesc.getMaximumEntryWidth(mXYDESCLabelPaint, mXYDesc.getxDesc())
                , mViewPortHandler.getChartHeight() - yoffset - mXYDesc.getMaximumEntryHeight(mXYDESCLabelPaint, mXYDesc.getxDesc()) + 10, mXYDesc.getxDesc());

    }

    /**
     * Draws the provided label at the given position.
     *
     * @param c     canvas to draw with
     * @param x
     * @param y
     * @param label the label to draw
     */
    protected void drawLabel(Canvas c, float x, float y, String label) {
        c.drawText(label, x, y, mXYDESCLabelPaint);
    }

}
