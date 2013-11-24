package org.opentele.server.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

public class ISO8601DateParser {
    private static final DateTimeFormatter ISO_8601_FORMAT = ISODateTimeFormat.dateTimeParser();

    public static Date parse(String input) throws java.text.ParseException {
        return ISO_8601_FORMAT.parseDateTime(input).toDate();
    }
}