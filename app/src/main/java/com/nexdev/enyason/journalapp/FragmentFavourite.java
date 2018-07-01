package com.nexdev.enyason.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by enyason on 6/27/18.
 */

public class FragmentFavourite extends Fragment {



    RecyclerView recyclerView;
    FavJournalListAdapter adapter;

    RecyclerView.LayoutManager layoutManager;

    TextView textViewEmptyState;

    AppDataBase appDataBase;


    public FragmentFavourite() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite,container,false);

        return view;

//        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        appDataBase = AppDataBase.getsInstance(getActivity());

        LiveData<List<Journal>> journals =  appDataBase.taskDao().getJournalByFav();


        textViewEmptyState = view.findViewById(R.id.tv_empty_state_fav);
        recyclerView = view.findViewById(R.id.recycler_view_favourite);
        adapter = new FavJournalListAdapter(getActivity());
        layoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(layoutManager);

        journals.observe(getActivity(), new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable List<Journal> journals) {

                if (journals  != null && journals.size() == 0 ) {
                    textViewEmptyState.setVisibility(View.VISIBLE);
                }
                adapter.setJournal(journals);
            }
        });

        recyclerView.setAdapter(adapter);



    }





}
