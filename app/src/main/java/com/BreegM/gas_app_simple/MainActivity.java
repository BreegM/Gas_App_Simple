package com.BreegM.gas_app_simple;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(v -> {
            Toast.makeText(this, "Кнопка працює!", Toast.LENGTH_SHORT).show();
        });
    }
}