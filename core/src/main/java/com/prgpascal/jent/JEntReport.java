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

public class JEntReport {
    private boolean isBinary;
    private double entropy;
    private int totalCount;
    private double compressionPercent;
    private double chiSquare;
    private double chiSquareProbability;
    private double aritmeticMean;
    private double monteCarloPI;
    private double monteCarloError;
    private double serialCorrelation;

    public boolean isBinary() {
        return isBinary;
    }

    public void setBinary(boolean isBinary) {
        this.isBinary = isBinary;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getCompressionPercent() {
        return compressionPercent;
    }

    public void setCompressionPercent(double compressionPercent) {
        this.compressionPercent = compressionPercent;
    }

    public double getChiSquare() {
        return chiSquare;
    }

    public void setChiSquare(double chiSquare) {
        this.chiSquare = chiSquare;
    }

    public double getChiSquareProbability() {
        return chiSquareProbability;
    }

    public void setChiSquareProbability(double chiSquareProbability) {
        this.chiSquareProbability = chiSquareProbability;
    }

    public double getAritmeticMean() {
        return aritmeticMean;
    }

    public void setAritmeticMean(double aritmeticMean) {
        this.aritmeticMean = aritmeticMean;
    }

    public double getMonteCarloPI() {
        return monteCarloPI;
    }

    public void setMonteCarloPI(double monteCarloPI) {
        this.monteCarloPI = monteCarloPI;
    }

    public double getMonteCarloError() {
        return monteCarloError;
    }

    public void setMonteCarloError(double monteCarloError) {
        this.monteCarloError = monteCarloError;
    }

    public double getSerialCorrelation() {
        return serialCorrelation;
    }

    public void setSerialCorrelation(double serialCorrelation) {
        this.serialCorrelation = serialCorrelation;
    }

    @Override
    public String toString() {
        String result = "";

        // Entropy
        result += "Entropy = " + formatDouble(entropy) + " bits per ";
        result += (isBinary ? "bit" : "byte") + ".\n";
        result += "\n";

        // Compression
        result += "Optimum compression would reduce the size \n";
        result += "of this " + totalCount + (isBinary ? " bit " : " byte ") + "file by ";
        result += compressionPercent + " percent. \n";
        result += "\n";

        // Chi-square
        result += "Chi square distribution for " + totalCount + " samples is " + formatDouble(chiSquare);
        result += ", and randomly \n";
        if (chiSquareProbability < 0.0001) {
            result += "would exceed this value less than 0.01 percent of the times.\n";
        } else if (chiSquareProbability > 0.9999) {
            result += "would exceed this value more than than 99.99 percent of the times.\n";
        } else {
            result += "would exceed this value " + formatDouble(chiSquareProbability * 100);
            result += " percent of the times.\n";
        }
        result += "\n";

        // Aritmetic mean
        result += "Arithmetic mean value of data ";
        result += (isBinary ? "bits" : "bytes");
        result += " is " + formatDouble(aritmeticMean);
        result += " (" + (isBinary ? 0.5 : 127.5) + " = random).";
        result += "\n";

        // Monte Carlo value
        result += "Monte Carlo value for Pi is " + formatDouble(monteCarloPI);
        result += " (error " + monteCarloError + " percent).";
        result += "\n";

        // Serial correlation
        result += "Serial correlation coefficient is ";
        if (serialCorrelation >= -99999) {
            result += formatDouble(serialCorrelation);
            result += " (totally uncorrelated = 0.0).";
        } else {
            result += "undefined (all values equal!).";
        }
        result += "\n";

        return result;
    }

    private String formatDouble(double value) {
        return String.format("%.12f", value);
    }

}
