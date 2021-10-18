package il.ac.hit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {

    public ScoreAdapter(Context context, List<Score> scoreList){
        super(context, R.layout.score, scoreList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Score score = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvNameItem);
        TextView tvScore = convertView.findViewById(R.id.tvScoreItem);

        tvName.setText(score.getName());
        tvScore.setText(String.valueOf(score.getScore()));

        return convertView;
    }

}
