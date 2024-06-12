package com.example.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs;
    private Context context;
    private MediaPlayer mediaPlayer;
    private int playingPosition = -1; // Position of the currently playing song
    private boolean isPaused = false;

    public SongAdapter(Context context, List<Song> songs) {
        this.songs = songs;
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying() && playingPosition == position && !isPaused) {
                // If the clicked item is already playing, pause it
                mediaPlayer.pause();
                isPaused = true;
            } else if (playingPosition == position && isPaused) {
                // If the clicked item was paused, resume it
                mediaPlayer.start();
                isPaused = false;
            } else {
                // Play the new song
                playSong(song, position);
            }
        });

        holder.options.setOnClickListener(v -> {
            // Handle extra options click
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    private void playSong(Song song, int position) {
        try {
            if (mediaPlayer.isPlaying() || isPaused) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(song.getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playingPosition = position;
            isPaused = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView songName;
        ImageView options;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            songName = itemView.findViewById(R.id.song_name);
            options = itemView.findViewById(R.id.options);
        }
    }
}
