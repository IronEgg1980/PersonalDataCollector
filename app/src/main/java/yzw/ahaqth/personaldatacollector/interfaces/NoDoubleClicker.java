package yzw.ahaqth.personaldatacollector.interfaces;

import android.view.View;

import yzw.ahaqth.personaldatacollector.BaseActivity;

public abstract class NoDoubleClicker implements View.OnClickListener {
    @Override
    final public void onClick(View v) {
        long time = System.currentTimeMillis();
        if(time - BaseActivity.firstClickTime > 1000) {
            BaseActivity.firstClickTime = time;
            noDoubleClick(v);
        }
    }

    public abstract void noDoubleClick(View v);
}
