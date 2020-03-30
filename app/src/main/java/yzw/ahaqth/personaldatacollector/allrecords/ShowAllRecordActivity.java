package yzw.ahaqth.personaldatacollector.allrecords;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yzw.ahaqth.personaldatacollector.BaseActivity;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.custom_views.MyDividerItemDecoration;
import yzw.ahaqth.personaldatacollector.details.ShowDetailsActivity;
import yzw.ahaqth.personaldatacollector.inputoredit.InputOrEditRecordActivity;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.interfaces.ItemClickListener;
import yzw.ahaqth.personaldatacollector.modules.AccountRecord;
import yzw.ahaqth.personaldatacollector.modules.RecordGroup;
import yzw.ahaqth.personaldatacollector.operators.FileOperator;
import yzw.ahaqth.personaldatacollector.operators.GroupOperator;
import yzw.ahaqth.personaldatacollector.operators.RecordOperator;
import yzw.ahaqth.personaldatacollector.operators.SetupOperator;
import yzw.ahaqth.personaldatacollector.tools.DialogFactory;
import yzw.ahaqth.personaldatacollector.tools.ToastFactory;
import yzw.ahaqth.personaldatacollector.tools.ToolUtils;

public class ShowAllRecordActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView recyclerview;
    private RecordAdapter adapter;
    private GroupAdapter groupAdapter;
    private List<RecordGroup> groupList;
    private List<AccountRecord> list;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView titleIV;
    private Random random;
    private int[] imagesId = {R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3, R.mipmap.bg4, R.mipmap.bg5};
    private long recordGroupId;
    private String title;
    private RecyclerView.ItemDecoration dividerItemDecoration;
    private BroadcastReceiver groupFlush, addRecordBR;
    private LocalBroadcastManager lbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_record_activity);
        FileOperator.initialAppDir(this);
        recordGroupId = -1L;
        title = "全部记录";
        initialView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        lbm = LocalBroadcastManager.getInstance(this);
        groupFlush = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onGoupBroadcastReceved();
            }
        };
        addRecordBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRecordBroadcastReceved();
            }
        };
        lbm.registerReceiver(addRecordBR, new IntentFilter("AddRecord"));
        lbm.registerReceiver(groupFlush, new IntentFilter("GroupFlush"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(addRecordBR);
        lbm.unregisterReceiver(groupFlush);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        readData();
    }

    private void onRecordBroadcastReceved(){
        recordGroupId = -1L;
        title = "全部记录";
        readData();
        recyclerview.scrollToPosition(list.size() - 1);
    }

    private void onGoupBroadcastReceved(){
        groupList.clear();
        groupList.addAll(GroupOperator.findAll(true));
        groupAdapter.notifyDataSetChanged();
    }

    private void showDataInGroup(int groupPosition){
        drawerLayout.closeDrawers();

        if(groupPosition == -1){
            recordGroupId = -1L;
            title = "全部记录";
        }else{
            RecordGroup recordGroup = groupList.get(groupPosition);
            recordGroupId = recordGroup.getId();
            title = recordGroup.getGroupName();
        }
        readData();
        appBarLayout.setExpanded(false);
        recyclerview.scrollToPosition(0);
    }

    private void setupRecordGroup() {
        drawerLayout.closeDrawers();
        startActivity(new Intent(ShowAllRecordActivity.this, RecordGroupActivity.class));
    }

    private void showInputPWDDialog(final int mode) {
        drawerLayout.closeDrawers();
        InputPWDDialog dialog = new InputPWDDialog();
        dialog.setOnDismiss(new DialogDismissListener() {
            @Override
            public void onDismiss(boolean isConfirm, Object... objects) {
                if (isConfirm) {
                    if (mode == 1) {
                        setUserNamePWD();// 设置用户名和密码
                    } else if (mode == 2) {
                        setGesturePWD();// 设置手势密码
                    }
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "showInputPWDDialog");
    }

    private void setUserNamePWD() {
        drawerLayout.closeDrawers();
        startActivity(new Intent(ShowAllRecordActivity.this, SetUserNamePWDActivity.class));
    }

    private void setGesturePWD() {
        drawerLayout.closeDrawers();
        startActivity(new Intent(ShowAllRecordActivity.this, SetGesturePWDActivity.class));
    }

    private void setTextPWD(){
        drawerLayout.closeDrawers();
        SetupOperator.setInputPassWordMode(1);
        new ToastFactory(ShowAllRecordActivity.this).showCenterToast("已启用文字密码");
    }

    private void deleResume() {
        drawerLayout.closeDrawers();
        startActivity(new Intent(ShowAllRecordActivity.this, DeleResumeActivity.class));
    }

    private void setImage() {
        int index = random.nextInt(5);
        titleIV.setImageResource(imagesId[index]);
    }

    private void showRecordDetails(int position,View view){
        AccountRecord accountRecord = list.get(position);
        Intent intent = new Intent(ShowAllRecordActivity.this, ShowDetailsActivity.class);
        intent.putExtra("id", accountRecord.getId());
        int startX = 0, startY = view.getMeasuredHeight() / 2;
        startActivity(intent, ActivityOptions.makeScaleUpAnimation(view, startX, startY, view.getMeasuredWidth(), 0).toBundle());
    }

    private void addRecord(){
        appBarLayout.setExpanded(false);
        Intent intent = new Intent(ShowAllRecordActivity.this, InputOrEditRecordActivity.class);
        startActivity(intent);
    }

    private void onRecyclerViewScrolled(){
        boolean isBottom = recyclerview.computeVerticalScrollRange() >= recyclerview.computeVerticalScrollOffset()+recyclerview.computeVerticalScrollExtent();
        if(recyclerview.canScrollVertically(-1) && isBottom)
            fab.hide();
        else
            fab.show();
    }

    private void readData() {
        toolbarLayout.setTitle(title);
        list.clear();
        list.addAll(RecordOperator.findAll(recordGroupId));
        if (list.isEmpty()) {
            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setSortIndex(999999);
            list.add(accountRecord);
        }
        adapter.notifyDataSetChanged();
    }

    private void deleRecord(final int position, final RecordAdapter.RecordVH recordVH) {
        final AccountRecord record = list.get(position);
        if (record != null) {
            String message = "是否要删除【" + record.getRecordName() + "】？";
            DialogFactory.getConfirmDialog(message)
                    .setDismissListener(new DialogDismissListener() {
                        @Override
                        public void onDismiss(boolean isConfirm, Object... valus) {
                            if (isConfirm) {
                                RecordOperator.dele(record);
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                            }else{
                                recordVH.closeMenu();
                            }
                        }
                    })
                    .show(getSupportFragmentManager(),"confirmDele");
        }
    }

    private void onAppbarLayoutScrolled(int i){
        int offset = Math.abs(i);
        int range = appBarLayout.getTotalScrollRange();
        if (offset > range - 1) {
            if (titleIV.getVisibility() == View.VISIBLE) {
                titleIV.setVisibility(View.INVISIBLE);
                setImage();
            }
        } else if (offset < range) {
            if (titleIV.getVisibility() != View.VISIBLE)
                titleIV.setVisibility(View.VISIBLE);
        }
    }

    private void initialView() {
        list = new ArrayList<>();
        groupList = new ArrayList<>();
        groupList.addAll(GroupOperator.findAll(true));
        random = new Random();
        groupAdapter = new GroupAdapter(this, groupList);
        adapter = new RecordAdapter(list);
        groupAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(int position, @Nullable Object... values) {
                showDataInGroup(position);
            }
        });
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void click(int position, @Nullable Object... values) {
                showRecordDetails(position,(View) values[1]);
            }
        });
        adapter.setDelClick(new ItemClickListener() {
            @Override
            public void click(int position, @Nullable Object... values) {
                deleRecord(position, (RecordAdapter.RecordVH) values[0]);
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_24dp);
        drawerLayout = findViewById(R.id.drawerLayout);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        dividerItemDecoration = new MyDividerItemDecoration();
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onRecyclerViewScrolled();
            }
        });
        toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        titleIV = findViewById(R.id.titleIV);
        setImage();
        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                onAppbarLayoutScrolled(i);
            }
        });
        findViewById(R.id.slide_menu_accountname_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputPWDDialog(1);
            }
        });
        findViewById(R.id.slide_menu_gesture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputPWDDialog(2);
            }
        });
        findViewById(R.id.slide_menu_textpwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextPWD();
            }
        });
        findViewById(R.id.slide_menu_groupmanage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRecordGroup();
            }
        });
        findViewById(R.id.slide_menu_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleResume();
            }
        });
        findViewById(R.id.slide_menu_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastFactory(ShowAllRecordActivity.this).showCenterToast("建设中...");
            }
        });
        findViewById(R.id.slide_menu_allgroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataInGroup(-1);
            }
        });
        TextView slideMenuTitleTV = findViewById(R.id.slidemenu_title);
        slideMenuTitleTV.setText(ToolUtils.getHelloString());
        RecyclerView recyclerView = findViewById(R.id.slide_menu_groupList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(groupAdapter);
    }
}
