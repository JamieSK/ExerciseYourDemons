package com.example.jamie.exerciseyourdemons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class DeleteConfirmationDialogFragment extends DialogFragment {
  public interface DeleteDialogListener {
    void onConfirmDelete();
  }
  DeleteDialogListener listener;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    listener = (DeleteDialogListener) context;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        listener.onConfirmDelete();
      }
    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });
    return builder.create();
  }
}
