package com.myadridev.mypocketcave.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.adapters.AboutAdapter;
import com.myadridev.mypocketcave.enums.AboutFieldsEnum;
import com.myadridev.mypocketcave.managers.VersionManager;
import com.myadridev.mypocketcave.models.AboutItem;

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

        ListView listView = (ListView) findViewById(R.id.about_list_view);

        List<AboutItem> items = initializeItems();

        AboutAdapter adapter = new AboutAdapter(this, R.layout.item_about, items, contactSubject);
        assert listView != null;
        listView.setAdapter(adapter);
    }

    private List<AboutItem> initializeItems() {
        List<AboutItem> items = new ArrayList<>(2);

        version = VersionManager.getVersion(this);

        contactSubject = computeEmailSubject();

        items.add(new AboutItem(this, AboutFieldsEnum.VERSION, version));
        items.add(new AboutItem(this, AboutFieldsEnum.CONTACT, getString(R.string.about_mail)));
        items.add(new AboutItem(this, AboutFieldsEnum.SOURCES, getString(R.string.about_sources_url)));
        items.add(new AboutItem(this, AboutFieldsEnum.LICENSE, getString(R.string.about_license_value)));

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
}
