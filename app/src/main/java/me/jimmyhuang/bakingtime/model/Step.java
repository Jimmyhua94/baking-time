package me.jimmyhuang.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

import static me.jimmyhuang.bakingtime.utility.Network.buildUrl;

public class Step implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private URL videoUrl;
    private URL thumbnailUrl;

    public Step(int id) {
        this.id = id;
    }

    public Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = buildUrl(in.readString());
        thumbnailUrl = buildUrl(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        if (videoUrl != null) {
            dest.writeString(videoUrl.toString());
        } else {
            dest.writeString("");
        }
        if (thumbnailUrl != null) {
            dest.writeString(thumbnailUrl.toString());
        } else {
            dest.writeString("");
        }
    }

    public final static Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int i) {
            return new Step[i];
        }
    };

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public URL getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = buildUrl(videoUrl);
    }

    public URL getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = buildUrl(thumbnailUrl);
    }
}
