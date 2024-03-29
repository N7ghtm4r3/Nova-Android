package com.tecknobit.nova.helpers.storage;

import static com.tecknobit.novacore.helpers.LocalSessionUtils.NovaSession.HOST_ADDRESS_KEY;
import static com.tecknobit.novacore.records.NovaItem.IDENTIFIER_KEY;
import static com.tecknobit.novacore.records.User.EMAIL_KEY;
import static com.tecknobit.novacore.records.User.PROFILE_PIC_URL_KEY;
import static com.tecknobit.novacore.records.User.ROLE_KEY;
import static com.tecknobit.novacore.records.User.TOKEN_KEY;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tecknobit.novacore.helpers.LocalSessionUtils;
import com.tecknobit.novacore.records.User.Role;

import java.util.ArrayList;
import java.util.List;

public class LocalSessionHelper extends SQLiteOpenHelper implements LocalSessionUtils {

    private static final int DATABASE_VERSION = 1;

    public LocalSessionHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SESSIONS_TABLE);
        onCreate(db);
    }

    @Override
    public void insertSession(String id, String token, String profilePicUrl, String email,
                              String hostAddress, Role role) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDENTIFIER_KEY, id);
        values.put(TOKEN_KEY, token);
        values.put(PROFILE_PIC_URL_KEY, profilePicUrl);
        values.put(EMAIL_KEY, email);
        values.put(HOST_ADDRESS_KEY, hostAddress);
        values.put(ROLE_KEY, role.name());
        database.insert(SESSIONS_TABLE, null, values);
    }

    @Override
    public List<NovaSession> getSessions() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SESSIONS_TABLE, null);
        List<NovaSession> sessions = new ArrayList<>();
        while (cursor.moveToNext())
            sessions.add(fillNovaSession(cursor));
        cursor.close();
        return sessions;
    }

    @Override
    public NovaSession getSession(String id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SESSIONS_TABLE + " WHERE "
                + IDENTIFIER_KEY + "=?", new String[]{id});
        NovaSession session = null;
        if(cursor.moveToFirst()) {
            session = fillNovaSession(cursor);
            cursor.close();
        }
        return session;
    }

    private NovaSession fillNovaSession(Cursor cursor) {
        return new NovaSession(
                cursor.getString(cursor.getColumnIndexOrThrow(IDENTIFIER_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(TOKEN_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(PROFILE_PIC_URL_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_KEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(HOST_ADDRESS_KEY)),
                Role.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ROLE_KEY)))
        );
    }

    @Override
    public void deleteAllSessions() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(SESSIONS_TABLE, null, null);
    }

    @Override
    public void deleteSession(String id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(SESSIONS_TABLE, IDENTIFIER_KEY + "=?", new String[]{id});
    }

}
