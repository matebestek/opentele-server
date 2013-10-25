package org.opentele.server.util

import org.junit.*
import org.opentele.server.util.NumberFormatUtil;

class NumberFormatUtilTest {
	@Test
    void testParse() {
        assert NumberFormatUtil.parse('37,5').doubleValue() == 37.5
	}

    @Test
    void testCanFormatFloats() {
        assert NumberFormatUtil.formatFloat(37.5f) == '37,5'
        assert NumberFormatUtil.formatFloat(37f) == '37'
        assert NumberFormatUtil.formatFloat(37.45f) == '37,45'
        assert NumberFormatUtil.formatFloat(0f) == '0'
    }

    @Test
    void testCanFormatDoubles() {
        assert NumberFormatUtil.formatDouble(3) == '3'
        assert NumberFormatUtil.formatDouble(123000) == '123000'
        assert NumberFormatUtil.formatDouble(10.5) == '10,5'
        assert NumberFormatUtil.formatDouble(10.432) == '10,432'
        assert NumberFormatUtil.formatDouble(0.0) == '0'
    }
}
