package com.example.p3;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TabThreeCreateActivity extends AppCompatActivity {

    private EditText ment;
    private Button input;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab3_create_ments);


        ment = findViewById(R.id.editText2);
        input = findViewById(R.id.button);
        ment.setInputType(InputType.TYPE_CLASS_TEXT);
        ment.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                TabThreeItem item = new TabThreeItem();
                System.out.println(ment.getText().toString());
                item.setWarning(ment.getText().toString());
                item.setIcon(true);
                intent.putExtra("item",item);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
