package yzw.ahaqth.personaldatacollector.tools;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import yzw.ahaqth.personaldatacollector.interfaces.NoDoubleClicker;

public final class DialogFactory extends DialogFragment {
    private String message = "";
    private boolean isConfirmMode;
    private View cancelView, confirmView;
    private TextView messageTextView;
    private DialogDismissListener dismissListener;

    public DialogFactory setDismissListener(DialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    public static DialogFactory getConfirmDialog(String message) {
        DialogFactory dialogFactory = new DialogFactory();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isConfirmMode", true);
        bundle.putString("message", message);
        dialogFactory.setArguments(bundle);
        return dialogFactory;
    }

    public static DialogFactory getTipsDialog(String message) {
        DialogFactory dialogFactory = new DialogFactory();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isConfirmMode", false);
        bundle.putString("message", message);
        dialogFactory.setArguments(bundle);
        return dialogFactory;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.isConfirmMode = bundle.getBoolean("isConfirmMode");
            this.message = bundle.getString("message");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_or_tips_view, container, true);
        cancelView = view.findViewById(R.id.cancelBT);
        cancelView.setOnClickListener(new NoDoubleClicker() {
            @Override
            public void noDoubleClick(View v) {
                if (dismissListener != null)
                    dismissListener.onDismiss(false);
                dismiss();
            }
        });
        confirmView = view.findViewById(R.id.confirmBT);
        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dismissListener != null)
                    dismissListener.onDismiss(true);
                dismiss();
            }
        });
        confirmView.setVisibility(isConfirmMode ? View.VISIBLE : View.GONE);
        messageTextView = view.findViewById(R.id.messageTV);
        messageTextView.setText(message);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            if (window != null) {
                DisplayMetrics dm = new DisplayMetrics();
                Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = (int) (dm.widthPixels * 0.75);
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setLayout(width, height);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }
}
