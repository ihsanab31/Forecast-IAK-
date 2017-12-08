package forecast.iak.ihsan.forecast.model;

/**
 * Created by ihsan on 12/05/2017.
 */

public class Weather {
    private String dt;
    private String day;
    private String main;

    public Weather() {
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
