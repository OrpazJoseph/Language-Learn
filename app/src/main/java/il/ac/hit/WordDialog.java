package il.ac.hit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class WordDialog extends AppCompatDialogFragment {
    private EditText wordCreator;
    private EditText translationCreator;
    private WordDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Create a Card")
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Create", (dialog, which) -> {
                    String word = wordCreator.getText().toString();
                    String translation = translationCreator.getText().toString();
                    listener.applyWord(word, translation);
                });

        wordCreator = view.findViewById(R.id.enter_word_dialog);
        translationCreator = view.findViewById(R.id.enter_translation_dialog);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (WordDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Error");
        }
    }

    public interface WordDialogListener{
        void applyWord(String word, String translation);
    }
}
