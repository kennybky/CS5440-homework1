package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.models.NewsItem;

import java.util.ArrayList;

import static com.example.android.newsapp.models.Contract.TABLE_NEWS.*;

/**
 * Created by kenny on 6/26/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private final ListItemClickListener mOnClickListener;
    private Cursor cursor;

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    public NewsAdapter(Cursor cursor, ListItemClickListener listener){
        mOnClickListener= listener;
        this.cursor = cursor;
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
                holder.bind(position);
        }

        @Override
        public int getItemCount() {
                return cursor.getCount();
        }

        public void swapCursor(Cursor newCursor) {
            if (cursor != null) cursor.close();
            cursor = newCursor;
            if (newCursor != null) {
                // Force the RecyclerView to refresh
                this.notifyDataSetChanged();
            }
        }

        public String getNewsURL(int position) {
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URL));
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

                void bind(int pos) {
                    cursor.moveToPosition(pos);
                    mTitle.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
                        mDescription.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIPTION)));
                        mDate.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));


                }

            @Override
            public void onClick(View view) {
                int clickedPosition = getAdapterPosition();
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }

}
