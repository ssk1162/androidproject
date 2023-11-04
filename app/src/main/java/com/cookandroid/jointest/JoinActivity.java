package com.cookandroid.jointest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    Button btnBack, btnGo;
    myDBHelper myHelper;
    EditText idEdit, pwEdit, pwEdit2, emailEdit;
    Button btnClick, btnPwClick;
    SQLiteDatabase sqlDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);
        setTitle("회원가입");

        btnBack = findViewById(R.id.btnBack);
        btnGo = findViewById(R.id.btnGo);
        idEdit = findViewById(R.id.idEdit);
        pwEdit = findViewById(R.id.pwEdit);
        pwEdit2 = findViewById(R.id.pwEdit2);
        btnClick = findViewById(R.id.btnClick);
        btnPwClick = findViewById(R.id.btnPwClick);
        emailEdit = findViewById(R.id.emailEdit);

//      중복 확인
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqlDB = myHelper.getReadableDatabase();
                    Cursor cursor;
                    cursor = sqlDB.rawQuery("select * from loginTBL;", null);

                    while (cursor.moveToNext()) {

                        if (cursor.getString(0).equals(idEdit.getText().toString())) {

                            Toast.makeText(getApplicationContext(), "사용불가", Toast.LENGTH_SHORT).show();
                            break;

                        }

                    }

                    cursor.close();
                    sqlDB.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//      비밀번호 확인
        btnPwClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pwEdit.getText().toString().equals(pwEdit2.getText().toString())) {

                    Toast.makeText(getApplicationContext(), "비밀번호가 같습니다", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "비밀번호가 다름니다", Toast.LENGTH_SHORT).show();

                }

            }
        });

//      회원가입
        myHelper = new myDBHelper(this);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (idEdit.getText().toString().replace(" ", "").equals("")) {

                    Toast.makeText(getApplicationContext(), "아이디를 확인해주세요", Toast.LENGTH_SHORT).show();

                } else if (pwEdit.getText().toString().replace(" ", "").equals("")) {

                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();

                } else {

                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("insert into loginTBL values('" + idEdit.getText().toString() + "', '" + pwEdit.getText().toString() + "', '" + emailEdit.getText().toString() + "');");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

//      뒤로가기
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "돌아가기", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public class myDBHelper extends SQLiteOpenHelper {

        public myDBHelper(Context context) {
            super(context, "loginDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE loginTBL ( id String primary key, pw String, email String );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int k) {
            db.execSQL("DROP TABLE IF EXISTS loginTBL");
            onCreate(db);
        }
    }

}
