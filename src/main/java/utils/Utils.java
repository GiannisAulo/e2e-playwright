package utils;

import utils.factory.ContextDataFactory;
import utils.factory.PageObjectFactory;
import utils.factory.ServiceObjectFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;

public class Utils {
    PageObjectFactory pageObjectFactory;
    ServiceObjectFactory serviceObjectFactory;
    public static ContextDataFactory contextFactory;

    public Utils() {
        contextFactory = new ContextDataFactory();
    }


    public PageObjectFactory ui() {
        return pageObjectFactory;
    }

    public ServiceObjectFactory service() {
        return serviceObjectFactory;
    }

    public ContextDataFactory context() {
        return contextFactory;
    }


    /**
     * Find the current date and time
     *
     * @return The datetime
     */
    public String findTheCurrentDateTime(String pattern) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(pattern); //HH:mm
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static Map<String, String> findQuarter(String requestedQuarter) {

        int quarter = 0;
        int year = 0;
        final Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        LocalDate now = LocalDate.now(ZoneId.systemDefault());

        switch (requestedQuarter) {
            case "current + 3":
                now = now.plus(9, ChronoUnit.MONTHS); // Add 9 months to get three quarters ahead
                quarter = (now.get(IsoFields.QUARTER_OF_YEAR)); // Now get the quarter of the updated date
                year = now.getYear(); // And also the year
                break;
            case "current + 2":
                now = now.plus(6, ChronoUnit.MONTHS); // Add 6 months to get two quarters ahead
                quarter = (now.get(IsoFields.QUARTER_OF_YEAR)); // Now get the quarter of the updated date
                year = now.getYear(); // And also the year
                break;
            case "current + 1":
                now = now.plus(3, ChronoUnit.MONTHS); // Add 3 months to get to the next quarter
                quarter = (now.get(IsoFields.QUARTER_OF_YEAR)); // Now get the quarter of the updated date
                year = now.getYear();
                break;
            case "current":
                quarter = now.get(IsoFields.QUARTER_OF_YEAR);
                year = now.getYear();
                break;
            case "previous":
                cal.add(Calendar.MONTH, -3);
                quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                year = cal.get(Calendar.YEAR);
                break;
            case "previous - 1":
                for (int i = 0; i < 2; i++) {
                    cal.add(Calendar.MONTH, -3);
                    quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                    year = cal.get(Calendar.YEAR);
                    if (i == 1)
                        break;
                }
                break;
            case "previous - 2":
                for (int i = 0; i < 3; i++) {
                    cal.add(Calendar.MONTH, -3);
                    quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                    year = cal.get(Calendar.YEAR);
                    if (i == 2)
                        break;
                }
                break;
            case "previous - 3":
                for (int i = 0; i < 4; i++) {
                    cal.add(Calendar.MONTH, -4);
                    quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                    year = cal.get(Calendar.YEAR);
                    if (i == 2)
                        break;
                }
                break;
        }

        Map<String, String> quarterMap = new HashMap<>();
        quarterMap.put("quarter", "Q" + quarter);
        quarterMap.put("year", String.valueOf(year));

        return quarterMap;
    }

    public static String getDateTime(String dateTime) {
        String dateEntry = null;
        if (dateTime != null && !dateTime.isEmpty()) {
            String[] parts = null;
            String dateFormat = null;
            String calCount = null;
            String calData = null;
            parts = dateTime.split(";");
            if (parts.length < 3) {
                dateFormat = parts[0];
                calCount = "0";
                calData = "";
            } else if (parts.length == 3) {
                dateFormat = parts[0];
                calCount = parts[1];
                calData = parts[2];
            } else {
                dateFormat = parts[0];
                calCount = parts[1];
                calData = parts[2];
                calCount = parts[3];
                calData = parts[4];
            }
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Calendar today = Calendar.getInstance();
            switch (calData) {
                case "d":
                    today.add(Calendar.DATE, Integer.parseInt(calCount));
                    break;
                case "M":
                    today.add(Calendar.MONTH, Integer.parseInt(calCount));
                    break;
                case "y":
                    today.add(Calendar.YEAR, Integer.parseInt(calCount));
                    break;
                case "H":
                    today.add(Calendar.HOUR_OF_DAY, Integer.parseInt(calCount));
                    break;
                case "m":
                    today.add(Calendar.MINUTE, Integer.parseInt(calCount));
                    break;
                default:
                    break;
            }
            dateEntry = sdf.format(today.getTime());
        }

        if (dateEntry == null) {
            dateEntry = "";
        }

        return dateEntry;
    }

    public static Calendar stringToCalendar(String date, String dateFormat) {
        Date d = new Date();
        try {
            DateFormat f = new SimpleDateFormat(dateFormat);
            d = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        return cal;
    }

    public static String calendarToString(Calendar calendar, String dateFormat) {
        SimpleDateFormat frmt = new SimpleDateFormat(dateFormat);
        String date = frmt.format(calendar.getTime());
        return date;
    }
}