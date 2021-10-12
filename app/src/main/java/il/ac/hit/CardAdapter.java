package il.ac.hit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardAdapter extends FirebaseRecyclerAdapter<Card, CardAdapter.CardViewHolder> {
    public CardAdapter(FirebaseRecyclerOptions<Card> options) {
        super(options);
    }
    FirebaseUser mCurrentUser;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    EditText wordCreator;
    EditText translationCreator;
    View getTextHelper;
    AlertDialog.Builder builder;

    @SuppressLint("InflateParams")
    @Override
    protected void onBindViewHolder(CardViewHolder cardViewHolder, int i, Card card) {
        cardViewHolder.word.setText(card.getWord());
        cardViewHolder.translation.setText(card.getTranslation());
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference().child("users").child(mCurrentUser.getUid()).child("cards")
        .child(Objects.requireNonNull(getRef(i).getKey()));

        cardViewHolder.editButton.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(v.getContext());

            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getTextHelper = inflater.inflate(R.layout.layout_dialog, null);

            builder.setView(getTextHelper).setTitle("Create a Card")
                    .setNegativeButton("Cancel", (dialog, which) -> {})
                    .setPositiveButton("Update", (dialog, which) -> {
                        if (wordCreator.getText().toString().length() == 0) {
                            Toast.makeText(((Dialog) dialog).getContext(), R.string.empty_word, Toast.LENGTH_LONG).show();
                        } else if (translationCreator.getText().toString().length() == 0) {
                            Toast.makeText(((Dialog) dialog).getContext(), R.string.empty_translation, Toast.LENGTH_LONG).show();
                        }
                        else {
                            String word = wordCreator.getText().toString();
                            String translation = translationCreator.getText().toString();

                            Map<String, Object> map = new HashMap<>();
                            map.put("word", word);
                            map.put("translation", translation);
                            database.getReference().child("users").child(mCurrentUser.getUid()).child("cards")
                                    .child(Objects.requireNonNull(getRef(i).getKey()))
                                    .updateChildren(map)
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(cardViewHolder.editButton.getContext(), "Card Updated successfully!", Toast.LENGTH_LONG).show())
                                    .addOnFailureListener(e -> Toast.makeText(cardViewHolder.editButton.getContext(), "Card Updated successfully!", Toast.LENGTH_LONG).show());
                            dialog.dismiss();
                        }
                    });
            wordCreator = getTextHelper.findViewById(R.id.enter_word_dialog);
            translationCreator = getTextHelper.findViewById(R.id.enter_translation_dialog);

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        cardViewHolder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(cardViewHolder.deleteButton.getContext());
            deleteDialog.setTitle("Are you sure?");
            deleteDialog.setMessage("Deleted Card can't be restored");
            deleteDialog.setPositiveButton("Delete", (dialog, which) -> {
                dbRef = database.getReference().child("users").child(mCurrentUser.getUid()).child("cards")
                        .child(Objects.requireNonNull(getRef(i).getKey()));

                dbRef.removeValue();
                Toast.makeText(cardViewHolder.deleteButton.getContext(), "" +
                        "Card is deleted successfully!", Toast.LENGTH_LONG).show();

            })
                    .setNegativeButton("Cancel", (dialog, which) ->
                    Toast.makeText(cardViewHolder.deleteButton.getContext(), "Cancelled!", Toast.LENGTH_LONG)
                            .show());
            deleteDialog.show();

            // TODO: fix deletion of cards. maybe update/refresh/reload the activity is the solution
            //notifyItemChanged(cardViewHolder.getAdapterPosition());
            //notifyDataSetChanged();
        });

    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new CardAdapter.CardViewHolder(view);
    }


    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView word, translation;
        ImageButton deleteButton;
        ImageButton editButton;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.tvWord);
            translation = itemView.findViewById(R.id.tvTranslation);
            deleteButton = itemView.findViewById(R.id.delete_card);
            editButton = itemView.findViewById(R.id.edit_card);
        }
     }
}

