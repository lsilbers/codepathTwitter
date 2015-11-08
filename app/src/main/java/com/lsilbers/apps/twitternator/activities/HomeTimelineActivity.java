package com.lsilbers.apps.twitternator.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsilbers.apps.twitternator.R;
import com.lsilbers.apps.twitternator.TwitterApplication;
import com.lsilbers.apps.twitternator.adapters.TweetAdapter;
import com.lsilbers.apps.twitternator.models.Tweet;
import com.lsilbers.apps.twitternator.network.TwitterClient;
import com.lsilbers.apps.twitternator.utils.EndlessRecyclerOnScrollListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTimelineActivity extends AppCompatActivity {

    private static final String TAG = "HT";
    private TwitterClient client;
    private TweetAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private RecyclerView rvTweets;
    private long oldestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tweets = new ArrayList<>();
        aTweets = new TweetAdapter(tweets);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);

        setupRecyclerView();

        client = TwitterApplication.getTwitterClient();
        retriveInitialResults();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(aTweets);
        rvTweets.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                client.getHomeTimeline(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        int count = aTweets.getItemCount();
                        ArrayList<Tweet> newTweets = Tweet.fromJSON(response);
                        for (Tweet tweet : newTweets) {
                            tweets.add(tweet);
                            aTweets.notifyItemInserted(count);
                            count++;
                            updateOldestId(tweet);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d(TAG, errorResponse.toString());
                    }
                }, 10, null, oldestId - 1);
            }
        });
    }

    private void updateOldestId(Tweet tweet) {
        if (tweet.getTweetId() < oldestId) {
            oldestId = tweet.getTweetId();
        }
    }


    // gets the basic set of results and notifies the adapter
    private void retriveInitialResults() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweets.addAll(Tweet.fromJSON(response));
                aTweets.notifyDataSetChanged();
                Tweet t1 = tweets.get(0);
                oldestId = t1.getTweetId();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
            }
        }, null, null, null); // use defaults for initial request
    }

}
