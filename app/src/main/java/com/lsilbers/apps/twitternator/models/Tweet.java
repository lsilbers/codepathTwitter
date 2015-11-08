package com.lsilbers.apps.twitternator.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lsilberstein on 11/5/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model {

    // tweetId
    @Column(name = "tweet_id")
    private long tweetId;

    // created_at
    @Column(name = "created_at")
    private String createdAt;

    // text
    @Column(name = "text")
    private String text;

    // user
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    public long getTweetId() {
        return tweetId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Tweet(){
        super();
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.tweetId = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.text = jsonObject.getString("text");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSON(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(fromJSON(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tweets;
    }
}
