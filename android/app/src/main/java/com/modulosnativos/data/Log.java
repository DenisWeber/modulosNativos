package com.modulosnativos.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Log {
    public static final int CHAMOU_WHATSAPP = 0;
    public static final int CHAMOU_WHATSAPP_SERVICO = 1;
    public static final int CHAMOU_LIGACAO = 2;
    public static final int CHAMOU_LIGACAO_SERVICO = 3;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date dateTime;
    public int action;
    public String description;

    public String getActionDescription() {
        switch (action) {
            case CHAMOU_WHATSAPP:
                return "Chamou Whatsapp...";
            case CHAMOU_WHATSAPP_SERVICO:
                return "Chamou Whatsapp pelo React Native Start Service...";
            case CHAMOU_LIGACAO:
                return "Chamou Ligação...";
            case CHAMOU_LIGACAO_SERVICO:
                return "Chamou Ligação pelo React Native Start Service...";
            default:
                return "Ação desconhecida";
        }
    }
}
