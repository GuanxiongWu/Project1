package edu.utah.cs4962.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guanxiong.Wu on 15/9/20.
 */
public class PaletteView extends ViewGroup
{
    public PaletteView(Context context)
    {
        super(context);
    }

    public List<Integer> newColors = new ArrayList<Integer>();
    private int clickedChild = -1;

    public void setClickedChild(int clickedChild)
    {
        this.clickedChild = clickedChild;
        invalidate();
    }

    public void addNewColor(int color)
    {
        newColors.add(color);
        clickedChild = -1;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        clickedChild = -1;
        invalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float childSize = Math.min(getWidth(), getHeight()) / (getChildCount() / 2.0f);

        RectF layoutOverRect = new RectF();
        layoutOverRect.left = getPaddingLeft();
        layoutOverRect.top = getPaddingTop();
        layoutOverRect.right = getWidth() - getPaddingRight();
        layoutOverRect.bottom = getHeight() - getPaddingBottom();
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.rgb(124, 55, 32));
        canvas.drawOval(layoutOverRect, p);

        layoutOverRect.left = getPaddingLeft() + childSize * 0.4f;
        layoutOverRect.top = getPaddingTop() + childSize * 0.4f;
        layoutOverRect.right = getWidth() - getPaddingRight() - childSize * 0.4f;
        layoutOverRect.bottom = getHeight() - getPaddingBottom() - childSize * 0.4f;

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View childView = getChildAt(childIndex);
            float childTheta = (float) childIndex / (float) getChildCount() * (2.0f * (float) Math.PI);

            PointF childCenter = new PointF();
            childCenter.x = layoutOverRect.centerX() + layoutOverRect.width() * 0.5f * (float) Math.cos(childTheta);
            childCenter.y = layoutOverRect.centerY() + layoutOverRect.height() * 0.5f * (float) Math.sin(childTheta);

            Rect childRect = new Rect();
            childRect.left = (int) (childCenter.x - childSize * 0.4f);
            childRect.top = (int) (childCenter.y - childSize * 0.4f);
            childRect.right = (int) (childCenter.x + childSize * 0.4f);
            childRect.bottom = (int) (childCenter.y + childSize * 0.4f);
            childView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
            if (childIndex == clickedChild)
            {
                Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintStroke.setStrokeWidth(15);
                paintStroke.setStyle(Paint.Style.STROKE);
                paintStroke.setColor(Color.WHITE);
                canvas.drawRect(childRect, paintStroke);
            }
        }

        int numOfColor = newColors.size();
        for (int childIndex = 0; childIndex < numOfColor; childIndex++)
        {
            PointF childCenter = new PointF();
            childCenter.x = layoutOverRect.centerX();
            childCenter.y = layoutOverRect.centerY();

            Rect childRect = new Rect();
            childRect.left = (int) (childCenter.x - childSize * 0.4f);
            childRect.top = (int) (childCenter.y - childSize * 0.4f);
            childRect.right = (int) (childCenter.x + childSize * 0.4f);
            childRect.bottom = (int) (childCenter.y + childSize * 0.4f);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(newColors.get(childIndex));
            canvas.drawRect(childRect, paint);
            if (clickedChild < 0)
            {
                Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintStroke.setStrokeWidth(8);
                paintStroke.setStyle(Paint.Style.STROKE);
                paintStroke.setColor(Color.WHITE);
                canvas.drawRect(childRect, paintStroke);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
    }
}