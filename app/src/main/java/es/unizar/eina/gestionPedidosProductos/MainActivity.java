package es.unizar.eina.gestionPedidosProductos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                        openActivityPedidos();
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivityProductos();
            }
        });
    }

    public void openActivityPedidos(){
        Intent i = new Intent(this,ActivityPedidos.class);
        startActivity(i);
    }

    public void openActivityProductos(){
        Intent i = new Intent(this, ActivityProductos.class);
        startActivity(i);
    }
}