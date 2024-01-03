package com.example.bookify.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

public class ScrollHandler implements SensorEventListener {

    private static final float SCROLL_THRESHOLD = 45.0f;
    private final SensorManager sensorManager;
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationValues = new float[3];
    private final View scrollableView;
    private Sensor accelerometer;
    private Sensor magnetometer;
    float[] accelerometerValues = new float[3];
    float[] magnetometerValues = new float[3];

    public ScrollHandler(Context context, View scrollableView) {
        this.scrollableView = scrollableView;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (accelerometer != null && magnetometer != null) {
                register();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER || event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues = event.values.clone();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetometerValues = event.values.clone();
                    break;
            }

            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues);
            SensorManager.getOrientation(rotationMatrix, orientationValues);
            //-90 vertikalno uspravno, 0 na strani
            float pitch = (float) Math.toDegrees(orientationValues[1]);
            //>0 desno,  <0 levo
            float roll = (float) Math.toDegrees(orientationValues[2]);

            if (Math.abs(pitch) < SCROLL_THRESHOLD && Math.abs(roll) > 25) {
                if (roll > 0) {
                    scrollDown();
                } else {
                    scrollUp();
                }
            }
        }
    }

    private void scrollDown() {
        if (scrollableView != null) {
            if (scrollableView instanceof ScrollView) {
                scrollScrollViewDown((ScrollView) scrollableView);
            } else if (scrollableView instanceof ListView) {
                scrollListViewDown((ListView) scrollableView);
            }
        }
    }

    private void scrollUp() {
        if (scrollableView != null) {
            if (scrollableView instanceof ScrollView) {
                scrollScrollViewUp((ScrollView) scrollableView);
            } else if (scrollableView instanceof ListView) {
                scrollListViewUp((ListView) scrollableView);
            }
        }
    }

    private void scrollScrollViewDown(ScrollView scrollView) {
        scrollView.post(() -> {
            int maxScroll = scrollView.getChildAt(0).getHeight() - scrollView.getHeight();
            int currentScroll = scrollView.getScrollY();
            int newScroll = Math.min(currentScroll + 10, maxScroll);

            if (newScroll != currentScroll) {
                scrollView.smoothScrollTo(0, newScroll);
            }
        });
    }

    private void scrollScrollViewUp(ScrollView scrollView) {
        scrollView.post(() -> {
            int currentScroll = scrollView.getScrollY();
            int newScroll = Math.max(currentScroll - 10, 0);

            if (newScroll != currentScroll) {
                scrollView.smoothScrollTo(0, newScroll);
            }
        });
    }

    private void scrollListViewDown(ListView listView) {
        listView.post(() -> {
            int lastVisiblePosition = listView.getLastVisiblePosition();
            int totalItems = listView.getCount();
            if (lastVisiblePosition < (totalItems - 1)) {
                listView.smoothScrollByOffset(1);
            }
        });
    }

    private void scrollListViewUp(ListView listView) {
        listView.post(() -> {
            int firstVisiblePosition = listView.getFirstVisiblePosition();
            if (firstVisiblePosition > 0) {
                listView.smoothScrollByOffset(-1);
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    public void unregister() {
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);
        sensorManager.unregisterListener(this);
    }

    public void register() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}

