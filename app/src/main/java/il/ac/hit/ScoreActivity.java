package il.ac.hit;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        setTitle(R.string.score_table);
        ListView scoreTable = (ListView) findViewById(R.id.score_table);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Moshe");
        arrayList.add("Ron");
        arrayList.add("Tomer");


        ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        scoreTable.setAdapter(arrayAdapter);
    }
}