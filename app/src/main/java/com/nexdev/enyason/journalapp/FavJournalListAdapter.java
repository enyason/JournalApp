/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.nexdev.enyason.journalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class FavJournalListAdapter extends RecyclerView.Adapter<FavJournalListAdapter.TaskViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";


    // Class variables for the List that holds task data and the Context
    private List<Journal> mFavJaournalEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public FavJournalListAdapter(Context context) {
        mContext = context;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.journal_row_item_view_fav, parent, false);

        return new TaskViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        // Determine the values of the wanted data
        Journal journalEntry = mFavJaournalEntries.get(position);

        String title = journalEntry.getTitle();
        String description = journalEntry.getDescription();

        final int id = journalEntry.getId(); // get item id
        String date = dateFormat.format(journalEntry.getDate());
        final String image = journalEntry.getImage();

        Log.i("Image Adapter ",image);


        holder.tvTitle.setText(title);


//        Picasso.get().load(new File(image)).into(holder.itemImageView);



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//                Picasso.with(mContext).load(image).resize(120, 60).into(holder.itemImageView);
//
//            }
//        });


        Glide.with(mContext).load(image).placeholder(R.mipmap.ic_row_image).into(holder.itemImageView);


    }






    @Override
    public int getItemCount() {
        if (mFavJaournalEntries == null) {
            return 0;
        }
        return mFavJaournalEntries.size();
    }


    public void setJournal(List<Journal>  journalList) {
        mFavJaournalEntries = journalList;
        notifyDataSetChanged();
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
//    public void setTasks(List<Task> taskEntries) {
//        mTaskEntries = taskEntries;
//        notifyDataSetChanged();
//    }

    public List<Journal> getJournal() {
        return mFavJaournalEntries;
    }

    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        ImageView itemImageView;
        TextView tvTitle;

        View view;




         // Constructor for the TaskViewHolders.

        public TaskViewHolder(View itemView) {
            super(itemView);


            itemImageView = itemView.findViewById(R.id.fav_item_image_view);
            tvTitle = itemView.findViewById(R.id.fav_tv_title);
//            view = itemView;
        }


    }
}