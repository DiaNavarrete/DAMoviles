package co.edu.unal.directorio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import DB.EmpresOperations;
import model.Empresa;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_NULL;

public class AddUpdateActivity extends AppCompatActivity {

    private static final String EXTRA_EMP_ID = "co.edu.unal.empId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.add_update";
    private String[] tipos ={"Consultoría","Desarrollo a la medida","Fábrica de software"};


    private CheckBox checkboxCons;
    private CheckBox checkboxMedida;
    private CheckBox checkboxFabrica;
    private EditText nombreEditText;
    private EditText urlEditText;
    private EditText telEditText;
    private EditText emailEditText;
    private EditText prodEditText;
    private ArrayList<Boolean> tipoEdit;
    private Button guardarBtn;
    private Button eliminarBtn;
    private Button editarBtn;
    private Empresa newEmpresa;
    private Empresa oldEmpresa;
    private String mode;
    private long empId;
    private EmpresOperations empresData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);
        oldEmpresa= new Empresa();
        newEmpresa = new Empresa();
        checkboxCons=findViewById(R.id.input_tipo_cons);
        checkboxMedida=findViewById(R.id.input_tipo_medida);
        checkboxFabrica=findViewById(R.id.input_tipo_fab);
        nombreEditText=findViewById(R.id.input_nombre);
        urlEditText=findViewById(R.id.input_url);
        telEditText=findViewById(R.id.input_tel);
        emailEditText=findViewById(R.id.input_email);
        prodEditText=findViewById(R.id.input_prods);
        tipoEdit= new ArrayList<Boolean>(){{add(false); add(false); add(false);}};
        guardarBtn=findViewById(R.id.btn_guardar);
        eliminarBtn=findViewById(R.id.btn_eliminar);
        editarBtn=findViewById(R.id.btn_editar);
        empresData = new EmpresOperations(this);
        empresData.open();

        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if(mode.equals("Read")){
            setUIRead();
            empId = getIntent().getLongExtra(EXTRA_EMP_ID,0);
            initializeEmp(empId);
        }
        else{
            setUIEditable();
        }
        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("Add")){
                    newEmpresa.setUrl(urlEditText.getText().toString());
                    newEmpresa.setTel(Integer.parseInt(telEditText.getText().toString()));
                    newEmpresa.setProd_serv(prodEditText.getText().toString());
                    newEmpresa.setNombre(nombreEditText.getText().toString());
                    newEmpresa.setEmail(emailEditText.getText().toString());
                    String tipo="";
                    tipo+= tipoEdit.get(0)?tipos[0] +",":"";
                    tipo+= tipoEdit.get(1)?tipos[1] +",":"";
                    tipo+= tipoEdit.get(2)?tipos[2] +",":"";
                    newEmpresa.setTipo(tipo.substring(0,tipo.length()-1));
                    empresData.addEmpresa(newEmpresa);
                    Toast t=Toast.makeText(AddUpdateActivity.this, "Empresa "+ newEmpresa.getNombre() + " se ha agregado correctamente !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateActivity.this,MainActivity.class);
                    startActivity(i);
                }
                else{
                    oldEmpresa.setUrl(urlEditText.getText().toString());
                    oldEmpresa.setTel(Integer.parseInt(telEditText.getText().toString()));
                    oldEmpresa.setProd_serv(prodEditText.getText().toString());
                    oldEmpresa.setNombre(nombreEditText.getText().toString());
                    oldEmpresa.setEmail(emailEditText.getText().toString());
                    String tipo="";
                    tipo+= tipoEdit.get(0)?tipos[0] +",":"";
                    tipo+= tipoEdit.get(1)?tipos[1] +",":"";
                    tipo+= tipoEdit.get(2)?tipos[2] +",":"";
                    oldEmpresa.setTipo(tipo.substring(0,tipo.length()-1));
                    empresData.updateEmpres(oldEmpresa);
                    Toast t=Toast.makeText(AddUpdateActivity.this, oldEmpresa.getNombre() + " se ha actualizado correctamente!", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        });
        eliminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddUpdateActivity.this);

                builder.setMessage(R.string.pregunta)
                        .setCancelable(false)
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                empresData.removeEmpresa(empId);
                                Toast t=Toast.makeText(AddUpdateActivity.this, "Empresa eliminada correctamente !", Toast.LENGTH_SHORT);
                                t.show();
                                Intent i = new Intent(AddUpdateActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                builder.create().show();

            }
        });
        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUIEditable();
            }
        });

    }

    private void setUIEditable(){
        guardarBtn.setVisibility(View.VISIBLE);
        editarBtn.setVisibility(View.INVISIBLE);
        eliminarBtn.setVisibility(View.INVISIBLE);
        nombreEditText.setEnabled(true);
        urlEditText.setEnabled(true);
        telEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        prodEditText.setEnabled(true);
        checkboxCons.setEnabled(true);
        checkboxMedida.setEnabled(true);
        checkboxFabrica.setEnabled(true);

    }
    private void setUIRead(){
        guardarBtn.setVisibility(View.INVISIBLE);
        editarBtn.setVisibility(View.VISIBLE);
        eliminarBtn.setVisibility(View.VISIBLE);
        nombreEditText.setEnabled(false);
        urlEditText.setEnabled(false);
        telEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        prodEditText.setEnabled(false);
        checkboxCons.setEnabled(false);
        checkboxMedida.setEnabled(false);
        checkboxFabrica.setEnabled(false);
    }

    private void initializeEmp(long empId) {
        oldEmpresa = empresData.getEmpresa(empId);
        nombreEditText.setText(oldEmpresa.getNombre());
        urlEditText.setText(oldEmpresa.getUrl());
        emailEditText.setText(oldEmpresa.getEmail());
        telEditText.setText("" + oldEmpresa.getTel());
        prodEditText.setText(oldEmpresa.getProd_serv());
        List<String> tiposemp = Arrays.asList(oldEmpresa.getTipo().split(","));

        tipoEdit.set(0, tiposemp.contains(tipos[0]));
        tipoEdit.set(1, tiposemp.contains(tipos[1]));
        tipoEdit.set(2, tiposemp.contains(tipos[2]));
        checkboxCons.setChecked(tipoEdit.get(0));
        checkboxMedida.setChecked(tipoEdit.get(1));
        checkboxFabrica.setChecked(tipoEdit.get(2));
    }


    public void onCheckBoxClicked(View v){
        boolean checked = ((CheckBox)v).isChecked();
        switch (v.getId()){
            case R.id.input_tipo_cons:
                tipoEdit.set(0,checked);
                break;
            case R.id.input_tipo_medida:
                tipoEdit.set(1,checked);
                break;
            case R.id.input_tipo_fab:
                tipoEdit.set(2,checked);
                break;
        }
    }
}
