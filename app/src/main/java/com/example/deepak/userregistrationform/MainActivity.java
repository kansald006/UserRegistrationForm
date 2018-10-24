package com.example.deepak.userregistrationform;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.app.DatePickerDialog;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{
    TextView textView;
    EditText etxtName, etxtEmail, etxtDate;
    Button btnDate, btnSubmit;
    RadioButton rbmale,rbfemale;
    Spinner spinnercity;
    ArrayAdapter<String> adapter;
    User user;
    ContentResolver resolver;
    boolean updatemode;
    void initviews(){
        textView = findViewById(R.id.textView);
        etxtName= findViewById(R.id.editTextName);
        etxtEmail= findViewById(R.id.editTextEmail);
        etxtDate= findViewById(R.id.editTextDate);
        btnDate= findViewById(R.id.button);
        btnSubmit=findViewById(R.id.buttonSubmit);
        rbmale=findViewById(R.id.radioButtonMale);
        rbfemale=findViewById(R.id.radioButtonFemale);
        user = new User();
        resolver=getContentResolver();

        Intent rcv= getIntent();
        updatemode=rcv.hasExtra("keyuser");

        if(updatemode){
            user=(User) rcv.getSerializableExtra("keyuser");
            etxtName.setText(user.name);
            etxtEmail.setText(user.email);
            etxtDate.setText(user.CDate);
            btnSubmit.setText("Update Customer");
            }


        rbmale.setOnCheckedChangeListener(this);
        rbfemale.setOnCheckedChangeListener(this);
        btnSubmit.setOnClickListener(this);
        spinnercity=findViewById(R.id.spinnerCity);
        ArrayAdapter<String> adapter;

    }

    boolean validateFields(){
        boolean flag = true;

        if(user.name.isEmpty())
            flag = false;

        if(user.email.isEmpty())
            flag = false;

        if(user.CDate.isEmpty())
            flag = false;

        return flag;
    }
  void clearFields(){
        etxtName.setText(" ");
      etxtEmail.setText(" ");
      etxtDate.setText(" ");
  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item);
        adapter.add("--Select City--");    //0
        adapter.add("Ludhiana");           //1
        adapter.add("Chandigarh");         //2
        adapter.add("Delhi");
        adapter.add("Pune");
        adapter.add("Bengaluru");

        spinnercity.setAdapter(adapter);
        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    user.city=adapter.getItem(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



     btnDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             showDatePicker();
         }
     });

    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog;
        DatePickerDialog.OnDateSetListener onDateSetListener;
        Calendar calendar = Calendar.getInstance();
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH);
        int yy = calendar.get(Calendar.YEAR);
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etxtDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                user.CDate=dayOfMonth+"/"+(month+1)+"/"+year;
            }
        };
        datePickerDialog = new DatePickerDialog(this,onDateSetListener,yy,mm,dd);
        datePickerDialog.show();

    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        if(id == R.id.radioButtonMale){
            user.gender = "Male";
        }else{
            user.gender = "Female";
        }

    }
    void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Press OK if you want to Submit Your Detail");
        builder.setMessage("Otherwise Press Cancel if you want to cancel");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.name= etxtName.getText().toString();
                user.email= etxtEmail.getText().toString();
                insertUser();
            }
        });
        // if you do not want the dialog should be dismissed on back press
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,101,0,"All Users");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 101){
            Intent intent = new Intent(MainActivity.this,AllUser.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    void insertUser(){
        ContentValues values= new ContentValues();
        values.put(Util.COL_NAME,user.name);
        values.put(Util.COL_EMAIL,user.email);
        values.put(Util.COL_DATE,user.CDate);
        values.put(Util.COL_GENDER,user.gender);
        values.put(Util.COL_CITY,user.city);
        if(validateFields()) {
            Uri uri = resolver.insert(Util.USER_URI, values);
            Toast.makeText(this, user.name + " added in Table at id " + uri.getLastPathSegment(), Toast.LENGTH_LONG).show();
            clearFields();
            if(updatemode){
                String where = Util.COL_ID+" = "+user.id;
                int i = resolver.update(Util.USER_URI,values,where,null);
                if(i>0){
                    Toast.makeText(this, user.name + " updated in Table", Toast.LENGTH_LONG).show();
                    Intent data = new Intent();
                    data.putExtra("keyUpdatedCustomer",user);
                    setResult(201,data);
                    finish();
                }
            }else {
                Uri uri1 = resolver.insert(Util.USER_URI, values);
                Toast.makeText(this, user.name + " added in Table at id " + uri1.getLastPathSegment(), Toast.LENGTH_LONG).show();
                clearFields();
            }
        }else {
            Toast.makeText(this, "Enter Details First", Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onClick(View v) {
        showAlertDialog();
    }

}

