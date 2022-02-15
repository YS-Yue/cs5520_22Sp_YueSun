package com.example.numad22sp_yuesun.link_collector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.example.numad22sp_yuesun.R;

import java.util.ArrayList;

public class LinkRecyclerViewAdapter extends RecyclerView.Adapter<LinkRecyclerViewHolder> {
    private final ArrayList<LinkItem> linkList;
    private LinkItemClickListener linkItemClickListener;

    public LinkRecyclerViewAdapter(ArrayList<LinkItem> linkList) {
        this.linkList = linkList;
    }

    public void setLinkItemClickListener(LinkItemClickListener linkItemClickListener) {
        this.linkItemClickListener = linkItemClickListener;
    }

    @Override
    public LinkRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_item_card, parent, false);
        return new LinkRecyclerViewHolder(view, this.linkItemClickListener);
    }

    @Override
    public void onBindViewHolder(LinkRecyclerViewHolder holder, int position) {
        LinkItem currentLink = linkList.get(position);

        holder.linkName.setText(currentLink.getName());
        holder.linkURL.setText(currentLink.getURL());

    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }
}
