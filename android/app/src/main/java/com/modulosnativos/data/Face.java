package com.modulosnativos.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Face {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String externalId;
    public byte[] template;
}
