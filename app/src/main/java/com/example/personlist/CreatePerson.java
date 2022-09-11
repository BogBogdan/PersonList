package com.example.personlist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePerson extends AppCompatActivity {


    EditText imeet;
    EditText prezimeet;
    EditText abilitys;
    RadarView radar;
    Button uzmipodatkeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        //mExampleList= (ArrayList<ExampleItem>) getIntent().getExtras().get("LIST_EXAMPLE");

        imeet=findViewById(R.id.edittextime);
        prezimeet=findViewById(R.id.edittextprezime);
        abilitys=findViewById(R.id.abilitys);
        radar=findViewById(R.id.radar);
        uzmipodatkeb=findViewById(R.id.uzmipodatkebtn);

        radar.setMaxValue(10);


        abilitys.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                {
                    String teksta=abilitys.getText().toString();
                    teksta=teksta.replace('\n','=');
                    List<String> listaabilityastring=Arrays.asList(teksta.split("="));
                    if(listaabilityastring.size()%2!=0)
                        return;

                    boolean drugiput=false;

                    String trenrec="";
                    ArrayList<RadarHolder> proga=new ArrayList<>();
                    int max=10;
                    for(String trenutni:listaabilityastring)
                    {
                        if(drugiput) {
                            drugiput = false;

                            try {
                                int d = Integer.parseInt(trenutni);
                                proga.add(new RadarHolder(trenrec,d));
                                if(d>max)
                                    max=d;
                            } catch (NumberFormatException nfe) {
                                return;
                            }
                        }
                        else
                        {
                            trenrec=trenutni;
                            drugiput=true;
                        }

                    }
                    radar.setData(proga);
                    radar.setMaxValue(max);
                }
            }
        });
        uzmipodatkeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radar.getData()!=null)
                {
                    String ime=imeet.getText().toString();
                    String prezime=prezimeet.getText().toString();
                    ArrayList<RadarHolder> proga=radar.getData();
                    AppCore.get().setIme(ime);
                    AppCore.get().setPrezime(prezime);
                    AppCore.get().setAbilitys(proga);
                    AppCore.get().setDodaj(0);
                    finish();
                }
                else
                {
                    Toast.makeText(CreatePerson.this, "You need to add abilitys", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}