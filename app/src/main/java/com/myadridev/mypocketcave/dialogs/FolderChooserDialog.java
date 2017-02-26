package com.myadridev.mypocketcave.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.helpers.CompatibilityHelper;
import com.myadridev.mypocketcave.listeners.OnFolderChosenListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FolderChooserDialog {
    private static final String separator = "/";
    public boolean isNewFolderEnabled = false;
    private String defaultFolder = "";
    private Context context;
    private TextView titleView;

    private String currentFolder = "";
    private List<String> subFolders = null;
    private OnFolderChosenListener onDirectoryChosenListener;
    private ArrayAdapter<String> listAdapter = null;
    private Button parentButton = null;

    public FolderChooserDialog(Context context, String defaultFolder, OnFolderChosenListener onDirectoryChosenListener) {
        this.context = context;
        this.defaultFolder = defaultFolder;
        this.onDirectoryChosenListener = onDirectoryChosenListener;
    }

    public void chooseFolder() {
        // Initial folder is sdcard folder
        chooseFolder(defaultFolder);
    }

    private void chooseFolder(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dir = defaultFolder;
            dirFile = new File(dir);
        }

        try {
            dir = dirFile.getCanonicalPath();
        } catch (IOException e) {
            return;
        }

        currentFolder = dir;
        subFolders = getSubFolders();

        AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(subFolders, (DialogInterface dialog, int item) -> {
            // Navigate into the sub-directory
            FolderChooserDialog.this.currentFolder += (currentFolder.equals(separator) ? "" : separator) + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
            parentButton.setVisibility(View.VISIBLE);
            updateDirectory();
        });

        dialogBuilder
                .setNegativeButton(R.string.global_cancel, null)
                .setPositiveButton(R.string.global_ok, (DialogInterface dialog, int which) -> {
                    // Current directory chosen
                    if (onDirectoryChosenListener != null) {
                        // Call registered listener supplied with the chosen directory
                        onDirectoryChosenListener.onFolderChosen(FolderChooserDialog.this.currentFolder);
                    }
                });

        // Show directory chooser dialog
        dialogBuilder.show();
    }

    private boolean createSubDir(String newFolder) {
        File newFolderFile = new File(newFolder);
        if (!newFolderFile.exists()) {
            return newFolderFile.mkdir();
        }
        return false;
    }

    private List<String> getSubFolders() {
        List<String> subFolders = new ArrayList<>();

        File dirFile = new File(currentFolder);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return subFolders;
        }
        if (dirFile.listFiles() == null) {
            return subFolders;
        }

        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                subFolders.add(file.getName());
            }
        }

        Collections.sort(subFolders);
        return subFolders;
    }

    private AlertDialog.Builder createDirectoryChooserDialog(List<String> listItems, DialogInterface.OnClickListener onClickListener) {
        // Create custom view for AlertDialog title containing current directory TextView and possible 'New folder' button.
        // Current directory TextView allows long directory path to be wrapped to multiple lines.
        LinearLayout titleLayout = new LinearLayout(context);
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        String title = currentFolder;

        titleView = new TextView(context);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        CompatibilityHelper.setTextAppearance(titleView, android.R.style.TextAppearance_Large);
        titleView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        titleView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        titleView.setText(title);

        titleLayout.addView(titleView);

        if (isNewFolderEnabled) {
            Button newDirButton = new Button(context);
            newDirButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            newDirButton.setText(R.string.new_folder);
            newDirButton.setOnClickListener((View view) -> {
                final EditText input = new EditText(context);

                // Show new folder name input dialog
                new AlertDialog.Builder(context).setTitle(R.string.new_folder_name).setView(input)
                        .setPositiveButton(R.string.global_ok, (DialogInterface dialog, int whichButton) -> {
                            String newFolderName = input.getText().toString();
                            // Create new directory
                            String newFolder = currentFolder + (currentFolder.equals(separator) ? "" : separator) + newFolderName;
                            if (createSubDir(newFolder)) {
                                // Navigate into the new directory
                                currentFolder = newFolder;
                                updateDirectory();
                            } else {
                                Toast.makeText(context, context.getString(R.string.new_folder_create_error, newFolderName), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.global_cancel, null)
                        .show();
            });
            titleLayout.addView(newDirButton);
        }

        parentButton = new Button(context);
        parentButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parentButton.setText(R.string.parent_folder);
        parentButton.setOnClickListener((View view) -> {
            if (!FolderChooserDialog.this.currentFolder.equals(separator)) {
                // Navigate back to an upper directory
                FolderChooserDialog.this.currentFolder = new File(FolderChooserDialog.this.currentFolder).getParent();
                if (FolderChooserDialog.this.currentFolder.equals(separator)) {
                    parentButton.setVisibility(View.GONE);
                }
                updateDirectory();
            }
        });
        titleLayout.addView(parentButton);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCustomTitle(titleLayout);

        listAdapter = createListAdapter(listItems);

        dialogBuilder.setSingleChoiceItems(listAdapter, -1, onClickListener).setCancelable(true);

        return dialogBuilder;
    }

    private void updateDirectory() {
        subFolders.clear();
        subFolders.addAll(getSubFolders());
        titleView.setText(currentFolder);

        listAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> createListAdapter(List<String> items) {
        return new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, android.R.id.text1, items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (view instanceof TextView) {
                    // Enable list item (directory) text wrapping
                    TextView textView = (TextView) view;
                    textView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    textView.setEllipsize(null);
                }
                return view;
            }
        };
    }
}
