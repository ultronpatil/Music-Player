package com.example.musicplayer;

import android.annotation.SuppressLint;
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
    private int playingPosition = -1;
    private boolean isPaused = false;

    public SongAdapter(Context context, List<Song> songs) {
        this.songs = songs;
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnCompletionListener(mp -> playNextSong());
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getTitle());

        if (playingPosition == position) {
            if (mediaPlayer.isPlaying() && !isPaused) {
                holder.playPauseIcon.setImageResource(R.drawable.ic_play);
            } else if (isPaused) {
                holder.playPauseIcon.setImageResource(R.drawable.ic_pause);
            } else {
                holder.playPauseIcon.setImageResource(R.drawable.ic_song_icon);
            }
        } else {
            holder.playPauseIcon.setImageResource(R.drawable.ic_song_icon);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying() && playingPosition == position && !isPaused) {
                pauseSong();
            } else if (playingPosition == position && isPaused) {
                resumeSong();
            } else {
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
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPaused = false;
            playingPosition = position; // Update the playing position
            notifyDataSetChanged(); // Refresh UI
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
            notifyDataSetChanged(); // Refresh UI
        }
    }

    private void resumeSong() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPaused = false;
            notifyDataSetChanged(); // Refresh UI
        }
    }

    private void playNextSong() {
        int nextPosition = (playingPosition + 1) % songs.size(); // Calculate the next position in the queue
        playingPosition = nextPosition;
        playSong(songs.get(nextPosition), nextPosition);
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView playPauseIcon;
        TextView songName;
        ImageView options;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            playPauseIcon = itemView.findViewById(R.id.play_pause_icon);
            songName = itemView.findViewById(R.id.song_name);
            options = itemView.findViewById(R.id.options);
        }
    }
}
