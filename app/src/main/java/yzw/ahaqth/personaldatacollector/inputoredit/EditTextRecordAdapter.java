package yzw.ahaqth.personaldatacollector.inputoredit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.DialogDismissListener;
import yzw.ahaqth.personaldatacollector.interfaces.ItemClickListener;
import yzw.ahaqth.personaldatacollector.modules.TextRecord;
import yzw.ahaqth.personaldatacollector.tools.DialogFactory;

public class EditTextRecordAdapter extends RecyclerView.Adapter<EditTextRecordAdapter.TextRecordVH> {
    private List<TextRecord> mList;
    private FragmentActivity mActivity;
    public void setEditClickListener(ItemClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }
    private ItemClickListener editClickListener;
    public EditTextRecordAdapter(FragmentActivity activity, List<TextRecord> list){
        mActivity = activity;
        mList = list;
    }
    @NonNull
    @Override
    public TextRecordVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_extra_text_item,viewGroup,false);
        return new TextRecordVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TextRecordVH textRecordVH, int i) {
        final TextRecord record = mList.get(i);
        textRecordVH.extraTextKeyTV.setText(record.getKey());
        textRecordVH.extraTextKeyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = textRecordVH.getAdapterPosition();
                DialogFactory.getConfirmDialog("是否立即移除该项？")
                        .setDismissListener(new DialogDismissListener() {
                            @Override
                            public void onDismiss(boolean isConfirm, Object... valus) {
                                if(isConfirm){
                                    record.delete();
                                    mList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,getItemCount() - position);
                                }
                            }
                        })
                        .show(mActivity.getSupportFragmentManager(),"confirmRemove");
            }
        });
        textRecordVH.extraTextContentTV.setText(record.getContent());
        textRecordVH.extraTextContentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClickListener.click(textRecordVH.getAdapterPosition());
            }
        });
//        textRecordVH.deleIB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                record.isDeleted = !record.isDeleted;
//                notifyItemChanged(textRecordVH.getAdapterPosition());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class TextRecordVH extends RecyclerView.ViewHolder{
        private TextView extraTextKeyTV;
        private AppCompatTextView extraTextContentTV;
        public TextRecordVH(@NonNull View itemView) {
            super(itemView);
            extraTextKeyTV = itemView.findViewById(R.id.extra_text_key_TV);
            extraTextContentTV = itemView.findViewById(R.id.extra_text_content_TV);
        }
    }
}
