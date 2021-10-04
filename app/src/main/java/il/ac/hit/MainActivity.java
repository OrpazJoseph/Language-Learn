package il.ac.hit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText userName;
    static final String INPUT_DATA_KEY = "MainActivity.INPUT_DATA_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.inputUserName);
    }

    public void openGameActivity(View view) {
        Intent game = new Intent(this, GameActivity.class);
        String userNameInput = userName.getText().toString();
        game.putExtra(INPUT_DATA_KEY, userNameInput);
        startActivity(game);
    }

    public void openScoreActivity(View view) {
        Intent scoreTable = new Intent(this, ScoreActivity.class);
        startActivity(scoreTable);
    }
}