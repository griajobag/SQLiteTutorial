package com.putuguna.sqllitetutorial;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPhone;
    private Button btnInsert;
    private Button btnUpdate;
    private ListView lvProfile;
    private List<String> mListProfile = new ArrayList<>();
    private Button btnDelete;
    DatabaseHandler handler = new DatabaseHandler(this);
    ProfileModel profile = new ProfileModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etName = (EditText) findViewById(R.id.edit_text_name);
        etPhone = (EditText) findViewById(R.id.edit_text_phone);
        btnInsert = (Button) findViewById(R.id.button_insert);
        lvProfile = (ListView) findViewById(R.id.listview_contact);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnUpdate = (Button) findViewById(R.id.button_update);


        //insert
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etName.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.msg_cannot_allow_empty_field, Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etPhone.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.msg_cannot_allow_empty_field, Toast.LENGTH_SHORT).show();
                }else{
                    insertData(handler);
                }
            }
        });

        //reading all profileModel
        displayAllData(handler, profile);

        //delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDelete();
            }
        });

        //update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataFromPopup();
            }
        });




    }

    /**
     * this method used to read all the data of contact
     * @param handler
     * @param profile
     */
    private void displayAllData(DatabaseHandler handler, ProfileModel profile){
        mListProfile.clear();
        List<ProfileModel> list = handler.getAllDataProfile();
        for(ProfileModel profileModel : list){
            String log = "ID : " + profileModel.getId() + "\n" +
                    "NAME : " + profileModel.getName() + "\n" +
                    "PHONE NUMBER : " + profileModel.getPhoneNumber();
            System.out.println(log);

            //add to list
            mListProfile.add(log);

            //set to the model
            profile.setId(profileModel.getId());
            profile.setName(profileModel.getName());
            profile.setPhoneNumber(profileModel.getPhoneNumber());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mListProfile);
        lvProfile.setAdapter(arrayAdapter);
        lvProfile.invalidateViews();
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * this method used to insert data
     * @param databaseHandler
     */
    private void insertData(final DatabaseHandler databaseHandler){
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();

        ProfileModel profileModel = new ProfileModel();
        profileModel.setName(name);
        profileModel.setPhoneNumber(phone);

        databaseHandler.addProfile(profileModel);
        displayAllData(databaseHandler, profileModel);

        etPhone.setText("");
        etName.setText("");
    }


    /**
     * this method used to delete item from popup
     */
    private void popupDelete(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.lbl_choose_an_id);
        dialog.setContentView(R.layout.popup_delete);

        final SharedPreferences sharedPreferences = getSharedPreferences("mPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final Spinner spinner= (Spinner) dialog.findViewById(R.id.spinner_id);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        final List<ProfileModel> profileModelList = handler.getAllDataProfile();

        //set into spinner adapter
        ArrayAdapter<ProfileModel> spinnerArrayAdapter = new ArrayAdapter<ProfileModel>(this,android.R.layout.simple_spinner_item, profileModelList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        //spinner on item selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putString("IDprofile", profileModelList.get(i).getId()+"");
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileId = sharedPreferences.getString("IDprofile", null);
                handler.deleteRow(profileId);
                displayAllData(handler,profile);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**
     * this method used to update item from popup
     */
    private void updateDataFromPopup(){
        final Dialog dialog  = new Dialog(this);
        dialog.setTitle(R.string.lbl_update_data_pop_up);
        dialog.setContentView(R.layout.popup_update);

        Spinner spinnerUpdate = (Spinner) dialog.findViewById(R.id.spinner_update);
        final EditText etUpdateName = (EditText) dialog.findViewById(R.id.et_update_name);
        final EditText etUpdatePhone = (EditText) dialog.findViewById(R.id.et_update_phone);
        Button btnSaveUpdate = (Button) dialog.findViewById(R.id.btn_save_update);

        final List<ProfileModel> profileUpdate = handler.getAllDataProfile();
        final SharedPreferences sharedPreferences = getSharedPreferences("mPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        //set into spinner adapter
        ArrayAdapter<ProfileModel> spinnerArrayAdapter = new ArrayAdapter<ProfileModel>(this,android.R.layout.simple_spinner_item, profileUpdate);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdate.setAdapter(spinnerArrayAdapter);

        //spinner on item selected
        spinnerUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                etUpdateName.setText(profileUpdate.get(i).getName());
                etUpdatePhone.setText(profileUpdate.get(i).getPhoneNumber());
                editor.putString("id-for-update", profileUpdate.get(i).getId()+"");
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etUpdateName.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.msg_cannot_allow_empty_field, Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etUpdatePhone.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.msg_cannot_allow_empty_field, Toast.LENGTH_SHORT).show();
                }else{
                    String newName = etUpdateName.getText().toString();
                    String newPhone = etUpdatePhone.getText().toString();
                    String idProfile = sharedPreferences.getString("id-for-update",null);

                    //update data
                    handler.updatedetails(Integer.parseInt(idProfile), newName, newPhone);
                    displayAllData(handler,profile);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}
