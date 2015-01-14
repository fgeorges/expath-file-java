/****************************************************************************/
/*  File:       AppendTest.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import java.net.URI;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test functions in {@link Paths}.
 * 
 * @author Florent Georges
 * @date   2015-01-13
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class PathsTest
{
    @Test
    public void name_simple()
            throws Exception
    {
        String file = new File(PATHS, "file.txt").getPath();
        Paths sut = new Paths();
        String result = sut.name(file);
        assertEquals(result, "file.txt", "The name of this path");
    }

    @Test
    public void name_subdir()
            throws Exception
    {
        String file = new File(PATHS, "dir/subdir/").getPath();
        Paths sut = new Paths();
        String result = sut.name(file);
        assertEquals(result, "subdir", "The name of this path");
    }

    @Test
    public void parent_simple()
            throws Exception
    {
        String file = new File(PATHS, "file.txt").getPath();
        Paths sut = new Paths();
        String result = sut.parent(file);
        String expected = PATHS.getAbsolutePath();
        assertEquals(result, expected, "The name of the parent");
    }

    @Test
    public void parent_subdir()
            throws Exception
    {
        String file = new File(PATHS, "dir/subdir/").getPath();
        Paths sut = new Paths();
        String result = sut.parent(file);
        String expected = new File(PATHS, "dir").getAbsolutePath();
        assertEquals(result, expected, "The name of the parent");
    }

    @Test
    public void pathToNative_simple()
            throws Exception
    {
        String file = new File(PATHS, "file.txt").getPath();
        Paths sut = new Paths();
        String result = sut.pathToNative(file);
        String expected = new File(PATHS, "file.txt").getAbsolutePath();
        assertEquals(result, expected, "The native path");
    }

    @Test
    public void pathToUri_simple()
            throws Exception
    {
        String file = new File(PATHS, "file.txt").getPath();
        Paths sut = new Paths();
        URI result = sut.pathToUri(file);
        URI expected = new File(PATHS, "file.txt").toURI();
        assertEquals(result, expected, "The URI");
    }

    @Test
    public void resolvePath_simple()
            throws Exception
    {
        String file = new File(PATHS, "file.txt").getPath();
        Paths sut = new Paths();
        String result = sut.resolvePath(file);
        String expected = new File(PATHS, "file.txt").getAbsolutePath();
        assertEquals(result, expected, "The resolved path");
    }

    // ----------------------------------------------------------------------
    //   Test setup
    // ----------------------------------------------------------------------

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        PATHS    = TestTools.initArea("paths");
//        PATHS_01 = new File(PATHS, "file.txt");
    }

    private static File PATHS    = null;
//    private static File PATHS_01 = null;
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
