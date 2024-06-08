package com.example.musicplayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SongViewModel extends ViewModel {
    private MutableLiveData<List<Song>> songsLiveData = new MutableLiveData<>();

    public void setSongs(List<Song> songs) {
        songsLiveData.setValue(songs);
    }

    public LiveData<List<Song>> getSongs() {
        return songsLiveData;
    }
}
