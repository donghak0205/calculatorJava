package fastcampus.aop.part2.chapter04_java;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import fastcampus.aop.part2.chapter04_java.model.History;

public class MainActivity extends AppCompatActivity {

    private TextView expressionTextView;
    private TextView resultTextView;
    private View historyLayout;
    private LinearLayout historyLinearLayout;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expressionTextView = findViewById(R.id.expressionTextView);
        resultTextView = findViewById(R.id.resultTextView);
        historyLayout = findViewById(R.id.historyLayout);
        historyLinearLayout = findViewById(R.id.historyLinearLayout);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "history"
        ).build();

    }

    public void buttonClicked(View v) {
        switch (v.getId()) {
            case R.id.button0:
                numberButtonClicked("0");
                break;
            case R.id.button1:
                numberButtonClicked("1");
                break;
            case R.id.button2:
                numberButtonClicked("2");
                break;
            case R.id.button3:
                numberButtonClicked("3");
                break;
            case R.id.button4:
                numberButtonClicked("4");
                break;
            case R.id.button5:
                numberButtonClicked("5");
                break;
            case R.id.button6:
                numberButtonClicked("6");
                break;
            case R.id.button7:
                numberButtonClicked("7");
                break;
            case R.id.button8:
                numberButtonClicked("8");
                break;
            case R.id.button9:
                numberButtonClicked("9");
                break;
            case R.id.moduleButton:
                operateButtonClicked("%");
                break;
            case R.id.dividerButton:
                operateButtonClicked("/");
                break;
            case R.id.multiButton:
                operateButtonClicked("*");
                break;
            case R.id.minusButton:
                operateButtonClicked("-");
                break;
            case R.id.plusButton:
                operateButtonClicked("+");
                break;
        }
    }

    private Boolean isOperator = false;   //연산 ing
    private Boolean hasOperator = false;  //연산이 끝난 상황

    public void numberButtonClicked(String number) {
        //TODO
        //1. 맨앞자리가 0인경우 return
        //2. 15자리 이상인 경우 return
        //3. 사칙연산자가 추가 되었을 경우(isOperator) - 띄어쓰기를 해준다.

        if (isOperator) { //연산중이라면
            expressionTextView.append(" ");
        }

        isOperator = false;

        String[] expressionText = expressionTextView.getText().toString().split(" ");
        if (expressionText[expressionText.length - 1].isEmpty() && number == "0") {
            return;
        } else if (!expressionText[expressionText.length - 1].isEmpty() && expressionText[expressionText.length - 1].length() > 15) {
            return;
        }

        expressionTextView.append(number);
        resultTextView.setText(calculateExpression());
    }

    //계산하기
    private String calculateExpression() {
        String[] expressionText = expressionTextView.getText().toString().split(" ");
        //Operator가 없는 상황이라면
        if (!hasOperator && expressionText.length != 3) {
            return "";
        }
        BigInteger num1 = new BigInteger(expressionText[0]);
        String op = expressionText[1];
        BigInteger num2 = new BigInteger(expressionText[2]);

        switch (op) {
            case "%":
                //return Integer.toString(num1 % num2);
                return num1.mod(num2).toString();
            case "/":
                return num1.divide(num2).toString();
            case "*":
                return num1.multiply(num2).toString();
            case "+":
                return num1.add(num2).toString();
            case "-":
                return num1.subtract(num2).toString();
            default:
                return "";
        }
    }

    private void operateButtonClicked(String op) {
        String[] expressionText = expressionTextView.getText().toString().split(" ");

        if (isOperator) {
            expressionTextView.setText(expressionText[0] + " " + op);
        } else if (hasOperator) {
            return;
        } else if (expressionTextView.getText().length() == 0) {
            return;
        } else {
            expressionTextView.append(" " + op);
        }

        isOperator = true;
        hasOperator = true;
    }


    public void resultButtonClicked(View v) {

        String expressionText = expressionTextView.getText().toString();
        String resultText = resultTextView.getText().toString();

        History history = new History();
        history.expression = expressionText;
        history.result = resultText;

        new Thread(new Runnable() {

            @Override
            public void run() {
                if (history != null) {
                    db.historyDao().insertHistory(history);
                }
            }
        }).start();

        expressionTextView.setText(resultText);
        resultTextView.setText("");

        isOperator = false;
        hasOperator = false;

    }

    public void clearButtonClicked(View v) {
        expressionTextView.setText("");
        resultTextView.setText("");

        isOperator = false;
        hasOperator = false;
    }

    public void historyButtonClicked(View v) {

        historyLayout.setVisibility(View.VISIBLE);
        historyLinearLayout.removeAllViews();

        new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                List<History> historyList = db.historyDao().getAll();
                Collections.reverse(historyList);
                for (History history : historyList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            View historyView = LayoutInflater.from(historyLayout.getContext()).inflate(R.layout.history_row, null, false);
                            historyView.<TextView>findViewById(R.id.expressionTextView).setText(history.expression);
                            historyView.<TextView>findViewById(R.id.resultTextView).setText("= " + history.result);

                            historyLinearLayout.addView(historyView);

                        }
                    });
                }

            }
        }).start();

    }

    public void historyClearButtonClicked(View v) {
        historyLinearLayout.removeAllViews();
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.historyDao().deleteAll();
            }
        }).start();
    }

    public void historyLayoutClicked(View v) {
        historyLayout.setVisibility(View.INVISIBLE);
    }
}