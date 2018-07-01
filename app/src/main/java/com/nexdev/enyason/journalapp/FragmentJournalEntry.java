package com.nexdev.enyason.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by enyason on 6/27/18.
 */

public class FragmentJournalEntry extends Fragment {


    FloatingActionButton floatingActionButton;

    TextView textViewEmptyState;
    RecyclerView recyclerView;
    JournalListAdapter adapter;

    RecyclerView.LayoutManager layoutManager;

    AppDataBase appDataBase;

    public FragmentJournalEntry() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal_entries, container, false);

        return view;

//        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        appDataBase = AppDataBase.getsInstance(getActivity());

       LiveData<List<Journal>> journals =  appDataBase.taskDao().loadAllJournal();


        textViewEmptyState = view.findViewById(R.id.tv_empty_state);
        recyclerView = view.findViewById(R.id.recycler_view_journal);
        adapter = new JournalListAdapter(getActivity());
        layoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(layoutManager);


        floatingActionButton = view.findViewById(R.id.fab_add_journal);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JournalActivity.class));

            }
        });


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
