package co.edu.unal.mapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private static final String EXTRA_R = "co.edu.unal.radius";
    private ImageButton btn_init;
    private EditText input_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_init=findViewById(R.id.btn_map);
        input_r=findViewById(R.id.rinput);
        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer radius=Integer.valueOf(input_r.getText().toString());
                Log.e("MAPI", "radius input: " + radius);
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra(EXTRA_R, radius);
                startActivity(intent);
            }
        });
    }
}
