package com.cakestudios.diettracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class newMeasurementActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{
    Button nextButton;
    TextView dateEditText;
    Spinner isHungrySpinner;
    EditText varMeasurementEditText,noteEditText;
    String myYear,myMonth,myDay,isHungry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measurement);

        Initilazion();
        setDateEditText();

        //Aç/Tok kısmını Spinner kullanarak kullanıcıya seçmesini sağlıyoruz
        //Spinnerdan seçileni almak için AdapterView.OnItemSelectedListener implementasyon yapılan fonksiyonu ile seçileni alıyoruz.
        isHungrySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.isHungry, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isHungrySpinner.setAdapter(adapter);


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isHungry=getIsHungry();
                String varMeasurement=varMeasurementEditText.getText().toString();
                String note=noteEditText.getText().toString();
                newMeasurementActivity2Pass(isHungry,varMeasurement,note);
            }
        });

    }

    //Bu Activity den alınan verileri diğer Activitye taşıyoruz verdiğimiz key ile alıyoruz.
    private void newMeasurementActivity2Pass(String isHungry, String varMeasurement, String note) {
        Intent intent=new Intent(newMeasurementActivity.this,newMeasurementActivity2.class);
        intent.putExtra("isHungry", isHungry);
        intent.putExtra("varMeasurement", varMeasurement);
        intent.putExtra("note", note);
        intent.putExtra("date",myYear+"/"+myMonth+"/"+myDay);
        startActivity(intent);
        finish();
    }

    //Activitiye ilk açıldığında date kısmını güncel saati setlemek için yazıldı.
    private void setDateEditText() {
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateEditText.setText(year+"/"+(month+1)+"/"+day);
    }

    //Date seçimini için DatePickerDialog özelliğini kullanıyoruz. Seçilen tarihi alma işlemi için de DatePickerDialog.OnDateSetListener
    //implemantasyunu yapıyoruz ve seçilen tarihi buradaki fonksiyon ile alıyoruz
    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(newMeasurementActivity.this, newMeasurementActivity.this, year, month, day);
        datePickerDialog.show();

    }

    private void Initilazion() {
        nextButton=findViewById(R.id.nextButton);
        isHungrySpinner =findViewById(R.id.isHungry);
        dateEditText=findViewById(R.id.date);
        varMeasurementEditText=findViewById(R.id.varMeasurement);
        noteEditText=findViewById(R.id.note);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
       dateEditText.setText(i+"/"+(i1+1)+"/"+i2);
        myYear=String.valueOf(i);
        myMonth=String.valueOf(i1+1);
        myDay=String.valueOf(i2);
    }


    @Override
    public void onBackPressed() {
        Intent intent= new Intent( newMeasurementActivity.this, selectMenuActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        setIsHungry(item);
        Toast.makeText(newMeasurementActivity.this,item,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getIsHungry() {
        return isHungry;
    }

    public void setIsHungry(String isHungry) {
        this.isHungry = isHungry;
    }

}