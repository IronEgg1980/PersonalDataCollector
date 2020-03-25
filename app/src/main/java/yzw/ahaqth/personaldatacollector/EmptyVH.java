package yzw.ahaqth.personaldatacollector;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyVH extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;
    public EmptyVH(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.empty_imageview);
        textView = itemView.findViewById(R.id.empty_textview);
    }
}
