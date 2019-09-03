package com.example.android.earthquakeapp.db.loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.earthquakeapp.R;
import com.example.android.earthquakeapp.activity.MapsActivity;
import com.example.android.earthquakeapp.activity.UiUtils;
import com.example.android.earthquakeapp.activity.WebActivity;
import com.example.android.earthquakeapp.bean.Earthquake;
import com.example.android.earthquakeapp.db.room.EarthquakeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {


    public EarthquakeAdapter(@NonNull final Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        if (context instanceof FragmentActivity){
            // Get a new or existing ViewModel from the ViewModelProvider.
            mEarthquakeViewModel = ViewModelProviders.of((FragmentActivity)context).get(EarthquakeViewModel.class);

            mEarthquakeViewModel.getAllEarthquakes().observe((FragmentActivity)context, earthquakes -> {
                // Update the cached copy of the earthquakes in the adapter.
                setEarthquakes(earthquakes);
                if (onChangeListener != null){
                    onChangeListener.onChange(earthquakes);
                }
            });
        }
        else {
            mEarthquakeViewModel = null;
        }
    }

    @CheckResult
    public Earthquake getItem(int position) {
        return mEarthquakes.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View parent, View view, int position, long id);
    }

    public interface OnChangeListener{
        void onChange(List<Earthquake> earthquakes);
    }

    public void setOnChangeListener(@Nullable OnChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }

    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View itemView = mInflater.inflate(R.layout.earthquake_item, parent, false);
        return new EarthquakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeAdapter.EarthquakeViewHolder holder, int position) {
        final Earthquake earthquake = mEarthquakes.get(position);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);


        if (earthquake != null) {

            if (position % 2 == 0) {
                holder.root.setBackgroundResource(R.color.backgroundColorEven);
            } else {
                holder.root.setBackgroundResource(R.color.backgroundColorOdd);
            }


            if (earthquake.getUrl() != null) {
                holder.web.setVisibility(View.VISIBLE);

                holder.web.setOnClickListener(v -> {
                    Toast.makeText(mContext, earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    final String webOpen = sharedPrefs.getString(mContext.getString(R.string.settings_web_open_key), mContext.getString(R.string.settings_web_open_default));

                    Intent intent = null;
                    if (webOpen != null) {
                        if (webOpen.equals(mContext.getString(R.string.settings_web_open_external_value))) {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(earthquake.getUrl()));
                        } else if (webOpen.equals(mContext.getString(R.string.settings_web_open_internal_value))) {
                            //TODO implement activity WebActivity
                            intent = new Intent(mContext, WebActivity.class);
                            intent.setData(Uri.parse(earthquake.getUrl()));
                            intent.putExtra(WebActivity.TITLE, earthquake.getLocationOffset() + " " + earthquake.getPrimaryLocation());
                        }
                    }

                    if (intent != null) {
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
            if (earthquake.isCoordinates()) {
                holder.mapSearch.setVisibility(View.VISIBLE);
                holder.mapSearch.setOnClickListener(v -> {
                    Toast.makeText(mContext, earthquake.getPrimaryLocation(), Toast.LENGTH_SHORT).show();
                    final String mapOpen = sharedPrefs.getString(mContext.getString(R.string.settings_map_open_key), mContext.getString(R.string.settings_map_open_default));
                    Intent intent = null;
                    if (mapOpen != null) {
                        final String label = Uri.encode(String.format("Earthquake of %1$s", UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude())));
                        if (mapOpen.equals(mContext.getString(R.string.settings_map_open_external_value))) {
                            final int zoom = 3;
                            intent = new Intent(Intent.ACTION_VIEW);
                            //TODO bugfix: https://developers.google.com/maps/documentation/urls/android-intents
                            final Uri uri = Uri.parse(String.format("geo:%1$s, %2$s?z=%4$s&q=(%3$s)@%1$s,%2$s", earthquake.getLatitude(), earthquake.getLongitude(), label, zoom));
                            intent.setData(uri);
                        } else if (mapOpen.equals(mContext.getString(R.string.settings_map_open_internal_value))) {
                            intent = new Intent(mContext, MapsActivity.class);
                            intent.setData(MapsActivity.FROM_LIST);
                            final ArrayList<Earthquake> list = new ArrayList<>();
                            list.add(earthquake);
                            intent.putParcelableArrayListExtra(MapsActivity.EARTHQUAKES, list);
                        }
                    }

                    if (intent != null) {
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }
                    }
                });
            }


            GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitude.getBackground();
            int magnitudeColor = ContextCompat.getColor(mContext, earthquake.getMagnitudeColorIdRef());
            magnitudeCircle.setColor(magnitudeColor);

            holder.magnitude.setText(UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()));
            holder.location.setText(earthquake.getPrimaryLocation());
            holder.locationOffset.setText(earthquake.getLocationOffset());
            holder.date.setText(UiUtils.DATE_FORMAT.format(earthquake.getDate()));
            holder.time.setText(UiUtils.TIME_FORMAT.format(earthquake.getDate()));
        }


    }


    @Override
    public int getItemCount() {
        return mEarthquakes.size();
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public void setEarthquakes(@Nullable final List<Earthquake> earthquakes) {
        this.mEarthquakes = earthquakes;
        notifyDataSetChanged();
    }

    class EarthquakeViewHolder extends RecyclerView.ViewHolder {
        private EarthquakeViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setTag(this);

            if (onItemClickListener != null) {
                itemView.setOnClickListener(v -> {
                    final RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
                    final int position = viewHolder.getAdapterPosition();
                    final long id = viewHolder.getItemId();
                    //viewHolder.getItemViewType();
                    final View view = viewHolder.itemView;
                    final View parent = view.getRootView();

                    onItemClickListener.onItemClick(parent, view, position, id);
                });
            }
            this.root = itemView;
            this.magnitude = itemView.findViewById(R.id.magnitude);
            this.location = itemView.findViewById(R.id.primary_location);
            this.locationOffset = itemView.findViewById(R.id.location_offset);
            this.date = itemView.findViewById(R.id.date);
            this.time = itemView.findViewById(R.id.time);
            this.web = itemView.findViewById(R.id.web);
            this.mapSearch = itemView.findViewById(R.id.map_search);

        }

        private final TextView magnitude;
        private final TextView location;
        private final TextView locationOffset;
        private final TextView date;
        private final TextView time;
        private final View web;
        private final View mapSearch;
        private final View root;
    }

    private List<Earthquake> mEarthquakes = Collections.emptyList(); // Cached copy of earthquakes
    private OnItemClickListener onItemClickListener;
    private OnChangeListener onChangeListener;
    private final EarthquakeViewModel mEarthquakeViewModel;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private static final String TAG = EarthquakeAdapter.class.getSimpleName();
}