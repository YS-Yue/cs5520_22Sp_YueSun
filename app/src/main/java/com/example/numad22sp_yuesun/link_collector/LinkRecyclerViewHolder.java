package com.example.numad22sp_yuesun.link_collector;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.numad22sp_yuesun.R;

public class LinkRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView linkName;
    public TextView linkURL;

    public LinkRecyclerViewHolder(View linkItemView, LinkItemClickListener linkItemClickListener) {
        super(linkItemView);
        this.linkName = linkItemView.findViewById(R.id.link_name);
        this.linkURL = linkItemView.findViewById(R.id.link_url);

        linkItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linkItemClickListener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        linkItemClickListener.onLinkItemClick(linkURL.getText().toString());
                    }
                }
            }
        });
    }
}
