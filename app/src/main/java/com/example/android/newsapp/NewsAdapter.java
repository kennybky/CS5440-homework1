package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.models.NewsItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.newsapp.models.Contract.TABLE_NEWS.*;

/**
 * Created by kenny on 6/26/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private final ListItemClickListener mOnClickListener;
    private Cursor cursor;
    private Context context;
    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    public NewsAdapter(Cursor cursor, ListItemClickListener listener){
        mOnClickListener= listener;
        this.cursor = cursor;
    }

    @Override
        public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                context = parent.getContext();
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
            ImageView img;


                public NewsAdapterViewHolder(View itemView) {
                        super(itemView);
                        mTitle = (TextView) itemView.findViewById(R.id.displayTitle);
                        mDescription = (TextView) itemView.findViewById(R.id.displayDescription);
                        mDate = (TextView) itemView.findViewById(R.id.displayDate);
                        img = (ImageView) itemView.findViewById(R.id.myImageView);

                    itemView.setOnClickListener(this);
                }

                void bind(int pos)  {
                    cursor.moveToPosition(pos);
                    mTitle.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
                        mDescription.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIPTION)));
                            String date = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
                    try {
                        String formattedDate= DateParse(date);
                        mDate.setText(formattedDate);
                    } catch (ParseException e) {
                        mDate.setText(date);
                    }


                    String url = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URL_TO_IMAGE));
                    if(url != null){
                        Picasso.with(context)
                                .load(url)
                                .into(img);
                    }

                }

            @Override
            public void onClick(View view) {
                int clickedPosition = getAdapterPosition();
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }

    public static String DateParse(String input) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ss'Z'");
        Date date = parser.parse(input);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy - hh:mma");
        String formattedDate = formatter.format(date);
        Log.d("NewsAdapter", formattedDate);
        return formattedDate;
    }

}
