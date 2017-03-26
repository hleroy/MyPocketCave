package com.myadridev.mypocketcave.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnPatternRemoveClickListener;
import com.myadridev.mypocketcave.managers.NavigationManager;

public class EditPatternAlertDialog extends AlertDialog {

    public EditPatternAlertDialog(Activity activity, int patternId, boolean isResizable, OnPatternRemoveClickListener onPatternRemoveClickListener) {
        super(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_edit_pattern, null);
        setView(dialogView);

        TextView resizePatternView = (TextView) dialogView.findViewById(R.id.alert_resize_pattern);
        TextView changePatternView = (TextView) dialogView.findViewById(R.id.alert_change_pattern);
        TextView removePatternView = (TextView) dialogView.findViewById(R.id.alert_remove_pattern);

        if (isResizable) {
            resizePatternView.setVisibility(View.VISIBLE);
            resizePatternView.setOnClickListener((View v) -> {
                dismiss();
                NavigationManager.navigateToEditPattern(activity, patternId);
            });
        } else {
            resizePatternView.setVisibility(View.GONE);
        }

        changePatternView.setOnClickListener((View v) -> {
            dismiss();
            NavigationManager.navigateToPatternSelection(activity);
        });

        removePatternView.setOnClickListener((View v) -> {
            final AlertDialog removeAlertDialogBuilder = new AlertDialog.Builder(activity)
                    .setTitle(R.string.title_remove_pattern)
                    .setMessage(R.string.message_remove_pattern)
                    .setPositiveButton(R.string.global_ok, (DialogInterface dialog, int which) -> {
                        if (onPatternRemoveClickListener != null) {
                            onPatternRemoveClickListener.onPatternRemove();
                        }
                        dialog.dismiss();
                        dismiss();
                    })
                    .setNegativeButton(R.string.global_no, (DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                        dismiss();
                    })
                    .create();
            removeAlertDialogBuilder.show();
        });
    }
}
