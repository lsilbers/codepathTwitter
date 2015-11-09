package com.lsilbers.apps.twitternator.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lsilberstein on 11/5/15.
 */
@Table(name = "Users")
public class User extends Model {

//        "name": "OAuth Dancer",
    @Column(name = "name")
    private String name;
//        "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
    @Column(name = "profile_image_url")
    private String profileImageUrl;
//        "id_str": "119476949",
    @Column(name = "id_str")
    private String idStr;
//        "url": "http://bit.ly/oauth-dancer",
//        "contributors_enabled": false,
//        "favourites_count": 7,
//        "utc_offset": null,
//        "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
//        "id": 119476949,
//        "listed_count": 1,
//        "screen_name": "oauth_dancer"
    @Column(name = "screen_name")
    private String screenName;

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getIdStr() {
        return idStr;
    }

    public String getScreenName() {
        return screenName;
    }

    public User(){
        super();
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.idStr = jsonObject.getString("id_str");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.screenName = jsonObject.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.save();
        return user;
    }

    public static void clearSavedUsers(){
        new Delete().from(User.class).execute();
    }
}
