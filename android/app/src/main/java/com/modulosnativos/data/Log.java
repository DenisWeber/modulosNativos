package com.modulosnativos.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Log {
    public static final int ACTION_START_SERVICE_INIT = 0;
    public static final int ACTION_START_SERVICE_SUCCESS = 1;
    public static final int ACTION_START_SERVICE_ERROR = 2;

    public static final int ACTION_STOP_SERVICE_INIT = 10;
    public static final int ACTION_STOP_SERVICE_SUCCESS = 11;

    public static final int ACTION_ON_DEMAND_START_SERVICE_INIT = 20;
    public static final int ACTION_ON_DEMAND_START_SERVICE_SUCCESS = 21;
    public static final int ACTION_ON_DEMAND_START_SERVICE_ERROR = 22;

    public static final int ACTION_ENROLL_INIT = 30;
    public static final int ACTION_ENROLL_SUCCESS = 31;
    public static final int ACTION_ENROLL_ERROR = 32;

    public static final int ACTION_IDENTIFY_INIT = 40;
    public static final int ACTION_IDENTIFY_SUCCESS = 41;
    public static final int ACTION_IDENTIFY_ERROR = 42;

    public static final int ACTION_GET_INIT = 50;
    public static final int ACTION_GET_SUCCESS = 51;
    public static final int ACTION_GET_ERROR = 52;

    public static final int ACTION_DELETE_INIT = 60;
    public static final int ACTION_DELETE_SUCCESS = 61;
    public static final int ACTION_DELETE_ERROR = 62;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date dateTime;
    public int action;
    public String description;

    public String getActionDescription() {
        switch (action) {
            case ACTION_START_SERVICE_INIT:
                return "Iniciando serviço...";
            case ACTION_START_SERVICE_SUCCESS:
                return "Serviço iniciado com sucesso.";
            case ACTION_START_SERVICE_ERROR:
                return "Falha ao iniciar serviço.";
            case ACTION_STOP_SERVICE_INIT:
                return "Parando serviço...";
            case ACTION_STOP_SERVICE_SUCCESS:
                return "Serviço parado com sucesso.";
            case ACTION_ON_DEMAND_START_SERVICE_INIT:
                return "Iniciando serviço sob demanda...";
            case ACTION_ON_DEMAND_START_SERVICE_SUCCESS:
                return "Serviço iniciado sob demanda com sucesso.";
            case ACTION_ON_DEMAND_START_SERVICE_ERROR:
                return "Falha ao iniciar serviço sob demanda.";
            case ACTION_ENROLL_INIT:
                return "Iniciando cadastramento...";
            case ACTION_ENROLL_SUCCESS:
                return "Cadastramento concluído com sucesso.";
            case ACTION_ENROLL_ERROR:
                return "Falha no cadastramento.";
            case ACTION_IDENTIFY_INIT:
                return "Iniciando identificação...";
            case ACTION_IDENTIFY_SUCCESS:
                return "Identificação concluída com sucesso.";
            case ACTION_IDENTIFY_ERROR:
                return "Falha na identificação.";
            case ACTION_GET_INIT:
                return "Iniciando busca...";
            case ACTION_GET_SUCCESS:
                return "Busca concluída com sucesso.";
            case ACTION_GET_ERROR:
                return "Falha na busca.";
            case ACTION_DELETE_INIT:
                return "Iniciando exclusão...";
            case ACTION_DELETE_SUCCESS:
                return "Exclusão concluída com sucesso.";
            case ACTION_DELETE_ERROR:
                return "Falha na exclusão.";
            default:
                return "Ação desconhecida";
        }
    }
}
