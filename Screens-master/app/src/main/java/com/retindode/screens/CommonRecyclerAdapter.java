package com.retindode.screens;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommonRecyclerAdapter extends RecyclerView.Adapter<CommonRecyclerAdapter.ViewHolder> {

    private final OnBindViewListener onGetViewListener;
    private boolean singleView;
    private ArrayList<?> mData;
    private LayoutInflater mInflater;
    private int resource;
    private ArrayList<Pair<Integer, Integer>> views;

    // data is passed into the constructor
    public CommonRecyclerAdapter(ArrayList<?> data, int resource, Activity activity, OnBindViewListener onGetViewListener) {
        this.mInflater = LayoutInflater.from(activity);
        this.mData = data;
        this.resource = resource;
        this.onGetViewListener = onGetViewListener;
        singleView = true;
    }

    public CommonRecyclerAdapter(ArrayList<?> data, ArrayList<Pair<Integer, Integer>> views, Activity activity, OnBindViewListener onGetViewListener) {
        this.mInflater = LayoutInflater.from(activity);
        this.mData = data;
        this.views = views;
        this.onGetViewListener = onGetViewListener;
        singleView = false;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (singleView) {
            View view = mInflater.inflate(resource, parent, false);
            return new ViewHolder(view);
        } else {
            View view = mInflater.inflate(getViewByViewType(viewType), parent, false);
            return new ViewHolder(view);
        }
    }

    private int getViewByViewType(int viewType) {
        for (Pair<Integer, Integer> view : views) {
            if (view.first == viewType) {
                return view.second;
            }
        }
        return views.get(0).first;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        this.onGetViewListener.onGetView(holder, position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public Object getItem(int id) {
        return mData.get(id);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}