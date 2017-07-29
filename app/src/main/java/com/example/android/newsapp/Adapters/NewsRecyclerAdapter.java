package com.example.android.newsapp.Adapters;

/**
 * Created by 2rite on 7/28/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.android.newsapp.R;
import com.example.android.newsapp.Utils.Contract;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.AdapterViewHolder> {

    private Cursor cursor;
    private final NewsAdapterOnClickHandler myClickHandler;
    Context context;


    public interface NewsAdapterOnClickHandler {
        void onClick(String url);
    }

    public NewsRecyclerAdapter(NewsAdapterOnClickHandler clickHandler, Cursor cursor) {
        myClickHandler = clickHandler;
        this.cursor = cursor;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView description;
        public final TextView time;
        public final ImageView thumb;

        public AdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.tv_title);
            description = (TextView) view.findViewById(R.id.tv_description);
            time = (TextView) view.findViewById(R.id.tv_time);
            thumb = (ImageView) view.findViewById(R.id.thumb);
        }
        @Override
        public void onClick(View v) {
            int myadapterPosition = getAdapterPosition();
            cursor.moveToPosition(myadapterPosition);
            myClickHandler.onClick(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL)));
        }

        //BIND
        public void bind(AdapterViewHolder holder, int pos) {
            cursor.moveToPosition(pos);

            Glide.with(context).load(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_THUMBNAIL)))
                    .into(holder.thumb);
            holder.title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_TITLE)));
            holder.time.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR)) + "    "
                    + cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT)));
            holder.description.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION)));
        }

    }


    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_recycler_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int position)
    {
        adapterViewHolder.bind(adapterViewHolder, position);

    }


    @Override
    public int getItemCount()
    {
        return cursor.getCount();
    }


}