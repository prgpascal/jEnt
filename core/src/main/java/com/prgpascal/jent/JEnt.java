/*
 * Copyright (C) 2017 Riccardo Leschiutta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prgpascal.jent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JEnt {
    private static final double LOG_2_OF_10 = Math.log(10) / Math.log(2);
    private static final int MAX_CHARS = 256;

    /*
     * Bytes used as Monte Carlo coordinates. This should be no more bits than
     * the mantissa of your "double" floating point type.
     */
    private static final int MONTE_N = 6;

    private long[] mCharsCounter = new long[MAX_CHARS];
    private double[] mCharsProb = new double[MAX_CHARS];
    private int[] mMonteCarlo = new int[MONTE_N];

    private int mTotalCount; // Total bits or bytes counted
    private double mEntropy;
    private double mChiSquare;
    private double mChiSquareProbability;
    private double mArithmeticMean;
    private double mMonteCarloPi;
    private double mSerialCorrelation;

    private JEntSettings mJEntSettings;

    // Support variables
    private boolean sccfirst = true;
    private double sccu0, scct1, scct2, scct3, scclast;
    private int mp;
    private double incirc = Math.pow(Math.pow(256.0, (MONTE_N / 2D)) - 1, 2.0);
    private double inmont, mcount, montex, montey, sccun;

    /**
     * Private constructor
     */
    private JEnt(JEntSettings jEntSettings) {
        mJEntSettings = jEntSettings;
    }

    /**
     * Execute all the randomness tests.
     *
     * @param jEntSettings the settings object, required to execute the randomness tests.
     * @return a test report, containing all the test results.
     */
    public static JEntReport executeTests(JEntSettings jEntSettings) {
        JEnt jEnt = new JEnt(jEntSettings);
        return jEnt.execute();
    }

    private JEntReport execute() {
        readInputFile();
        executeSerialCorrelation();
        calculateArithmeticMeanAndChiSquare();
        calculateChiSquareProbability();
        calculateEntropy();
        calculateMonteCarloPi();

        return generateTestReport();
    }

    private void readInputFile() {
        BufferedReader reader = null;
        try {
            InputStream inputStream = new FileInputStream(mJEntSettings.getInputFile());
            reader = new BufferedReader(new InputStreamReader(inputStream, mJEntSettings.getCharset()));
            int c = -1;
            while ((c = reader.read()) != -1) {
                addNewChar((char) c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    private void addNewChar(int c) {
        int bitIndex = 0;

        do {
            int bitValue;
            if (mJEntSettings.isBinary()) {
                bitValue = ((c >>> 7) & 0x01);
                mCharsCounter[bitValue]++;
            } else {
                mCharsCounter[c]++;
            }

            mTotalCount++;

            /*
             * Update inside / outside circle counts for Monte Carlo computation
             * of PI
             */
            if (bitIndex == 0) {
                mMonteCarlo[mp++] = c; /* Save character for Monte Carlo */
                if (mp >= MONTE_N) { /* Calculate every MONTEN character */
                    int mj;

                    mp = 0;
                    mcount++;
                    montex = montey = 0;
                    for (mj = 0; mj < MONTE_N / 2; mj++) {
                        montex = (montex * 256.0) + mMonteCarlo[mj];
                        montey = (montey * 256.0) + mMonteCarlo[(MONTE_N / 2) + mj];
                    }
                    if ((montex * montex + montey * montey) <= incirc) {
                        inmont++;
                    }
                }
            }

            /* Update calculation of serial correlation coefficient */
            sccun = mJEntSettings.isBinary() ? c & 0x80 : c;
            if (sccfirst) {
                sccfirst = false;
                scclast = 0;
                sccu0 = sccun;
            } else {
                scct1 = scct1 + scclast * sccun;
            }
            scct2 = scct2 + sccun;
            scct3 = scct3 + (sccun * sccun);
            scclast = sccun;

            c <<= 1;

        } while (mJEntSettings.isBinary() && (++bitIndex < 8));
    }

    private void executeSerialCorrelation() {
        scct1 = scct1 + scclast * sccu0;
        scct2 = scct2 * scct2;
        mSerialCorrelation = mTotalCount * scct3 - scct2;
        if (mSerialCorrelation == 0.0) {
            mSerialCorrelation = -100000;
        } else {
            mSerialCorrelation = (mTotalCount * scct1 - scct2) / mSerialCorrelation;
        }
    }

    private void calculateArithmeticMeanAndChiSquare() {
        double expected = mTotalCount / (mJEntSettings.isBinary() ? 2.0 : 256.0);
        double datasum = 0;
        for (int i = 0; i < (mJEntSettings.isBinary() ? 2 : 256); i++) {
            double a = mCharsCounter[i] - expected;
            mCharsProb[i] = ((double) mCharsCounter[i]) / mTotalCount;
            mChiSquare += (a * a) / expected;
            datasum += ((double) i) * mCharsCounter[i];
        }
        mArithmeticMean = datasum / mTotalCount;
    }

    private void calculateChiSquareProbability() {
        int degreeOfFreedom = (mJEntSettings.isBinary() ? 1 : 255);
        mChiSquareProbability = new ChiSquareProbability().calculateChiSquareProb(mChiSquare, degreeOfFreedom);
    }

    private void calculateEntropy() {
        for (int i = 0; i < (mJEntSettings.isBinary() ? 2 : 256); i++) {
            if (mCharsProb[i] > 0.0) {
                mEntropy += mCharsProb[i] * calculateLog2(1 / mCharsProb[i]);
            }
        }
    }

    private void calculateMonteCarloPi() {
        mMonteCarloPi = 4.0 * (((double) inmont) / mcount);
    }

    private double calculateLog2(double value) {
        return LOG_2_OF_10 * Math.log10(value);
    }

    private JEntReport generateTestReport() {
        JEntReport testReport = new JEntReport();
        testReport.setBinary(mJEntSettings.isBinary());
        testReport.setEntropy(mEntropy);
        testReport.setTotalCount(mTotalCount);
        testReport.setCompressionPercent((short) ((100 * ((mJEntSettings.isBinary() ? 1 : 8) - mEntropy)
                / (mJEntSettings.isBinary() ? 1.0 : 8.0))));
        testReport.setChiSquare(mChiSquare);
        testReport.setChiSquareProbability(mChiSquareProbability);
        testReport.setArithmeticMean(mArithmeticMean);
        testReport.setMonteCarloPI(mMonteCarloPi);
        testReport.setMonteCarloError(100.0 * (Math.abs(Math.PI - mMonteCarloPi) / Math.PI));
        testReport.setSerialCorrelation(mSerialCorrelation);
        return testReport;
    }

}
