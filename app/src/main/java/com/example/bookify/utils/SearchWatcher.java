package com.example.bookify.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class SearchWatcher implements TextWatcher {
    private final TextView textView;

    public SearchWatcher(TextView textView) {
        this.textView = textView;
    }
    public abstract void applySearch(TextView textView, String text);

    @Override
    public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        applySearch(textView, text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
