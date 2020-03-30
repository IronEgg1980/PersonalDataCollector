package yzw.ahaqth.personaldatacollector.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ScrollView;

import androidx.fragment.app.FragmentManager;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import yzw.ahaqth.personaldatacollector.BaseActivity;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.allrecords.SetGesturePWDActivity;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.modules.AccountRecord;
import yzw.ahaqth.personaldatacollector.modules.Setup;
import yzw.ahaqth.personaldatacollector.operators.FileOperator;
import yzw.ahaqth.personaldatacollector.operators.RecordOperator;
import yzw.ahaqth.personaldatacollector.operators.SetupOperator;
import yzw.ahaqth.personaldatacollector.tools.DialogFactory;
import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;
import yzw.ahaqth.personaldatacollector.tools.ToastFactory;
import yzw.ahaqth.personaldatacollector.tools.ToolUtils;

public class MainActivity extends BaseActivity {
    private String TAG = "殷宗旺";
    private ScrollView scrollView;
    private FragmentManager fragmentManager;
    private ToastFactory toastFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollview);
        fragmentManager = getSupportFragmentManager();
        toastFactory = new ToastFactory(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SetupOperator.initialSeed()) {
            int mode = SetupOperator.getInputPassWordMode();
            if (TextUtils.isEmpty(SetupOperator.getUserName())) {
                initial();
            } else {
                if (mode == 1)
                    changeToInputMode();
                else if (mode == 2)
                    changeToGestureMode();
            }
            update();
        } else {
            toastFactory.showCenterToast("抱歉，程序初始化出错...");
            finish();
        }
    }

    private void initial() {
        if (XXPermissions.isHasPermission(this, Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)) {
            fragmentManager.beginTransaction().replace(R.id.fragment_group, new FirstRunFragment()).commit();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            fragmentManager.beginTransaction().replace(R.id.fragment_group, new FirstRunFragment()).commit();
                        } else {
                            toastFactory.showCenterToast("已拒绝授权，APP终止运行！");
                            finish();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            XXPermissions.gotoPermissionSettings(MainActivity.this, true);
                        } else {
                            toastFactory.showCenterToast("已拒绝授权，APP终止运行！");
                            finish();
                        }
                    }
                });
    }

    public void changeToGestureMode() {
        fragmentManager.beginTransaction().replace(R.id.fragment_group, new GesturePassWordFragment()).commit();
    }

    public void changeToInputMode() {
        fragmentManager.beginTransaction().replace(R.id.fragment_group, new InputPassWordFragment()).commit();
    }

    public void changeToSetGesturePWD() {
        startActivity(new Intent(MainActivity.this, SetGesturePWDActivity.class));
    }

    public void onFirstRunFinished() {
        DialogFactory.getConfirmDialog("用户名和密码设置成功！是否现在设置手势密码？")
                .setDismissListener(new DialogDismissListener() {
                    @Override
                    public void onDismiss(boolean isConfirm, Object... valus) {
                        if (isConfirm) {
                            changeToSetGesturePWD();
                        } else {
                            changeToInputMode();
                        }
                    }
                })
                .show(getSupportFragmentManager(), "confirm");
    }

    public void scrollToBottom() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollBy(0, 250);
            }
        }, 100);
    }

    public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RecordOperator.clearOldDeletedRecord();// 清除删除30天以上的项目
                long version = ToolUtils.getAppVersionCode(MainActivity.this);
                SetupOperator.setLastAppVersion(version);
            }
        }).start();
    }
}
