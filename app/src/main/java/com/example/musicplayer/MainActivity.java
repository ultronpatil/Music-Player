package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private PlaylistFragment playlistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Request permission to read external storage
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, load songs and PlaylistFragment
            loadSongsAndFragment();
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_playlist) {
            if (playlistFragment == null) {
                playlistFragment = new PlaylistFragment();
            }
            fragment = playlistFragment;
        } else if (id == R.id.nav_player) {
            fragment = new PlayerFragment();
        } else if (id == R.id.nav_online) {
            fragment = new OnlineFragment();
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load songs and PlaylistFragment
                loadSongsAndFragment();
            } else {
                // Permission denied
                // Handle the case where the user denies the permission
                // For example, show a message to the user or request permission again
                // Here, we'll just log a message
                Log.d("MainActivity", "Permission denied for reading external storage");
            }
        }
    }

    private void loadSongsAndFragment() {
        // Fetch songs from device storage
        List<Song> songs = fetchSongsFromStorage();

        // Load PlaylistFragment and pass the songs as arguments
        if (playlistFragment == null) {
            playlistFragment = new PlaylistFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songs", new ArrayList<>(songs));
        playlistFragment.setArguments(bundle);

        // Load the PlaylistFragment
        loadFragment(playlistFragment);
    }

    private List<Song> fetchSongsFromStorage() {
        List<Song> songs = new ArrayList<>();

        // Define the projection for the query
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
        };

        // Define the selection criteria
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        // Sort the results by title
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        // Perform the query
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        );

        // Iterate through the cursor and add songs to the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                // Create a Song object and add it to the list
                songs.add(new Song(title, artist, data));
            }
            cursor.close();
        }

        return songs;
    }
}