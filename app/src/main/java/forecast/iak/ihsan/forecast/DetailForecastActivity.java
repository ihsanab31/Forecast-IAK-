package forecast.iak.ihsan.forecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailForecastActivity extends AppCompatActivity {
    private TextView tvTemp;
    private TextView tvDt;
    private TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_forecast);

        tvTemp = (TextView)findViewById(R.id.tv_temp);
        tvDt = (TextView)findViewById(R.id.tv_dt);
        tvMain = (TextView)findViewById(R.id.tv_main);

        Intent intent = getIntent();
        tvTemp.setText(intent.getStringExtra("temp"));
        tvDt.setText(intent.getStringExtra("dt"));
        tvMain.setText(intent.getStringExtra("main"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
