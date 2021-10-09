package il.ac.hit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser mCurrentUser;
    List<Card> mCardList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle(R.string.start_game);

        mCardList = new ArrayList<>();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        getListFromDB();
    }

    private void getListFromDB(){
        myRef = database.getReference().child("users").child(mCurrentUser.getUid()).child("cards");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCardList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    mCardList.add(ds.getValue(Card.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
