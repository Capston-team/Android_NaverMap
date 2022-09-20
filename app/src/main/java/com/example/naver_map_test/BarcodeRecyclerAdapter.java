package com.example.naver_map_test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BarcodeRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private ArrayList<BarcodeItem> barcodeList;
    Context ctx;

    BarcodeRecyclerAdapter(Context ctx){
        this.ctx=ctx;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barcode, parent, false);
        return new MyViewHolder(ctx, view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(barcodeList.get(position));
    }
    public void setBarcodeList(ArrayList<BarcodeItem> list){
        this.barcodeList = list;
//        notifyDataSetChanged();
    }
//    public void updateAdapter(ArrayList<BarcodeItem> list){
//        this.barcodeList = list;
//        // update adapter element like NAME, EMAIL e.t.c. here
//
//        // then in order to refresh the views notify the RecyclerView
//        notifyDataSetChanged();
//    }
    @Override
    public int getItemCount() {
        return barcodeList.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    LinearLayout layout_barcode;
    ImageView imgaeBarcode;
    TextView brandName;

    Context mContext;

    public MyViewHolder(Context ctx, @NonNull View itemView) {
        super(itemView);

        mContext=ctx;

        layout_barcode = (LinearLayout) itemView.findViewById(R.id.layoutBarcode);
        imgaeBarcode = (ImageView) itemView.findViewById(R.id.barcode);
        brandName = (TextView) itemView.findViewById(R.id.brandName);
    }

    void onBind(BarcodeItem item){

        Bitmap bitmap_barcode = BitmapFactory.decodeFile(item.getBarcodePath());

        imgaeBarcode.setImageBitmap(bitmap_barcode);
        brandName.setText(item.getBrandName());



        layout_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BarcodeImageActivity.class);
                intent.putExtra("imagePath", item.getBarcodePath());
                mContext.startActivity(intent);
            }
        });
        layout_barcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
    }

}
