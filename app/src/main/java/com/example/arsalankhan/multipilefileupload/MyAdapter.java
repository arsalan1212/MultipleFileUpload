package com.example.arsalankhan.multipilefileupload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arsalan khan on 1/15/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<String> arrayListFileName,arrayListUploadStatus;

    public MyAdapter(Context context, ArrayList<String> arrayListFileName,ArrayList<String> arrayListUploadStatus){
        this.context = context;
        this.arrayListFileName = arrayListFileName;
        this.arrayListUploadStatus = arrayListUploadStatus;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textViewFileName.setText(arrayListFileName.get(position));

        if(arrayListUploadStatus.get(position).equals("uploading")){
            holder.imageViewStatus.setImageResource(R.drawable.circle);
            holder.textViewUploadStatus.setText("Uploading...");
        }
        else{
            holder.imageViewStatus.setImageResource(R.drawable.checked);
            holder.textViewUploadStatus.setText("Uploaded");
        }
    }

    @Override
    public int getItemCount() {
        return arrayListFileName.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView textViewFileName,textViewUploadStatus;
    ImageView imageViewStatus;
    public MyViewHolder(View itemView) {
        super(itemView);

        textViewFileName = itemView.findViewById(R.id.textViewFileName);
        textViewUploadStatus = itemView.findViewById(R.id.textViewUploadStatus);

        imageViewStatus = itemView.findViewById(R.id.imageViewProgress);
    }
}
