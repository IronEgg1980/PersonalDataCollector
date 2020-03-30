package yzw.ahaqth.personaldatacollector.data_safe;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.operators.FileOperator;
import yzw.ahaqth.personaldatacollector.operators.SetupOperator;
import yzw.ahaqth.personaldatacollector.tools.DialogFactory;
import yzw.ahaqth.personaldatacollector.tools.ToastFactory;

public class BackupRestoreActivity extends AppCompatActivity {
    private TextView backupInfoTV, restoreInfoTV;
    private SimpleDateFormat dateFormat;
    private String[] backupFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        dateFormat = new SimpleDateFormat("最近备份时间：yyyy年M月d日 HH:mm:ss", Locale.CHINA);
        initialView();
        showInfo();
    }

    private void initialView() {
        findViewById(R.id.backup_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup();
            }
        });
        findViewById(R.id.restore_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBackupFileList();
            }
        });
        backupInfoTV = findViewById(R.id.backup_data_info_TV);
        restoreInfoTV = findViewById(R.id.restore_data_info_TV);
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showInfo(){
        long backupTime = SetupOperator.getLastBackupTime();
        backupInfoTV.setText(backupTime > 0?dateFormat.format(backupTime):"还没有备份数据");
        backupFiles = FileOperator.getFileList(FileOperator.backupDir,FileOperator.BACKUP_FILE_ENDNAME);
        String s = "共有 "+backupFiles.length+" 条备份记录";
        restoreInfoTV.setText(s);
    }

    private void backup(){
        try {
            long time = System.currentTimeMillis();
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis());
            FileOperator.backup(fileName);
            SetupOperator.setBackupTime(time);
            new ToastFactory(BackupRestoreActivity.this).showCenterToast("备份成功！");
            showInfo();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            DialogFactory.getTipsDialog(e.getMessage())
                    .show(getSupportFragmentManager(),"backupError");
        }
    }

    private void showBackupFileList(){
        SelectFileDialog selectFileDialog = new SelectFileDialog();
        selectFileDialog.setDismissListener(new DialogDismissListener() {
            @Override
            public void onDismiss(boolean isConfirm, Object... valus) {
                if(isConfirm){
                    new ToastFactory(BackupRestoreActivity.this).showCenterToast((String) valus[0]);
                }else{
                    new ToastFactory(BackupRestoreActivity.this).showCenterToast("cancel");
                }
            }
        });
        selectFileDialog.show(getSupportFragmentManager(),"fileList");
    }

    private boolean checkUsePWD(){
        return false;
    }

    private void restore(){

    }
}
