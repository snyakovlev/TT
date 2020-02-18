package ru.television.online;

import androidx.annotation.Nullable;

public interface PlaylistItem {


    public String getAlbum();


    public String getArtist();


    public String getArtworkUrl();


    public boolean getDownloaded();


    public String getDownloadedMediaUri();


    public long getId();


    public int getMediaType();


    public String getMediaUrl();


    public String getThumbnailUrl();


    public String getTitle();

}
