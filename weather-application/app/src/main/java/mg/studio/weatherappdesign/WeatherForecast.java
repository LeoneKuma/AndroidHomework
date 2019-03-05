package mg.studio.weatherappdesign;

import android.util.Log;

public class WeatherForecast {
    //private String date;
    private String city;
    private String wendu;
    public ForecastInfo[] ForecastDate;
    public WeatherForecast(){
        ForecastDate=new ForecastInfo[5];
        ForecastDate[0]=new ForecastInfo();
        ForecastDate[1]=new ForecastInfo();
        ForecastDate[2]=new ForecastInfo();
        ForecastDate[3]=new ForecastInfo();
        ForecastDate[4]=new ForecastInfo();



    }
    public void setCity(String city){
        this.city=city;
        return;
    }
    public String getCity(){
        return this.city;
    }
    public void setWendu(String wendu){
        this.wendu=wendu;
    }
    public String getWendu(){
        return this.wendu;
    }
    public void setForecastDate(String week,String ymd,String type,int num){
        if(num<0||num>4){
            return;
        }

        ForecastDate[num].setWeek(week);
        ForecastDate[num].setYmd(ymd);
        ForecastDate[num].setType(type);
        return;
    }
    public ForecastInfo getForecastInfo(int num){
        return ForecastDate[num];
    }


}
 class ForecastInfo {
    private String week;
    private String type;
    private String ymd;
    public ForecastInfo(){
        week="";
        type="";
        ymd="";
    }
    public void setWeek(String week){
        this.week=week;
    }

    public void setType(String type){
        this.type=type;
    }
    public void setYmd(String ymd){
        this.ymd=ymd;
    }
    public String getWeek(){
        return this.week;
    }

    public String getType(){
        return this.type;
    }
    public String getYmd(){
        return this.ymd;
    }

}
