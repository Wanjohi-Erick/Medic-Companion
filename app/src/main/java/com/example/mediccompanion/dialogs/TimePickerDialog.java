package com.example.mediccompanion.dialogs;

import android.content.Context;

public class TimePickerDialog extends android.app.TimePickerDialog {
    public TimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }
}
