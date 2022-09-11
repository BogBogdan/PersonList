package com.example.personlist;

import com.ultramegasoft.radarchart.RadarHolder;

import java.util.ArrayList;

public class ExampleItem {


    private String mtext1;
    private String mtext2;
    private ArrayList<RadarHolder> abilitys;

    public ExampleItem(int IimageR,String mtext1,String mtext2,ArrayList<RadarHolder> abilitys)
    {

        this.mtext1=mtext1;
        this.mtext2=mtext2;
        this.abilitys=abilitys;
    }


    public void changetext(String text)
    {
        mtext2=text;
    }

    public String getMtext1() {
        return mtext1;
    }

    public void setMtext1(String mtext1) {
        this.mtext1 = mtext1;
    }

    public String getMtext2() {
        return mtext2;
    }

    public void setMtext2(String mtext2) {
        this.mtext2 = mtext2;
    }

    public ArrayList<RadarHolder> getAbilitys() {return abilitys;}

    public void setAbilitys(ArrayList<RadarHolder> abilitys) {this.abilitys = abilitys;}
}
