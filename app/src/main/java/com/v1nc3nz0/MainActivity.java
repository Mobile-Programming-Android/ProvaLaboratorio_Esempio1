package com.v1nc3nz0;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private Button resetButton;
    private TableLayout gamefield;
    private TextView counter;

    private int[][] numbers;
    private boolean win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        win = false;

        counter = findViewById(R.id.counter);
        gamefield = findViewById(R.id.gametable);
        resetButton = findViewById(R.id.reset);
        resetButton.setBackground(getDrawable(R.drawable.resetbutton));
        resetButton.setOnClickListener((event) -> { resetGame(); });
        resetGame();
    }

    public void buildField()
    {
        if(gamefield.getChildCount() != 0) gamefield.removeAllViews();
        TableRow row;
        for(int x = 0;x < 5;x++)
        {
            row = new TableRow(this);
            row.setPadding(0,0,0,50);
            row.setGravity(Gravity.CENTER);
            for(int y = 0;y < 5;y++)
            {
                if(x == 0)
                {
                    if(y == 0 || y == 4) continue;
                    row.addView(getArrowButton("up",y));
                }
                else if(x == 4)
                {
                    if(y == 0 || y == 4) continue;
                    row.addView(getArrowButton("down",y));
                }
                else if(y == 0)
                {
                    if(x == 0 || x == 4) continue;
                    row.addView(getArrowButton("left",x));
                }
                else if(y == 4)
                {
                    if(x == 0 || x == 4) continue;
                    row.addView(getArrowButton("right",x));
                }
                else row.addView(getTextNumber(x-1,y-1));
            }
            gamefield.addView(row);
        }

        win = checkWin();
        if(win) showToast();
    }

    public boolean checkWin()
    {
        int c = 1;
        for(int x = 0;x < 3;x++)
        {
            for(int y = 0;y < 3;y++)
            {
                if(numbers[x][y] != c) return false;
                c++;
            }
        }
        return true;
    }

    public Button getArrowButton(String type,int pos)
    {
        Button button = new Button(gamefield.getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(175,175);

        button.setGravity(Gravity.CENTER);
        switch(type)
        {
            case "up":
                if(pos == 1) params.setMargins(0,0,10,0);
                if(pos == 2) params.setMargins(10,0,10,0);
                if(pos == 3) params.setMargins(10,0,0,0);
                button.setBackground(getDrawable(R.drawable.arrow_top));
                break;
            case "down":
                if(pos == 1) params.setMargins(0,0,10,0);
                if(pos == 2) params.setMargins(10,0,10,0);
                if(pos == 3) params.setMargins(10,0,0,0);
                button.setBackground(getDrawable(R.drawable.arrow_down));
                break;
            case "left":
                params.setMargins(0,0,10,0);
                button.setBackground(getDrawable(R.drawable.arrow_left));
                break;
            case "right":
                params.setMargins(10,0,0,0);
                button.setBackground(getDrawable(R.drawable.arrow_right));
                break;
        }
        button.setLayoutParams(params);
        button.setTextSize(36);
        button.setOnClickListener((event) -> {
            if(!win)
            {
                shift(pos-1,type);
            }
        });
        return button;
    }

    public TextView getTextNumber(int x,int y)
    {
        Button text = new Button(gamefield.getContext());
        text.setLayoutParams(new TableRow.LayoutParams(175,175));
        text.setGravity(Gravity.CENTER);
        text.setBackground(getDrawable(R.drawable.textnumber));
        text.setText(String.valueOf(numbers[x][y]));
        text.setTextColor(Color.RED);
        text.setTextSize(36);
        return text;
    }

    public void resetGame()
    {
        numbers = new int[][] {{1,2,3},{4,5,6},{7,8,9}};
        swapNumbers(100);
        counter.setText("0");
        win = false;
        buildField();
    }

    private void swapNumbers(int value)
    {
        int posX_1,posX_2,posY_1,posY_2;

        for(int x = 0;x < value;x++)
        {
            posX_1 = ThreadLocalRandom.current().nextInt(0,3);
            posY_1 = ThreadLocalRandom.current().nextInt(0,3);

            posX_2 = ThreadLocalRandom.current().nextInt(0,3);
            posY_2 = ThreadLocalRandom.current().nextInt(0,3);

            while(posX_1 == posX_2 && posY_1 == posY_2)
            {
                posX_2 = ThreadLocalRandom.current().nextInt(0,3);
                posY_2 = ThreadLocalRandom.current().nextInt(0,3);
            }

            int tmp = numbers[posX_1][posY_1];
            numbers[posX_1][posY_1] = numbers[posX_2][posY_2];
            numbers[posX_2][posY_2] = tmp;

        }
    }

    public void shift(int value,String type)
    {
        if(win) return;
        switch(type)
        {
            case "up":
                shiftLT(value,true);
                break;
            case "down":
                shiftRD(value,true);
                break;
            case "left":
                shiftLT(value,false);
                break;
            case "right":
                shiftRD(value,false);
                break;
        }

        int counterValue = Integer.parseInt(counter.getText().toString());
        counter.setText(String.valueOf(counterValue+1));
        buildField();
    }

    public void shiftLT(int value,boolean type)
    {
        int tmp = -1;
        for(int x = 0;x < 3;x++)
        {
            if(type)
            {
                if(tmp == -1) tmp = numbers[x][value];
                if(x == 2) numbers[x][value] = tmp;
                else numbers[x][value] = numbers[x+1][value];
            }
            else
            {
                if(tmp == -1) tmp = numbers[value][x];
                if(x == 2) numbers[value][x] = tmp;
                else numbers[value][x] = numbers[value][x+1];
            }
        }
    }

    public void shiftRD(int value,boolean type)
    {
        int tmp = -1;
        for(int x = 2;x >= 0;x--)
        {
            if(type)
            {
                if(tmp == -1) tmp = numbers[x][value];
                if(x == 0) numbers[x][value] = tmp;
                else numbers[x][value] = numbers[x-1][value];
            }
            else
            {
                if(tmp == -1) tmp = numbers[value][x];
                if(x == 0) numbers[value][x] = tmp;
                else numbers[value][x] = numbers[value][x-1];
            }
        }
    }

    public void showToast()
    {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);

        LinearLayout toastLayout = new LinearLayout(getApplicationContext());
        toastLayout.setBackground(getDrawable(R.drawable.toastdrawable));
        toastLayout.setPadding(40,0,40,0);

        TextView toastText = new TextView(getApplicationContext());
        toastText.setTextColor(Color.BLACK);
        toastText.setTextSize(48);
        toastText.setTypeface(null, Typeface.BOLD);
        toastText.setText("Completato in " + counter.getText() +" mosse");
        toastText.setGravity(Gravity.CENTER);

        toastLayout.addView(toastText);

        toast.setView(toastLayout);
        toast.show();

    }

}