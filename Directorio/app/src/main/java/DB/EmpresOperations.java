package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Empresa;

public class EmpresOperations {
    public static final String LOGTAG = "EMP_DIR";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allCols= {
            EmpresDBHandler.COL_ID,
            EmpresDBHandler.COL_NOMBRE,
            EmpresDBHandler.COL_URL,
            EmpresDBHandler.COL_TEL,
            EmpresDBHandler.COL_EMAIL,
            EmpresDBHandler.COL_PROD_SERVE,
            EmpresDBHandler.COL_TIPO
    };
    private static final String[] twoCols= {
            EmpresDBHandler.COL_ID,
            EmpresDBHandler.COL_NOMBRE
    };

    public  EmpresOperations(Context context){
        dbhandler = new EmpresDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    public Empresa addEmpresa(Empresa Empresa){
        ContentValues values = new ContentValues();
        values.put(EmpresDBHandler.COL_EMAIL, Empresa.getEmail());
        values.put(EmpresDBHandler.COL_NOMBRE, Empresa.getNombre());
        values.put(EmpresDBHandler.COL_PROD_SERVE, Empresa.getProd_serv());
        values.put(EmpresDBHandler.COL_TEL, Empresa.getTel());
        values.put(EmpresDBHandler.COL_TIPO, Empresa.getTipo());
        values.put(EmpresDBHandler.COL_URL, Empresa.getUrl());
        long insertid = database.insert(EmpresDBHandler.TABLE_EMPRESAS,null,values);
        Empresa.setId(insertid);
        return  Empresa;
    }

    public Empresa getEmpresa(long id){

        System.out.println("Id: "+id);
        Cursor cursor = database.query(EmpresDBHandler.TABLE_EMPRESAS,allCols,
                EmpresDBHandler.COL_ID + "=?",new String[]{String.valueOf(id)},
                null,null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Empresa emp = new Empresa(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        return emp;
    }
    public HashMap<String, Long> getNamesEmpres(){
        Cursor cursor = database.query(EmpresDBHandler.TABLE_EMPRESAS,twoCols,null,
                null,null, null, null);
        HashMap<String, Long> empresas =new HashMap<String, Long>();
        if(cursor.getCount() > 0 ){
            while(cursor.moveToNext()) {
                Long id=cursor.getLong(cursor.getColumnIndex(EmpresDBHandler.COL_ID));
                String nombre=cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_NOMBRE));
                empresas.put(nombre, id);
            }

        }
        return empresas;
    }

    public HashMap<String, Long> getNamesFilter(String name, Boolean consultor, Boolean medida, Boolean fabrica){
        String[] args=new String[]{name};
        String queryTipo="";
        queryTipo += name.length()>0 ? EmpresDBHandler.COL_NOMBRE + " LIKE '%"+name+"%' AND (":"";
        queryTipo += consultor ? (EmpresDBHandler.COL_TIPO + " LIKE '%Con%' OR  "):"";
        queryTipo += medida ? (EmpresDBHandler.COL_TIPO + " LIKE '%Des%' OR  "):"";
        queryTipo += fabrica ? (EmpresDBHandler.COL_TIPO + " LIKE '%soft%' OR  "):"";
        queryTipo=queryTipo.substring(0,queryTipo.length()-5);
        queryTipo += name.length()>0 ? ")":"";
        System.out.println(queryTipo);
        Cursor cursor = database.query(EmpresDBHandler.TABLE_EMPRESAS,twoCols,
                 queryTipo,  null,null, null, null);
        HashMap<String, Long> empresas =new HashMap<String, Long>();
        if(cursor.getCount() > 0 ){
            while(cursor.moveToNext()) {
                Long id=cursor.getLong(cursor.getColumnIndex(EmpresDBHandler.COL_ID));
                String nombre=cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_NOMBRE));
                empresas.put(nombre, id);
            }

        }
        return empresas;
    }

    public List<Empresa> getAllEmpres(){
        Cursor cursor = database.query(EmpresDBHandler.TABLE_EMPRESAS,allCols,null,
                null,null, null, null);
        List<Empresa> empresas =new ArrayList<>();
        if(cursor.getCount() > 0 ){
            while(cursor.moveToNext()){
                Empresa empresa = new Empresa();
                empresa.setId(cursor.getLong(cursor.getColumnIndex(EmpresDBHandler.COL_ID)));
                empresa.setEmail(cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_EMAIL)));
                empresa.setNombre(cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_NOMBRE)));
                empresa.setProd_serv(cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_PROD_SERVE)));
                empresa.setTel(cursor.getInt(cursor.getColumnIndex(EmpresDBHandler.COL_TEL)));
                empresa.setTipo(cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_TIPO)));
                empresa.setUrl(cursor.getString(cursor.getColumnIndex(EmpresDBHandler.COL_URL)));

            }
        }
        return empresas;
    }

    public int updateEmpres(Empresa empresa){
        ContentValues values = new ContentValues();
        values.put(EmpresDBHandler.COL_EMAIL, empresa.getEmail());
        values.put(EmpresDBHandler.COL_NOMBRE, empresa.getNombre());
        values.put(EmpresDBHandler.COL_PROD_SERVE, empresa.getProd_serv());
        values.put(EmpresDBHandler.COL_TEL, empresa.getTel());
        values.put(EmpresDBHandler.COL_TIPO, empresa.getTipo());
        values.put(EmpresDBHandler.COL_URL, empresa.getUrl());
        return database.update(EmpresDBHandler.TABLE_EMPRESAS, values,
                EmpresDBHandler.COL_ID + "=?",new String[] { String.valueOf(empresa.getId())});
    }
    public void removeEmpresa(Long empresa_id) {

        database.delete(EmpresDBHandler.TABLE_EMPRESAS,
                EmpresDBHandler.COL_ID + "=" + empresa_id, null);
    }
}
