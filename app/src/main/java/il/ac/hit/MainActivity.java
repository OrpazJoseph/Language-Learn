package il.ac.hit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WordDialog.WordDialogListener{
    private static final int SIGN_IN_CREATE = 1;
    private static final int SIGN_IN_SIGN_OUT = 2;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser mCurrentUser;

    public enum languageChoosing{
        Hebrew,
        English
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_main);
        Button changeLanguage = findViewById(R.id.change_language);
        changeLanguage.setOnClickListener(v -> showChangeLanguagueDialog());
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent signUp = AuthUI.getInstance().createSignInIntentBuilder().build();
            startActivityForResult(signUp, SIGN_IN_CREATE);
        } else {
            showDetails(true);
        }
    }

    private void showChangeLanguagueDialog() {
        final String[] languageItems =  {"English", "עברית"};
        AlertDialog.Builder languageDialog = new AlertDialog.Builder(this);
        languageDialog.setTitle("Please Choose Language");
        languageDialog.setSingleChoiceItems(languageItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == languageChoosing.Hebrew.ordinal()){
                    setLocale("en");
                    recreate();
                }
                if(which == languageChoosing.English.ordinal()){
                    setLocale("he");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog myDialog = languageDialog.create();
        myDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources()
                .updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadlocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CREATE){
            if (resultCode == RESULT_OK){
                showDetails(true);
            }
            else{
                showDetails(false);
                finish();
            }
        }
        if (requestCode == SIGN_IN_SIGN_OUT){
            // TODO: Check why crashing after re-sign-in
            if (resultCode == RESULT_OK){
                showDetails(true);
            }
            else{
                showDetails(false);
                finish();
            }
        }

    }

    private void showDetails(boolean flag){
        if (flag) {
            String userDetails = "Hello, your name is " +
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            Toast.makeText(this, userDetails, Toast.LENGTH_LONG).show();
            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            database = FirebaseDatabase.getInstance();
        }
        else{
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_LONG).show();
            }
    }

    private void afterSignOut(){
        Intent signUp = AuthUI.getInstance().createSignInIntentBuilder().build();
        startActivityForResult(signUp, SIGN_IN_SIGN_OUT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out_item){
            signOut();
        }
        return true;
    }

    public void signOut(){
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(task ->
                {
                    Toast.makeText(this, "Sign Out Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    afterSignOut();
                });
    }

    public void openGameActivity(View view) {
        Intent game = new Intent(this, GameActivity.class);
        startActivity(game);
    }

    public void openScoreActivity(View view) {
        Intent scoreTable = new Intent(this, ScoreActivity.class);
        startActivity(scoreTable);
    }

    public void createACard(View view) {
        openDialog();
    }

    public void openDialog() {
        WordDialog wordDialog = new WordDialog();
        wordDialog.show(getSupportFragmentManager(), "Create a Card");
    }

    @Override
    public void saveCardToDB(String word, String translation) {
        Card card = new Card();
        card.setWord(word);
        card.setTranslation(translation);
        myRef = database.getReference().child("users").child(mCurrentUser.getUid()).child("cards");
        myRef.push().setValue(card);
    }
}