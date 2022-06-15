package com.example.naver_map_test;

import android.content.Context;
import android.content.Intent;
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
    Context ctx;

    CustomAdapter(Context ctx){
        this.ctx=ctx;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new Holder(ctx, view);
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
    TextView distance;

    Context mContext;
    public Holder(Context ctx, @NonNull View itemView) {
        super(itemView);

        mContext=ctx;

        layout_text = (LinearLayout) itemView.findViewById(R.id.LinearLayout2);
        profile = (ImageView) itemView.findViewById(R.id.imageView);
        title = (TextView) itemView.findViewById(R.id.textTitle);
        message = (TextView) itemView.findViewById(R.id.textContent);
        distance = (TextView) itemView.findViewById(R.id.textDistance);
    }

    void onBind(StoreItem item){
        profile.setImageResource(item.getResourceId());
        title.setText(item.getTitle());
        message.setText(item.getMessage());
        distance.setText(item.getDistance());


        layout_text.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ShopActivity.class);
            intent.putExtra("title", title.getText());
            intent.putExtra("message", message.getText());
            intent.putExtra("distance", distance.getText());
            intent.putExtra("profile", item.getResourceId());

            mContext.startActivity(intent);
        });
    }
}