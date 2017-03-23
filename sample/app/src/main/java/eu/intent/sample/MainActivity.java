package eu.intent.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import eu.intent.sdk.api.ITAccountApi;
import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITApiError;
import eu.intent.sdk.model.ITUser;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private ITAccountApi mAccountApi;
    private ITUser mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);

        Retrofit retrofit = App.INSTANCE.getRetrofit();
        mAccountApi = retrofit.create(ITAccountApi.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            App.INSTANCE.getSessionManager().logout();
            startActivity(new Intent(this, StartupActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMyData() {
        mAccountApi.me().enqueue(new ITApiCallback<ITUser>() {
            @Override
            public void onSuccess(Call<ITUser> call, ITUser user) {
                mUser = user;
                updateTextView();
            }

            @Override
            public void onFailure(Call<ITUser> call, ITApiError apiError) {
                // D'oh! :(
            }
        });
    }

    private void updateTextView() {
        String text = getString(R.string.user_info, mUser.getFullName());
        mTextView.setText(text);
    }
}
