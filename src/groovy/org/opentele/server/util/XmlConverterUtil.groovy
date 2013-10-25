package org.opentele.server.util

import javax.xml.datatype.DatatypeFactory

class XmlConverterUtil {

    // TODO: Zulu time?
    public static def getDateAsXml(Date d) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(d.getTime());
        DatatypeFactory df = DatatypeFactory.newInstance()
        return df.newXMLGregorianCalendar(gc)
    }
}
