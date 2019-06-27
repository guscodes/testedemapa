package softs.gustavo.testedemapa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import org.w3c.dom.Text;

import softs.gustavo.testedemapa.R;
import softs.gustavo.testedemapa.config.ConfiguracaoFireBase;
import softs.gustavo.testedemapa.helper.UsuarioFirebase;
import softs.gustavo.testedemapa.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoNome, campoEmail, campoSenha;
    private Switch SwitchTipoUsuario;
    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //inicia componente
        campoNome  = findViewById(R.id.txtNomeCompleto);
        campoEmail = findViewById(R.id.txtEmailCadastro);
        campoSenha = findViewById(R.id.txtPasswordCadastro);
        SwitchTipoUsuario = findViewById(R.id.switch1);

    }
    public void ValidarCadastroUsuario(View view){
        // recupera texto
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        // verifica se esta vazio
        if(!textoNome.isEmpty()){
            if(!textoEmail.isEmpty()){
                if(!textoSenha.isEmpty()){
                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    usuario.setTipo( verificarTipoUsuario ());

                    CadastrarUsuario(usuario);
                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha a senha", Toast.LENGTH_SHORT);
                }

            }else{
                Toast.makeText(CadastroActivity.this, "Preencha o Email", Toast.LENGTH_SHORT);
            }
        }else {

            Toast.makeText(CadastroActivity.this, "Preencha o nome", Toast.LENGTH_SHORT);

        }


    }
    public void CadastrarUsuario(final Usuario usuario){
     autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
     autenticacao.createUserWithEmailAndPassword(
             usuario.getEmail(),
             usuario.getSenha()
     ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if (task.isSuccessful()){
                 try {
                     String idUsuario = task.getResult().getUser().getUid();
                     usuario.setId(idUsuario);
                     usuario.salvar();
                     // atualiar nome de perfil
                     UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                     if (verificarTipoUsuario() == "P") {
                         startActivity(new Intent(CadastroActivity.this, MapsActivity.class));
                         finish();
                         Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar Passageiro!", Toast.LENGTH_SHORT).show();

                     } else {
                         startActivity(new Intent(CadastroActivity.this, RequisicoesActivity.class));
                         finish();
                         Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar Motorista", Toast.LENGTH_SHORT).show();
                     }
                 }catch (Exception e){
                     e.printStackTrace();

                 }
             }else{
                 String excecao = "";
                 try{
                     throw task.getException();
                 } catch (FirebaseAuthWeakPasswordException e){
                     excecao = "Digite uma senha mais forte!";

                 }catch (FirebaseAuthInvalidCredentialsException e){
                     excecao = "Por favor, digite um e-mail valido!";
                 }catch (FirebaseAuthUserCollisionException e){
                     excecao = "Esta conta ja foi cadastrada";
                 } catch (Exception e){
                     excecao = "Erro ao cadastrar usuario" + e.getMessage();
                     e.printStackTrace();
                     Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();

                 }
             }
         }
     });

    }
    public String verificarTipoUsuario(){
        return SwitchTipoUsuario.isChecked() ? "M": "P"; // operador tenario;

    }
}
