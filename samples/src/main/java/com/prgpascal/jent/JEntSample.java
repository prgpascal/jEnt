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
import java.nio.charset.Charset;

import com.prgpascal.jent.JEnt;
import com.prgpascal.jent.JEntSettings;
import com.prgpascal.jent.JEntReport;

public class JEntSample {

	public static void main(String[] args) {
		JEntSettings jEntSettings = new JEntSettings.Builder()
				.setIsBinary(true)
				.setCharset(Charset.forName(JEnt.ISO88591))
				.setInputFile(new File("input/inputFile.txt"))
				.build();
		
		JEntReport report = JEnt.executeTests(jEntSettings);
		System.out.println(report.toString());
	}

}
