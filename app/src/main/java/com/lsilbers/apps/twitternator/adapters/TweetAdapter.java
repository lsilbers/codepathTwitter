package com.lsilbers.apps.twitternator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsilbers.apps.twitternator.R;
import com.lsilbers.apps.twitternator.models.Tweet;
import com.lsilbers.apps.twitternator.utils.TweetDisplayUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lsilberstein on 11/5/15.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private ArrayList<Tweet> tweets;

    public TweetAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetAdapter.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.tvUsername.setText(tweet.getUser().getScreenName());
        holder.tvName.setText(tweet.getUser().getName());
        holder.tvBody.setText(tweet.getText());
        holder.tvTimestamp.setText(TweetDisplayUtils.getRelativeTimeAgo(tweet.getCreatedAt()));

        Picasso.with(holder.context).load(tweet.getUser().getProfileImageUrl()).into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfile;
        public TextView tvName;
        public TextView tvUsername;
        public TextView tvTimestamp;
        public TextView tvBody;
        public Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }
}
