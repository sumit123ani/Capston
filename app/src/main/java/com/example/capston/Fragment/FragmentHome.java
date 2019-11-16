package com.example.capston.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capston.ParentRecyclerAdapter;
import com.example.capston.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> itemArrayList=new ArrayList<>();

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_home, container, false);



        recyclerView=view.findViewById(R.id.parentRc);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new ParentRecyclerAdapter(itemArrayList,getContext());
        recyclerView.setAdapter(adapter);
        String[] items={"Latest News","Trending News","Most seen","Political News","Entertainment"};
        for(int i=0;i<items.length;i++){

            itemArrayList.add(items[i]);
        }
        adapter.notifyDataSetChanged();
        return view;

    }

}
