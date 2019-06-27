package softs.gustavo.testedemapa.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFireBase {
    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;

    // retonra a instancia do data base
    public static DatabaseReference getFirebaseDatabase(){
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
    public  static FirebaseAuth getFirebaseAutenticacao(){
        if (firebaseAuth == null){
            firebaseAuth = firebaseAuth.getInstance();
        }
        return firebaseAuth;

    }
}
