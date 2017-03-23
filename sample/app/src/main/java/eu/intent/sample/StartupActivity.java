package eu.intent.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import eu.intent.sdk.auth.ITAuthHelper;

public class StartupActivity extends AppCompatActivity {
    private static final int REQUEST_AUTH = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ITAuthHelper.INSTANCE.openLoginActivity(this, App.INSTANCE.getApiClient(), REQUEST_AUTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_AUTH && resultCode == RESULT_OK) {
            // Authentication succeeded
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
