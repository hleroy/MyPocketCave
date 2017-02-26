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
import com.myadridev.mypocketcave.listeners.OnPathChosenListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathChooserDialog {
    private static final String separator = "/";
    private boolean isNewFolderEnabled;
    private String defaultPath = "";
    private Context context;
    private TextView titleView;

    private String currentPath = "";
    private List<String> subFoldersAndAllowedFiles = null;
    private List<String> allowedFileExtensions;
    private OnPathChosenListener onPathChosenListener;
    private ArrayAdapter<String> listAdapter = null;
    private Button parentButton = null;
    private boolean needToSelectFile;

    public PathChooserDialog(Context context, String defaultPath, boolean isNewFolderEnabled, @NonNull List<String> allowedFileExtensions, OnPathChosenListener onPathChosenListener) {
        this.context = context;
        this.defaultPath = defaultPath;
        this.isNewFolderEnabled = isNewFolderEnabled;
        this.allowedFileExtensions = allowedFileExtensions;
        this.onPathChosenListener = onPathChosenListener;
        needToSelectFile = true;
    }

    public PathChooserDialog(Context context, String defaultPath, boolean isNewFolderEnabled, OnPathChosenListener onPathChosenListener) {
        this.context = context;
        this.defaultPath = defaultPath;
        this.isNewFolderEnabled = isNewFolderEnabled;
        this.allowedFileExtensions = new ArrayList<>();
        this.onPathChosenListener = onPathChosenListener;
        needToSelectFile = false;
    }

    public void choosePath() {
        String path = defaultPath;
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            path = defaultPath;
            pathFile = new File(path);
        } else if (!pathFile.isDirectory()) {
            if (isFileAllowed(pathFile)) {
                path = pathFile.getParent();
            } else {
                path = defaultPath;
            }
            pathFile = new File(path);
        }

        try {
            path = pathFile.getCanonicalPath();
        } catch (IOException e) {
            return;
        }

        currentPath = path;
        subFoldersAndAllowedFiles = getSubFoldersAndAllowedFiles();

        AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(subFoldersAndAllowedFiles, (DialogInterface dialog, int item) -> {
            // update path
            PathChooserDialog.this.currentPath += (currentPath.equals(separator) ? "" : separator) + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
            File currentPathFile = new File(PathChooserDialog.this.currentPath);
            if (currentPathFile.isDirectory()) {
                // Navigate into the sub-directory
                parentButton.setVisibility(View.VISIBLE);
                updateDirectory();
            } else if (needToSelectFile && currentPathFile.isFile()) {
                // Current file chosen
                if (onPathChosenListener != null) {
                    // Call registered listener supplied with the chosen file
                    onPathChosenListener.onPathChosen(PathChooserDialog.this.currentPath);
                }
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton(R.string.global_cancel, null);
        if (!needToSelectFile) {
            dialogBuilder.setPositiveButton(R.string.global_ok, (DialogInterface dialog, int which) -> {
                // Current directory chosen
                if (onPathChosenListener != null) {
                    // Call registered listener supplied with the chosen directory
                    onPathChosenListener.onPathChosen(PathChooserDialog.this.currentPath);
                }
            });
        }

        // Show directory chooser dialog
        dialogBuilder.show();
    }

    private boolean isFileAllowed(File pathFile) {
        String fileName = pathFile.getName();
        String[] splitFileName = fileName.split("\\.");
        String fileExtension = splitFileName[splitFileName.length - 1];
        return allowedFileExtensions.contains(fileExtension);
    }

    private boolean createSubDir(String newFolder) {
        File newFolderFile = new File(newFolder);
        if (!newFolderFile.exists()) {
            return newFolderFile.mkdir();
        }
        return false;
    }

    private List<String> getSubFoldersAndAllowedFiles() {
        List<String> subFolders = new ArrayList<>();
        List<String> files = new ArrayList<>();

        File pathFile = new File(currentPath);
        if (!pathFile.exists() || !pathFile.isDirectory()) {
            return subFolders;
        }
        if (pathFile.listFiles() == null) {
            return subFolders;
        }

        for (File file : pathFile.listFiles()) {
            if (file.isDirectory()) {
                subFolders.add(file.getName());
            } else if (isFileAllowed(file)) {
                files.add(file.getName());
            }
        }

        Collections.sort(files);
        Collections.sort(subFolders);
        files.addAll(subFolders);
        return files;
    }

    private AlertDialog.Builder createDirectoryChooserDialog(List<String> listItems, DialogInterface.OnClickListener onClickListener) {
        // Create custom view for AlertDialog title containing current directory TextView and possible 'New folder' button.
        // Current directory TextView allows long directory path to be wrapped to multiple lines.
        LinearLayout titleLayout = new LinearLayout(context);
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        String title = currentPath;

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
                            String newFolder = currentPath + (currentPath.equals(separator) ? "" : separator) + newFolderName;
                            if (createSubDir(newFolder)) {
                                // Navigate into the new directory
                                currentPath = newFolder;
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
            if (!PathChooserDialog.this.currentPath.equals(separator)) {
                // Navigate back to an upper directory
                PathChooserDialog.this.currentPath = new File(PathChooserDialog.this.currentPath).getParent();
                if (PathChooserDialog.this.currentPath.equals(separator)) {
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
        subFoldersAndAllowedFiles.clear();
        subFoldersAndAllowedFiles.addAll(getSubFoldersAndAllowedFiles());
        titleView.setText(currentPath);

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
