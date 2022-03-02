package com.example.numad22sp_yuesun.at_your_service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22sp_yuesun.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HolidayRecyclerViewAdapter extends RecyclerView.Adapter<HolidayRecyclerViewHolder>{
    private final ArrayList<HolidayItem> holidayItems;

    public HolidayRecyclerViewAdapter(ArrayList<HolidayItem> holidayItems) {
        this.holidayItems = holidayItems;
    }

    @Override
    public @NotNull HolidayRecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_item_card, parent, false);
        return new HolidayRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolidayRecyclerViewHolder holder, int position) {
        HolidayItem currentHoliday = holidayItems.get(position);
        holder.date.setText(currentHoliday.getDate());
        String localNameText = "Local Name: " + currentHoliday.getLocalName();
        holder.localName.setText(localNameText);
        holder.name.setText(currentHoliday.getName());
        String isFixedText = currentHoliday.getFixed() ? "Is fixed date: Yes" : "Is fixed date: No";
        holder.isFixed.setText(isFixedText);
        holder.countryCode.setText(currentHoliday.getCountryCode());
    }

    @Override
    public int getItemCount() {
        return holidayItems.size();
    }
}
