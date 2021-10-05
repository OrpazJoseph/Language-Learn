package il.ac.hit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateCardActivity extends AppCompatActivity {
    private EditText wordEditText;
    private EditText translateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creator);
        setTitle(R.string.create_card);
    }

    public void enterWord(View view) {
        wordEditText = findViewById(R.id.EnterWord);
        wordEditText.setVisibility(View.VISIBLE);
        String word = wordEditText.getText().toString();
        translateEditText = findViewById(R.id.EnterTranslation);
        translateEditText.setVisibility(View.VISIBLE);
        String translate = translateEditText.getText().toString();


    }
}
