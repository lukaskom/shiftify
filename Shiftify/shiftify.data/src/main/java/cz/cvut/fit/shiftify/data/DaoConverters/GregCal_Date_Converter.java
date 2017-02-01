package cz.cvut.fit.shiftify.data.DaoConverters;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;
import java.util.GregorianCalendar;

import cz.cvut.fit.shiftify.data.Utilities;

/**
 * Created by lukas on 19.01.2017.
 */

public class GregCal_Date_Converter implements PropertyConverter<GregorianCalendar, String> {
    @Override
    public GregorianCalendar convertToEntityProperty(String databaseValue) {
        return Utilities.StrToGregCal(databaseValue, Utilities.CalType.DATE);
    }
    @Override
    public String convertToDatabaseValue(GregorianCalendar entityProperty) {
        return Utilities.GregCalToStr(entityProperty, Utilities.CalType.DATE);
    }
}
