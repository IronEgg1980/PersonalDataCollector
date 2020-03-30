package yzw.ahaqth.personaldatacollector.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.Nullable;

public class ScaleMaterialCardView extends MaterialCardView {
    private Animation scaleAnimation1, scaleAnimation2;

    public ScaleMaterialCardView(Context context) {
        super(context);
        inital();
    }

    public ScaleMaterialCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inital();
    }

    public ScaleMaterialCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inital();
    }

    private void inital() {
        scaleAnimation1 = new ScaleAnimation(1.0f, 0.86f, 1.0f, 0.86f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setDuration(100);
        scaleAnimation1.setFillAfter(true);
        scaleAnimation1.setInterpolator(new LinearInterpolator());

        scaleAnimation2 = new ScaleAnimation(0.86f, 1.0f, 0.86f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(200);
        scaleAnimation2.setFillAfter(true);
        scaleAnimation2.setInterpolator(new OvershootInterpolator());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAnimation(scaleAnimation1);
                break;

            case MotionEvent.ACTION_CANCEL:
                startAnimation(scaleAnimation2);
                break;

            case MotionEvent.ACTION_UP:
                startAnimation(scaleAnimation2);
                performClick();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
