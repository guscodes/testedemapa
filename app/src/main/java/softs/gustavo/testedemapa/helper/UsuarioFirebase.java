package softs.gustavo.testedemapa.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import softs.gustavo.testedemapa.activity.MapsActivity;
import softs.gustavo.testedemapa.activity.RequisicoesActivity;
import softs.gustavo.testedemapa.config.ConfiguracaoFireBase;
import softs.gustavo.testedemapa.model.Usuario;

public class UsuarioFirebase {
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFireBase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();

    }

    public static boolean atualizarNomeUsuario(String nome){
       try{
           FirebaseUser user = getUsuarioAtual();
           UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                   .setDisplayName(nome).build();
           user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                 if(!task.isSuccessful()){
                     Log.d("Perfil", "Erro ao atualizar nome de perfil");
                 }
               }
           });
           return true;

       }catch (Exception e){
        e.printStackTrace();
        return false;
       }
    }
    public static void redirecionaUsuarioLogado(final Activity activity){
        DatabaseReference usuarioRef = ConfiguracaoFireBase.getFirebaseDatabase()
                .child("Usuarios")
                .child(getIndentificadorUsuario());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                String TipoUsuario = usuario.getTipo();
                if (TipoUsuario.equals("M")){
                    Intent i =new Intent(activity, RequisicoesActivity.class);
                    activity.startActivity(i);

                }else{
                    Intent i =new Intent(activity, MapsActivity.class);
                    activity.startActivity(i);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static String getIndentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }
}
