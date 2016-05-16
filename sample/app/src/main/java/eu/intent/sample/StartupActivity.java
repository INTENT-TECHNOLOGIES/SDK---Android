package eu.intent.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import eu.intent.sdk.auth.ITAuthClient;
import eu.intent.sdk.auth.ITAuthRequest;

public class StartupActivity extends AppCompatActivity {
    private static final int REQUEST_AUTH = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Declare the scopes you need in your app
        ITAuthRequest authRequest = new ITAuthRequest.Builder(this).addScopes("read:me", "read:datahub:parts").build();
        ITAuthClient.openLoginActivity(this, REQUEST_AUTH, authRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_AUTH && resultCode == RESULT_OK) {
            // Authentication succeeded
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
