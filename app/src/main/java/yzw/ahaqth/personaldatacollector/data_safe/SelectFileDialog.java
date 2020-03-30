package yzw.ahaqth.personaldatacollector.data_safe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.custom_views.MyDividerItemDecoration;
import yzw.ahaqth.personaldatacollector.custom_views.RecyclerViewIndicator;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.operators.FileOperator;

public class SelectFileDialog extends DialogFragment {
    private class _VH extends RecyclerView.ViewHolder{
        TextView textView;
        _VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.filelist_item_textview);
        }
    }
    private class _Adapter extends RecyclerView.Adapter<_VH>{

        @NonNull
        @Override
        public _VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new _VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull final _VH holder, int position) {
            String s = fileNames[position];
            holder.textView.setText(s.substring(0,s.length() - 4));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return fileNames.length;
        }
    }

    private String[] fileNames;
    private String selectedFileName;
    private DialogDismissListener dialogDismissListener;
    private boolean confirmFlag= false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fileNames = FileOperator.getFileList(FileOperator.backupDir,FileOperator.BACKUP_FILE_ENDNAME);
        View view = inflater.inflate(R.layout.dialog_list_view,container,false);
        initialView(view);
        return view;
    }


    public void setDismissListener(DialogDismissListener dismissListener){
        this.dialogDismissListener = dismissListener;
    }

    private void initialView(View view){
        TextView titleTV = view.findViewById(R.id.dialog_list_view_title);
        titleTV.setText("备份文件列表");
        RecyclerView recyclerView = view.findViewById(R.id.dialog_list_view_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new _Adapter());
        recyclerView.addItemDecoration(new MyDividerItemDecoration());
        recyclerView.addItemDecoration(new RecyclerViewIndicator(R.color.colorPrimary));
        view.findViewById(R.id.dialog_list_view_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void itemClick(int position){
        selectedFileName = fileNames[position];
        confirmFlag = true;
        dismiss();
    }

    private void cancel(){
        confirmFlag = false;
        selectedFileName = "";
        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dialogDismissListener!=null){
            dialogDismissListener.onDismiss(confirmFlag,selectedFileName);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            if (window != null) {
                DisplayMetrics dm = new DisplayMetrics();
                Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = (int) (dm.widthPixels * 0.86);
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setLayout(width, height);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }
}
