package forecast.iak.ihsan.forecast.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import forecast.iak.ihsan.forecast.DetailForecastActivity;
import forecast.iak.ihsan.forecast.R;
import forecast.iak.ihsan.forecast.listener.ItemClickListener;
import forecast.iak.ihsan.forecast.model.Weather;

/**
 * Created by ihsan on 12/05/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private List<Weather> weatherList;

    public WeatherAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_weather, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        final Weather weather = weatherList.get(position);
        holder.tvDt.setText(weather.getDt());
        holder.tvDay.setText(weather.getDay() + "°");
        holder.tvMain.setText(weather.getMain());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailForecastActivity.class);
                intent.putExtra("temp", weather.getDay());
                intent.putExtra("dt", weather.getDt());
                intent.putExtra("main", weather.getMain());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvDt;
        TextView tvDay;
        TextView tvMain;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDt = (TextView) itemView.findViewById(R.id.tv_dt);
            tvDay = (TextView) itemView.findViewById(R.id.tv_temp);
            tvMain = (TextView) itemView.findViewById(R.id.tv_main);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }
}

