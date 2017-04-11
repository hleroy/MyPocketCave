package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.AboutAdapter;
import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;
import com.myadridev.mypocketcave.managers.NavigationManager;
import com.myadridev.mypocketcave.managers.VersionManager;
import com.myadridev.mypocketcave.models.v2.AboutItemV2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private String version;
    private String contactSubject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ListView listView = (ListView) findViewById(R.id.about_list_view);

        List<AboutItemV2> items = initializeItems();

        AboutAdapter adapter = new AboutAdapter(this, R.layout.item_about, items, contactSubject);
        assert listView != null;
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                return true;
        }
    }

    private List<AboutItemV2> initializeItems() {
        List<AboutItemV2> items = new ArrayList<>(2);

        version = VersionManager.getVersion(this);

        contactSubject = computeEmailSubject();

        items.add(new AboutItemV2(this, AboutFieldsEnumV2.v, version));
        items.add(new AboutItemV2(this, AboutFieldsEnumV2.c, getString(R.string.about_mail)));
        items.add(new AboutItemV2(this, AboutFieldsEnumV2.s, getString(R.string.about_sources_url)));
        items.add(new AboutItemV2(this, AboutFieldsEnumV2.l, getString(R.string.about_license_value)));

        Collections.sort(items);

        return items;
    }

    private String computeEmailSubject() {
        return "[" + getString(R.string.app_name) + "]"
                + (
                !version.equals(getString(R.string.about_version_default))
                        ? "[" + getString(R.string.about_version_prefix) + version + "]"
                        : "");
    }

    @Override
    protected void onResume() {
        if (NavigationManager.restartIfNeeded(this)) {
            finish();
        } else {
            super.onResume();
        }
    }
}
