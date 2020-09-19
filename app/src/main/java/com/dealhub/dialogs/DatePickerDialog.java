package com.dealhub.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dealhub.R;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DatePickerDialog extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DecimalFormat df=new DecimalFormat("00");

        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                TextView datefield=getActivity().findViewById(R.id.exp_date);
                datefield.setText(year+"-"+df.format(month+1)+"-"+df.format(dayOfMonth));
            }
        }, mYear, mMonth, mDay);



        return dialog;
    }
}
