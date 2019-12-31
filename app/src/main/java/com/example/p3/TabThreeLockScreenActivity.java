package com.example.p3;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TabThreeLockScreenActivity extends AppCompatActivity {

    EditText editText;
    String[] quizes = {"전남친한테 연락하지마!","전여친한테 연락하지마!"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_lockscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        TextView textView = findViewById(R.id.quiz);
        editText = findViewById(R.id.answer);
        final int r = (int)(Math.random()*2);
        textView.setText(quizes[r]);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String answer_text = editText.getText().toString();
                //Toast.makeText(getApplicationContext(),answer_text + "\n"+quizes[r],Toast.LENGTH_SHORT).show();
                if(actionId == EditorInfo.IME_ACTION_DONE&& answer_text.equals(quizes[r]) ){
                    Toast.makeText(getApplicationContext(),"정답!", Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }else{
                    Toast.makeText(getApplicationContext(),"다시 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
