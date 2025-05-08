package com.example.bms_finalexer2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText contentEditText;
    private TextView dateTextView;
    private EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentEditText = findViewById(R.id.content_edittext);
        dateTextView = findViewById(R.id.date_textview);
        titleEditText = findViewById(R.id.title_edittext);

        updateDateDisplay();

        ImageButton saveButton = findViewById(R.id.save_button);
        ImageButton loadButton = findViewById(R.id.load_button);
        ImageButton newButton = findViewById(R.id.new_button);
        ImageButton deleteButton = findViewById(R.id.delete_button);

        saveButton.setOnClickListener(v -> saveEntry());
        loadButton.setOnClickListener(v -> loadEntry());
        newButton.setOnClickListener(v -> newEntry());
        deleteButton.setOnClickListener(v -> deleteEntry());

        newButton.setOnLongClickListener(v -> {
            Toast.makeText(this, "Create a new diary entry", Toast.LENGTH_SHORT).show();
            return true;
        });
        saveButton.setOnLongClickListener(v -> {
            Toast.makeText(this, "Save this entry", Toast.LENGTH_SHORT).show();
            return true;
        });
        loadButton.setOnLongClickListener(v -> {
            Toast.makeText(this, "Load a saved entry", Toast.LENGTH_SHORT).show();
            return true;
        });
        deleteButton.setOnLongClickListener(v -> {
            Toast.makeText(this, "Delete this entry", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        dateTextView.setText(currentDate);
    }

    private void saveEntry() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString();

        if (title.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            title = "entry_" + dateFormat.format(new Date());
        }

        if (!title.endsWith(".txt")) {
            title += ".txt";
        }

        try {
            FileOutputStream fos = openFileOutput(title, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();

            Toast.makeText(this, "Entry saved as: " + title, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving entry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadEntry() {
        String title = titleEditText.getText().toString().trim();

        if (!title.endsWith(".txt")) {
            title += ".txt";
        }

        try {
            File file = new File(getFilesDir(), title);
            if (!file.exists()) {
                Toast.makeText(this, "Entry not found: " + title, Toast.LENGTH_SHORT).show();
                return;
            }

            FileInputStream fis = openFileInput(title);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            fis.close();

            contentEditText.setText(content.toString());
            Toast.makeText(this, "Entry loaded: " + title, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading entry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void newEntry() {
        contentEditText.setText("");
        titleEditText.setText("");
        updateDateDisplay();
        Toast.makeText(this, "New entry created", Toast.LENGTH_SHORT).show();
    }

    private void deleteEntry() {
        String title = titleEditText.getText().toString().trim();

        if (!title.endsWith(".txt")) {
            title += ".txt";
        }

        try {
            File file = new File(getFilesDir(), title);
            if (!file.exists()) {
                Toast.makeText(this, "Entry not found: " + title, Toast.LENGTH_SHORT).show();
                return;
            }

            if (file.delete()) {
                Toast.makeText(this, "Entry deleted: " + title, Toast.LENGTH_SHORT).show();
                newEntry();
            } else {
                Toast.makeText(this, "Failed to delete entry", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error deleting entry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
