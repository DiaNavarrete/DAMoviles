package co.edu.unal.directorio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import DB.EmpresOperations;
import model.Empresa;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private ImageButton btn_buscar;
    private ImageButton btn_limpiar;
    private CheckBox checkboxCons;
    private CheckBox checkboxMedida;
    private CheckBox checkboxFabrica;
    private EditText buscar_text;
    private Boolean buscar_cons;
    private Boolean buscar_medida;
    private Boolean buscar_fab;
    private int total;

    ListView listView;
    HashMap<String, Long> empresas;
    private EmpresOperations empreOps;
    private static final String EXTRA_EMP_ID = "co.edu.unal.empId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.add_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empreOps = new EmpresOperations(this);
        addButton = findViewById(R.id.floatingActionButton);
        btn_buscar=findViewById(R.id.btn_buscar);
        btn_limpiar=findViewById(R.id.btn_limpiar);
        buscar_text=findViewById(R.id.buscar_text);
        checkboxCons=findViewById(R.id.input_tipo_cons2);
        checkboxMedida=findViewById(R.id.input_tipo_medida2);
        checkboxFabrica=findViewById(R.id.input_tipo_fab2);
        buscar_cons=true;
        buscar_fab=true;
        buscar_medida=true;
        listView=findViewById(R.id.list_view);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddUpdateActivity.class);
                i.putExtra(EXTRA_ADD_UPDATE, "Add");
                startActivity(i);
            }
        });
        buscarTodas();

    }
    public void buscarTodas(){
        empreOps.open();
        empresas=empreOps.getNamesEmpres();
        empreOps.close();
        final List<String> empnames= new ArrayList<>(empresas.keySet());
        total=empnames.size();
        Collections.sort(empnames);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,empnames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent inten = new Intent(MainActivity.this,AddUpdateActivity.class);
                inten.putExtra(EXTRA_ADD_UPDATE, "Read");
                Long idread=empresas.get(empnames.get(i));
                inten.putExtra(EXTRA_EMP_ID, idread);
                startActivity(inten);
            }
        });
    }
    public void limpiar(View v){
        buscar_text.setText("");
        buscar_cons=true;
        buscar_fab=true;
        buscar_medida=true;
        checkboxCons.setChecked(true);
        checkboxMedida.setChecked(true);
        checkboxFabrica.setChecked(true);
        buscarTodas();
    }
    public void buscar(View v){
        String nombreBuscar=buscar_text.getText().toString();
        System.out.println("filtro "+ buscar_cons + buscar_medida + buscar_fab);
        empreOps.open();
        if(nombreBuscar.length()>0 || buscar_cons || buscar_medida || buscar_fab )
            empresas=empreOps.getNamesFilter(nombreBuscar,buscar_cons,buscar_medida,buscar_fab);
        else
            empresas=empreOps.getNamesEmpres();
        empreOps.close();
        final List<String> empnames= new ArrayList<>(empresas.keySet());
        Collections.sort(empnames);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,empnames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent inten = new Intent(MainActivity.this,AddUpdateActivity.class);
                inten.putExtra(EXTRA_ADD_UPDATE, "Read");
                Long idread=empresas.get(empnames.get(i));
                inten.putExtra(EXTRA_EMP_ID, idread);
                startActivity(inten);
            }
        });
    }

    public void onCheckBoxClicked(View v){
        boolean checked = ((CheckBox)v).isChecked();
        switch (v.getId()){
            case R.id.input_tipo_cons2:
                buscar_cons=checked;
                break;
            case R.id.input_tipo_medida2:
                buscar_medida=checked;
                break;
            case R.id.input_tipo_fab2:
                buscar_fab=checked;
                break;
        }
        buscar(v);
    }

    @Override
    protected void onResume(){
        super.onResume();
        empreOps.open();
    }

    @Override
    protected void onPause(){
        super.onPause();
        empreOps.close();

    }
}
