package com.alexsykes.bankmonsterr.utility;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alexsykes.bankmonsterr.R;

public class MarkerDialogFragment extends DialogFragment {
    public static String TAG = "MarkerDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.hint_word))
                .setPositiveButton(getString(R.string.button_save), (dialog, which) -> {
                })
                .create();
    }
}

