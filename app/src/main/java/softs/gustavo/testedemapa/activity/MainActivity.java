package softs.gustavo.testedemapa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import softs.gustavo.testedemapa.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);
        getSupportActionBar().hide();
    }
    public void abrirTelaLogin (View view){
        startActivity(new Intent(this, LoginActivity.class));


    }
    public void abrirTelaCadastro (View view){
        startActivity(new Intent(this, CadastroActivity.class));

    }

}
