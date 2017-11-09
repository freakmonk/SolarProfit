package com.freak_stuff.solarprofit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.lang.String;

public class MainActivity extends AppCompatActivity {
    BarChart chart,chart2 ;
    ArrayList<BarEntry> BARENTRY, BARENTRY2;
    private Spinner spinner;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset, Bardataset2 ;
    BarData BARDATA, BARDATA2 ;
    Double SolPowerC,GreenTar,Elect_kW,Nalog,system_volt;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Обработчик кнопки калькуляции
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onButtonPressed(view);
            }
        });

        //Обработчик кнопки сохранить настройки
        Button button2 = findViewById(R.id.buttonsave);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onButtonSavePressed(view);
            }
        });

        //Выпадающий список профилей и обработка изменения профиля
        spinner = (Spinner) findViewById(R.id.spinner);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                switch(item) {
                    case "Profile":
                        break;
                    case "Ukraine":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.0622");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.21");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("18");
                        break;
                    case "United States":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.125");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.15");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("10");
                        break;
                    case "Russia":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.082");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.1");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("13");
                        break;
                    case "Germany":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.36");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.16");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("2.56");
                        break;

                    case "France":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.17");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.22");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("5.5");
                        break;

                    case "Italy":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.25");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.18");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("23");
                        break;

                    case "Poland":
                        text = (TextView)findViewById(R.id.local_electric_tarif);
                        text.setText("0.152");
                        text = (TextView)findViewById(R.id.local_green_tarif);
                        text.setText("0.17");
                        text = (TextView)findViewById(R.id.local_taxes);
                        text.setText("19");
                        break;
                    default:
                        Snackbar.make(view, "No data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        //Кнопка в нижнем правом крае с карандашиком - назад
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.mainp).setVisibility(View.VISIBLE);
                findViewById(R.id.resp).setVisibility(View.INVISIBLE);
            }
        });

        //Вытягивание настроек из сохрана
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String ElectroTar = preferences.getString("ElectroTarif", "");
        String GrenTar = preferences.getString("GreenTarif", "");
        String Ltaxes = preferences.getString("LocalTaxes", "");
        if (ElectroTar == null || ElectroTar == "" || GrenTar == null || GrenTar == "" || Ltaxes == null || Ltaxes == "")
        {
            //установка дефолтных значений
            Elect_kW = 0.0622;
            GreenTar = 0.21;
            Nalog = 0.82;
         }else{
            //запись сохраненных значений
            Elect_kW = Double.parseDouble(ElectroTar);
            text = (TextView)findViewById(R.id.local_electric_tarif);
            text.setText(Elect_kW.toString());
            GreenTar = Double.parseDouble(GrenTar);
            text = (TextView)findViewById(R.id.local_green_tarif);
            text.setText(GreenTar.toString());
            Nalog = Double.parseDouble(Ltaxes);
            text = (TextView)findViewById(R.id.local_taxes);
            double percnalog = 100 - Nalog*100;
            text.setText(Nalog.toString());
        }

    }

    //Сохранение настроек
    public void onButtonSavePressed(View view) {
        //close keyboard
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
        //save preferenses
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ElectroTarif",((EditText) findViewById(R.id.local_electric_tarif)).getText().toString());
        editor.putString("GreenTarif",((EditText) findViewById(R.id.local_green_tarif)).getText().toString());
        editor.putString("LocalTaxes",((EditText) findViewById(R.id.local_taxes)).getText().toString());
        editor.apply();

        Elect_kW = Double.parseDouble(((EditText) findViewById(R.id.local_electric_tarif)).getText().toString());
        GreenTar = Double.parseDouble(((EditText) findViewById(R.id.local_green_tarif)).getText().toString());
        Nalog = Double.parseDouble(((EditText) findViewById(R.id.local_taxes)).getText().toString());

        //open main view
        findViewById(R.id.mainp).setVisibility(View.VISIBLE);
        findViewById(R.id.sett).setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //обработка нажатий на меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            findViewById(R.id.mainp).setVisibility(View.INVISIBLE);
            findViewById(R.id.resp).setVisibility(View.INVISIBLE);
            findViewById(R.id.sett).setVisibility(View.VISIBLE);
        }
        if (id == R.id.action_about) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("About");
            alertDialog.setMessage("Freak-monk (c) 2017 freakmonk@me.com");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //обработка нажатия физической кнопки телефона назад
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            findViewById(R.id.mainp).setVisibility(View.VISIBLE);
            findViewById(R.id.resp).setVisibility(View.INVISIBLE);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //кнопка калькуляции
    public void onButtonPressed(View view) {

        //close keyboard
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
        //open snake bar
        Snackbar.make(view, "Solar calculations in progress...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //get input data
        Double val2,val3,val_s_cost;
        //SolPowerC = 1.0;
        Integer SolPow = Integer.valueOf(((EditText) findViewById(R.id.solarpower)).getText().toString());
        if (SolPow==null || SolPow==0) SolPow=1;
        val2 =1.0;
        if(SolPow <= 800) val2=0.95;
        else val2=0.98;
        CheckBox checkBox = findViewById(R.id.mpptcheck);
        if(checkBox.isChecked()){
            SolPowerC=val2*1.05;
        }else{
            SolPowerC=1.0;
        }

        // за киловат 1.68 uah -> 0.062 $
        //Elect_kW = 0.0622;
        //0.18 euro -> 0.21 $
        //GreenTar = 0.21;
        //18
        Log.e("Nalog do", Nalog.toString());
        if (Nalog>1) Nalog = (100-Nalog)/100;
        Log.e("Nalog posle", Nalog.toString());

        //open next View of results
        findViewById(R.id.mainp).setVisibility(View.INVISIBLE);
        findViewById(R.id.resp).setVisibility(View.VISIBLE);

        //Set rating for solar station
        final RatingBar rate_bar = findViewById(R.id.ratingBar);
        if(SolPow >= 1) rate_bar.setRating(Float.parseFloat("1.0"));
        if(SolPow >= 500) rate_bar.setRating(Float.parseFloat("2.0"));
        if(SolPow >= 1000) rate_bar.setRating(Float.parseFloat("3.0"));
        if(SolPow >= 5000) rate_bar.setRating(Float.parseFloat("4.0"));
        if(SolPow >= 10000) rate_bar.setRating(Float.parseFloat("5.0"));
        if(SolPow >= 20000) rate_bar.setRating(Float.parseFloat("6.0"));

        text = new TextView(this);

        //расчет батарей
        Double s_curr,s_curr_24,s_power,batt_price;
        system_volt = 12.0;
        s_curr_24 = round(SolPow * SolPowerC / system_volt, 1);

        val2 = s_curr_24/45;
        val2 = round(val2, 0);
        if (val2>1){
            s_curr=round(s_curr_24/val2,1);
            system_volt=round(system_volt*val2,0);

        }else
        {
            s_curr=round(s_curr_24,0);
        }
        text = (TextView)findViewById(R.id.table_max_corrent);
        text.setText(s_curr.toString()+" A");
        text = (TextView)findViewById(R.id.table_system_voltage);
        text.setText(system_volt.toString()+" V");

        text = (TextView)findViewById(R.id.table_battery_cap);
        s_power = round(SolPow * SolPowerC*0.00045*12, 1);
        text.setText(s_power.toString()+" kWh");

        text = (TextView)findViewById(R.id.table_battery_type);
        val2 = s_power*1000/12;
        val2 = val2/200;
        if (val2<1) val2 = 1.0;
        val2 = round(val2, 0);
        text.setText(String.valueOf(val2.intValue()) +"x 12V 200Ah");

        text = (TextView)findViewById(R.id.table_battery_price);
        batt_price = round(val2*170, 0);
        text.setText(String.valueOf(batt_price.intValue()) +" $");

        //  расчет домашних характеристик
        text = (TextView)findViewById(R.id.table_total_y_produce);
        val2 = round(SolPow * SolPowerC*1.05, 1);
        text.setText(val2.toString()+" kWh");

        text = (TextView)findViewById(R.id.table_home_y_use);
        Integer HomePow = Integer.valueOf(((EditText) findViewById(R.id.homepower)).getText().toString());
        val3 = round(HomePow * 12, 1);
        text.setText(val3.toString()+" kWh");

        text = (TextView)findViewById(R.id.table_station_cost);
        val_s_cost = (double)SolPow;
        if (val_s_cost<=1000) val2 = 2.5;
        else if (val_s_cost<=2000) val2 = 2.0;
        else if (val_s_cost<=5000) val2 = 1.5;
        else if (val_s_cost<=10000) val2 = 1.2;
        else if (val_s_cost<=20000) val2 = 1.0;
        else if (val_s_cost<=30000) val2 = 0.95;
        else if (val_s_cost>30000) val2 = 0.9;
        val_s_cost = round(val_s_cost*val2, 1);
        text.setText(val_s_cost.toString()+" $");
        text = (TextView)findViewById(R.id.table_station_cost2);
        text.setText(val_s_cost.toString()+" $");

        text = (TextView)findViewById(R.id.table_additional_price);
        val2 = round(val_s_cost*0.1+100, 0);
        text.setText(String.valueOf(val2.intValue()) +" $");

        text = (TextView)findViewById(R.id.table_station_total);
        val2 = round(val_s_cost+batt_price+val2, 0);
        text.setText(String.valueOf(val2.intValue()) +" $");

        text = (TextView)findViewById(R.id.table_y_eco);
        val2 = round(SolPow * SolPowerC*1.05, 1);
        val3 = val2 / val3;
        val3 = round(val3 * 100, 1);
        if (val3>100){
            val3 = val2 - HomePow * 12;
            text.setText("+ "+val3.toString()+" kWh");
        } else text.setText("- "+val3.toString()+" %");

        text = (TextView)findViewById(R.id.table_year_profit);
        val3 = (SolPow * SolPowerC*1.05 - HomePow * 12)*Nalog*GreenTar;
        Log.e("Nalog", Nalog.toString());
        Log.e("GreenTar", GreenTar.toString());
        val3 = round(Math.abs(val3), 1);
        text.setText(val3.toString()+" $");

        text = (TextView)findViewById(R.id.table_year_electr);
        val3 = HomePow * 12*Elect_kW;
        val3 = round(val3, 1);
        text.setText(val3.toString()+" $");

        text = (TextView)findViewById(R.id.table_ocupaemost);
        //квт общие = Выработка панелями в год - потребеление дома в год
        val2 = SolPow * SolPowerC*1.05 - HomePow * 12;
        if(val2<0){
            // 0.062 $  квт в Украине, получаем выработку в год в долларах
            val3 = SolPow * SolPowerC*1.05*Elect_kW;
            //цена станции / доход в год
            val2 = val_s_cost/val3;
            val2 = round(val2, 1);
        }else{
            //зароботок в год - 18% налог и 0,18$ зеленый тариф
            val3 = val2*Nalog*GreenTar + HomePow*12*Elect_kW;
            // економия на отсутствии счетов потребление дома * 12 месяцев * 0.062 $ тариф
            val2 = HomePow*12*Elect_kW;
            //окупаемость в годах
            val2 = val_s_cost/(val3+val2);
            val2 = round(val2, 1);
        }
        text.setText(val2.toString());

        text = (TextView)findViewById(R.id.table_profit_40);
        //Зароботок за 40 лет
        val3 = 40*(SolPow * SolPowerC*1.05 - HomePow * 12)*Nalog*GreenTar;
        //отнимем стоимость станции
        val2 =  val3 - val_s_cost;
        val2 = round(Math.abs(val2), 1);
        text.setText(val2.toString()+" $");

        //charts gen by month
        chart = (BarChart) findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddValuesToBarEntryLabels();

        Double[] MonthC = new Double[]{ 0.032,0.06,0.08,0.11,0.13,0.15,0.14,0.13,0.09,0.06,0.042,0.03 };

        for(int lc=0; lc<=11; lc++){
            float val = (float) (SolPow * MonthC[lc] * SolPowerC);
            BARENTRY.add(new BarEntry(val, lc));
        }
        Bardataset = new BarDataSet(BARENTRY, "");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(BARDATA);
        chart.animateY(2000);


        //charts gen by days
        chart2 = (BarChart) findViewById(R.id.chart2);
        BARENTRY2 = new ArrayList<>();
        for(int lc=0; lc<=11; lc++){
            float val = (float) (SolPow * MonthC[lc] * SolPowerC / 31);
            BARENTRY2.add(new BarEntry(val, lc));
        }
        Bardataset2 = new BarDataSet(BARENTRY2, "");
        BARDATA2 = new BarData(BarEntryLabels, Bardataset2);
        Bardataset2.setColors(ColorTemplate.COLORFUL_COLORS);
        chart2.setData(BARDATA2);
        chart2.animateY(2000);




//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage(SolPower.toString());
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
    }

    //добавление в графики столбцов месяцев
    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");

        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");

        BarEntryLabels.add("June");
        BarEntryLabels.add("July");
        BarEntryLabels.add("August");

        BarEntryLabels.add("September");
        BarEntryLabels.add("October");
        BarEntryLabels.add("November");

        BarEntryLabels.add("December");

    }

    //функция округления дабла
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}