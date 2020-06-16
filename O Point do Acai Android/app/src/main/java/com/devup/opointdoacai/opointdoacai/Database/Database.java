package com.devup.opointdoacai.opointdoacai.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.devup.opointdoacai.opointdoacai.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME= "cartdatabase.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "Quantidade", "Complementos", "Preco"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()){

            do {

                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("Quantidade")),
                        c.getString(c.getColumnIndex("Complementos")),
                        c.getString(c.getColumnIndex("Preco"))
                        ));

            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,Quantidade,Complementos,Preco) VALUES('%s','%s','%s','%s');",
                order.getProductId(),
                order.getQuantidade(),
                order.getComplementos(),
                order.getPreco());
        db.execSQL(query);
    }

    public void cleanCart(){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    public void removeFromCart(String id) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ProductId='%s'", id);
        db.execSQL(query);

    }
}
