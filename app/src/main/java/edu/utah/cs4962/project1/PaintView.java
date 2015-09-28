package edu.utah.cs4962.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guanxiong.Wu
 */
public class PaintView extends ViewGroup
{
    public interface OnPolylineComposedListener
    {
        public void onPolylineComposed(List<Polyline> polylines);
    }
    OnPolylineComposedListener _onValueChangedListener = null;

    private List<Polyline> _polyline = new ArrayList<>();
    private List<PointF> _points = new ArrayList<>();
    private List<Float> _times = new ArrayList<>();
    private int currentColor = Color.BLACK;
    private boolean enabledTouch = true;

    public PaintView(Context context)
    {
        super(context);
        setBackgroundColor(Color.WHITE);
    }

    public void setOnPolylineComposedListener(OnPolylineComposedListener onValueChangedListener)
    {
        _onValueChangedListener = onValueChangedListener;
    }

    public void setEnabledTouch(boolean flag)
    {
        enabledTouch = flag;
    }

    public int getCurrentColor()
    {
        return currentColor;
    }

    public void setCurrentColor(int currentColor)
    {
        this.currentColor = currentColor;
        invalidate();
    }

    public void setPolyline(List<Polyline> polyline)
    {
        _polyline = polyline;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (enabledTouch)
        {
            PointF touchPoint = new PointF(event.getX() / getWidth(), event.getY() / getHeight());
            _points.add(touchPoint);
            if (event.getActionMasked() == MotionEvent.ACTION_UP)
            {
                _polyline.add(new Polyline(_points, currentColor));
                _points.clear();
                if (_onValueChangedListener != null)
                    _onValueChangedListener.onPolylineComposed(_polyline);
            }
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // Draw Current line
        Path path = new Path();
        drawLine(path, _points);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f * getResources().getDisplayMetrics().density);
        paint.setColor(currentColor);
        canvas.drawPath(path, paint);

        // Draw all history line
        for (int lineIndex = 0; lineIndex < _polyline.size(); lineIndex++)
        {
            List<PointF> points = _polyline.get(lineIndex).points;
            path = new Path();
            drawLine(path, points);
            paint.setColor(_polyline.get(lineIndex).color);
            canvas.drawPath(path, paint);
        }
    }

    private void drawLine(Path path, List<PointF> points)
    {
        for (int pointIndex = 0; pointIndex < points.size(); pointIndex++)
        {
            PointF point = points.get(pointIndex);
            if (pointIndex == 0)
                path.moveTo(point.x * getWidth(), point.y * getHeight());
            else
                path.lineTo(point.x * getWidth(), point.y * getHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {

    }
}