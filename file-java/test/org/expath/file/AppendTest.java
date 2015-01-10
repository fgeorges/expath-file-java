/****************************************************************************/
/*  File:       AppendTest.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-09                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.expath.tools.model.Element;
import org.expath.tools.model.Sequence;
import org.expath.tools.model.dom.DomElement;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for the `append` functions in {@link InputOutput}.
 * 
 * @author Florent Georges
 * @date   2015-01-09
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class AppendTest
{
    // TODO: Add more tests with more complex sequences.
    @Test
    public void appendSequence_simpleString()
            throws Exception
    {
        String xml = "<root>Second line.&#10;</root>";
        Element elem = DomElement.parseString(xml);
        Sequence seq = elem.getContent();
        String file = APPEND_01.getAbsolutePath();
        InputOutput sut = new InputOutput();
        sut.append(file, seq);
        String result = readTextFile(APPEND_01);
        assertEquals(result, "First line.\nSecond line.\n",
                "The content of the text file after append");
    }

    @Test
    public void appendBinary_simpleBinary()
            throws Exception
    {
        byte[] bytes = { 0b100, 0b101, 0b110, 0b111 };
        String file = APPEND_02.getAbsolutePath();
        InputOutput sut = new InputOutput();
        sut.appendBinary(file, bytes);
        byte[] result = readBinFile(APPEND_02);
        byte[] expect = { 0b0, 0b1, 0b10, 0b11, 0b100, 0b101, 0b110, 0b111 };
        assertEquals(result, expect, "The content of the binary file after append");
    }

    // TODO: Add tests with encoding.
    @Test
    public void appendText_simpleString()
            throws Exception
    {
        String str = "Second line.\n";
        String file = APPEND_03.getAbsolutePath();
        InputOutput sut = new InputOutput();
        sut.appendText(file, str);
        String result = readTextFile(APPEND_03);
        assertEquals(result, "First line.\nSecond line.\n",
                "The content of the text file after text append");
    }

    @Test
    public void appendTextLines_simpleStrings()
            throws Exception
    {
        List<String> lines = new ArrayList<>();
        lines.add("Second line.");
        lines.add("Third line.");
        String file = APPEND_04.getAbsolutePath();
        InputOutput sut = new InputOutput();
        sut.appendTextLines(file, lines);
        String result = readTextFile(APPEND_04);
        assertEquals(result, "First line.\nSecond line.\nThird line.\n",
                "The content of the text file after text append");
    }

    // ----------------------------------------------------------------------
    //   Utility functions
    // ----------------------------------------------------------------------

    private String readTextFile(File f)
            throws IOException
    {
        try ( BufferedReader in = new BufferedReader(new FileReader(f)) ) {
            StringBuilder buf = new StringBuilder();
            String line = in.readLine();
            while ( line != null ) {
                buf.append(line);
                buf.append(System.lineSeparator());
                line = in.readLine();
            }
            return buf.toString();
        }
    }

    private byte[] readBinFile(File f)
            throws IOException
    {
        Path p = f.toPath();
        return Files.readAllBytes(p);
    }

    @AfterClass
    public static void tearDownClass()
            throws Exception
    {
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        // validate dirs exist
        validateDir(PWD);
        validateDir(RSRC);
        validateDir(INITIAL);
        validateDir(STAGE);
        // delete test-src/stage/
        deleteDir(STAGE);
        // copy test-rsrc/initial/ to test-src/stage/ again
        copyDir(INITIAL, STAGE);
        // create "append #2"
        writeBin(APPEND_02, 0b0, 0b1, 0b10, 0b11);
    }

    // ----------------------------------------------------------------------
    //   Setup utility functions
    // ----------------------------------------------------------------------

    private static void validateDir(File d)
    {
        if ( ! d.exists() ) {
            throw new RuntimeException("Directory does not exist: " + d);
        }
        if ( ! d.isDirectory() ) {
            throw new RuntimeException("Directory is not a directory: " + d);
        }
    }

    private static boolean deleteDir(File f)
    {
        if ( f.isDirectory() ) {
            boolean res = true;
            for ( File child : f.listFiles() ) {
                res = deleteDir(child) && res;
            }
            res = f.delete() && res;
            return res;
        }
        else {
            return f.delete();
        }
    }

    private static void copyDir(File from, File to)
    {
        File[] files = from.listFiles();
        if ( files == null ) {  // null if security restricted
            throw new RuntimeException("Failed to list contents of " + from);
        }
        if ( to.exists() ) {
            throw new RuntimeException("Destination '" + to + "' exists");
        }
        if ( ! to.mkdirs() ) {
            throw new RuntimeException("Destination '" + to + "' directory cannot be created");
        }
        if ( ! to.canWrite() ) {
            throw new RuntimeException("Destination '" + to + "' cannot be written to");
        }
        for ( File file : files ) {
            File copied = new File(to, file.getName());
            if ( file.isDirectory() ) {
                copyDir(file, copied);
            }
            else {
                try {
                    byte[] buffer = new byte[4096];
                    InputStream in = new FileInputStream(file);
                    OutputStream out = new FileOutputStream(copied);
                    int count;
                    while ( (count = in.read(buffer)) > 0 ) {
                        out.write(buffer, 0, count);
                    }
                }
                catch ( IOException ex ) {
                    throw new RuntimeException("Error copying '" + file + "' to '" + copied + "'", ex);
                }
            }
        }
    }

    private static void writeBin(File f, int... bytes)
            throws IOException
    {
        OutputStream out = new FileOutputStream(f);
        for ( int b : bytes ) {
            out.write(b);
        }
    }

    private static final String PWD_PROP  = System.getProperty("user.dir");
    private static final File   PWD       = new File(PWD_PROP);
    private static final File   RSRC      = new File(PWD, "test-rsrc");
    private static final File   INITIAL   = new File(RSRC, "initial");
    private static final File   STAGE     = new File(RSRC, "stage");
    private static final File   APPEND    = new File(STAGE, "append");
    private static final File   APPEND_01 = new File(APPEND, "first.txt");
    private static final File   APPEND_02 = new File(APPEND, "second.bin");
    private static final File   APPEND_03 = new File(APPEND, "third.txt");
    private static final File   APPEND_04 = new File(APPEND, "fourth.txt");
}


/* ------------------------------------------------------------------------ */
/*  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS COMMENT.               */
/*                                                                          */
/*  The contents of this file are subject to the Mozilla Public License     */
/*  Version 1.0 (the "License"); you may not use this file except in        */
/*  compliance with the License. You may obtain a copy of the License at    */
/*  http://www.mozilla.org/MPL/.                                            */
/*                                                                          */
/*  Software distributed under the License is distributed on an "AS IS"     */
/*  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See    */
/*  the License for the specific language governing rights and limitations  */
/*  under the License.                                                      */
/*                                                                          */
/*  The Original Code is: all this file.                                    */
/*                                                                          */
/*  The Initial Developer of the Original Code is Florent Georges.          */
/*                                                                          */
/*  Contributor(s): none.                                                   */
/* ------------------------------------------------------------------------ */
