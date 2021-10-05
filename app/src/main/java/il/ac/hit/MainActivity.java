package il.ac.hit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
//    private EditText userName;
//    static final String INPUT_DATA_KEY = "MainActivity.INPUT_DATA_KEY";
    private static final int SIGN_IN_CREATE = 1;
    private static final int SIGN_IN_SIGNOUT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        userName = findViewById(R.id.inputUserName);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent signUp = AuthUI.getInstance().createSignInIntentBuilder().build();
            startActivityForResult(signUp, SIGN_IN_CREATE);
        } else {
            showDetails(true);
        }
    }
//
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("message");
//
//    myRef.setValue("Hello, World!");
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
        if (requestCode == SIGN_IN_SIGNOUT){
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
        }
        else{
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_LONG).show();
            }
    }
    private void afterSignOut(){
        Intent signUp = AuthUI.getInstance().createSignInIntentBuilder().build();
        startActivityForResult(signUp, SIGN_IN_SIGNOUT);

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
        //String userNameInput = userName.getText().toString();
        //game.putExtra(INPUT_DATA_KEY, userNameInput);
        startActivity(game);
    }

    public void openScoreActivity(View view) {
        Intent scoreTable = new Intent(this, ScoreActivity.class);
        startActivity(scoreTable);
    }
}