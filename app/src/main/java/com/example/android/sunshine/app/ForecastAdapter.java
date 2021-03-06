package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private final int VIEW_COUNT = 2;
    private boolean mUseTodayLayout = true;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;

                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder)view.getTag();
        int viewType = getItemViewType(cursor.getPosition());

        switch (viewType) {
            case VIEW_TYPE_TODAY:
                viewHolder.mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            case VIEW_TYPE_FUTURE_DAY:
                viewHolder.mIconView.setImageResource(Utility.getIconResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
        }

        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.mDateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        String desc = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.mDescView.setText(desc);

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.mHighView.setText(Utility.formatTemperature(context, high));

        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.mLowView.setText(Utility.formatTemperature(context, low));
    }

    private static class ViewHolder {
        private ImageView mIconView;
        private TextView mDateView;
        private TextView mDescView;
        private TextView mHighView;
        private TextView mLowView;

        public ViewHolder(View view) {
            mIconView = (ImageView)view.findViewById(R.id.list_item_icon);
            mDateView = (TextView)view.findViewById(R.id.list_item_date_textview);
            mDescView = (TextView)view.findViewById(R.id.list_item_forecast_textview);
            mHighView = (TextView)view.findViewById(R.id.list_item_high_textview);
            mLowView = (TextView)view.findViewById(R.id.list_item_low_textview);
        }
    }

}