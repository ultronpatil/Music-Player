package com.example.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayerFragment extends Fragment {

    private ImageView playbackModeButton, previousButton, playPauseButton, nextButton, optionsButton;
    private ImageView songImage;
    private TextView songName;
    private SeekBar seekBar;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        songImage = view.findViewById(R.id.song_image);
        songName = view.findViewById(R.id.song_name);
        seekBar = view.findViewById(R.id.seekBar);
        playbackModeButton = view.findViewById(R.id.playback_mode_button);
        previousButton = view.findViewById(R.id.previous_button);
        playPauseButton = view.findViewById(R.id.play_pause_button);
        nextButton = view.findViewById(R.id.next_button);
        optionsButton = view.findViewById(R.id.options_button);

        // Add onClickListeners for playback controls
        playbackModeButton.setOnClickListener(v -> {
            // Handle playback mode change
        });

        previousButton.setOnClickListener(v -> {
            // Handle previous song
        });

        playPauseButton.setOnClickListener(v -> {
            // Handle play/pause
        });

        nextButton.setOnClickListener(v -> {
            // Handle next song
        });

        optionsButton.setOnClickListener(v -> {
            // Handle options
        });

        return view;
    }
}
