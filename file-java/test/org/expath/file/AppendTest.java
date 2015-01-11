/****************************************************************************/
/*  File:       AppendTest.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-09                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.expath.file.TestTools.assertFileEquals;
import org.expath.tools.model.Element;
import org.expath.tools.model.Sequence;
import org.expath.tools.model.dom.DomElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for the `append*` functions in {@link InputOutput}.
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
        assertFileEquals(APPEND_01, "First line.\nSecond line.\n",
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
        byte[] expect = { 0b0, 0b1, 0b10, 0b11, 0b100, 0b101, 0b110, 0b111 };
        assertFileEquals(APPEND_02, expect, "The content of the binary file after append");
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
        assertFileEquals(APPEND_03, "First line.\nSecond line.\n",
                "The content of the text file after appending text");
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
        assertFileEquals(APPEND_04, "First line.\nSecond line.\nThird line.\n",
                "The content of the text file after appending text lines");
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        APPEND    = TestTools.initArea("append");
        APPEND_01 = new File(APPEND, "first.txt");
        APPEND_02 = new File(APPEND, "second.bin");
        APPEND_03 = new File(APPEND, "third.txt");
        APPEND_04 = new File(APPEND, "fourth.txt");
    }

    private static File APPEND    = null;
    private static File APPEND_01 = null;
    private static File APPEND_02 = null;
    private static File APPEND_03 = null;
    private static File APPEND_04 = null;
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
