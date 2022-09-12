package com.example.personlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Collections;

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
            Collections.reverse(mExampleList);
            upisiuMemoriju();
        }
        catch(Exception e){

            Toast.makeText(getBaseContext(),"GRESKAA",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.maxsort:
                sortMax();
                Collections.reverse(mExampleList);
                for(int i=0;i<mExampleList.size();i++)
                    adapter.notifyItemChanged(i);
                return true;
            case R.id.minsort:
                sortMax();
                for(int i=0;i<mExampleList.size();i++)
                    adapter.notifyItemChanged(i);
                return true;
            case R.id.oldestsort:
                BuildRecycleViewer();
                createFile();
                return true;
            case R.id.newestsort:
                BuildRecycleViewer();
                createFile();
                Collections.reverse(mExampleList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertItem(int position, String ime, String prezime, ArrayList<RadarHolder> radarHolder)
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



    void sortMax()
    {
        double[] pomocnalista=new double[mExampleList.size()];
        int brojac=0;
        for(ExampleItem ei:mExampleList)
        {
            double br=0;
            double vrednost=0;
            for(RadarHolder s:ei.getAbilitys())
            {
                vrednost+=s.value;br++;
            }

            pomocnalista[brojac]=vrednost/br;
            brojac++;
        }

        Toast.makeText(this, pomocnalista[0]+" "+pomocnalista[1]+" "+pomocnalista[2]+" "+pomocnalista[3], Toast.LENGTH_SHORT).show();
        quickSort(pomocnalista,0,mExampleList.size()-1);

        Toast.makeText(this, pomocnalista[0]+" "+pomocnalista[1]+" "+pomocnalista[2]+" "+pomocnalista[3], Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, mExampleList.get(0).getMtext2()+" "+mExampleList.get(1).getMtext2()+" "+mExampleList.get(2).getMtext2()+" "+mExampleList.get(3).getMtext2(), Toast.LENGTH_SHORT).show();
    }


    //QUICK SORT

    public void swap(double[] arr, int i, int j)
    {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

    }

    public void swapExampleItem(int i,int j)
    {
        ExampleItem tren=mExampleList.get(i);
        mExampleList.set(i,mExampleList.get(j));
        mExampleList.set(j,tren);
    }

    public int partition(double[] arr, int low, int high)
    {

        // pivot
        double pivot = arr[high];

        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {

            // If current element is smaller
            // than the pivot
            if (arr[j] < pivot)
            {

                // Increment index of
                // smaller element
                i++;
                swap(arr, i, j);
                swapExampleItem(i,j);
            }
        }
        swap(arr, i + 1, high);
        swapExampleItem(i+1,high);
        return (i + 1);
    }


    public void quickSort(double[] arr, int low, int high)
    {
        if (low < high)
        {

            // pi is partitioning index, arr[p]
            // is now at right place
            int pi = partition(arr, low, high);

            // Separately sort elements before
            // partition and after partition
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
}