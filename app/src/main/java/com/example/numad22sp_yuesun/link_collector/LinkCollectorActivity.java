package com.example.numad22sp_yuesun.link_collector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.example.numad22sp_yuesun.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;

public class LinkCollectorActivity extends AppCompatActivity {
    private RecyclerView linkRecyclerView;
    private LinkRecyclerViewAdapter viewAdapter;
    private final ArrayList<LinkItem> linkItemList = new ArrayList<>();;
    private static final String KEY_OF_LINK = "KEY_OF_LINK";
    private static final String NUMBER_OF_LINKS = "NUMBER_OF_LINKS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);
        init(savedInstanceState);
        FloatingActionButton addLinkButton = findViewById(R.id.addLinkButton);
        addLinkButton.setOnClickListener(view -> {
            showAddLinkDialog(view);});

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.@NotNull ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.@NotNull ViewHolder viewHolder, int direction) {
                View parentLayout = findViewById(android.R.id.content);
                int position = viewHolder.getLayoutPosition();
                LinkItem linkToRemove = linkItemList.get(position);

                linkItemList.remove(position);
                viewAdapter.notifyItemRemoved(position);

                Snackbar.make(parentLayout, R.string.snackbar_msg_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action_undo, v -> {
                            linkItemList.add(position, linkToRemove);
                            viewAdapter.notifyItemInserted(position);
                        })
                        .show();
            }
        });

        itemTouchHelper.attachToRecyclerView(linkRecyclerView);
    }

    private void init(Bundle savedInstanceState) {
        initialLinkItemsData(savedInstanceState);
        createRecyclerView();
    }

    private void initialLinkItemsData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_LINKS)) {
            if (linkItemList.size() == 0) {
                int size = savedInstanceState.getInt(NUMBER_OF_LINKS);

                for (int i = 0; i < size; i++) {
                    String linkName = savedInstanceState.getString(KEY_OF_LINK + i + "0");
                    String linkURL = savedInstanceState.getString(KEY_OF_LINK + i + "1");
                    if (linkName != null && linkURL != null) {
                        LinkItem linkItem = new LinkItem(linkName, linkURL);
                        linkItemList.add(linkItem);
                    }
                }
            }
        }
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        linkRecyclerView = findViewById(R.id.link_collector_recycler_view);
        linkRecyclerView.setHasFixedSize(true);
        viewAdapter = new LinkRecyclerViewAdapter(linkItemList);
        LinkItemClickListener linkClickListener = url -> {
            if (!(url.startsWith(getString(R.string.http)) || url.startsWith(getString(R.string.https)))) {
                url = getString(R.string.http_start_of_url) + url;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        };
        viewAdapter.setLinkItemClickListener(linkClickListener);
        linkRecyclerView.setAdapter(viewAdapter);
        linkRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = linkItemList.size();
        outState.putInt(NUMBER_OF_LINKS, size);
        for (int i = 0; i < size; i++) {
            outState.putString(KEY_OF_LINK + i + "0", linkItemList.get(i).getName());
            outState.putString(KEY_OF_LINK + i + "1", linkItemList.get(i).getURL());
        }
        super.onSaveInstanceState(outState);
    }

    private boolean isValidURL(@NonNull String urlString) {
        if (TextUtils.isEmpty(urlString)) {
            return false;
        }

        boolean isURL = Patterns.WEB_URL.matcher(urlString).matches();
        if (!isURL) {
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString).toURI();
                    isURL = true;
                } catch (Exception ignored) {
                }
            }
        }
        return isURL;
    }

    public void showAddLinkDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_link_dialog, null);
        builder.setView(layout)
                .setTitle(R.string.dialog_title)
                .setNegativeButton(R.string.bottom_cancel, ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(R.string.button_save, (dialog, which) -> {
            EditText editTextLinkName = layout.findViewById(R.id.edit_link_name);
            EditText editTextLinkUrl = layout.findViewById(R.id.edit_link_url);
            String name = editTextLinkName.getText().toString();
            String url = editTextLinkUrl.getText().toString();

            if (name.isEmpty() || url.isEmpty()) {
                Snackbar.make(view, R.string.snackbar_msg_missing_input, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action, null)
                        .show();
            } else if (!isValidURL(url)) {
                Snackbar.make(view, R.string.snackbar_msg_invalid_url, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action, null)
                        .show();
            } else {
                linkItemList.add(new LinkItem(name, url));
                int position = linkItemList.size() - 1;
                viewAdapter.notifyItemInserted(position+1);
                Snackbar.make(view, R.string.snackbar_msg_success, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action_undo, v -> {
                            linkItemList.remove(position);
                            viewAdapter.notifyItemRemoved(position);
                        })
                        .show();
            }
        });

        builder.show();

    }
}