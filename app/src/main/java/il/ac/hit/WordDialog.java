package il.ac.hit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class WordDialog extends AppCompatDialogFragment {
    private EditText wordCreator;
    private EditText translationCreator;
    private WordDialogListener listener;
    AlertDialog.Builder builder;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Create a Card")
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Create", (dialog, which) -> {
                    if (wordCreator.getText().toString().length() == 0) {
                        Toast.makeText(((Dialog) dialog).getContext(), R.string.empty_word, Toast.LENGTH_LONG).show();
                        ((Dialog) dialog).show();
                    } else if (translationCreator.getText().toString().length() == 0) {
                        Toast.makeText(((Dialog) dialog).getContext(), R.string.empty_translation, Toast.LENGTH_LONG).show();
                        ((Dialog) dialog).show();
                    } else {
                        String word = wordCreator.getText().toString();
                        String translation = translationCreator.getText().toString();
                        dialog.dismiss();
                        listener.saveCardToDB(word, translation);
                    }
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
        void saveCardToDB(String word, String translation);
    }
}
