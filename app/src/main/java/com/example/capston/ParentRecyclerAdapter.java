package com.example.capston;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ParentRecyclerAdapter extends RecyclerView.Adapter<ParentRecyclerAdapter.MyViewHolder> {

    ArrayList<String> parentArrayList;

    ArrayList<String> NewsArraylist=new ArrayList<>();

    public ParentRecyclerAdapter(ArrayList<String> parentArrayList, Context context) {
        this.parentArrayList = parentArrayList;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_recycler,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.ItemName.setText(parentArrayList.get(position));
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.ChildRV.setLayoutManager(layoutManager);
        holder.ChildRV.setHasFixedSize(true);
        NewsArraylist.clear();
        String [] news= {"News1","News2","News3","News4","News4","News5"};
        for(int i=0;i<news.length;i++){
            NewsArraylist.add(news[i]);
        }
        ChildRecyclerAdapter childRecyclerAdapter=new ChildRecyclerAdapter(NewsArraylist);
        holder.ChildRV.setAdapter(childRecyclerAdapter);
        childRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return parentArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ItemName;
        RecyclerView ChildRV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName=itemView.findViewById(R.id.itemname);
            ChildRV=itemView.findViewById(R.id.childrv);
        }
    }
}


