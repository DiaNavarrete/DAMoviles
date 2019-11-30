package DB;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EmpresDBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NOMBRE = "empresas.db";
    public static final int DATABASE_VERSION =1;
    public static final String TABLE_EMPRESAS= "empresas";
    public static final String COL_ID = "Id";
    public static final String COL_NOMBRE ="nombre";
    public static final String COL_URL = "url";
    public static final String COL_TEL = "tel";
    public static final String COL_EMAIL = "email";
    public static final String COL_PROD_SERVE = "prod_serv";
    public static final String COL_TIPO = "tipo";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EMPRESAS + "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NOMBRE + " TEXT, " +
                    COL_URL +  " TEXT, " +
                    COL_TEL + " NUMERIC, " +
                    COL_EMAIL + " TEXT, " +
                    COL_PROD_SERVE + " TEXT, " +
                    COL_TIPO + " TEXT" +
                    ")";
    public EmpresDBHandler(Context context){
        super(context,DATABASE_NOMBRE,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EMPRESAS);
        db.execSQL(TABLE_CREATE);
    }
}
