import com.prgpascal.jent.JEntReport;
import com.prgpascal.jent.JEntSample;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JEntSampleTest {
    private static final double TESTING_DELTA = 0.0000000000000000001;

    @Test
    public void testSampleInputResultingValues() {
        JEntReport jEntReport = JEntSample.generateReportForSampleFile();

        assertTrue(jEntReport.isBinary());
        assertEquals(0.9999997749810996, jEntReport.getEntropy(), TESTING_DELTA);
        assertEquals(6886080, jEntReport.getTotalCount());
        assertEquals(0.0, jEntReport.getCompressionPercent(), TESTING_DELTA);
        assertEquals(2.14806043496445, jEntReport.getChiSquare(), TESTING_DELTA);
        assertEquals(0.14275012362450024, jEntReport.getChiSquareProbability(), TESTING_DELTA);
        assertEquals(0.5002792590269065, jEntReport.getAritmeticMean(), TESTING_DELTA);
        assertEquals(3.140025094102886, jEntReport.getMonteCarloPI(), TESTING_DELTA);
        assertEquals(0.04989696818637273, jEntReport.getMonteCarloError(), TESTING_DELTA);
        assertEquals(5.666290773151834E-4, jEntReport.getSerialCorrelation(), TESTING_DELTA);
    }

    @Test
    public void testSampleInputResultingStringMessage() {
        JEntReport jEntReport = JEntSample.generateReportForSampleFile();
        assertEquals("Entropy = 0,999999774981 bits per bit.\n" +
                "\n" +
                "Optimum compression would reduce the size \n" +
                "of this 6886080 bit file by 0.0 percent. \n" +
                "\n" +
                "Chi square distribution for 6886080 samples is 2,148060434964, and randomly \n" +
                "would exceed this value 14,275012362450 percent of the times.\n" +
                "\n" +
                "Arithmetic mean value of data bits is 0,500279259027 (0.5 = random).\n" +
                "Monte Carlo value for Pi is 3,140025094103 (error 0.04989696818637273 percent).\n" +
                "Serial correlation coefficient is 0,000566629077 (totally uncorrelated = 0.0).\n", jEntReport.toString());
    }

}
