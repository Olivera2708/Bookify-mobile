package com.example.bookify.fragments;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PriceDecorator implements DayViewDecorator {
    private HashSet<CalendarDay> dates;

    private Set<CalendarDay> datesToDecorate = new HashSet<>();
    String price;

    public PriceDecorator(List<CalendarDay> dates, String price) {
        this.dates = new HashSet<>(dates);
        this.price = price;
    }

    public PriceDecorator(String price) {
        this.price = price;
    }

    public void addDateToDecorate(CalendarDay day) {
        datesToDecorate.add(day);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AddTextToDates(price));
    }
}