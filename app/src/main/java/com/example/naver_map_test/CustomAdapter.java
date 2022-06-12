package com.example.naver_map_test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//리사이클러뷰 어댑터
public class CustomAdapter extends RecyclerView.Adapter<Holder> {
    private ArrayList<StoreItem> storeList;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new Holder(view);
    }
    public void setStoreList(ArrayList<StoreItem> list){
        this.storeList = list;
//        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.onBind(storeList.get(position));
    }
    @Override
    public int getItemCount() {
        return storeList.size();
    }
}

class Holder extends RecyclerView.ViewHolder{
    LinearLayout layout_text;
    ImageView profile;
    TextView title;
    TextView message;

    public Holder(@NonNull View itemView) {
        super(itemView);

        layout_text = (LinearLayout) itemView.findViewById(R.id.LinearLayout2);
        profile = (ImageView) itemView.findViewById(R.id.imageView);
        title = (TextView) itemView.findViewById(R.id.textTitle);
        message = (TextView) itemView.findViewById(R.id.textContent);
    }

    void onBind(StoreItem item){
        profile.setImageResource(item.getResourceId());
        title.setText(item.getTitle());
        message.setText(item.getMessage());


        layout_text.setOnClickListener(view -> {
        });
    }
}