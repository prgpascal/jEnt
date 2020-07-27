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

/*
 * This code was developed by Gary Perlman of the Wang Institute (full
 * citation below) and has been minimally modified for use in this program.
 * Programmer: Gary Perlman Organization: Wang Institute, Tyngsboro, MA
 * 01879 Copyright: none
 */
public class ChiSquareProbability {
    private static final double LOG_SQRT_PI = Math.log(Math.sqrt(Math.PI));
    private static final double I_SQRT_PI = 1 / Math.sqrt(Math.PI);

    // max value to represent exp (x)
    private static final double BIG_X = 20.0;

    // maximum meaningful z value
    private static final double Z_MAX = 6.0;

    private double ex(double x) {
        if (x < -BIG_X)
            return 0.0;
        return Math.exp(x);
    }

    /**
     * Compute probability of measured chi-square value.
     *
     * @param chiSquare  obtained chi-square value.
     * @param freedomDeg degrees of freedom.
     * @return the probability of chi-square value.
     */
    double calculateChiSquareProb(double chiSquare, int freedomDeg) {
        double x = chiSquare;
        double a, y = 0, s;
        double e, c, z;
        boolean isFreedomDegEven;

        if (x <= 0.0 || freedomDeg < 1) {
            return 1.0;
        }

        a = 0.5 * x;
        isFreedomDegEven = (2 * (freedomDeg / 2)) == freedomDeg;
        if (freedomDeg > 1) {
            y = ex(-a);
        }
        s = (isFreedomDegEven ? y : (2.0 * zValueProb(-Math.sqrt(x))));
        if (freedomDeg > 2) {
            x = 0.5 * (freedomDeg - 1.0);
            z = (isFreedomDegEven ? 1.0 : 0.5);
            if (a > BIG_X) {
                e = (isFreedomDegEven ? 0.0 : LOG_SQRT_PI);
                c = Math.log(a);
                while (z <= x) {
                    e = Math.log(z) + e;
                    s += ex(c * z - a - e);
                    z += 1.0;
                }
                return (s);
            } else {
                e = (isFreedomDegEven ? 1.0 : (I_SQRT_PI / Math.sqrt(a)));
                c = 0.0;
                while (z <= x) {
                    e = e * (a / z);
                    c = c + e;
                    z += 1.0;
                }
                return (c * y + s);
            }
        } else {
            return s;
        }
    }

    /**
     * Calculate the probability of normal z value.
     * <p>
     * Adapted from a polynomial approximation in: Ibbetson D, Algorithm 209
     * Collected Algorithms of the CACM 1963 p. 616
     *
     * @param z normal z value.
     * @return cumulative probability from -oo to z
     */
    public double zValueProb(double z) {
        double y, x, w;

        if (z == 0.0) {
            x = 0.0;
        } else {
            y = 0.5 * Math.abs(z);
            if (y >= (Z_MAX * 0.5)) {
                x = 1.0;
            } else if (y < 1.0) {
                w = y * y;
                x = ((((((((0.000124818987 * w - 0.001075204047) * w + 0.005198775019) * w - 0.019198292004) * w
                        + 0.059054035642) * w - 0.151968751364) * w + 0.319152932694) * w - 0.531923007300) * w
                        + 0.797884560593) * y * 2.0;
            } else {
                y -= 2.0;
                x = (((((((((((((-0.000045255659 * y + 0.000152529290) * y - 0.000019538132) * y - 0.000676904986) * y
                        + 0.001390604284) * y - 0.000794620820) * y - 0.002034254874) * y + 0.006549791214) * y
                        - 0.010557625006) * y + 0.011630447319) * y - 0.009279453341) * y + 0.005353579108) * y
                        - 0.002141268741) * y + 0.000535310849) * y + 0.999936657524;
            }
        }
        return (z > 0.0 ? ((x + 1.0) * 0.5) : ((1.0 - x) * 0.5));
    }

}
