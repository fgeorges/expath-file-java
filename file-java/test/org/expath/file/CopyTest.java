/****************************************************************************/
/*  File:       CopyTest.java                                               */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-12                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import static org.expath.file.TestTools.assertDirEquals;
import static org.expath.file.TestTools.assertFileEquals;
import static org.expath.file.TestTools.readBinFile;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for the `copy` function in {@link InputOutput}.
 * 
 * @author Florent Georges
 * @date   2015-01-12
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class CopyTest
{
    @Test
    public void copyDirToDir_simple()
            throws Exception
    {
        File source   = new File(DIR_TO_DIR, "source");
        File target   = new File(DIR_TO_DIR, "target");
        File expected = new File(DIR_TO_DIR, "result");
        InputOutput sut = new InputOutput();
        sut.copy(source.getAbsolutePath(), target.getAbsolutePath());
        assertDirEquals(expected, target, "The target dir after the merge copy");
    }

    @Test
    public void copyDirToFile_simple()
            throws Exception
    {
        File source = new File(DIR_TO_FILE, "source");
        File target = new File(DIR_TO_FILE, "target.txt");
        InputOutput sut = new InputOutput();
        try {
            sut.copy(source.getAbsolutePath(), target.getAbsolutePath());
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.EXISTS ) {
                fail("Wrong exception thrown (must be EXISTS): " + ex.getType(), ex);
            }
        }
    }

    @Test
    public void copyDirToNew_simple()
            throws Exception
    {
        File source = new File(DIR_TO_NEW, "source");
        File target = new File(DIR_TO_NEW, "target");
        InputOutput sut = new InputOutput();
        sut.copy(source.getAbsolutePath(), target.getAbsolutePath());
        assertDirEquals(source, target, "The target dir after the new copy");
    }

    @Test
    public void copyFileToDir_simple()
            throws Exception
    {
        File source   = new File(FILE_TO_DIR, "source.txt");
        File target   = new File(FILE_TO_DIR, "target");
        File expected = new File(FILE_TO_DIR, "result");
        InputOutput sut = new InputOutput();
        sut.copy(source.getAbsolutePath(), target.getAbsolutePath());
        assertDirEquals(expected, target, "The target dir after the new file copy");
    }

    @Test
    public void copyFileToFile_simple()
            throws Exception
    {
        File source   = new File(FILE_TO_FILE, "source.txt");
        File target   = new File(FILE_TO_FILE, "target.txt");
        File expected = new File(FILE_TO_FILE, "result.txt");
        InputOutput sut = new InputOutput();
        sut.copy(source.getAbsolutePath(), target.getAbsolutePath());
        assertFileEquals(expected, readBinFile(target),
                "The target file after the file overwrite copy");
    }

    @Test
    public void copyFileToNew_simple()
            throws Exception
    {
        File source   = new File(FILE_TO_NEW, "source.txt");
        File target   = new File(FILE_TO_NEW, "target.txt");
        File expected = new File(FILE_TO_NEW, "result.txt");
        InputOutput sut = new InputOutput();
        sut.copy(source.getAbsolutePath(), target.getAbsolutePath());
        assertFileEquals(expected, readBinFile(target),
                "The target file after the file new copy");
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        COPY         = TestTools.initArea("copy");
        DIR_TO_DIR   = new File(COPY, "dir-to-dir");
        DIR_TO_FILE  = new File(COPY, "dir-to-file");
        DIR_TO_NEW   = new File(COPY, "dir-to-new");
        FILE_TO_DIR  = new File(COPY, "file-to-dir");
        FILE_TO_FILE = new File(COPY, "file-to-file");
        FILE_TO_NEW  = new File(COPY, "file-to-new");
    }

    // ----------------------------------------------------------------------
    //   Setup utility functions
    // ----------------------------------------------------------------------

    private static File COPY         = null;
    private static File DIR_TO_DIR   = null;
    private static File DIR_TO_FILE  = null;
    private static File DIR_TO_NEW   = null;
    private static File FILE_TO_DIR  = null;
    private static File FILE_TO_FILE = null;
    private static File FILE_TO_NEW  = null;
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
