package il.ac.hit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.firebase.ui.auth.ui.phone.CountryListSpinner;

import java.nio.charset.StandardCharsets;

public class CreateCardActivity extends AppCompatActivity implements WordDialog.WordDialogListener {
    private Button button;
    private TextView wordEditText;
    private TextView translateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creator);
        setTitle(R.string.create_card);
        wordEditText = findViewById(R.id.word_textView);
        translateEditText = findViewById(R.id.translation_textView);
        button = findViewById(R.id.enter_word);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        WordDialog wordDialog = new WordDialog();
        wordDialog.show(getSupportFragmentManager(), "Create a Card");
    }

    @Override
    public void applyWord(String word, String translation) {
        // TODO: use the text entered in the dialog to open cards

        wordEditText.setText(word);
        translateEditText.setText(translation);
    }
}
