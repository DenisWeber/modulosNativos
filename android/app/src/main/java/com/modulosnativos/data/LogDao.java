package com.modulosnativos.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Date;
import java.util.List;

@Dao
public abstract class LogDao {
    @Query("SELECT * FROM Log ORDER BY dateTime")
    public abstract List<Log> getAll();

    @Query("DELETE FROM Log")
    public abstract void deleteAll();

    public void insert(int action) {
        insert(action, null);
    }

    public void insert(int action, String description) {
        Log log = new Log();

        log.dateTime = new Date();
        log.action = action;
        log.description = description;

        insert(log);
    }

    @Insert
    protected abstract void insert(Log log);
}
