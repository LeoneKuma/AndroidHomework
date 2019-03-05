package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.lang.String;


public class MainActivity extends AppCompatActivity {
    WeatherForecast myForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myForecast=new WeatherForecast();
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://t.weather.sojson.com/api/weather/city/101040100";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(3000);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));



                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    //Log.d("TAG", line);
                    buffer.append(line + "\n");
                }
                JSONObject weatherJson= new JSONObject(buffer.toString());

               // Log.i("MYMES", json);
                //Toast.makeText(getApplicationContext(),"点击",Toast.LENGTH_LONG).show();

                //Log.d("TAG",weatherJson.getString("time"));
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            //Update the temperature displayed
            if(jsonData==null){
                Toast.makeText(MainActivity.this, "网络连接失败或服务器无响应", Toast.LENGTH_SHORT).show();
                return;
            }

            try{
                JSONObject weatherJson= new JSONObject(jsonData);
                // Log.i("系统更新时间：",weatherJson.getString("time"));
                JSONObject cityInfoJson=new JSONObject(weatherJson.getString("cityInfo"));
                JSONObject dataJson=new JSONObject(weatherJson.getString("data"));
                JSONArray forecastJsonArray=new JSONArray(dataJson.getString("forecast"));
               // Log.i("第一天数据：",((JSONObject)forecastJsonArray.get(0)).getString("week"));

               // Log.i("位置:",cityInfoJson.getString("city"));
                myForecast.setCity(cityInfoJson.getString("city"));
                myForecast.setWendu(dataJson.getString("wendu"));

                for(int i=0;i<5;i++){
                    String week=((JSONObject)forecastJsonArray.get(i)).getString("week");
                    String type=((JSONObject)forecastJsonArray.get(i)).getString("type");
                    String ymd=((JSONObject)forecastJsonArray.get(i)).getString("ymd");
                    myForecast.setForecastDate(week,ymd,type,i);
                }



            }catch (Exception e){
                e.printStackTrace();
            }
            updatePanel();
            Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
        }

    }

    public void updatePanel(){
        //Log.i("test: ", json);
        //return;
        ((TextView)findViewById(R.id.tv_location)).setText(myForecast.getCity());
        ((TextView)findViewById(R.id.tv_date)).setText((myForecast.getForecastInfo(0)).getYmd());
        ((TextView)findViewById(R.id.temperature_of_the_day)).setText(myForecast.getWendu());
        ((TextView)findViewById(R.id.week_today)).setText(myForecast.getForecastInfo(0).getWeek());
        ((TextView)findViewById(R.id.week_today_plus1)).setText(myForecast.getForecastInfo(1).getWeek());
        ((TextView)findViewById(R.id.week_today_plus2)).setText(myForecast.getForecastInfo(2).getWeek());
        ((TextView)findViewById(R.id.week_today_plus3)).setText(myForecast.getForecastInfo(3).getWeek());
        ((TextView)findViewById(R.id.week_today_plus4)).setText(myForecast.getForecastInfo(4).getWeek());
        updateWeatherIcon((ImageView)findViewById(R.id.img_weather_condition),myForecast.getForecastInfo(0).getType());
        updateWeatherIcon((ImageView)findViewById(R.id.img_weather_condition_plus1),myForecast.getForecastInfo(1).getType());
        updateWeatherIcon((ImageView)findViewById(R.id.img_weather_condition_plus2),myForecast.getForecastInfo(2).getType());
        updateWeatherIcon((ImageView)findViewById(R.id.img_weather_condition_plus3),myForecast.getForecastInfo(3).getType());
        updateWeatherIcon((ImageView)findViewById(R.id.img_weather_condition_plus4),myForecast.getForecastInfo(4).getType());


    }
    public void updateWeatherIcon(ImageView iconImageView,String type){

        switch (type){
            case "晴":
                iconImageView.setImageResource(R.drawable.sunny_small);
                break;
            case "阴":
                iconImageView.setImageResource(R.drawable.partly_sunny_small);
                break;
            case"多云":
                iconImageView.setImageResource(R.drawable.partly_sunny_small);
                break;
            case"霾":
                iconImageView.setImageResource(R.drawable.partly_sunny_small);
                break;
            case"小雨":
                iconImageView.setImageResource(R.drawable.rainy_small);
                break;
            case"大雨":
                iconImageView.setImageResource(R.drawable.rainy_small);
                break;
            case"雪":
                iconImageView.setImageResource(R.drawable.snowy_small);
                break;
                default:
                    break;

        }

    }
}
