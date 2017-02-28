package com.golubroman.golub.weatherv201;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by User on 20.12.2016.
 */

public class UnitsDialog extends DialogFragment{
    View unitsDialog;
    int currentChoice = -1;
    public interface UnitsDialogListener{
        void onUnitsDialogItemChecked(DialogInterface dialogInterface, int i);
    }
    UnitsDialogListener unitsDialogListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        unitsDialogListener = (UnitsDialogListener)activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        unitsDialog = getActivity().getLayoutInflater().inflate(R.layout.units_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Dialog);
        builder.setView(R.layout.units_dialog).
                setTitle("Units").
                setSingleChoiceItems(R.array.units, currentChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentChoice = i;
                    }
                }).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unitsDialogListener.onUnitsDialogItemChecked(dialogInterface, currentChoice);
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.units_width);
        int height = getResources().getDimensionPixelSize(R.dimen.units_height);
        getDialog().getWindow().setLayout(width, height);
    }
}
