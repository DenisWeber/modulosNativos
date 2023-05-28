package com.modulosnativos;

import static com.facebook.common.internal.Throwables.getRootCause;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.modulosnativos.data.AppDatabase;
import com.modulosnativos.data.Log;
import com.modulosnativos.data.LogDao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class LogServicoService extends Service {
    public static final String ACAO_WHATSAPP = "com.modulosnativos.ACAO_WHATSAPP";
    public static final String ACAO_TELEFONE = "com.modulosnativos.ACAO_TELEFONE";

    private AppDatabase m_appDatabase;
    private LogDao m_logDao;


    @Override
    public void onCreate() {
        m_appDatabase = AppDatabase.getInstance(getApplicationContext());
        m_logDao = m_appDatabase.logDao();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AsyncTask.execute(() -> {
            switch (intent.getAction()) {
                case ACAO_WHATSAPP:
                    handleActionWhatsapp(intent);
                    return;
                case ACAO_TELEFONE:
                    try {
                        handleActionTelefone(intent);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
            }
        });

        return START_STICKY;
    }

    private void handleActionWhatsapp(Intent intent) {
//        showNotification();

        Bundle params = intent.getBundleExtra("params");
        Bundle result = new Bundle();
        ResultReceiver receiver = intent.getParcelableExtra("receiver");

        m_logDao.insert(Log.CHAMOU_WHATSAPP_SERVICO);

        String numero = params.getString("numero");
        String mensagem = params.getString("mensagem");

        try {
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + Uri.encode(mensagem);

            Intent intentWhats = new Intent(Intent.ACTION_VIEW);
            intentWhats.setData(Uri.parse(url));
            intentWhats.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getApplicationContext().startActivity(intentWhats);

            result.putBoolean("error", false);
            result.putString("message", "Deu tudo certo na chamada pelo Js Whatsapp");
        } catch (Exception e) {
            String message = getRootCause(e).getMessage();

            result.putBoolean("error", true);
            result.putString("message", message);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        receiver.send(0, result);
    }

    private void handleActionTelefone(Intent intent) throws MalformedURLException {
        Bundle result = new Bundle();
        ResultReceiver receiver = intent.getParcelableExtra("receiver");

        m_logDao.insert(Log.CHAMOU_LIGACAO_SERVICO);

        URL url = new URL("https://download.secullum.com.br/BiowebNativeFiles/BiowebNativeFiles.zip");
        File zipNdfFiles = new File(getCacheDir(), "BiowebNativeFiles.zip");

        try {
           download(url, zipNdfFiles, 0, false);

            result.putBoolean("error", false);
            result.putString("message", "download feito com sucesso");
        } catch (Exception e) {
            String message = getRootCause(e).getMessage();

            result.putBoolean("error", true);
            result.putString("message", message);
        }

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();

        receiver.send(0, result);
    }

    private void showNotification(long fileSize) {
        Notification.Builder builder;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            final String channelId = "modulos-nativos";
            NotificationChannel channel = new NotificationChannel(channelId,"Modulos Nativos", NotificationManager.IMPORTANCE_NONE);
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            builder = new Notification.Builder(getBaseContext(), channelId);
        } else {
            builder = new Notification.Builder(getBaseContext());
        }

        builder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Modulos Nativos")
                .setContentText("0 MB / " + fileSize + " MB")
                .setProgress(100, 0, false);

        startForeground(1, builder.build());
    }

    private void updateDownloadProgress(int progress, long fileSize, long progressoDownload) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            final String channelId = "modulos-nativos";
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }

        builder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Modulos Nativos")
                .setContentText(progressoDownload + " MB / " + fileSize + " MB")
                .setProgress(100, progress, false);

        notificationManager.notify(1, builder.build());

        if (progress == 100) {
            // Remove a notificação quando o progresso atingir 100%
            notificationManager.cancel(1);
        }
    }


    int tentativas = 0;
    int maxTentativas = 5;
    int TAMANHO_BUFFER = 8192;

    private void download(URL url, File zipNdfFile, long pontoInicial, boolean retry) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int fileSize = connection.getContentLength();
        long fileSizeMb = fileSize / (1024 * 1024);

        if (!retry) {
            showNotification(fileSizeMb);
        }
        
        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(zipNdfFile, true)) {

            inputStream.skip(pontoInicial);

            byte[] buffer = new byte[TAMANHO_BUFFER];
            int bytesRead;
            long totalBytesRead = pontoInicial;

            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                pontoInicial += bytesRead;

                // Calculate the current progress percentage
                int progress = (int) ((totalBytesRead * 100) / fileSize);

                android.util.Log.d("progress", String.valueOf(progress));
                long progressoDownload = totalBytesRead / (1024 * 1024);

                // Update the download progress notification
                updateDownloadProgress(progress, fileSizeMb, progressoDownload);
            }
        }
        catch (Exception e) {
            android.util.Log.d("download", "caiu no catch das tentativas");
            android.util.Log.d("download", String.valueOf(pontoInicial));
            tentativas++;

            if (tentativas > maxTentativas) {
                throw new IOException("Falha ao baixar arquivos de modelos.");
            }

            android.util.Log.d("erro", e.getMessage());
            Thread.sleep(3000);
            download(url, zipNdfFile, pontoInicial, true);
        }
    }
}
