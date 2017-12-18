package vanetsim.rsa;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {
	public String getNewDate(){
		Date date = new Date(2017, 12, 17, 6, 0);
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");
		String str=  String.valueOf(ft.format(date));
		return str;
	}
	
	public String getOldDate(){
		Date date = new Date(2017, 12, 17, 5, 0);
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");
		String str=  String.valueOf(ft.format(date));
		return str;
	}
}
