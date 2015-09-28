package edu.utah.cs4962.project1;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private List<Polyline> _polyline = new ArrayList<Polyline>();
    private List<Integer> newColors = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout subLayout = new LinearLayout(this);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);

        final PaintView paintView = new PaintView(this);
        rootLayout.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
        rootLayout.addView(subLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        final PaintView outline = new PaintView(this);
        outline.setEnabledTouch(false);
        subLayout.addView(outline, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        paintView.setOnPolylineComposedListener(new PaintView.OnPolylineComposedListener()
        {
            @Override
            public void onPolylineComposed(List<Polyline> polylines)
            {
                _polyline = new ArrayList<Polyline>(polylines);
                outline.setPolyline(_polyline);
            }
        });

        final PaletteView paletteView = new PaletteView(this);
        paletteView.setPadding(30, 60, 90, 120);
        paletteView.setBackgroundColor(Color.WHITE);
        subLayout.addView(paletteView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        int numOfColor = 32;
        int newColorCount = newColors.size();
        int totalColor = numOfColor - 10 + newColorCount;
        float step = 0.3f;
        int splotchIndex = 0;
        while (splotchIndex < totalColor || !newColors.isEmpty())
        {
            View splotchView = new View(this);

            paletteView.addView(splotchView);

            int r = (int) (Math.sin(step * splotchIndex) * 127) + 127;
            int g = (int) (Math.sin(step * splotchIndex + 2) * 127) + 127;
            final int[] b = {(int) (Math.sin(step * splotchIndex + 4) * 127) + 127};
            if (splotchIndex < numOfColor - 10)
                splotchView.setBackgroundColor(Color.rgb(r, g, b[0]));
            else
                splotchView.setBackgroundColor(newColors.get(newColorCount - (totalColor - splotchIndex)));

            // When new color created select flog
            final int[] flag = {0};

            final int finalSplotchIndex = splotchIndex;
            splotchView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (event.getActionMasked() == MotionEvent.ACTION_UP)
                    {
                        if (flag[0] == 0)
                        {
                            paletteView.setClickedChild(finalSplotchIndex);
                            paintView.setCurrentColor(((ColorDrawable) v.getBackground()).getColor());
                            Log.w("View", "" + finalSplotchIndex);
                        } else
                        {
                            flag[0] = 0;
                        }
                    }
                    return false;
                }
            });

            splotchView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int colorA = paintView.getCurrentColor();
                    int colorB = ((ColorDrawable) v.getBackground()).getColor();
                    int newColor = mixTwoColor(colorA, colorB);
                    paintView.setCurrentColor(newColor);
                    paletteView.addNewColor(newColor);
                    newColors.add(newColor);
                    flag[0] = 1;
                    return true;
                }
            });
            if (splotchIndex < totalColor)
                splotchIndex++;
        }
        setContentView(rootLayout);
    }

    private int mixTwoColor(int a, int b)
    {
        int newR = Math.min(Color.red(a) + Color.red(b), 255);
        int newG = Math.min(Color.green(a) + Color.green(b), 255);
        int newB = Math.min(Color.blue(a) + Color.blue(b), 255);
        return Color.rgb(newR, newB, newG);
    }
}