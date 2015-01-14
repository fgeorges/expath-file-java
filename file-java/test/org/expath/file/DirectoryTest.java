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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test directory entries functions in {@link InputOutput}.
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
            fail("Must throw an EXISTS exception");
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
            fail("Must throw a NOT_FOUND exception");
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
        File dir = DELETE_EMPTY;
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
            fail("Must throw an IS_DIR exception");
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

    @Test
    public void list_notExists()
            throws Exception
    {
        File file = new File(LIST, "does-not-exist");
        InputOutput sut = new InputOutput();
        try {
            sut.list(file.getAbsolutePath());
            fail("Must throw a NO_DIR exception");
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.NO_DIR ) {
                fail("Wrong exception thrown (must be NO_DIR): " + ex.getType(), ex);
            }
        }
    }

    @Test
    public void list_file()
            throws Exception
    {
        File file = new File(LIST, "file.txt");
        InputOutput sut = new InputOutput();
        try {
            sut.list(file.getAbsolutePath());
            fail("Must throw a NO_DIR exception");
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.NO_DIR ) {
                fail("Wrong exception thrown (must be NO_DIR): " + ex.getType(), ex);
            }
        }
    }

    @Test
    public void list_empty()
            throws Exception
    {
        File dir = LIST_EMPTY;
        InputOutput sut = new InputOutput();
        List<String> result = sut.list(dir.getAbsolutePath());
        Collections.sort(result);
        List<String> expected = new ArrayList<>();
        assertEquals(result, expected, "Files listed are wrong: " + dir);
    }

    @Test
    public void list_nonEmpty()
            throws Exception
    {
        File dir = new File(LIST, "non-empty-dir");
        InputOutput sut = new InputOutput();
        List<String> result = sut.list(dir.getAbsolutePath());
        Collections.sort(result);
        List<String> expected = new ArrayList<>();
        expected.add("file.txt");
        assertEquals(result, expected, "Files listed are wrong: " + dir);
    }

    @Test
    public void list_nonRecursive()
            throws Exception
    {
        File dir = new File(LIST, "dir");
        InputOutput sut = new InputOutput();
        List<String> result = sut.list(dir.getAbsolutePath());
        Collections.sort(result);
        List<String> expected = new ArrayList<>();
        expected.add("file.txt");
        expected.add("subdir");
        assertEquals(result, expected, "Files listed are wrong: " + dir);
    }

    @Test
    public void list_nonRecursive_2()
            throws Exception
    {
        File dir = new File(LIST, "dir");
        InputOutput sut = new InputOutput();
        List<String> result = sut.list(dir.getAbsolutePath(), false);
        Collections.sort(result);
        List<String> expected = new ArrayList<>();
        expected.add("file.txt");
        expected.add("subdir");
        assertEquals(result, expected, "Files listed are wrong: " + dir);
    }

    @Test
    public void list_recursive()
            throws Exception
    {
        File dir = new File(LIST, "dir");
        InputOutput sut = new InputOutput();
        List<String> result = sut.list(dir.getAbsolutePath(), true);
        Collections.sort(result);
        List<String> expected = new ArrayList<>();
        expected.add("file.txt");
        expected.add("subdir");
        expected.add("subdir/file.txt");
        assertEquals(result, expected, "Files listed are wrong: " + dir);
    }

    @Test
    public void list_pattern()
            throws Exception
    {
        File dir = new File(LIST, "dir");
        InputOutput sut = new InputOutput();
        List<String> result = sut.list(dir.getAbsolutePath(), true, "*.txt");
        Collections.sort(result);
        List<String> expected = new ArrayList<>();
        expected.add("file.txt");
        expected.add("subdir/file.txt");
        assertEquals(result, expected, "Files listed are wrong: " + dir);
    }

    @Test
    public void move_noSource()
            throws Exception
    {
        File source = new File(LIST, "does-not-exist");
        File target = new File(LIST, "doesnt-exists-either");
        assertFalse(source.exists(), "File must not exist: " + source);
        assertFalse(target.exists(), "File must not exist: " + target);
        // do it
        InputOutput sut = new InputOutput();
        try {
            sut.move(source.getAbsolutePath(), target.getAbsolutePath());
            fail("Must throw a NOT_FOUND exception");
        }
        catch ( FileException ex ) {
            if ( ex.getType() != FileException.Type.NOT_FOUND ) {
                fail("Wrong exception thrown (must be NOT_FOUND): " + ex.getType(), ex);
            }
            assertFalse(source.exists(), "File must still not exist: " + source);
            assertFalse(target.exists(), "File must still not exist: " + target);
        }
    }

    @Test
    public void move_create()
            throws Exception
    {
        File source = new File(MOVE, "first.txt");
        File target = new File(MOVE, "renamed.txt");
        assertTrue(source.exists(), "File must exist: " + source);
        assertFalse(target.exists(), "File must not exist: " + target);
        // do it
        InputOutput sut = new InputOutput();
        sut.move(source.getAbsolutePath(), target.getAbsolutePath());
        assertFalse(source.exists(), "File must not exist any more: " + source);
        assertTrue(target.exists(), "File must exist after moving: " + target);
    }

    @Test
    public void move_overwrite()
            throws Exception
    {
        File source = new File(MOVE, "second.txt");
        File target = new File(MOVE, "third.txt");
        assertTrue(source.exists(), "File must exist: " + source);
        assertTrue(target.exists(), "File must exist: " + target);
        assertEquals(TestTools.readTextFile(source), "Second file.\n", "File content: " + source);
        assertEquals(TestTools.readTextFile(target), "Third file.\n", "File content: " + target);
        // do it
        InputOutput sut = new InputOutput();
        sut.move(source.getAbsolutePath(), target.getAbsolutePath());
        assertFalse(source.exists(), "File must not exist any more: " + source);
        assertTrue(target.exists(), "File must exist after moving: " + target);
        assertEquals(TestTools.readTextFile(target), "Second file.\n",
                "File content must have been moved: " + target);
    }

    @Test
    public void move_toEmptyDir()
            throws Exception
    {
        File source = new File(MOVE, "fourth.txt");
        File target = new File(MOVE, "empty-dir");
        assertTrue(source.exists(), "File must exist: " + source);
        assertTrue(target.exists(), "Dir must exist: " + target);
        // do it
        InputOutput sut = new InputOutput();
        sut.move(source.getAbsolutePath(), target.getAbsolutePath());
        assertFalse(source.exists(), "File must not exist any more: " + source);
        File result = new File(target, "fourth.txt");
        assertTrue(result.exists(), "File must exist after moving: " + result);
    }

    @Test
    public void move_dir()
            throws Exception
    {
        File source = new File(MOVE, "dir");
        File target = new File(MOVE, "renamed-dir");
        assertTrue(source.exists(), "Dir must exist: " + source);
        assertFalse(target.exists(), "Dir must not exist: " + target);
        // do it
        InputOutput sut = new InputOutput();
        sut.move(source.getAbsolutePath(), target.getAbsolutePath());
        assertFalse(source.exists(), "Dir must not exist any more: " + source);
        assertTrue(target.exists(), "Dir must exist after moving: " + target);
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        DIR          = TestTools.initArea("directory");
        CREATE_DIR   = new File(DIR, "create-dir");
        DELETE       = new File(DIR, "delete");
        DELETE_EMPTY = new File(DELETE, "empty-dir");
        // because git does not allow to commit an empty directory
        if ( ! DELETE_EMPTY.exists() ) {
            DELETE_EMPTY.mkdir();
        }
        LIST         = new File(DIR, "list");
        LIST_EMPTY   = new File(LIST, "empty-dir");
        // because git does not allow to commit an empty directory
        if ( ! LIST_EMPTY.exists() ) {
            LIST_EMPTY.mkdir();
        }
        MOVE         = new File(DIR, "move");
        MOVE_EMPTY   = new File(MOVE, "empty-dir");
        // because git does not allow to commit an empty directory
        if ( ! MOVE_EMPTY.exists() ) {
            MOVE_EMPTY.mkdir();
        }
    }

    private static File DIR          = null;
    private static File CREATE_DIR   = null;
    private static File DELETE       = null;
    private static File DELETE_EMPTY = null;
    private static File LIST         = null;
    private static File LIST_EMPTY   = null;
    private static File MOVE         = null;
    private static File MOVE_EMPTY   = null;
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
