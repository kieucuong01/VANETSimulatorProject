package vanetsim.rsa;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {
	public String getDate(){
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");
		String str=  String.valueOf(ft.format(date));
		return str;
	}
}
