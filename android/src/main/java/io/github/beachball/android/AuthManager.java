package io.github.beachball.android;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.github.beachball.AuthInterface;

public class AuthManager implements AuthInterface {
    private final Activity activity;
    private final FirebaseAuth auth;

    public AuthManager(Activity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    Toast.makeText(activity, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Вход выполнен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }
}
