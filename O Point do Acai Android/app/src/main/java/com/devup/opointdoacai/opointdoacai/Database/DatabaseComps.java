package com.devup.opointdoacai.opointdoacai.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.devup.opointdoacai.opointdoacai.Model.SelectedComps;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseComps extends SQLiteAssetHelper {

    private static final String DB_NAME = "opdacompbd.db";
    private static final int DB_VER = 1;

    public DatabaseComps(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<SelectedComps> getComps(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelected = {"compName", "compPrice"};
        String sqlTable = "SelectedComps";

        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelected, null, null, null, null, null);

        final List<SelectedComps> result = new ArrayList<>();

        if (c.moveToFirst()){

            do {

                result.add(new SelectedComps(c.getString(c.getColumnIndex("compName")),
                        c.getString(c.getColumnIndex("compPrice"))
                        ));

            }while (c.moveToNext());

        }

        return result;

    }

    public void addToSelectedComps(SelectedComps selectedComps){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO SelectedComps(compName, compPrice) VALUES('%s','%s');",
                selectedComps.getCompName(),
                selectedComps.getCompPrice());
        db.execSQL(query);

    }

    public void cleanSelectedComps(){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM SelectedComps");
        db.execSQL(query);

    }

    public void cleanOfSelectedComps(String key){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM SelectedComps WHERE compName = '%s'",
                key);
        db.execSQL(query);

    }


}
