package com.example.naver_map_test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class BarcodeRecyclerAdapter extends RecyclerView.Adapter<BarcodeRecyclerAdapter.MyViewHolder>{
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

    interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onDeleteClick(View v, int positon);
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout_barcode;
        ImageView imgaeBarcode;
        TextView brandName;
        ImageButton btnDelete;
        Context mContext;

        public MyViewHolder(Context ctx, @NonNull View itemView) {
            super(itemView);

            mContext=ctx;

            layout_barcode = (LinearLayout) itemView.findViewById(R.id.layoutBarcode);
            imgaeBarcode = (ImageView) itemView.findViewById(R.id.barcode);
            brandName = (TextView) itemView.findViewById(R.id.brandName);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
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
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("정말로 멤버십 바코드를 삭제하겠습니까?");
                    builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String fileName = item.getBarcodePath();
                            File file = new File(fileName);
                            Boolean result = file.delete();
                            Log.d("Delete Result", result+"");

                            int position = getAdapterPosition ();
                            if (position!=RecyclerView.NO_POSITION){
                                if (mListener!=null){
                                    mListener.onDeleteClick(view,position);
                                }
                            }
                        }
                    });
                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }
            });
        }

    }

}