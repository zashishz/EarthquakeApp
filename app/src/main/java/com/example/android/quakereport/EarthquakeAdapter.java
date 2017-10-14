package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ashish on 10/6/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake earthquakeData = getItem(position);
        TextView magnitude = (TextView) listItem.findViewById(R.id.earthquake_magnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        int magnitudeColor = getMagnitudeColor(earthquakeData.getMagnitude());

        magnitudeCircle.setColor(magnitudeColor);

        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(earthquakeData.getMagnitude());

        magnitude.setText(output);

        TextView primaryLocation = (TextView) listItem.findViewById(R.id.earthquake_primary_location);
        TextView locationOffset = (TextView) listItem.findViewById(R.id.earthquake_location_offset);
        String location = earthquakeData.getLocation();
        if(location.indexOf(",") > 0) {
            String [] locationArray = earthquakeData.getLocation().split(",");
            locationOffset.setText(locationArray[0].trim());
            primaryLocation.setText(locationArray[1].trim());
            locationOffset.setVisibility(View.VISIBLE);
        } else {
            locationOffset.setVisibility(View.GONE);
            primaryLocation.setText(location.trim());
        }

        TextView date = (TextView) listItem.findViewById(R.id.earthquake_date);
        TextView time = (TextView) listItem.findViewById(R.id.earthquake_time);

        Date earthquakeDate = new Date(earthquakeData.getTimeInMilliseconds());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = simpleDateFormat.format(earthquakeDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormat.format(earthquakeDate);

        date.setText(formattedDate);
        time.setText(formattedTime);

        return listItem;
    }

    private int getMagnitudeColor(double mag) {
        int magnitudeResourceId;
        int magFloor = (int) Math.floor(mag);
        switch (magFloor) {
            case 0:
            case 1:
                magnitudeResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeResourceId);
    }
}
