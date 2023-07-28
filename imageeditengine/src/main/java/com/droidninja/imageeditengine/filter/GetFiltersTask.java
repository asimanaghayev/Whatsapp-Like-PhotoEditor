package com.droidninja.imageeditengine.filter;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.droidninja.imageeditengine.model.ImageFilter;
import com.droidninja.imageeditengine.utils.FilterHelper;
import com.droidninja.imageeditengine.utils.TaskCallback;

import java.util.List;

public final class GetFiltersTask extends AsyncTask<Void, Void, List<ImageFilter>> {
    private final TaskCallback<List<ImageFilter>> listenerRef;
    private final Bitmap srcBitmap;

    public GetFiltersTask(TaskCallback<List<ImageFilter>> taskCallbackWeakReference, Bitmap srcBitmap) {
        this.srcBitmap = srcBitmap;
        this.listenerRef = taskCallbackWeakReference;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(List<ImageFilter> result) {
        super.onPostExecute(result);
        if (listenerRef != null) {
            listenerRef.onTaskDone(result);
        }
    }

    @Override
    protected List<ImageFilter> doInBackground(Void... params) {
        FilterHelper filterHelper = new FilterHelper();
        List<ImageFilter> filters = filterHelper.getFilters();
        for (int index = 0; index < filters.size(); index++) {
            ImageFilter imageFilter = filters.get(index);
            imageFilter.filterImage = PhotoProcessing.filterPhoto(getScaledBitmap(srcBitmap), imageFilter);
        }
        return filters;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private Bitmap getScaledBitmap(Bitmap srcBitmap) {
        // Determine how much to scale down the image
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        int targetWidth = 320;
        int targetHeight = 240;
        if (srcWidth < targetWidth || srcHeight < targetHeight) {
            return srcBitmap;
        }

        float scaleFactor =
                Math.max(
                        (float) srcWidth / targetWidth,
                        (float) srcHeight / targetHeight);

        return
                Bitmap.createScaledBitmap(
                        srcBitmap,
                        (int) (srcWidth / scaleFactor),
                        (int) (srcHeight / scaleFactor),
                        true);
    }
}// end inner class