package io.github.beachball.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import io.github.beachball.Main;

public class AndroidLauncher extends AndroidApplication {
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authManager = new AuthManager(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Main(authManager), config);
    }

    public void setGameScreen() {
        ((Main) getApplicationListener()).setToGameScreen(); // метод, который переключает экран в core
    }
}
