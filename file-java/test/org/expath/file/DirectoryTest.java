/****************************************************************************/
/*  File:       DirectoryTest.java                                          */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-12                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test functions related to directory entries.
 * 
 * - file:create-dir
 * - file:create-temp-dir
 * - file:create-temp-file
 * - file:delete
 * - file:list
 * - file:move
 * 
 * @author Florent Georges
 * @date   2015-01-12
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class DirectoryTest
{
    @Test
    public void createDir_new()
            throws Exception
    {
        File dir = new File(CREATE_DIR, "new-dir");
        InputOutput sut = new InputOutput();
        sut.createDir(dir.getAbsolutePath());
        assertTrue(dir.exists(), "New dir must exist");
        assertTrue(dir.isDirectory(), "New dir must be a dir");
    }

    @Test
    public void createDir_exists()
            throws Exception
    {
        File dir = new File(CREATE_DIR, "existing");
        InputOutput sut = new InputOutput();
        sut.createDir(dir.getAbsolutePath());
        assertTrue(dir.exists(), "Existing dir must exist");
        assertTrue(dir.isDirectory(), "Existing dir must be a dir");
    }

    @Test
    public void createDir_onFile()
            throws Exception
    {
        File dir = new File(CREATE_DIR, "file.txt");
        InputOutput sut = new InputOutput();
        try {
            sut.createDir(dir.getAbsolutePath());
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.EXISTS ) {
                fail("Wrong exception thrown (must be EXISTS): " + ex.getType(), ex);
            }
        }
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        DIR        = TestTools.initArea("directory");
        CREATE_DIR = new File(DIR, "create-dir");
    }

    private static File DIR        = null;
    private static File CREATE_DIR = null;
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
