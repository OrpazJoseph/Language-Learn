package il.ac.hit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //userName = findViewById(R.id.input);
        setContentView(R.layout.activity_game);
        setTitle(R.string.start_game);
        Intent intent = getIntent();
        String userName = intent.getStringExtra(MainActivity.INPUT_DATA_KEY);
        textView = findViewById(R.id.inputUserName);
        textView.setText(userName);
    }
}
