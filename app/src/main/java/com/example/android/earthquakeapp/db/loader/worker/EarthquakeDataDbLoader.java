package com.example.android.earthquakeapp.db.loader.worker;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ArrayCreatingInputMerger;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.android.earthquakeapp.EarthquakeCallback;
import com.example.android.earthquakeapp.db.loader.LoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EarthquakeDataDbLoader {


    public EarthquakeDataDbLoader(@Nullable final EarthquakeCallback callback) {
        this.mCallback = callback;
    }

    public void cancelLoadData(@NonNull Application context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_MAIN);
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_DOWNLOAD);
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_FINAL);
    }

    public void loadData(@NonNull Application context) {
        cancelLoadData(context);

        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        final OneTimeWorkRequest mainWork = new OneTimeWorkRequest.Builder(MainWorker.class)
                .setConstraints(constraints)
                .addTag(TAG_MAIN)
                .build();

        uuidMain = mainWork.getId();

        final String[] urls = LoaderUtils.getQueryUrl(context);
        final List<OneTimeWorkRequest> works = new ArrayList<>();
        if (urls != null) {
            for (int index = 0; index < urls.length; index++) {
                final String url = urls[index];
                Log.d(TAG, "Url: " + url);
                if (TextUtils.isEmpty(url)) {
                    continue;
                }
                final OneTimeWorkRequest request = initWorkRequestDownload(url, index);
                uuidList.add(request.getId());
                works.add(request);
            }
        }

        final OneTimeWorkRequest finalWork = new OneTimeWorkRequest.Builder(FinalWorker.class)
                .setInputMerger(ArrayCreatingInputMerger.class)
                .addTag(TAG_FINAL)
                .build();

        uuidFinal = finalWork.getId();

        WorkManager.getInstance(context).beginWith(mainWork).then(works).then(finalWork).enqueue();

        if (mCallback != null) {
            for (final UUID uuid : uuidList) {
                WorkManager.getInstance(context).getWorkInfoByIdLiveData(uuid)
                        .observe(mCallback, workInfo -> {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                final Data data = workInfo.getOutputData();
                                int numberUrls = works.size();
                                int numberOfQuake = data.getInt(NUMBER_QUAKE_INSERTED, 0);

                                mCallback.notifyNewData(numberOfQuake, numberUrls);
                            }
                        });
            }


            WorkManager.getInstance(context).getWorkInfoByIdLiveData(uuidFinal)
                    .observe(mCallback, workInfo -> {
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            final Data data = workInfo.getOutputData();
                            int numberOfQuake = data.getInt(NUMBER_QUAKE_INSERTED_FINAL, 0);

                            mCallback.notifyEarthquakeFinalCount(numberOfQuake);
                        }
                    });
        }
    }

    private static OneTimeWorkRequest initWorkRequestDownload(@NonNull final String url, final int index) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        final Data data = new Data.Builder()
                .putString(EarthquakeDataDbLoader.URL, url)
                .putInt(EarthquakeDataDbLoader.INDEX, index)
                .build();

        final OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(LoadDataWorker.class)
                .setInputData(data)
                .addTag(EarthquakeDataDbLoader.TAG_DOWNLOAD)
                .setConstraints(constraints)
                .build();

        Log.d(TAG, "create work " + index);

        return work;
    }

    private final EarthquakeCallback mCallback;
    private UUID uuidMain;
    private final List<UUID> uuidList = new ArrayList<>();
    private UUID uuidFinal;


    static final String URL = "URL";
    static final String INDEX = "INDEX";
    static final String TAG_DOWNLOAD = "TAG_DOWNLOAD";
    static final String TAG_MAIN = "TAG_MAIN";
    static final String TAG_FINAL = "TAG_FINAL";
    static final String NUMBER_QUAKE_INSERTED = "NUMBER_QUAKE_INSERTED";
    static final String NUMBER_QUAKE_INSERTED_FINAL = "NUMBER_QUAKE_INSERTED_FINAL";
    static final String EXCEPTION_CLASS = "EXCEPTION_CLASS";
    static final String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";

    private static final String TAG = EarthquakeDataDbLoader.class.getSimpleName();
}
