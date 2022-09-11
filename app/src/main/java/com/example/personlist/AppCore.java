package com.example.personlist;

import android.util.Pair;

import com.ultramegasoft.radarchart.RadarHolder;

import java.util.ArrayList;
import java.util.List;

public class AppCore {

    private static AppCore appCore;



    private ArrayList<ExampleItem> mExampleList;
    String ime;
    String prezime;
    int dodaj;
    int position;
    ArrayList<RadarHolder> abilitys;
    private AppCore()
    {
        position=0;
        ime="Link1";
        prezime="Link2";
        dodaj=-1;
        abilitys=new ArrayList<>();
    }

    public static AppCore get(){
        if(appCore==null)
            appCore=new AppCore();

        return appCore;
    }

    public ArrayList<ExampleItem> getmExampleList() {
        return mExampleList;
    }

    public void setmExampleList(ArrayList<ExampleItem> mExampleList) {this.mExampleList = mExampleList;}

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public int isDodaj() {
        return dodaj;
    }

    public void setDodaj(int dodaj) {
        this.dodaj = dodaj;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<RadarHolder> getAbilitys() {return abilitys;}

    public void setAbilitys(ArrayList<RadarHolder> abilitys) {this.abilitys = abilitys;}
}
