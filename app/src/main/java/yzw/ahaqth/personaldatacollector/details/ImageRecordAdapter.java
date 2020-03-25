package yzw.ahaqth.personaldatacollector.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import yzw.ahaqth.personaldatacollector.R;
import yzw.ahaqth.personaldatacollector.interfaces.ItemClickListener;
import yzw.ahaqth.personaldatacollector.modules.ImageRecord;
import yzw.ahaqth.personaldatacollector.operators.ImageOperator;

public class ImageRecordAdapter extends RecyclerView.Adapter<ImageRecordAdapter.ImageRcordVH> {
    String TAG = "殷宗旺";
    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private List<ImageRecord> mList;
    public ImageRecordAdapter(List<ImageRecord> list){
        this.mList = list;
    }
    @NonNull
    @Override
    public ImageRcordVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.extra_image_item,viewGroup,false);
        return new ImageRcordVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageRcordVH imageRcordVH, final int i) {
        final ImageRecord imageRecord = mList.get(i);
        imageRcordVH.imageView.setImageBitmap(ImageOperator.getRealImage(imageRecord));
        imageRcordVH.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickListener.click(imageRcordVH.getAdapterPosition(),imageRcordVH.imageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class ImageRcordVH extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageRcordVH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.extra_image_IV);
        }
    }
}
