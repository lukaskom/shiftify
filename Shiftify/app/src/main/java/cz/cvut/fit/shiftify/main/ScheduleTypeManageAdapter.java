package cz.cvut.fit.shiftify.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cz.cvut.fit.shiftify.R;
import cz.cvut.fit.shiftify.data.models.ScheduleType;

/**
 * Created by petr on 12/15/16.
 */

public class ScheduleTypeManageAdapter extends ArrayAdapter<ScheduleType> {

    private final Context mContext;
    private final int mResource;

    ScheduleTypeManageAdapter(Context context, int resource, List<ScheduleType> types) {
        super(context, resource, types);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleType scheduleType = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mResource, parent, false);

        TextView scheduleTypeTitleTextView = (TextView) view.findViewById(R.id.schedule_type_title);
        TextView scheduleTypeDaysCountTextView = (TextView) view.findViewById(R.id.schedule_type_days_count);
        scheduleTypeTitleTextView.setText(scheduleType.getName());
        scheduleTypeDaysCountTextView.setText(String.valueOf(scheduleType.getDaysOfScheduleCycle()));
        return view;
    }
}
