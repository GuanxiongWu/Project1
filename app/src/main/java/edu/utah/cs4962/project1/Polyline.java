package edu.utah.cs4962.project1;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guanxiong.Wu
 */
public class Polyline
{
    List<PointF> points = new ArrayList<>();
    List<Float> times = new ArrayList<>();
    int color = 0;

    public Polyline(List<PointF> line, int c)
    {
        points.addAll(line);
        color = c;
    }

    public Polyline(List<PointF> line, List<Float> t, int c)
    {
        points.addAll(line);
        times.addAll(t);
        color = c;
    }
}
