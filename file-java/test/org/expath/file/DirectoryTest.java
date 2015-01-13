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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
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

    @Test
    public void createTempDir_default()
            throws Exception
    {
        InputOutput sut = new InputOutput();
        File result = sut.createTempDir("prefix-", "-suffix");
        // just because it's created outside of this test area...
        System.out.println("Temp dir: " + result);
        assertTrue(result.exists(), "Temp dir must exist: " + result);
        assertTrue(result.isDirectory(), "Temp dir must be a directory: " + result);
        assertTrue(result.getName().startsWith("prefix-"),
                "Temp dir name must start with 'prefix-': " + result);
        assertTrue(result.getName().endsWith("-suffix"),
                "Temp dir name must end with '-suffix': " + result);
    }

    @Test
    public void createTempDir_inDir()
            throws Exception
    {
        InputOutput sut = new InputOutput();
        File result = sut.createTempDir("prefix-", "-suffix", CREATE_DIR.getAbsolutePath());
        assertTrue(result.exists(), "Temp dir must exist: " + result);
        assertTrue(result.isDirectory(), "Temp dir must be a directory: " + result);
        assertTrue(result.getName().startsWith("prefix-"),
                "Temp dir name must start with 'prefix-': " + result);
        assertTrue(result.getName().endsWith("-suffix"),
                "Temp dir name must end with '-suffix': " + result);
        assertEquals(result.getParentFile(), CREATE_DIR,
                "Temp dir parent must be CREATE_DIR: " + result.getParent());
    }

    @Test
    public void createTempFile_default()
            throws Exception
    {
        InputOutput sut = new InputOutput();
        File result = sut.createTempFile("prefix-", "-suffix");
        // just because it's created outside of this test area...
        System.out.println("Temp file: " + result);
        assertTrue(result.exists(), "Temp file must exist: " + result);
        assertFalse(result.isDirectory(), "Temp file must not be a directory: " + result);
        assertTrue(result.getName().startsWith("prefix-"),
                "Temp file name must start with 'prefix-': " + result);
        assertTrue(result.getName().endsWith("-suffix"),
                "Temp file name must end with '-suffix': " + result);
    }

    @Test
    public void createTempFile_inDir()
            throws Exception
    {
        InputOutput sut = new InputOutput();
        File result = sut.createTempFile("prefix-", "-suffix", CREATE_DIR.getAbsolutePath());
        assertTrue(result.exists(), "Temp file must exist: " + result);
        assertFalse(result.isDirectory(), "Temp file must not be a directory: " + result);
        assertTrue(result.getName().startsWith("prefix-"),
                "Temp file name must start with 'prefix-': " + result);
        assertTrue(result.getName().endsWith("-suffix"),
                "Temp file name must end with '-suffix': " + result);
        assertEquals(result.getParentFile(), CREATE_DIR,
                "Temp file parent must be CREATE_DIR: " + result.getParent());
    }

    @Test
    public void delete_notExists()
            throws Exception
    {
        File file = new File(DELETE, "does-not-exist");
        InputOutput sut = new InputOutput();
        try {
            sut.delete(file.getAbsolutePath());
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.NOT_FOUND ) {
                fail("Wrong exception thrown (must be NOT_FOUND): " + ex.getType(), ex);
            }
        }
    }

    @Test
    public void delete_file()
            throws Exception
    {
        File file = new File(DELETE, "file.txt");
        assertTrue(file.exists(), "File must exist before delete: " + file);
        InputOutput sut = new InputOutput();
        sut.delete(file.getAbsolutePath());
        assertFalse(file.exists(), "File must not exist after delete: " + file);
    }

    @Test
    public void delete_emptyDir()
            throws Exception
    {
        File dir = new File(DELETE, "empty-dir");
        assertTrue(dir.exists(), "Dir must exist before delete: " + dir);
        InputOutput sut = new InputOutput();
        sut.delete(dir.getAbsolutePath());
        assertFalse(dir.exists(), "Dir must not exist after delete: " + dir);
    }

    @Test
    public void delete_notEmptyDir()
            throws Exception
    {
        File dir = new File(DELETE, "non-empty-dir");
        InputOutput sut = new InputOutput();
        try {
            sut.delete(dir.getAbsolutePath());
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.IS_DIR ) {
                fail("Wrong exception thrown (must be IS_DIR): " + ex.getType(), ex);
            }
            assertTrue(dir.exists(), "Dir must still exist after delete: " + dir);
            File child = new File(dir, "file.txt");
            assertTrue(child.exists(), "Child must still exist after delete: " + child);
        }
    }

    @Test
    public void delete_dir()
            throws Exception
    {
        File dir = new File(DELETE, "dir");
        assertTrue(dir.exists(), "Dir must exist before delete: " + dir);
        InputOutput sut = new InputOutput();
        sut.delete(dir.getAbsolutePath(), true);
        assertFalse(dir.exists(), "Dir must not exist after delete: " + dir);
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
        DELETE     = new File(DIR, "delete");
    }

    private static File DIR        = null;
    private static File CREATE_DIR = null;
    private static File DELETE     = null;
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
