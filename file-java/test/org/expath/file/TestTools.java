/****************************************************************************/
/*  File:       TestTools.java                                              */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-11                                                  */
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

/**
 * Tools for tests of file-java features.
 * 
 * Because the classes tested in this project have side-effects on the file-
 * system (that is their raison d'etre), the `test-rsrc/` directory contains an
 * `initial/` subdirectory, containing the static resources.  Its sibling
 * `stage/` is the subdirectory where tests actually modify stuff.
 * 
 * The directory `initial/` (and so its copy in `stage/`) contains subdirectories
 * itself.  Each one is called an "area", and corresponds to a specific unit
 * test class.  For instance `initial/append/` is the area for the unit tests in
 * {@link AppendTest}.
 * 
 * The method annotated with @BeforeClass must therefore call initArea() with
 * the name of the area (e.g. for {@link AppendTest}: {@code initArea("append")}.
 * 
 * @author Florent Georges
 * @date   2015-01-11
 */
public class TestTools
{
    public static File initArea(String area)
            throws IOException
    {
        if ( area == null ) {
            throw new NullPointerException("area is null");
        }
        if ( ! area.matches("[-a-z0-9]+") ) {
            throw new IllegalArgumentException("area name does not match [-a-z0-9]+: " + area);
        }
        // validate dirs exist
        validateDir(PWD);
        validateDir(RSRC);
        validateDir(INITIAL);
        // delete test-src/stage/
        if ( STAGE.exists() ) {
            if ( ! deleteDir(STAGE) ) {
                throw new IOException("Error deleting the stage directory: " + STAGE);
            }
        }
        STAGE.mkdir();
        // copy test-rsrc/initial/{area}/ to test-src/stage/{area}/
        File src  = new File(INITIAL, area);
        File dest = new File(STAGE, area);
        copyDir(src, dest);
        // return the copied area
        return dest;
    }

    public static String readTextFile(File f)
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

    public static byte[] readBinFile(File f)
            throws IOException
    {
        Path p = f.toPath();
        return Files.readAllBytes(p);
    }

    // ----------------------------------------------------------------------
    //   Utility functions
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

    private static final String PWD_PROP  = System.getProperty("user.dir");
    private static final File   PWD       = new File(PWD_PROP);
    private static final File   RSRC      = new File(PWD, "test-rsrc");
    private static final File   INITIAL   = new File(RSRC, "initial");
    private static final File   STAGE     = new File(RSRC, "stage");
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
