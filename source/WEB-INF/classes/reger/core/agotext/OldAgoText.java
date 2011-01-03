package reger.core.agotext;

import reger.core.TimeUtils;
import reger.core.DateDiff;
import reger.core.Util;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jun 18, 2009
 * Time: 3:54:51 PM
 */
public class OldAgoText {

    public static String agoText(Calendar indate){
	    Calendar indateClone;
	    try{
	        indateClone = (Calendar) indate.clone();
        } catch (Exception e){
            reger.core.Debug.debug(4, "agotext.java", "input to agoText(indate) was not a date.");
            indateClone = Calendar.getInstance();
        }

        //Get time on the physical server (probably in Atlanta)
        Calendar now = Calendar.getInstance();
        //Convert from server (not GMT... server in Atlanta) time to gmt timezone.
        now = TimeUtils.convertFromOneTimeZoneToAnother(now, now.getTimeZone().getID(), "GMT");


		//Calculate datediff at various units
		int yearsago= DateDiff.dateDiff("year", now, indateClone);
		int monthsago= DateDiff.dateDiff("month", now, indateClone);
		int weeksago= DateDiff.dateDiff("week", now, indateClone);
		int daysago= DateDiff.dateDiff("day", now, indateClone);
		int hoursago= DateDiff.dateDiff("hour", now, indateClone);
		int minutesago= DateDiff.dateDiff("minute", now, indateClone);
		int secondsago= DateDiff.dateDiff("second", now, indateClone);
		int millisago= DateDiff.dateDiff("milli", now, indateClone);

		//Initialize output string
		String result="";

		//Used for debugging
		//String tmp="";
		//tmp = tmp + "<br>yearsago:" + yearsago;
		//tmp = tmp + "<br>monthsago:" + monthsago;
		//tmp = tmp + "<br>daysago:" + daysago;
		//tmp = tmp + "<br>hoursago:" + hoursago;
		//tmp = tmp + "<br>minutesago:" + minutesago;
		//tmp = tmp + "<br>secondsago:" + secondsago;
		//tmp = tmp + "<br>millisago:" + millisago;
		//tmp = tmp + "<br>";

		//Reminder:Need to deal with "Yesterday" case

		//Determine the best way to say it
		if (Util.qAbs(secondsago) < 60) {
			//Seconds
			result = agoTextEnd(secondsago, "Second");
		} else {
			if (Util.qAbs(minutesago) < 60) {
				//Minutes
				result = agoTextEnd(minutesago, "Minute");
			} else {
				if (Util.qAbs(hoursago) < 24) {
					//Hours
					result = agoTextEnd(hoursago, "Hour");
				} else {
					if (Util.qAbs(daysago) < 31) {
						//Days
						result = agoTextEnd(daysago, "Day");
						//Special for yesterday
                        if (daysago==1){
                            result = "Yesterday";
                        }
                        //Special for today
                        if (daysago==0){
                            result = "Today";
                        }
                        //Special for tomorrow
                        if (daysago==-1){
                            result = "Tomorrow";
                        }
					} else {
						if (Util.qAbs(weeksago) < 4) {
							//Weeks
							result = agoTextEnd(weeksago, "Week");
						} else {
							if (Util.qAbs(monthsago) < 12) {
								//Months
								result = agoTextEnd(monthsago, "Month");
							} else {
								//Years
								result = agoTextEnd(yearsago, "Year");
							}
						}
					}
				}
			}
		}

		//Return the result to the user
		return result;
	}





	/**
	* Figures out the "Units Ago" part of it
	*/
	public static String agoTextEnd(int diff, String unit){
		String end="";
		String suffix = agoTextSuffix(diff);
		String ess = agoTextEss(diff);
		end = Util.qAbs(diff) + " " + unit + ess + " " + suffix;
		return end;
	}

	/**
	* Determines Ago or Future based on sign of diff
	*/
	public static String agoTextSuffix(int diff){
		String suffix="";
		if (diff >= 0 ) {
			suffix="Ago";
		} else {
			suffix="In The Future";
		}
		return suffix;
	}

	/**
	* Determines whether we need to add an "s" to the end
	*/
	public static String agoTextEss(int diff){
		String s="";
		if (diff > 1 || diff < -1 ) {
			s="s";
		} else {
			s="";
		}
		return s;
	}


}
