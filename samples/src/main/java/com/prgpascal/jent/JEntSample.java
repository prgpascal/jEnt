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

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JEntSample {

    public static void main(String[] args) {
        JEntReport report = generateReportForSampleFile();

        if (report.getEntropy() > 7.5) {
            // OK, it may be random...
        } else {
            // mmmh, too little entropy...
        }

        System.out.println(report.toString());
    }

    public static JEntReport generateReportForSampleFile() {
        File sampleFile = getFileFromResources("input/inputFile.txt");

        JEntSettings settings = new JEntSettings.Builder()
                .setBinary(true)
                .setCharset(StandardCharsets.ISO_8859_1)
                .setInputFile(sampleFile).build();

        return JEnt.executeTests(settings);
    }

    private static File getFileFromResources(String fileName) {
        ClassLoader classLoader = JEntSample.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        } else {
            return new File(resource.getFile());
        }
    }

}
