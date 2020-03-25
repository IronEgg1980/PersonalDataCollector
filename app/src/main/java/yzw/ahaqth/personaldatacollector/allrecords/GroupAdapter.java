package yzw.ahaqth.personaldatacollector.allrecords;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.ItemClickListener;
import yzw.ahaqth.personaldatacollector.modules.RecordGroup;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ItemVH> {
    private List<RecordGroup> mList;
    private Context mContext;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;
    public GroupAdapter(Context context,List<RecordGroup> list){
        this.mList = list;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ItemVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_item,viewGroup,false);
        return new ItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemVH itemVH, int i) {
        RecordGroup recordGroup = mList.get(i);
        Drawable drawable = mContext.getDrawable(R.drawable.ic_class_24dp).mutate();
        drawable.setColorFilter(Color.parseColor(recordGroup.getColor()), PorterDuff.Mode.MULTIPLY);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        itemVH.textView.setCompoundDrawables(drawable,null,null,null);
        itemVH.textView.setText(recordGroup.getGroupName());
        itemVH.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.click(itemVH.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class ItemVH extends RecyclerView.ViewHolder{
        TextView textView;
        public ItemVH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview1);
        }
    }
}
