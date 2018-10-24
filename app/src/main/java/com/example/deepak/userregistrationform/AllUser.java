package com.example.deepak.userregistrationform;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class AllUser extends Activity implements CustomItemClickListner {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ContentResolver resolver;
    User user;
    ArrayList<User> users;
    int pos;

    void initviews(){
        recyclerView = findViewById(R.id.recyclerViewUser);

       /* userAdapter = new UserAdapter(this,R.layout.list_item,users);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(this);*/

        resolver=getContentResolver();
        users=new ArrayList<>();
    }


    void fetchUsers(){
        String[] projection = {Util.COL_ID,Util.COL_NAME,Util.COL_EMAIL,Util.COL_CITY,Util.COL_DATE,Util.COL_GENDER};
        Cursor cursor = resolver.query(Util.USER_URI,projection,null,null,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                User user= new User();
                user.id = cursor.getInt(cursor.getColumnIndex(Util.COL_ID));
                user.name = cursor.getString(cursor.getColumnIndex(Util.COL_NAME));
                user.city = cursor.getString(cursor.getColumnIndex(Util.COL_CITY));
                user.email = cursor.getString(cursor.getColumnIndex(Util.COL_EMAIL));
                user.CDate = cursor.getString(cursor.getColumnIndex(Util.COL_DATE));
                user.gender= cursor.getString(cursor.getColumnIndex(Util.COL_GENDER));
                users.add(user);
                //recyclerAdapter.add(user.name);
              // recyclerAdapter.add(user.name);

            }
            recyclerAdapter = new RecyclerAdapter(this,R.layout.list_item,users);
            recyclerAdapter.registerCustomitemclicklistner(this);
            LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
            //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(recyclerAdapter);

            //recyclerView.setOnClickListener((View.OnClickListener) this);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        initviews();
        fetchUsers();
    }
    void showInfo(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details of "+user.name);
        builder.setMessage(user.toString());
        builder.setPositiveButton("Done",null);
        builder.create().show();
    }
    void deleteFromDB(){
        String where = Util.COL_ID+" = "+user.id;
        int i = resolver.delete(Util.USER_URI,where,null);
        if(i>0){
            Toast.makeText(this,user.name+" deleted",Toast.LENGTH_LONG).show();
           // recyclerAdapter.remove(user.name+"\n"+user.email);
            recyclerAdapter.notifyDataSetChanged(); // Refresh
        }else{
            Toast.makeText(this,user.name+" not deleted",Toast.LENGTH_LONG).show();
        }
    }
    void askForDeletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+user.name+" ?");
        builder.setMessage("Are You Sure to delete record?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFromDB();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }
    void showOptions(){
        String[] options = {"View","Delete","Update","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        showInfo();
                        break;
                    case 1:
                        askForDeletion();
                        break;
                    case 2:
                        Intent intent= new Intent(AllUser.this,MainActivity.class);
                        intent.putExtra("keyuser",  user);
                        startActivityForResult(intent,101);
                        break;
                    case 3:
                        //finish();
                        break;
                }
            }
        });
        builder.create().show();
    }



    @Override
    public void onItemClick(View v, int position) {
            pos= position;
             user= users.get(position);
             showOptions();
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101 && resultCode == 201){
            User cRef = (User) data.getSerializableExtra("keyUpdatedCustomer");

            users.set(pos,cRef);

           // recyclerAdapter.removeAllItems();
            for(User c : users){
                Log.i("CustomerList","Customer Details:"+c.toString());

               // recyclerAdapter = new RecyclerAdapter(this,R.layout.list_item,users);
            }
            // Refresh
            recyclerAdapter.notifyDataSetChanged();
            Toast.makeText(this,"==onActivityResult==",Toast.LENGTH_LONG).show();
        }
    }






   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = users.get(position);
        Toast.makeText(this,"You Selected: "+user.name,Toast.LENGTH_LONG).show();
        showOptions();
    }*/


}