package co.edu.unal.webservices;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    static final int CHIP_GROUP=0;
    static final int NAME=1;
    static final String[] ORDENES=new String[]{"operador","kpi"};
    ServiceConnection connection;
    ImageButton btn_filtrar;
    ListView listView;
    private Adaptador adaptador;
    Spinner spinnerOrdenar;
    LayoutInflater inflater;
    List<String> a_oList;
    String a_o;
    String orden;
    HashMap<String, List<String>> parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection= new ServiceConnection();
        listView= findViewById(R.id.list_view);
        inflater=LayoutInflater.from(MainActivity.this);
        new RequestItemsServiceTask().execute(R.id.chipGroupOperadores,"operador");
    //    new RequestItemsServiceTask().execute(R.id.chipGroupKpi, "kpi");
        a_oList= new ArrayList<String>(){{add("2016"); add("2017"); add("2018");}};
        a_o="2018";
        parametros= new HashMap<>();
        parametros.put("operador",new ArrayList<String>(4));
        parametros.put("kpi",new ArrayList<String>(4));
        spinnerOrdenar=findViewById(R.id.spinner);
        spinnerOrdenar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                orden=ORDENES[pos];
                new RequestFilterServiceTask().execute();
                Log.i("Reto","ordenar "+orden);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {    }
        });
        btn_filtrar=findViewById(R.id.btn_buscar);
        btn_filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Reto","Click");
                new RequestFilterServiceTask().execute();
            }
        });

    }

    public void a_oSelect(View v){
        RadioButton radio=(RadioButton)v;
        a_o=radio.getText().toString();
        Log.i("Reto",a_o);
        new RequestFilterServiceTask().execute();

    }




    /**
     * populate list in background while showing progress dialog.
     */
    private class RequestItemsServiceTask extends AsyncTask<Object, Void, List<String>> {
        ChipGroup chipGroup;
        String namefltr;

        private ProgressDialog dialog =
                new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected List<String>  doInBackground(Object... param) {
            chipGroup=findViewById(Integer.parseInt(param[CHIP_GROUP].toString()));
            List<String> itemsList=null;
            namefltr=param[NAME].toString();
            try {
                itemsList = connection.findListNames(namefltr);//connection.findAllItems();
            } catch (Throwable e) {
                // handle exceptions
            }
            parametros.replace(namefltr,itemsList);
            return itemsList;
        }

        @Override
        protected void onPostExecute(List<String> itemsList) {

            // setListAdapter must not be called at doInBackground()
            // since it would be executed in separate Thread
           /* setListAdapter(
                    new ArrayAdapter<MyItem>(ItemsListActivity.this,
                            R.layout.list_item, itemsList));
*/          Collections.sort(itemsList);
            if(itemsList.contains("Tiempo de carga Web por destino")) itemsList.remove("Tiempo de carga Web por destino");

            for (String item : itemsList) {
                Chip chip = (Chip)inflater.inflate(R.layout.chip_item, null,false); // new Chip(chipOperadores.getContext());
             //   chip.setChipDrawable(ChipDrawable.createFromResource(this, R.));
                chip.setText(item);
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                        // Handle the checked chip change.
                        if(isChecked)
                            parametros.get(namefltr).add(view.getText().toString());
                        else
                            parametros.get(namefltr).remove(view.getText().toString());

                        Log.i("Reto",namefltr +": "+ parametros.get(namefltr).size());
                    }
                });
                chipGroup.addView(chip);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * populate list in background while showing progress dialog.
     */
    private class RequestFilterServiceTask extends AsyncTask<Void, Void, List<Item>> {
        private ProgressDialog dialog =
                new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            Log.i("Reto","Iniciando filtering");
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected List<Item>  doInBackground(Void... v) {
            List<Item> itemsList=null;
            List<String> ops= parametros.get("operador");
            List<String> kpis= parametros.get("kpi");
            Log.i("Reto","filtering por "+orden);
            try {
                itemsList = connection.filterItems(a_o,orden, ops, kpis);// findListNames(param[NAME].toString());
            } catch (Throwable e) {
                Log.i("Reto","excepcion "+ e);
                // handle exceptions
            }
            return itemsList;
        }

        @Override
        protected void onPostExecute(List<Item> itemsList) {
            adaptador= new Adaptador(itemsList,MainActivity.this);
            listView.setAdapter(adaptador);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
