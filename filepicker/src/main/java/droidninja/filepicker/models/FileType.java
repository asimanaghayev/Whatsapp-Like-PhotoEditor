package droidninja.filepicker.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import java.util.Objects;

import droidninja.filepicker.R;

/**
 * Created by droidNinja on 29/07/16.
 */
public class FileType implements Parcelable {
    public String title;

    @DrawableRes
    public int drawable;

    public String[] extensions;

    public FileType(String title, String[] extensions, int drawable) {
        this.title = title;
        this.extensions = extensions;
        this.drawable = drawable;
    }

    protected FileType(Parcel in) {
        title = in.readString();
        drawable = in.readInt();
        extensions = in.createStringArray();
    }

    public static final Creator<FileType> CREATOR = new Creator<FileType>() {
        @Override
        public FileType createFromParcel(Parcel in) {
            return new FileType(in);
        }

        @Override
        public FileType[] newArray(int size) {
            return new FileType[size];
        }
    };

    public int getDrawable() {
        if (drawable == 0)
            return R.drawable.icon_file_unknown;
        return drawable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeInt(drawable);
        parcel.writeStringArray(extensions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileType fileType = (FileType) o;

        return Objects.equals(title, fileType.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
