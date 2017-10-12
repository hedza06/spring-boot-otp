package com.starter.springboot.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Converter
public class LocalDateToUtilDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate localDate)
    {
        DateFormat dateFormatLD = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormatLD.format(localDate);
        try {
            DateFormat dateFormatDL = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormatDL.parse(strDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date)
    {
        // format java.util.Date to String
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = dateFormat.format(date);
        // format String to LocalDate
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        return LocalDate.parse(dateString, dateTimeFormatter);
    }
}
