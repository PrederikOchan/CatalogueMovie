package com.rozedfrozzy.cataloguemovie.scheduler;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.rozedfrozzy.cataloguemovie.BuildConfig;
import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.model.ResultMovieItems;
import com.rozedfrozzy.cataloguemovie.views.DetailMovieActivity;
import com.rozedfrozzy.cataloguemovie.views.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class ReleaseReminderReceiver extends BroadcastReceiver {
    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private final int NOTIF_ID_ONETIME = 200;
    private final int NOTIF_ID_REPEATING = 201;

    private static final String API_KEY = BuildConfig.APIKey;

    private ResultMovieItems mData;

    public ReleaseReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = context.getResources().getString(R.string.app_name);

        mData = (ResultMovieItems) intent.getSerializableExtra("detailData");

        int notifId = type.equalsIgnoreCase(TYPE_ONE_TIME) ? NOTIF_ID_ONETIME : NOTIF_ID_REPEATING;

        showAlarmNotification(context, title, message, notifId, mData);
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId, ResultMovieItems mData) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, DetailMovieActivity.class);
        intent.putExtra(DetailMovieActivity.EXTRA_DETAIL, mData);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }

    public void setRepeatingAlarm(final Context context, final String type, final String time) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<ResultMovieItems> nowPlayingItemsList = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+API_KEY+"&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    Log.d("NowPlayingData", result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i<list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        ResultMovieItems nowPlayingItems = new ResultMovieItems(movie);
                        nowPlayingItemsList.add(nowPlayingItems);
                    }

                    Random random = new Random();
                    int randomNowPlaying = random.nextInt(5);

                    String movieTitle = context.getString(R.string.today_on_cinema)+" "+nowPlayingItemsList.get(randomNowPlaying).getTitle();

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(context, DailyReminderReceiver.class);
                    intent.putExtra(EXTRA_MESSAGE, movieTitle);
                    intent.putExtra(EXTRA_TYPE, type);
                    intent.putExtra("detailData", nowPlayingItemsList.get(randomNowPlaying));

                    String timeArray[] = time.split(":");

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
                    calendar.set(Calendar.SECOND, 0);

                    if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

                    int requestCode = NOTIF_ID_REPEATING;
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);

        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? NOTIF_ID_ONETIME : NOTIF_ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
