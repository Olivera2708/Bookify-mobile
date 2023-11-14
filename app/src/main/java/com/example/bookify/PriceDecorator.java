package com.example.bookify;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;
import java.util.List;

public class PriceDecorator implements DayViewDecorator
{
    private HashSet<CalendarDay> dates;
    String price;
    public PriceDecorator(List<CalendarDay> dates, String price){
        this.dates = new HashSet<>(dates);
        this.price = price;
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