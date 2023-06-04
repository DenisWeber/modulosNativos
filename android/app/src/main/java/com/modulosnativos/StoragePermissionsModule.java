package com.modulosnativos;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class StoragePermissionsModule extends ReactContextBaseJavaModule implements ActivityEventListener, ActivityCompat.OnRequestPermissionsResultCallback{
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ReactApplicationContext m_reactContext;
    private Promise m_StoragePermissionPromise;

    public StoragePermissionsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        m_reactContext = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "StoragePermissions";
    }

    @ReactMethod
    public void checkStoragePermissionAsync(Promise promise) {
        boolean managePermissionResult;
        int readPermissionResult;
        int writePermissionResult;

        if (Build.VERSION.SDK_INT >= 30) {
            managePermissionResult = Environment.isExternalStorageManager();

            promise.resolve(managePermissionResult);
        } else {
            readPermissionResult = ContextCompat.checkSelfPermission(m_reactContext, READ_EXTERNAL_STORAGE);
            writePermissionResult = ContextCompat.checkSelfPermission(m_reactContext, WRITE_EXTERNAL_STORAGE);

            boolean hasPermission = readPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        writePermissionResult == PackageManager.PERMISSION_GRANTED;

            promise.resolve(hasPermission);
        }
    }

    @ReactMethod
    public void requestStoragePermissionAsync(Promise promise) {
        m_StoragePermissionPromise = promise;

        if (Build.VERSION.SDK_INT >= 30) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse(String.format("package:%s", m_reactContext.getPackageName())));

            getCurrentActivity().startActivityForResult(intent, STORAGE_PERMISSION_CODE);
        } else {
            String[] neededPermissions = new String[] {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

            ActivityCompat.requestPermissions(getCurrentActivity(), neededPermissions, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            boolean hasPermissions = false;

            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                    break;
                }
            }

            m_StoragePermissionPromise.resolve(hasPermissions);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            boolean hasPermission = false;

            if (Build.VERSION.SDK_INT >= 30) {
                hasPermission = Environment.isExternalStorageManager();
            }

            m_StoragePermissionPromise.resolve(hasPermission);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
