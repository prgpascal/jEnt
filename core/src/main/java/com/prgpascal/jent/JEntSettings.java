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
import java.nio.charset.StandardCharsets;

public class JEntSettings {
    private File inputFile;
    private Charset charset;
    private boolean isBinary;

    private JEntSettings(File inputFile, Charset charset, boolean isBinary) {
        this.inputFile = inputFile;
        this.charset = charset;
        this.isBinary = isBinary;
    }

    public File getInputFile() {
        return inputFile;
    }

    public Charset getCharset() {
        return charset;
    }

    public boolean isBinary() {
        return isBinary;
    }

    public static class Builder {
        private File inputFile;
        private Charset charset = StandardCharsets.ISO_8859_1;
        private boolean isBinary = false;

        public Builder setInputFile(File inputFile) {
            this.inputFile = inputFile;
            return this;
        }

        public Builder setCharset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder setBinary(boolean isBinary) {
            this.isBinary = isBinary;
            return this;
        }

        public JEntSettings build() {
            // Check the required params
            if (inputFile == null)
                throw new IllegalArgumentException("Input File cannot be null");

            if (!inputFile.exists())
                throw new IllegalArgumentException("Input File does not exist");

            return new JEntSettings(inputFile, charset, isBinary);
        }
    }

}
