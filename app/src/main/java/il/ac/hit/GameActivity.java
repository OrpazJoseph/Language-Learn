package il.ac.hit;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser mCurrentUser;
    List<Card> mCardList;
    Random rand;

    // Animation for Card reveal
    TextView cardFront;
    TextView cardBack;
    EditText etAnswer;
    Button btnCheckAnswer;
    AnimatorSet frontAnimator;
    AnimatorSet backAnimator;
    boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle(R.string.start_game);

        mCardList = new ArrayList<>();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        getListFromDB();

        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        cardFront = findViewById(R.id.card_front);
        cardBack = findViewById(R.id.card_back);
        btnCheckAnswer = findViewById(R.id.check_answer);
        etAnswer = findViewById(R.id.translation);
        cardFront.setCameraDistance(8000 * density);
        cardBack.setCameraDistance(8000 * density);

        frontAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        backAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);
        btnCheckAnswer.setOnClickListener(v -> {
            if(isFront){
                frontAnimator.setTarget(cardFront);
                backAnimator.setTarget(cardBack);
                String answer = etAnswer.getText().toString().trim().toLowerCase();
                String back = cardBack.getText().toString().trim().toLowerCase();
                String message;
                final MediaPlayer mediaPlayer;
                if (answer.equals(back)){
                    message = getString(R.string.correct_answer);
                    // TODO: check if the correct message audio is bugged or not
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                } else {
                    message = getString(R.string.wrong_answer);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                }
                mediaPlayer.start();
                etAnswer.setText("");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
            else{
                frontAnimator.setTarget(cardBack);
                backAnimator.setTarget(cardFront);
            }

            frontAnimator.start();
            backAnimator.start();
            isFront = !isFront;
        });
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

                setRandomCard();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setRandomCard() {
        rand = new Random();
        Card randomCard = mCardList.get(rand.nextInt(mCardList.size()));
        cardFront.setText(randomCard.getWord());
        cardBack.setText(randomCard.getTranslation());
    }

    public void nextCard(View view) {
        // TODO: make it choose another card than the current one
        getListFromDB();
        if (!isFront){
            cardBack.setVisibility(View.INVISIBLE);
            frontAnimator.setTarget(cardBack);
            backAnimator.setTarget(cardFront);
            frontAnimator.start();
            backAnimator.start();
            isFront = !isFront;
            backAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cardBack.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
