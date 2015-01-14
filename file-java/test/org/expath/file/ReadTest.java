/****************************************************************************/
/*  File:       ReadTest.java                                               */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-11                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for the `read*` functions in {@link Read}.
 * 
 * @author Florent Georges
 * @date   2015-01-11
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class ReadTest
{
    @Test
    public void readBinary_all()
            throws Exception
    {
        String file = READ_01.getAbsolutePath();
        Read sut = new Read();
        byte[] result = sut.readBinary(file);
        byte[] expect = { 0b0, 0b1, 0b10, 0b11, 0b100, 0b101, 0b110, 0b111 };
        assertEquals(result, expect, "Reading the binary file");
    }

    @Test
    public void readBinary_withOffset()
            throws Exception
    {
        String file = READ_01.getAbsolutePath();
        Read sut = new Read();
        byte[] result = sut.readBinary(file, 4);
        byte[] expect = { 0b100, 0b101, 0b110, 0b111 };
        assertEquals(result, expect, "Reading the binary file with an offset");
    }

    @Test
    public void readBinary_chunk()
            throws Exception
    {
        String file = READ_01.getAbsolutePath();
        Read sut = new Read();
        byte[] result = sut.readBinary(file, 2, 4);
        byte[] expect = { 0b10, 0b11, 0b100, 0b101 };
        assertEquals(result, expect,
                "Reading the binary file with an offset and a length");
    }

    // TODO: Add tests with encoding.
    @Test
    public void readText_simple()
            throws Exception
    {
        String file = READ_02.getAbsolutePath();
        Read sut = new Read();
        String result = sut.readText(file);
        assertEquals(result, "First line.\n",
                "The content of the text file after writing text");
    }

    // TODO: Add tests with encoding.
    @Test
    public void readTextLines_simple()
            throws Exception
    {
        String file = READ_03.getAbsolutePath();
        Read sut = new Read();
        List<String> result = sut.readTextLines(file);
        List<String> expected = new ArrayList<>();
        expected.add("First line.");
        expected.add("Second line.");
        expected.add("Third line.");
        assertEquals(result, expected, "Reading the text lines");
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        READ    = TestTools.initArea("read");
        READ_01 = new File(READ, "first.bin");
        READ_02 = new File(READ, "second.txt");
        READ_03 = new File(READ, "third.txt");
    }

    private static File READ    = null;
    private static File READ_01 = null;
    private static File READ_02 = null;
    private static File READ_03 = null;
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
