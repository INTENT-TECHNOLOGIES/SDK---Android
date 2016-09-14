package eu.intent.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.auth.ITAuthClient;
import eu.intent.sdk.model.ITPart;
import eu.intent.sdk.model.ITUser;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private ITUserApi mUserApi;
    private ITPartApi mPartApi;
    private ITUser mUser;
    private ITPart mPart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mUserApi = new ITUserApi(this);
        mPartApi = new ITPartApi(this);
        loadMyData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ITAuthClient.logout(this);
            startActivity(new Intent(this, StartupActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMyData() {
        mUserApi.getCurrentUser(new ITApiCallback<ITUser>() {
            @Override
            public void onSuccess(ITUser result) {
                mUser = result;
                updateTextView();
            }

            @Override
            public void onFailure(int httpCode, String message) {
                // D'oh! :(
            }
        });

        mPartApi.getMyPart(new ITApiCallback<ITPart>() {
            @Override
            public void onSuccess(ITPart result) {
                mPart = result;
                updateTextView();
            }

            @Override
            public void onFailure(int httpCode, String message) {
                // D'oh! :(
            }
        });
    }

    private void updateTextView() {
        String text = mUser.getFullName() + "\n" + mPart.address.city + " " + mPart.address.country;
        mTextView.setText(text);
    }
}
