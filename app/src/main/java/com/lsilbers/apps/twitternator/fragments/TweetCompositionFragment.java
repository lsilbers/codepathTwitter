package com.lsilbers.apps.twitternator.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsilbers.apps.twitternator.R;
import com.lsilbers.apps.twitternator.TwitterApplication;
import com.lsilbers.apps.twitternator.models.User;
import com.lsilbers.apps.twitternator.network.TwitterClient;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class TweetCompositionFragment extends Fragment {

    private static final String TAG = "TCF";
    private ImageView ivMyProfile;
    private TextView tvMyName;
    private TextView tvMyUsername;
    private EditText etTweet;
    private Button btnTweet;
    private OnFragmentInteractionListener mListener;
    private TwitterClient client;
    private User user;
    private boolean canPopulate;

    public static TweetCompositionFragment newInstance() {
        return new TweetCompositionFragment();
    }

    public TweetCompositionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getTwitterClient();
        client.getUserAccount(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                populateView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, errorResponse.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tweet_composition, container, false);
        ivMyProfile = (ImageView) view.findViewById(R.id.ivMyProfile);
        tvMyName = (TextView) view.findViewById(R.id.tvMyName);
        tvMyUsername = (TextView) view.findViewById(R.id.tvMyUsername);
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweet();
            }
        });
        canPopulate = true;
        populateView();

        return view;
    }

    // first check if all the necessary configuration is done then populates the fields if it is
    private void populateView() {
        if (user != null && canPopulate) {
            tvMyUsername.setText(user.getScreenName());
            tvMyName.setText(user.getName());
            Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivMyProfile);
        }
    }

    public void onTweet() {
        if (mListener != null) {
            mListener.onTweet(etTweet.getText().toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onTweet(String tweet);
    }

}
