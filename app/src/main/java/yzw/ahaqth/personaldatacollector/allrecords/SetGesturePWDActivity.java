package yzw.ahaqth.personaldatacollector.allrecords;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import yzw.ahaqth.personaldatacollector.BaseActivity;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.OnGestureViewValidateListener;
import yzw.ahaqth.personaldatacollector.custom_views.GestureView;
import yzw.ahaqth.personaldatacollector.operators.SetupOperator;
import yzw.ahaqth.personaldatacollector.tools.ToastFactory;

public class SetGesturePWDActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView setGesturePWDInfoTV;
    private GestureView setGesturePWDGestureView;
    private boolean isFirsTouch = true;
    private Vibrator vibrator;
    private void initialView(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setGesturePWDInfoTV = findViewById(R.id.set_gesturePWD_info_TV);
        setGesturePWDGestureView = findViewById(R.id.set_gesturePWD_gestureView);
        setGesturePWDGestureView.setUnMatchExceedBoundary(9999);
        setGesturePWDGestureView.setAnswer("");
        setGesturePWDGestureView.setValidateListener(new OnGestureViewValidateListener() {
            @Override
            public void onBlockSelected(int cId) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect effect = VibrationEffect.createOneShot(30, 50);
                    vibrator.vibrate(effect);
                } else {
                    vibrator.vibrate(30);
                }
            }

            @Override
            public void onGestureEvent(boolean matched) {
                if(TextUtils.isEmpty(setGesturePWDGestureView.getAnswer())){
                    setGesturePWDInfoTV.setText("最少连接4个点");
                    return;
                }
                if(matched) {
                    SetupOperator.saveGesturePassWord(setGesturePWDGestureView.getAnswer());
                    SetupOperator.setInputPassWordMode(2);
                    new ToastFactory(SetGesturePWDActivity.this).showCenterToast("设置成功");
                    finish();
                }else if(isFirsTouch){
                    setGesturePWDInfoTV.setText("请确认手势");
                    setGesturePWDGestureView.setAnswer(setGesturePWDGestureView.getAnswer());
                    isFirsTouch = false;
                }else{
                    isFirsTouch = true;
                    setGesturePWDGestureView.setAnswer("");
                    setGesturePWDInfoTV.setText("两次输入的手势密码不一致，请重新设置！");
                }
            }

            @Override
            public void onUnmatchedExceedBoundary() {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_gesture_pwd);
        initialView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vibrator = (Vibrator) Objects.requireNonNull(getSystemService(Context.VIBRATOR_SERVICE));
    }
}
