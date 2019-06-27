package softs.gustavo.testedemapa.activity;

import android.app.usage.UsageEvents;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.w3c.dom.Text;

import softs.gustavo.testedemapa.R;
import softs.gustavo.testedemapa.config.ConfiguracaoFireBase;
import softs.gustavo.testedemapa.helper.UsuarioFirebase;
import softs.gustavo.testedemapa.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button btnLogar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // inicializar componentes
        campoEmail = findViewById(R.id.txtLoginEmail);
        campoSenha = findViewById(R.id.txtLoginPassword);
        btnLogar = findViewById(R.id.button_logar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrarLoginUsuario();
            }
        });

    }

    private void entrarLoginUsuario() {
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);

                LogarUsuario(usuario);

            } else {
                Toast.makeText(LoginActivity.this, "Preencha a Senha", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(LoginActivity.this, "Preencha o Email", Toast.LENGTH_SHORT).show();
        }

    }

    public void LogarUsuario(Usuario usuario) {
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UsuarioFirebase.redirecionaUsuarioLogado(LoginActivity.this);
                } else {
                    String execao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        execao = "Usuario nao cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        execao = "E-mail nao corresponde a um usuario";
                    } catch (Exception e) {
                        execao = "Erro ao cadastrar usuario:" + e.getMessage();
                        e.printStackTrace();
                        ;
                    }
                    Toast.makeText(LoginActivity.this, execao, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
