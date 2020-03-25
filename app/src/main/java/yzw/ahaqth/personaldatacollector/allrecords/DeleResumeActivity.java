package yzw.ahaqth.personaldatacollector.allrecords;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import yzw.ahaqth.personaldatacollector.BaseActivity;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.interfaces.ItemClickListener;
import yzw.ahaqth.personaldatacollector.interfaces.NoDoubleClicker;
import yzw.ahaqth.personaldatacollector.modules.AccountRecord;
import yzw.ahaqth.personaldatacollector.operators.RecordOperator;
import yzw.ahaqth.personaldatacollector.tools.DialogFactory;

public class DeleResumeActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayout buttonGroup;
    private AppCompatTextView infoTV;
//    private LinearLayout infoGroup;
    private DeleResumeAdapter adapter;
    private boolean isMultiMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dele_resume);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        infoTV = findViewById(R.id.info_TV);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        List<AccountRecord> list = RecordOperator.findAllDeleted();
        recyclerView.removeItemDecoration(divider);
        if(list.isEmpty()){
            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setSortIndex(999999);
            list.add(accountRecord);
        }else{
            recyclerView.addItemDecoration(divider);
        }
        adapter = new DeleResumeAdapter(this,list);
        adapter.setLongClick(new ItemClickListener() {
            @Override
            public void click(int position, @Nullable Object... values) {
                isMultiMode = true;
                infoTV.setVisibility(View.GONE);
                buttonGroup.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setAdapter(adapter);
        buttonGroup = findViewById(R.id.button_group);
        buttonGroup.setVisibility(View.GONE);
//        infoGroup = findViewById(R.id.info_Group);
        findViewById(R.id.resume_button).setOnClickListener(new NoDoubleClicker() {
            @Override
            public void noDoubleClick(View v) {
                adapter.resumeAll();
                cancelMultiMode();
            }
        });
        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFactory.getConfirmDialog( "是否彻底清除选中的项目？\n注意：清除后无法恢复！")
                        .setDismissListener(new DialogDismissListener() {
                            @Override
                            public void onDismiss(boolean isConfirm, Object... valus) {
                                if(isConfirm) {
                                    adapter.clearAll();
                                    cancelMultiMode();
                                }
                            }
                        })
                        .show(getSupportFragmentManager(),"confirm");
            }
        });
        findViewById(R.id.cancel_multiMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelMultiMode();
            }
        });
//        findViewById(R.id.close_info_Group).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                infoGroup.setVisibility(View.GONE);
//            }
//        });
    }

    private void cancelMultiMode(){
        adapter.cancelMultiMode();
        buttonGroup.setVisibility(View.GONE);
        isMultiMode = false;
    }

    @Override
    public void onBackPressed() {
        if(isMultiMode){
            cancelMultiMode();
        }else{
            super.onBackPressed();
        }
    }
}
