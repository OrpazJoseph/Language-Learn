package il.ac.hit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardAdapter extends FirebaseRecyclerAdapter<Card, CardAdapter.CardViewHolder>{
    public CardAdapter(FirebaseRecyclerOptions<Card> options) {
        super(options);
    }
    FirebaseUser mCurrentUser;
    DatabaseReference myRef;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    private EditText wordCreator;
    private EditText translationCreator;

    @Override
    protected void onBindViewHolder(CardViewHolder cardViewHolder, @SuppressLint("RecyclerView") int i, Card card) {
        cardViewHolder.word.setText(card.getWord());
        cardViewHolder.translation.setText(card.getTranslation());

        cardViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // TODO: dialog show keep crashing the app, need to understand why

                openDialog();
                WordDialog wordDialog = new WordDialog();
                assert wordDialog.getFragmentManager() != null;
                wordDialog.show(wordDialog.getFragmentManager(), "Update Card");

                Map<String, Object> map = new HashMap<>();
                map.put("word", cardViewHolder.word.getText().toString());
                map.put("translation", cardViewHolder.translation.getText().toString());
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                database = FirebaseDatabase.getInstance();
                dbRef = database.getReference().child("users").child(mCurrentUser.getUid()).child("cards")
                        .child(getRef(i).getKey());
                dbRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(cardViewHolder.editButton.getContext(), "Card Updated successfully!", Toast.LENGTH_LONG).show();
                                wordDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(cardViewHolder.editButton.getContext(), "Card Updated successfully!", Toast.LENGTH_LONG).show();

                            }

            });
            }
    });

        cardViewHolder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(cardViewHolder.deleteButton.getContext());
            deleteDialog.setTitle("Are you sure?");
            deleteDialog.setMessage("Deleted Card can't be restored");
            deleteDialog.setPositiveButton("Delete", (dialog, which) -> {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                database = FirebaseDatabase.getInstance();
                dbRef.removeValue();
                Toast.makeText(cardViewHolder.deleteButton.getContext(), "" +
                        "Card is deleted successfully!", Toast.LENGTH_LONG).show();

            });
            deleteDialog.setNegativeButton("Cancel", (dialog, which) ->
                    Toast.makeText(cardViewHolder.deleteButton.getContext(), "Cancelled!", Toast.LENGTH_LONG)
                            .show());
            deleteDialog.show();
        });
    }

    public void openDialog() {



        WordDialog wordDialog = new WordDialog();
        assert wordDialog.getFragmentManager() != null;
        //wordDialog.show(wordDialog.getFragmentManager(), "Update Card");
        wordDialog.show();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new CardAdapter.CardViewHolder(view);
    }

//    @Override
//    public void saveCardToDB(String word, String translation) {
//        Card card = new Card();
//        card.setWord(word);
//        card.setTranslation(translation);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("word", word);
//        map.put("translation", translation);
//        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//        database = FirebaseDatabase.getInstance();
//        dbRef.updateChildren(map).addOnCompleteListener(new OnSuccessListener<Void>(){
//
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText()
//            }
//        })
//        myRef = database.getReference().child("users").child(mCurrentUser.getUid()).child("cards");
//        myRef.push().setValue(card);

//    }

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

