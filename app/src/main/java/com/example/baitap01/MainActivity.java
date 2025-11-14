package com.example.baitap01;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtNumbers, edtText;
    Button btnArray, btnReverse;
    TextView txtResultArray, txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bai 3 (An thanh tieu de)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        edtNumbers = findViewById(R.id.edtNumbers);
        btnArray = findViewById(R.id.btnArray);
        edtText = findViewById(R.id.edtText);
        btnReverse = findViewById(R.id.btnReverse);
        txtResultArray = findViewById(R.id.txtResultArray);
        txtResult = findViewById(R.id.txtResult);

        // Bai 4 (So chan le)
        btnArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edtNumbers.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập số!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] parts = input.split(",");
                ArrayList<Integer> soChan = new ArrayList<>();
                ArrayList<Integer> soLe = new ArrayList<>();

                for (String s : parts) {
                    try {
                        int n = Integer.parseInt(s.trim());
                        if (n % 2 == 0) {
                            soChan.add(n);
                        } else {
                            soLe.add(n);
                        }
                    } catch (NumberFormatException e) {
                        Log.e("Error", "Không thể chuyển: " + s);
                    }
                }

                // Hien thi Logcat
                Log.d("Even", "Số chẵn: " + soChan);
                Log.d("Odd", "Số lẻ: " + soLe);

                // Hien thi ra Textview
                String kq = "Số lẻ: " + soLe.toString().replace("[", "").replace("]", "") +
                        "\nSố chẵn: " + soChan.toString().replace("[", "").replace("]", "");
                txtResultArray.setText(kq);
            }
        });


        // Bai 5 (Dao nguoc chuoi)
        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edtText.getText().toString().trim();
                if (s.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập chuỗi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] words = s.split(" ");
                StringBuilder reversed = new StringBuilder();
                for (int i = words.length - 1; i >= 0; i--) {
                    reversed.append(words[i].toUpperCase());
                    if (i > 0) reversed.append(" ");
                }

                txtResult.setText(reversed.toString());
                Toast.makeText(MainActivity.this, reversed.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
