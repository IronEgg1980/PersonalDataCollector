package yzw.ahaqth.personaldatacollector.custom_views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewIndicator extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Path topPath = null, bottomPath = null;
    private float indicatorWidth, indicatorHeight;
    private float x, topY, bottomY;

    public RecyclerViewIndicator(int indicatorColor) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(indicatorColor);

        indicatorWidth = 40;
        indicatorHeight = 30;
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        x = parent.getWidth() - indicatorWidth - 10;
        topY = 10;
        bottomY = parent.getHeight() - 10;
        if (parent.computeVerticalScrollOffset() > 0) {
            drawTopIndicator(c);
        }
        if (parent.computeVerticalScrollOffset() + parent.computeVerticalScrollExtent() < parent.computeVerticalScrollRange()) {
            drawBottomIndicator(c);
        }
    }

    private void drawTopIndicator(Canvas c) {
        c.save();
        if (topPath == null) {
            topPath = new Path();
            topPath.moveTo(x, topY);
            topPath.lineTo(x - indicatorWidth / 2, topY + indicatorHeight);
            topPath.lineTo(x + indicatorWidth / 2, topY + indicatorHeight);
            topPath.close();
        }
        c.drawPath(topPath, mPaint);
        c.restore();
    }

    private void drawBottomIndicator(Canvas c) {
        c.save();
        if (bottomPath == null) {
            bottomPath = new Path();
            bottomPath.moveTo(x, bottomY);
            bottomPath.lineTo(x - indicatorWidth / 2, bottomY - indicatorHeight);
            bottomPath.lineTo(x + indicatorWidth / 2 , bottomY - indicatorHeight);
            bottomPath.close();
        }
        c.drawPath(bottomPath, mPaint);
        c.restore();
    }
}
