package ru.at.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView resultField;
    EditText numberField;
    TextView operationField;
    Double operand = null;
    String lastOperation = "*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.editTextText);
        operationField = findViewById(R.id.textView5);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if (operand != null) outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand = savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);


    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        numberField.append(button.getText());
        if (lastOperation.equals("=") && operand != null) {
            operand = null;
        }

    }

    private void performOperation(Double number, String op) {
        if (operand != null) {
            if (lastOperation.equals("=")) {
                lastOperation = operand.toString();
            }
            switch (lastOperation) {
                case "=":
                    operand = number;
                    break;
                case "/":
                    if (number == 0) operand = 0.0;
                    else operand /= number;
                    break;
                case "*":
                    operand *= number;
                    break;
                case "+":
                    operand += number;
                    break;
                case "-":
                    operand -= number;
                    break;
            }
        }
        else {

            operand = number;
        }
        resultField.setText(operand.toString().replace(".", ","));
        numberField.setText("");
    }

    public void onOperationClick(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();

        if (!number.isEmpty()) {

            number = number.replace(",", ".");
            try {
                lastOperation = op;
                performOperation(Double.valueOf(number), op);

            } catch (NumberFormatException ex) {
                numberField.setText("");
            }
        }

        operationField.setText(lastOperation);
    }
}