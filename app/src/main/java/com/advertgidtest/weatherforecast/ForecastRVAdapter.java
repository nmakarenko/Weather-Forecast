package com.advertgidtest.weatherforecast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ForecastRVAdapter extends RecyclerView.Adapter<ForecastRVAdapter.ViewHolder> {
    Context context;
    List<Forecast> data;

    public ForecastRVAdapter(Context context, List<Forecast> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Calendar cal = data.get(i).date;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        viewHolder.tvDate.setText(sdf.format(cal.getTime()));
        viewHolder.tvDescription.setText(data.get(i).description);
        viewHolder.tvTemperature.setText(String.format(Locale.US, "%.1f", data.get(i).temperature));
        viewHolder.tvPressure.setText(String.format(Locale.US, "%.1f hpa", data.get(i).pressure));
        viewHolder.tvWind.setText(String.format(Locale.US, "%.1f m/s", data.get(i).wind));
//        viewHolder.tvWindDegr.setText(String.valueOf(data.get(i).windDegrees));
//        viewHolder.tvHumidity.setText(String.format(Locale.US, "%d%%", data.get(i).humidity));

        Glide.with(context)
                .load(API.ICON_URL + data.get(i).iconId + ".png")
                .error(R.drawable.error)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(viewHolder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        TextView tvDate;
        TextView tvDescription;
        TextView tvTemperature;
        TextView tvPressure;
        TextView tvWind;
        TextView tvWindDegr;
        TextView tvHumidity;

        ImageView ivIcon;

        ViewHolder(View itemView) {
            super(itemView);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);

            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvTemperature = (TextView) itemView.findViewById(R.id.tvTemperature);
            tvPressure = (TextView) itemView.findViewById(R.id.tvPressure);
            tvWind = (TextView) itemView.findViewById(R.id.tvWind);
            tvWindDegr = (TextView) itemView.findViewById(R.id.tvWindDegr);
            tvHumidity = (TextView) itemView.findViewById(R.id.tvHumidity);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
        }
    }
}
