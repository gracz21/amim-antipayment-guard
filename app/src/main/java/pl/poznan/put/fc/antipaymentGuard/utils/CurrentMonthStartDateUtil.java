package pl.poznan.put.fc.antipaymentGuard.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Kamil Walkowiak
 */
public abstract class CurrentMonthStartDateUtil {
    public static Date getCurrentMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.DATE, 1);

        return calendar.getTime();
    }
}
