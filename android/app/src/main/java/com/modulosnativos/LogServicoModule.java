package com.modulosnativos;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.modulosnativos.data.AppDatabase;
import com.modulosnativos.data.FaceDao;
import com.modulosnativos.data.Log;
import com.modulosnativos.data.LogDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogServicoModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext m_reactContext;
    private AppDatabase m_appDatabase;
    private LogDao m_logDao;
    private SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public LogServicoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        m_reactContext = reactContext;

        m_appDatabase = AppDatabase.getInstance(m_reactContext);
        m_logDao = m_appDatabase.logDao();
    }

    @Override
    public String getName() {
        return "LogServico";
    }

    @ReactMethod
    public void iniciarServico() {
        m_logDao.insert(Log.ACTION_START_SERVICE_INIT);
        sendLog(m_dateFormat.format(new Date()) + " - " + "Iniciando serviço...");

        m_logDao.insert(Log.ACTION_START_SERVICE_SUCCESS);
        sendLog(m_dateFormat.format(new Date()) + " - " + "Serviço iniciado com sucesso.");
    }

    @ReactMethod
    public void pararServico() {
        m_logDao.insert(Log.ACTION_STOP_SERVICE_INIT);
        sendLog(m_dateFormat.format(new Date()) + " - " + "Parando serviço...");

        m_logDao.insert(Log.ACTION_STOP_SERVICE_SUCCESS);
        sendLog(m_dateFormat.format(new Date()) + " - " + "Serviço parado com sucesso.");
    }

    @ReactMethod
    public void mostrarLogs() {
        sendLog(getAllLogText());
    }

    @ReactMethod
    public void deletarLogs() {
        m_logDao.deleteAll();
    }

    // Required for rn built in EventEmitter Calls.
    @ReactMethod
    public void addListener(String eventName) { }

    // Required for rn built in EventEmitter Calls.
    @ReactMethod
    public void removeListeners(Integer count) { }

    public void sendLog(String message) {
        WritableMap params = Arguments.createMap();
        params.putString("message", message);

        m_reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("log", params);
    }

    private String getAllLogText() {
        List<Log> logs = m_logDao.getAll();
        StringBuilder logText = new StringBuilder();

        for (Log log : logs) {
            logText.append(m_dateFormat.format(log.dateTime));
            logText.append(" - ");
            logText.append(log.getActionDescription());

            if (log.description != null) {
                logText.append(" - ");
                logText.append(log.description);
            }

            logText.append("\n");
        }

        return logText.toString();
    }
}
