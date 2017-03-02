package com.golubroman.golub.weatherv201.WeatherList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.golubroman.golub.weatherv201.R;

/**
 * Created by User on 20.12.2016.
 */

public class UnitsDialog extends DialogFragment{

    private UnitsDialogListener unitsDialogListener;
    private String units = "Units";
    private String ok = "Ok";
    private int currentChoice = -1;

    public interface UnitsDialogListener{
        void onUnitsDialogItemChecked(DialogInterface dialogInterface, int i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        unitsDialogListener = (UnitsDialogListener)activity;
    }

    /* Method of creating the dialog
        */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Dialog);
        builder.setView(R.layout.units_dialog).
                setTitle(units).
                setSingleChoiceItems(R.array.units, currentChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentChoice = i;
                    }
                }).
                setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unitsDialogListener.onUnitsDialogItemChecked(dialogInterface, currentChoice);
                        dismiss();
                    }
                });
        return builder.create();
    }




}
