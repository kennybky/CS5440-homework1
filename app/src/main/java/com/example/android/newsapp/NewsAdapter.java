package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.models.NewsItem;

import java.util.ArrayList;

/**
 * Created by kenny on 6/26/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private final ListItemClickListener mOnClickListener;
private ArrayList<NewsItem> mNewsData;

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    public NewsAdapter(ListItemClickListener listener){
        mOnClickListener= listener;
    }

    @Override
        public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                int layoutIdForListItem = R.layout.list_item;
                LayoutInflater inflater = LayoutInflater.from(context);
                boolean shouldAttachToParentImmediately = false;
                View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
                NewsAdapterViewHolder viewHolder = new NewsAdapterViewHolder(view);
                return viewHolder;
        }

        @Override
        public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
                NewsItem data = mNewsData.get(position);
                holder.bind(data);
        }

        @Override
        public int getItemCount() {
                if (mNewsData ==null)
                return 0;
                else return mNewsData.size();
        }

        public void setNewsData(ArrayList<NewsItem> data) {
                mNewsData = data;
                notifyDataSetChanged();
        }

        public ArrayList<NewsItem> getNewsData() {
            return mNewsData;
        }

        class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
                TextView mTitle, mDescription, mDate;

                public NewsAdapterViewHolder(View itemView) {
                        super(itemView);
                        mTitle = (TextView) itemView.findViewById(R.id.displayTitle);
                        mDescription = (TextView) itemView.findViewById(R.id.displayDescription);
                        mDate = (TextView) itemView.findViewById(R.id.displayDate);
                    itemView.setOnClickListener(this);
                }

                void bind(NewsItem item) {
                        mTitle.setText(item.getTitle());
                        mDescription.setText(item.getDescription());
                        mDate.setText(item.getDate());
                }

            @Override
            public void onClick(View view) {
                int clickedPosition = getAdapterPosition();
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }

}
