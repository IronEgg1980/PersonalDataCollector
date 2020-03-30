package yzw.ahaqth.personaldatacollector.allrecords;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yzw.ahaqth.personaldatacollector.BaseActivity;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.interfaces.ItemClickListener;
import yzw.ahaqth.personaldatacollector.modules.RecordGroup;
import yzw.ahaqth.personaldatacollector.operators.GroupOperator;
import yzw.ahaqth.personaldatacollector.tools.DialogFactory;
import yzw.ahaqth.personaldatacollector.tools.ToastFactory;

public class RecordGroupActivity extends BaseActivity {
    private String TAG = "RecordGroupActivity-YZW";
    private Toolbar toolbar;
    private EditText inputET;
    private ImageView flagColorIV;
    private Button addBT;
    private RecyclerView recyclerview;
    private List<RecordGroup> mList;
    private RecyclerView.Adapter adapter;
    private int editPosition = -1;
    private String color;
    private SelectColorFlagPopWin popWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_group);
        mList = new ArrayList<>();
        mList.addAll(GroupOperator.findAll(true));
        adapter = new Adapter();
        color = "#777777";
        initialView();
        popWin = new SelectColorFlagPopWin(this);
        popWin.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(int position, @Nullable Object... values) {
                if (position == R.id.textview1) {
                    // 自定义颜色
                    new ToastFactory(RecordGroupActivity.this).showCenterToast("自定义颜色");
                } else {
                    color = (String) values[0];
                    changeColorFlagImage(color);
                }
            }
        });
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(!verify_password_flag)
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addBT.requestFocus();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("GroupFlush"));
        super.onDestroy();
    }

    private void changeColorFlagImage(String color) {
        Drawable drawable = getDrawable(R.drawable.ic_class_24dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
        flagColorIV.setImageDrawable(drawable);
    }

    private void initialView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        inputET = findViewById(R.id.input_ET);
        flagColorIV = findViewById(R.id.flagColorIV);
        flagColorIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWin.showAsDropDown(v);
            }
        });
        changeColorFlagImage(color);
        addBT = findViewById(R.id.add_BT);
        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(adapter);
    }

    private void add() {
        if (TextUtils.isEmpty(inputET.getText())) {
            inputET.setError("名称不能为空！");
            inputET.requestFocus();
            return;
        }
        String name = inputET.getText().toString().trim();
        if (editPosition != -1) {
            RecordGroup recordGroup = mList.get(editPosition);
            String oldName = recordGroup.getGroupName();
            if (!name.equals(oldName) && GroupOperator.isExist(name)) {
                inputET.setError("名称重复，请修改！");
                inputET.selectAll();
                inputET.requestFocus();
                return;
            }
            recordGroup.setGroupName(name);
            recordGroup.setColorString(color);
            GroupOperator.save(recordGroup);
            adapter.notifyItemChanged(editPosition);
            editPosition = -1;
            addBT.setText("增加");
        } else {
            if (GroupOperator.isExist(name)) {
                inputET.setError("名称重复，请修改！");
                inputET.selectAll();
                inputET.requestFocus();
                return;
            }
            int index = mList.size() + 1;
            RecordGroup recordGroup = new RecordGroup();
            recordGroup.setGroupName(name);
            recordGroup.setColorString(color);
            recordGroup.setSortIndex(index);
            GroupOperator.save(recordGroup);
            mList.add(recordGroup);
            adapter.notifyDataSetChanged();
//        adapter.notifyItemInserted(index);
            recyclerview.smoothScrollToPosition(index);
        }
        inputET.setText("");
        inputET.setError(null);
        inputET.requestFocus();
    }

    private void del(final int position) {
        final RecordGroup recordGroup = mList.get(position);
        String s = "是否删除【" + recordGroup.getGroupName() + "】？";
        DialogFactory.getConfirmDialog(s)
                .setDismissListener(new DialogDismissListener() {
                    @Override
                    public void onDismiss(boolean isConfirm, Object... valus) {
                        if(isConfirm){
                            GroupOperator.dele(recordGroup);
                            mList.remove(position);
                            adapter.notifyDataSetChanged();
//                        adapter.notifyItemRemoved(position);
//                        adapter.notifyItemRangeChanged(position,adapter.getItemCount() - position);
                            for (int i = position; i < mList.size(); i++) {
                                RecordGroup recordGroup = mList.get(i);
                                recordGroup.setSortIndex(i);
                                GroupOperator.save(recordGroup);
                            }
                        }
                    }
                })
                .show(getSupportFragmentManager(),"confirmDele");
    }

    private void edit(int position) {
        RecordGroup recordGroup = mList.get(position);
        color = recordGroup.getColor();
        changeColorFlagImage(color);
        editPosition = position;
        addBT.setText("确定");
        inputET.setText(recordGroup.getGroupName());
        inputET.selectAll();
        inputET.requestFocus();
    }

    private void up(int position) {
        RecordGroup pre = mList.get(position - 1);
        RecordGroup current = mList.get(position);
        GroupOperator.swapSortIndex(pre, current);
        Collections.swap(mList, position - 1, position);
        adapter.notifyItemRangeChanged(position - 1, 2);
    }

    private void down(int position) {
        RecordGroup next = mList.get(position + 1);
        RecordGroup current = mList.get(position);
        GroupOperator.swapSortIndex(next, current);
        Collections.swap(mList, position, position + 1);
        adapter.notifyItemRangeChanged(position, 2);
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recordgroup_edit_item, viewGroup, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH vh, int i) {
            RecordGroup recordGroup = mList.get(i);
            Drawable drawable = getDrawable(R.drawable.ic_class_24dp).mutate();
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            drawable.setColorFilter(Color.parseColor(recordGroup.getColor()), PorterDuff.Mode.MULTIPLY);
            vh.recordGroupNameTV.setCompoundDrawables(drawable,null,null,null);
            vh.recordGroupNameTV.setText(recordGroup.getGroupName());
            vh.up.setEnabled(vh.getAdapterPosition() != 0);
            vh.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    up(vh.getAdapterPosition());
                }
            });
            vh.down.setEnabled(vh.getAdapterPosition() < getItemCount() - 1);
            vh.down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    down(vh.getAdapterPosition());
                }
            });
            vh.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    del(vh.getAdapterPosition());
                }
            });
            vh.rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit(vh.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private class VH extends RecyclerView.ViewHolder {
            private TextView recordGroupNameTV;
            private TextView up;
            private TextView down;
            private TextView del;
            private TextView rename;

            private VH(@NonNull View itemView) {
                super(itemView);
                recordGroupNameTV = itemView.findViewById(R.id.record_group_name_TV);
                up = itemView.findViewById(R.id.up);
                down = itemView.findViewById(R.id.down);
                del = itemView.findViewById(R.id.del);
                rename = itemView.findViewById(R.id.rename);
            }
        }
    }
}
