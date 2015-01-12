/****************************************************************************/
/*  File:       WriteTest.java                                              */
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
import static org.expath.file.TestTools.assertFileEquals;
import org.expath.tools.model.Element;
import org.expath.tools.model.Sequence;
import org.expath.tools.model.dom.DomElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for the `write*` functions in {@link InputOutput}.
 * 
 * @author Florent Georges
 * @date   2015-01-11
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class WriteTest
{
    // TODO: Add more tests with more complex sequences.
    @Test
    public void writeSequence_simpleString()
            throws Exception
    {
        String xml = "<root>First line.&#10;</root>";
        Element elem = DomElement.parseString(xml);
        Sequence seq = elem.getContent();
        String file = WRITE_01.getAbsolutePath();
        Write sut = new Write();
        sut.write(file, seq);
        assertFileEquals(WRITE_01, "First line.\n",
                "The content of the text file after write");
    }

    @Test
    public void writeSequence_overwriteSimpleString()
            throws Exception
    {
        String xml = "<root>First line.&#10;</root>";
        Element elem = DomElement.parseString(xml);
        Sequence seq = elem.getContent();
        String file = WRITE_02.getAbsolutePath();
        Write sut = new Write();
        sut.write(file, seq);
        assertFileEquals(WRITE_02, "First line.\n",
                "The content of the text file after write");
    }

    @Test
    public void writeBinary_simpleBinary()
            throws Exception
    {
        byte[] bytes = { 0b100, 0b101, 0b110, 0b111 };
        String file = WRITE_03.getAbsolutePath();
        Write sut = new Write();
        sut.writeBinary(file, bytes);
        byte[] expect = { 0b100, 0b101, 0b110, 0b111 };
        assertFileEquals(WRITE_03, expect,
                "The content of the binary file after write");
    }

    // TODO: Add tests with encoding.
    @Test
    public void writeText_simpleString()
            throws Exception
    {
        String str = "First line.\n";
        String file = WRITE_04.getAbsolutePath();
        Write sut = new Write();
        sut.writeText(file, str);
        assertFileEquals(WRITE_04, "First line.\n",
                "The content of the text file after writing text");
    }

    @Test
    public void writeTextLines_simpleStrings()
            throws Exception
    {
        List<String> lines = new ArrayList<>();
        lines.add("First line.");
        lines.add("Second line.");
        lines.add("Third line.");
        String file = WRITE_05.getAbsolutePath();
        Write sut = new Write();
        sut.writeTextLines(file, lines);
        assertFileEquals(WRITE_05, "First line.\nSecond line.\nThird line.\n",
                "The content of the text file after writing text lines");
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        WRITE    = TestTools.initArea("write");
        WRITE_01 = new File(WRITE, "first.txt");
        WRITE_02 = new File(WRITE, "second.txt");
        WRITE_03 = new File(WRITE, "third.bin");
        WRITE_04 = new File(WRITE, "fourth.txt");
        WRITE_05 = new File(WRITE, "fifth.txt");
    }

    // ----------------------------------------------------------------------
    //   Setup utility functions
    // ----------------------------------------------------------------------

    private static File WRITE    = null;
    private static File WRITE_01 = null;
    private static File WRITE_02 = null;
    private static File WRITE_03 = null;
    private static File WRITE_04 = null;
    private static File WRITE_05 = null;
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
