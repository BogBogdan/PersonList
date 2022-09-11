package com.example.personlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ultramegasoft.radarchart.RadarHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView recyclerView;
    private ExampleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
/*
            CUVANJE PODATAKA unosenje

            IME\PREZIME\AB1\AB2\AB3\AB4/

            Razmak je sledeca rec
            Kosa crta je sledeci karakter

 */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==3 && AppCore.get().isDodaj()==0)
        {

            String ime=AppCore.get().getIme();
            String prezime= AppCore.get().getPrezime();
            ArrayList<RadarHolder> rh=AppCore.get().getAbilitys();
            insertItem(0,ime,prezime,rh);
            super.onActivityResult(requestCode, resultCode, data);
        }
        else if(requestCode==2)
        {

            if(AppCore.get().isDodaj()==0)
            {
                String ime=AppCore.get().getIme();
                String prezime= AppCore.get().getPrezime();
                ArrayList<RadarHolder> rh=AppCore.get().getAbilitys();
                removeItem(AppCore.get().getPosition());
                insertItem(AppCore.get().getPosition(),ime, prezime, rh);

            }
            else  if(AppCore.get().isDodaj()==1)

            {
                removeItem(AppCore.get().getPosition());
            }
        }
        AppCore.get().setDodaj(-1);
        upisiuMemoriju();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        BuildRecycleViewer();
        createFile();

        fab=findViewById(R.id.fab);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreatePersonActivity();
            }
        });

    }



    public String ObradjenString()
    {
        String tren="";
        for(ExampleItem e:mExampleList)
        {
            tren=tren.concat(e.getMtext1()+'\\');
            tren=tren.concat(e.getMtext2()+'\\');
            ArrayList<RadarHolder> novi=e.getAbilitys();
            for(RadarHolder r:novi)
            {
                tren=tren+r.name+'\\'+r.value+'\\';
            }
            tren=tren.concat(Character.toString('/'));
        }

        return tren;
    }

    public void createFile()
    {

        try {
            FileInputStream fin = openFileInput("data.txt");

            int c;

            mExampleList.clear();

            String s1="";
            String s2="";
            int biorazmak=0;

            String hg="";
            String name="";
            String value="";
            ArrayList<RadarHolder> rh=new ArrayList<>();
            while( (c = fin.read()) != -1){


                hg = hg + Character.toString((char)c);
                if((char)c=='/')
                {

                    if(rh.size()==0)
                        rh.add(new RadarHolder("prvi",Integer.parseInt("3")));
                    insertItem(0,s1,s2,rh);
                    biorazmak=0;
                    s1="";
                    s2="";
                    rh=new ArrayList<>();
                }
                else if((char)c=='\\')
                {
                    biorazmak++;
                    if(biorazmak>2 && biorazmak%2==0)
                    {
                        rh.add(new RadarHolder(name,Integer.parseInt(value)));
                        name="";
                        value="";
                    }

                }
                else if(biorazmak==1)
                {
                    s2 = s2 + Character.toString((char)c);
                }
                else if(biorazmak==0)
                {
                    s1 = s1 + Character.toString((char)c);
                }
                else
                {
                    if(biorazmak%2==0)
                    {
                        name=name + Character.toString((char)c);
                    }
                    else
                    {
                        value=value + Character.toString((char)c);
                    }
                }
            }

            upisiuMemoriju();
        }
        catch(Exception e){

            Toast.makeText(getBaseContext(),"GRESKAA",Toast.LENGTH_SHORT).show();
        }
    }



        public void insertItem(int position,String ime,String prezime,ArrayList<RadarHolder> radarHolder)
    {

        mExampleList.add(position,new ExampleItem(R.drawable.ic_launcher_background,ime,prezime,radarHolder));
        adapter.notifyItemInserted(position);
       // Toast.makeText(getBaseContext(),ime+"  je ime",Toast.LENGTH_SHORT).show();

    }

    public void upisiuMemoriju()
    {
        File path=getApplicationContext().getFilesDir();
        try {
            FileOutputStream fOut = new FileOutputStream(new File(path,"data.txt"));
            // FileOutputStream fOut = openFileOutput("data.txt",MODE_PRIVATE);


            String s=ObradjenString();
            fOut.write(s.getBytes());


            fOut.close();

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void removeItem(int position)
    {
        mExampleList.remove(position);
        adapter.notifyItemRemoved(position);
    }
    public void createExampleList(){
        mExampleList=new ArrayList<>();


    }

    public void BuildRecycleViewer()
    {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this, 2);;
        adapter=new ExampleAdapter(mExampleList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickLisener(new ExampleAdapter.OnInemClickLisener() {
            @Override
            public void onItemClick(int position) {

                AppCore.get().setIme(mExampleList.get(position).getMtext1());
                AppCore.get().setPrezime(mExampleList.get(position).getMtext2());
                AppCore.get().setPosition(position);
                AppCore.get().setmExampleList(mExampleList);
                AppCore.get().setAbilitys(mExampleList.get(position).getAbilitys());
                startEditPersonActivity();

            }
        });
    }

    //Activitys
    public void startEditPersonActivity()
    {
        Intent intent = new Intent(this, EditPerson.class);
        this.startActivityForResult(intent,2);

    }

    public void startCreatePersonActivity()
    {
        Intent intent = new Intent(this, CreatePerson.class);
        // intent.putExtra("LIST_EXAMPLE", mExampleList);
        startActivityForResult(intent,3);

    }

}