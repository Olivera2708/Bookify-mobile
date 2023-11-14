package com.example.bookify;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class AddTextToDates implements LineBackgroundSpan {

    private String dayPrice;

    public AddTextToDates(String text) {
        this.dayPrice = text;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline,
            int bottom, CharSequence text, int start, int end, int lnum
    ) {
        float x = (left + right) / 4.0f;
        float y = bottom + 20.0f;

        Paint dayPricePaint = new Paint(paint);
        float originalTextSize = dayPricePaint.getTextSize();
        dayPricePaint.setTextSize(originalTextSize * 0.8f);

        canvas.drawText(dayPrice, x, y, dayPricePaint);
    }
}
