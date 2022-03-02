package com.example.numad22sp_yuesun.at_your_service;

import android.view.View;
import android.widget.TextView;
import com.example.numad22sp_yuesun.R;
import androidx.recyclerview.widget.RecyclerView;

public class HolidayRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView localName;
    public TextView name;
    public TextView isFixed;
    public TextView countryCode;

    public HolidayRecyclerViewHolder(View holidayItemView) {
        super(holidayItemView);
        this.date = holidayItemView.findViewById(R.id.holiday_date);
        this.localName = holidayItemView.findViewById(R.id.holiday_local_name);
        this.name = holidayItemView.findViewById(R.id.holiday_name);
        this.isFixed = holidayItemView.findViewById(R.id.holiday_is_fix);
        this.countryCode = holidayItemView.findViewById(R.id.holiday_country_code);
    }
}
