package fr.acinq.eclair.swordfish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.acinq.eclair.swordfish.App;
import fr.acinq.eclair.swordfish.EclairHelper;
import fr.acinq.eclair.swordfish.R;
import fr.acinq.eclair.swordfish.tasks.StartupTask;

public class LauncherActivity extends AppCompatActivity implements StartupTask.AsyncSetupResponse {

  public static final String EXTRA_AUTOSTART = "fr.acinq.eclair.swordfish.EXTRA_AUTOSTART";

  private Button mRestartButton;
  private TextView mSubtitleTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);

    mRestartButton = (Button) findViewById(R.id.launcher_restart);
    mSubtitleTextView = (TextView) findViewById(R.id.launcher_subtitle);

    Intent intent = getIntent();
    if (intent.getBooleanExtra(EXTRA_AUTOSTART, true)) {
      initEclair();
    } else {
      showRestart();
    }
  }

  public void launcher_restart(View view) {
    initEclair();
  }

  private void showRestart() {
    mSubtitleTextView.setText("Eclair could not be started.");
    mRestartButton.setVisibility(View.VISIBLE);
  }

  private void initEclair() {
    mRestartButton.setVisibility(View.GONE);
    mSubtitleTextView.setText("Please Wait...");
    new StartupTask(this, getApplicationContext()).execute();
  }

  @Override
  public void processFinish(EclairHelper instance) {
    if (instance != null) {
      ((App) getApplication()).setEclairInstance(instance);
      Intent intent = new Intent(this, HomeActivity.class);
      startActivity(intent);
      finish();
    } else {
      showRestart();
    }
  }
}

