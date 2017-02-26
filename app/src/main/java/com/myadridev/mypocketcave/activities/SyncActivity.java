package com.myadridev.mypocketcave.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.dialogs.PathChooserDialog;
import com.myadridev.mypocketcave.helpers.PermissionsHelper;
import com.myadridev.mypocketcave.managers.SyncManager;

import java.util.ArrayList;
import java.util.List;

public class SyncActivity extends AppCompatActivity {

    private final static String extension = "mpc";
    private final static String exportFileName = "export." + extension;
    private List<String> allowedFileExtensions;

    private ImageButton exportButton;
    private ImageButton importButton;
    private TextView locationLabel;
    private TextView locationValue;

    private Button syncButton;
    private String importLocation;
    private String exportLocation;

    private boolean isWriteExternalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync);

        isWriteExternalStorage = PermissionsHelper.askForPermissionIfNeeded(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionsHelper.writeExternalStorageRequestCode);
        SyncManager.resetDefaultLocation(isWriteExternalStorage);

        importLocation = SyncManager.getImportLocation();
        exportLocation = SyncManager.getExportLocation();

        allowedFileExtensions = new ArrayList<>();
        allowedFileExtensions.add(extension);

        setLayout();
        setLayoutValues();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsHelper.writeExternalStorageRequestCode:
                // If request is cancelled, the result arrays are empty.
                isWriteExternalStorage = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                SyncManager.resetDefaultLocation(isWriteExternalStorage);
                importLocation = SyncManager.getImportLocation();
                exportLocation = SyncManager.getExportLocation();
                locationValue.setText(exportLocation);
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setLayout() {
        exportButton = (ImageButton) findViewById(R.id.sync_export_button);
        exportButton.setOnClickListener(onExportButtonClick());

        importButton = (ImageButton) findViewById(R.id.sync_import_button);
        importButton.setOnClickListener(onImportButtonClick());

        locationLabel = (TextView) findViewById(R.id.sync_location_label);

        locationValue = (TextView) findViewById(R.id.sync_location_value);
        locationValue.setOnClickListener(onLocationValueClick());

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
            locationValue.setText(exportLocation);
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
            locationValue.setText(importLocation);
            syncButton.setText(R.string.sync_import);
        };
    }

    private View.OnClickListener onLocationValueClick() {
        return (View v) -> {
            if (importButton.isSelected()) {
                PathChooserDialog pathChooserDialog = new PathChooserDialog(SyncActivity.this, locationValue.getText().toString(), false, allowedFileExtensions, (String chosenFile) -> {
                    locationValue.setText(chosenFile);
                });
                pathChooserDialog.choosePath();
            } else if (exportButton.isSelected()) {
                PathChooserDialog pathChooserDialog = new PathChooserDialog(SyncActivity.this, locationValue.getText().toString(), true, (String chosenFolder) -> {
                    locationValue.setText(chosenFolder);
                });
                pathChooserDialog.choosePath();
            }
        };
    }

    private View.OnClickListener onSyncButtonClick() {
        return (View v) -> {
            if (importButton.isSelected()) {
                importLocation = locationValue.getText().toString();
                SyncManager.saveImportLocation(this, importLocation);
                importFile();
            } else if (exportButton.isSelected()) {
                exportLocation = locationValue.getText().toString();
                SyncManager.saveExportLocation(this, exportLocation);
                exportFile();
            }
        };
    }

    private void importFile() {

    }

    private void exportFile() {

//        exportLocation + "/" + exportFileName;

    }

    private void setLayoutValues() {
        exportButton.setSelected(true);
        exportButton.setImageResource(R.drawable.export_icon_selected);
        importButton.setSelected(false);
        importButton.setImageResource(R.drawable.import_icon);

        locationLabel.setText(R.string.sync_export_location);
        locationValue.setText(exportLocation);
        syncButton.setText(R.string.sync_export);
    }
}