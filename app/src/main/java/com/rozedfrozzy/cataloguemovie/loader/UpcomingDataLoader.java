package com.rozedfrozzy.cataloguemovie.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.rozedfrozzy.cataloguemovie.BuildConfig;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UpcomingDataLoader extends AsyncTaskLoader<ArrayList<ResultMovieItems>> {
    private ArrayList<ResultMovieItems> mData;
    private boolean mHasResult = false;

    private static final String API_KEY = BuildConfig.APIKey;

    public UpcomingDataLoader(Context context) {
        super(context);

        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()){
            forceLoad();
        } else if (mHasResult){
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(ArrayList<ResultMovieItems> data) {
        super.deliverResult(data);

        mData = data;
        mHasResult = true;
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult){
            onReleaseResources(mData);
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<ResultMovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<ResultMovieItems> upcomingItemsList = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+API_KEY+"&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    Log.d("UpcomingData", result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i<list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        ResultMovieItems upcomingItems = new ResultMovieItems(movie);
                        upcomingItemsList.add(upcomingItems);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return upcomingItemsList;
    }

    protected void onReleaseResources(ArrayList<ResultMovieItems> mData) {

    }
}
