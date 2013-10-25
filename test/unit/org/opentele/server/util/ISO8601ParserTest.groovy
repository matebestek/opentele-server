package org.opentele.server.util

import org.junit.*

class ISO8601ParserTest {
	@Test
    void testParsesDates() {
        Date date1 = ISO8601DateParser.parse("2012-05-17T18:25:38Z")
        assert date1.time == 1337279138000

        Date date2 = ISO8601DateParser.parse("2012-05-17T18:30:38Z")
        assert date2.time == 1337279438000

        Date date3 = ISO8601DateParser.parse("2012-05-17T18:35:38Z")
        assert date3.time == 1337279738000

        Date date4 = ISO8601DateParser.parse("2012-05-17T20:35:38+02:00")
        assert date4.time == 1337279738000
	}
}
