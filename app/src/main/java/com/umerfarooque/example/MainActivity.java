package com.umerfarooque.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.umerfarooque.floatinghinteditlayout.FloatingHintEditLayout;

public class MainActivity extends AppCompatActivity {
    Button okButton;
    private FloatingHintEditLayout fhet;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fhet = findViewById(R.id.fhel);
        editText = findViewById(R.id.test_et);
        okButton = findViewById(R.id.ok_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                editText.clearFocus();
            }
        });
    }

    private void validate() {
        if (editText.getText().toString().isEmpty()) {
            fhet.setErrorText("Empty field");
            fhet.showError();
        } else if (editText.getText().toString().equals("1")) {
            fhet.setErrorText("1 not allowed");
            fhet.showError();
        }
    }
}
