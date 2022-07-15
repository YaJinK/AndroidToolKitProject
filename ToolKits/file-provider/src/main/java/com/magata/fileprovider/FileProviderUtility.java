package com.magata.fileprovider;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

public class FileProviderUtility {
    public static String providerSuffix = ".magata.fileprovider";

    public static Uri getFileUri(Context context, File file){
        return FileProvider.getUriForFile(context, context.getPackageName() + providerSuffix, file);
    }

    public static Uri getFileUri(Context context, String filePath){
        File file = new File(filePath);
        return FileProvider.getUriForFile(context, context.getPackageName() + providerSuffix, file);
    }
}
