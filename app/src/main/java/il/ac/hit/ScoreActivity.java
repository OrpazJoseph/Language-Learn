package il.ac.hit;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScoreActivity extends AppCompatActivity {

    private DatabaseReference usersRef;
    private ScoreAdapter scoreAdapter;
    private List<Score> arrayList;
    private ListView scoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        setTitle(R.string.score_table);
        scoreTable = findViewById(R.id.score_table);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference().child("users");
        arrayList = new ArrayList<>();


        getScoreListFromDB();






//        List<String> scoreList = new ArrayList<>();
//        scoreList.add("Moshe");
//        scoreList.add("Ron");
//        scoreList.add("Tomer");
//
//
//        ArrayAdapter<String> arrayAdapter;
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreList);
//        scoreTable.setAdapter(arrayAdapter);
    }

    private void getScoreListFromDB(){
        Query topScore = usersRef.orderByChild("score");
        topScore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot scoreSnap : snapshot.getChildren()){
                    Map<String, Object> snapValue = (HashMap<String, Object>) scoreSnap.getValue();
                    String name = (String) Objects.requireNonNull(snapValue).get("name");
                    long score = (long)snapValue.get("score");
                    arrayList.add(new Score(name, score));
                }

                Collections.reverse(arrayList);
                scoreAdapter = new ScoreAdapter(getApplicationContext(), arrayList);
                scoreTable.setAdapter(scoreAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}