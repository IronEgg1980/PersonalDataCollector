package yzw.ahaqth.personaldatacollector.allrecords;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.operators.SetupOperator;
import yzw.ahaqth.personaldatacollector.tools.ToastFactory;

public class SetupDialogFragment extends DialogFragment {
    private TextView setupUserPwdTV;
    private TextView setupGesturePwdTV;
    private TextView setupTextPwdTV;
    private TextView deletedResumeTV;
    private int clickedId;
    private boolean flag = false;

    public void setOnDismiss(DialogDismissListener onDismiss) {
        this.onDismiss = onDismiss;
    }

    private DialogDismissListener onDismiss;

    private void initialView(View view){
        setupUserPwdTV = view.findViewById(R.id.setup_user_pwd_TV);
        setupUserPwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedId = 1;
                flag = true;
                dismiss();
            }
        });
        setupGesturePwdTV = view.findViewById(R.id.setup_gesture_pwd_TV);
        setupGesturePwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedId = 2;
                flag = true;
                dismiss();
            }
        });
        setupTextPwdTV = view.findViewById(R.id.setup_text_pwd_TV);
        setupTextPwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedId = -1;
                flag = true;
                SetupOperator.setInputPassWordMode(1);
                new ToastFactory(getContext()).showCenterToast("设置成功");
                dismiss();
            }
        });
        deletedResumeTV = view.findViewById(R.id.setup_resume_TV);
        deletedResumeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedId = 3;
                flag = true;
                dismiss();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setup_layout,container,false);
        initialView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            DisplayMetrics dm = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = (int) (dm.widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Window window = dialog.getWindow();
            if(window!=null){
                window.setLayout(width,height);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismiss.onDismiss(flag,clickedId);
    }
}
