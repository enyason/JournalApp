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
public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.TaskViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";


    // Class variables for the List that holds task data and the Context
    private List<Journal> mJaournalEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public JournalListAdapter(Context context) {
        mContext = context;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.journal_row_item_view, parent, false);

        return new TaskViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        // Determine the values of the wanted data
        Journal journalEntry = mJaournalEntries.get(position);

        String title = journalEntry.getTitle();
        String description = journalEntry.getDescription();

        final int id = journalEntry.getId(); // get item id
        String date = dateFormat.format(journalEntry.getDate());
        final String image = journalEntry.getImage();

        Log.i("Image Adapter ",image);


        holder.tvTitle.setText(title);


        Glide.with(mContext).load(image).dontAnimate().into(holder.itemImageView);



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


//        Picasso.get().load(image).placeholder(R.mipmap.ic_row_image).into(holder.itemImageView, new Callback.EmptyCallback() {
//            @Override public void onSuccess() {
//                // Index 0 is the image view.
////                animator.setDisplayedChild(0);
//            }
//        });


    }






    @Override
    public int getItemCount() {
        if (mJaournalEntries == null) {
            return 0;
        }
        return mJaournalEntries.size();
    }


    public void setJournal(List<Journal>  journalList) {
        mJaournalEntries = journalList;
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
        return mJaournalEntries;
    }

    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        ImageView itemImageView;
        TextView tvTitle;

//        View view;




         // Constructor for the TaskViewHolders.

        public TaskViewHolder(View itemView) {
            super(itemView);


            itemImageView = itemView.findViewById(R.id.item_image_view);
            tvTitle = itemView.findViewById(R.id.tv_title);
//            view = itemView;
        }


    }
}