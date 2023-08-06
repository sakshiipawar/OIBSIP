package com.example.todotask;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.todotask.Utils.DataBasedHelper;
import com.example.todotask.model.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddnewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddnewTask";
    private EditText mEdittext;
    private Button mSaveButton;

    private DataBasedHelper myDb;

    public static AddnewTask newInstance() {
        return new AddnewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEdittext = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);

        myDb = new DataBasedHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            if (task != null) {
                mEdittext.setText(task);

                if (task.length() > 0) {
                    mSaveButton.setEnabled(false);
                }
            }
        }
        mEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(android.R.color.darker_gray);
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEdittext.getText().toString();
                if (finalIsUpdate && bundle != null) {
                    myDb.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDb.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentActivity activity = getActivity();
        if (activity instanceof OnDialogCloseListner) {
            ((OnDialogCloseListner) activity).OnDialogClose(dialog);
        }
    }
}