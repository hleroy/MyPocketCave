package com.myadridev.mypocketcave.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.dialogs.PathChooserDialog;
import com.myadridev.mypocketcave.helpers.PermissionsHelper;
import com.myadridev.mypocketcave.helpers.SnackbarHelper;
import com.myadridev.mypocketcave.managers.SyncManager;

import java.util.ArrayList;
import java.util.List;

public class SyncActivity extends AppCompatActivity {

    private final static String extension = "mpc";
    private List<String> allowedFileExtensions;

    private ImageButton exportButton;
    private ImageButton importButton;
    private Button locationLabel;
    private TextView locationValue;

    private Button syncButton;
    private String importLocation;
    private String exportLocation;
    private String defaultLocation;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync);

        PermissionsHelper.askForPermissionIfNeeded(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionsHelper.writeExternalStorageRequestCode);

        importLocation = SyncManager.getImportLocation();
        exportLocation = SyncManager.getExportLocation();
        defaultLocation = SyncManager.getDefaultLocation();

        allowedFileExtensions = new ArrayList<>();
        allowedFileExtensions.add(extension);

        setLayout();
        setLayoutValues();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsHelper.writeExternalStorageRequestCode:
                importLocation = SyncManager.getImportLocation();
                exportLocation = SyncManager.getExportLocation();
                defaultLocation = SyncManager.getDefaultLocation();
                locationValue.setText("");
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setLayout() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.sync_coordinator_layout);

        exportButton = (ImageButton) findViewById(R.id.sync_export_button);
        exportButton.setOnClickListener(onExportButtonClick());

        importButton = (ImageButton) findViewById(R.id.sync_import_button);
        importButton.setOnClickListener(onImportButtonClick());

        locationLabel = (Button) findViewById(R.id.sync_location_label);
        locationLabel.setOnClickListener(onLocationLabelClick());

        locationValue = (TextView) findViewById(R.id.sync_location_value);

        syncButton = (Button) findViewById(R.id.sync_button);
        syncButton.setOnClickListener(onSyncButtonClick());
    }

    private View.OnClickListener onExportButtonClick() {
        return (View v) -> {
            if (exportButton.isSelected()) return;
            exportButton.setSelected(true);
            exportButton.setImageResource(R.drawable.export_icon_selected);
            importButton.setSelected(false);
            importButton.setImageResource(R.drawable.import_icon);

            locationLabel.setText(R.string.sync_export_location);
            locationValue.setText("");
            syncButton.setText(R.string.sync_export);
        };
    }

    private View.OnClickListener onImportButtonClick() {
        return (View v) -> {
            if (importButton.isSelected()) return;
            exportButton.setSelected(false);
            exportButton.setImageResource(R.drawable.export_icon);
            importButton.setSelected(true);
            importButton.setImageResource(R.drawable.import_icon_seleted);

            locationLabel.setText(R.string.sync_import_location);
            locationValue.setText("");
            syncButton.setText(R.string.sync_import);
        };
    }

    private View.OnClickListener onLocationLabelClick() {
        return (View v) -> {
            if (importButton.isSelected()) {
                PathChooserDialog pathChooserDialog = new PathChooserDialog(SyncActivity.this, defaultLocation + SyncManager.separator + locationValue.getText().toString(), defaultLocation, false, allowedFileExtensions, (String chosenFile) -> {
                    locationValue.setText(chosenFile.length() >= defaultLocation.length() + 1 ? chosenFile.substring(defaultLocation.length() + 1) : "");
                });
                pathChooserDialog.choosePath();
            } else if (exportButton.isSelected()) {
                PathChooserDialog pathChooserDialog = new PathChooserDialog(SyncActivity.this, defaultLocation + SyncManager.separator + locationValue.getText().toString(), defaultLocation, true, (String chosenFolder) -> {
                    locationValue.setText(chosenFolder.length() >= defaultLocation.length() + 1 ? chosenFolder.substring(defaultLocation.length() + 1) : "");
                });
                pathChooserDialog.choosePath();
            }
        };
    }

    private View.OnClickListener onSyncButtonClick() {
        return (View v) -> {
            if (importButton.isSelected()) {
                importLocation = defaultLocation + SyncManager.separator + locationValue.getText().toString();
                importFile();
            } else if (exportButton.isSelected()) {
                exportLocation = defaultLocation + SyncManager.separator + locationValue.getText().toString();
                exportFile();
            }
        };
    }

    private void importFile() {
        if (!importLocation.endsWith("." + extension)) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_no_import_file_selected, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
            return;
        }

        AlertDialog.Builder importConfirmationDialogBuilder = new AlertDialog.Builder(this);
        importConfirmationDialogBuilder.setCancelable(true);
        importConfirmationDialogBuilder.setMessage(R.string.sync_import_confirmation);
        importConfirmationDialogBuilder.setNegativeButton(R.string.global_cancel, (DialogInterface dialog, int which) -> dialog.dismiss());
        importConfirmationDialogBuilder.setPositiveButton(R.string.global_ok, (DialogInterface dialog, int which) -> {
            dialog.dismiss();
            importFileInner();
        });
        importConfirmationDialogBuilder.show();
    }

    private void importFileInner() {
        int errorResourceId = SyncManager.importData(this, importLocation);

        if (errorResourceId > -1) {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, errorResourceId, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        } else {
            SnackbarHelper.displaySuccessSnackbar(this, coordinatorLayout, R.string.success_import, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void exportFile() {
        String exportedFileName = SyncManager.exportData(this, exportLocation, extension);

        if (exportedFileName != null && !exportedFileName.isEmpty()) {
            SnackbarHelper.displaySuccessSnackbar(this, coordinatorLayout, getString(R.string.success_export, exportedFileName), R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        } else {
            SnackbarHelper.displayErrorSnackbar(this, coordinatorLayout, R.string.error_export, R.string.global_ok, Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void setLayoutValues() {
        exportButton.setSelected(true);
        exportButton.setImageResource(R.drawable.export_icon_selected);
        importButton.setSelected(false);
        importButton.setImageResource(R.drawable.import_icon);

        locationLabel.setText(R.string.sync_export_location);
        locationValue.setText("");
        syncButton.setText(R.string.sync_export);
    }
}
