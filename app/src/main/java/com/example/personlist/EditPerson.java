package com.example.personlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditPerson extends AppCompatActivity {


    EditText imeet;
    EditText prezimeet;
    EditText abilitys;
    Button uzmipodatkeb;

    RadarView radar;
    Button obrisib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);


        imeet=findViewById(R.id.edittextime);
        prezimeet=findViewById(R.id.edittextprezime);
        radar=findViewById(R.id.radar);
        uzmipodatkeb=findViewById(R.id.uzmipodatkebtn);
        obrisib=findViewById(R.id.obrisibtn);
        abilitys=findViewById(R.id.abilitys);

        imeet.setText(AppCore.get().getIme());
        prezimeet.setText(AppCore.get().getPrezime());

        citajRadar();
        radar.setPolygonColor(Color.parseColor("#800000"));;



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
                    List<String> listaabilityastring= Arrays.asList(teksta.split("="));
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
                    radar.setMaxValue(max);
                    radar.setData(proga);
                }
            }
        });

        obrisib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCore.get().setDodaj(1);

                String ime=imeet.getText().toString();
                String prezime=prezimeet.getText().toString();
                ArrayList<RadarHolder> proga=radar.getData();

                AppCore.get().setIme(ime);
                AppCore.get().setPrezime(prezime);
                AppCore.get().setAbilitys(proga);
                finish();
            }
        });

        uzmipodatkeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(radar.getData()!=null)
                {
                    AppCore.get().setDodaj(0);

                    String ime=imeet.getText().toString();
                    String prezime=prezimeet.getText().toString();
                    ArrayList<RadarHolder> proga=radar.getData();

                    AppCore.get().setIme(ime);
                    AppCore.get().setPrezime(prezime);
                    AppCore.get().setAbilitys(proga);
                    finish();
                }
                else
                {
                    Toast.makeText(EditPerson.this, "You need to add abilitys", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    public void citajRadar()
    {
        List<RadarHolder> holder=AppCore.get().getAbilitys();
        int max=10;
        String abilityseditor="";
        for(RadarHolder rh:holder)
        {

            abilityseditor=abilityseditor.concat(rh.name+"="+rh.value+"\n");

            if(rh.value>max)
                max=rh.value;

        }
        abilitys.setText(abilityseditor);
        radar.setMaxValue(max);
        radar.setData(holder);


    }
    public void positionAction(View view) {
        int position = (int) view.getTag();
        Toast.makeText(view.getContext(),"Polozaj je  "+Integer.toString(position),Toast.LENGTH_SHORT).show();
        AppCore.get().setPosition(position);
    }
}