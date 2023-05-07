package com.modulosnativos.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class FaceDao {
    @Query("SELECT * FROM Face")
    public abstract List<Face> getAll();

    @Query("SELECT * FROM Face WHERE externalId = :externalId LIMIT 1")
    public abstract Face getByExternalId(String externalId);

    @Transaction
    public void save(String externalId, byte[] template) {
        Face face = getByExternalId(externalId);

        if (face == null) {
            face = new Face();
            face.externalId = externalId;
        }

        face.template = template;

        if (face.id == 0) {
            insert(face);
        } else {
            update(face);
        }
    }

    @Query("DELETE FROM Face WHERE externalId = :externalId")
    public abstract void deleteByExternalId(String externalId);

    @Insert
    protected abstract void insert(Face face);

    @Update
    protected abstract void update(Face face);
}
