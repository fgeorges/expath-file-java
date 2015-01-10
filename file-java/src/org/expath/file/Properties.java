/****************************************************************************/
/*  File:       Properties.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-07                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

/**
 * Facade for EXPath File module function in section "File Properties".
 * 
 * @author Florent Georges
 * @date   2015-01-07
 * @see http://expath.org/spec/file#props
 * @see http://expath.org/spec/file/20131203#props
 */
public class Properties
{
    // file:exists($path as xs:string) as xs:boolean
    public boolean exists(String path)
    {
        return exists(getPath(path));
    }

    public boolean exists(Path path)
    {
        return Files.exists(path);
    }

    // file:is-dir($path as xs:string) as xs:boolean
    public boolean isDir(String path)
    {
        return isDir(getPath(path));
    }

    public boolean isDir(Path path)
    {
        return Files.isDirectory(path);
    }

    // file:is-file($path as xs:string) as xs:boolean
    public boolean isFile(String path)
    {
        return isFile(getPath(path));
    }

    public boolean isFile(Path path)
    {
        return Files.isRegularFile(path);
    }

    // file:last-modified($path as xs:string) as xs:dateTime
    // [file:not-found] is raised if $path does not exist.
    // [file:io-error] is raised if any other error occurs.
    public Date lastModified(String path)
            throws FileException
    {
        return lastModified(getPath(path));
    }

    public Date lastModified(Path path)
            throws FileException
    {
        try {
            FileTime time = Files.getLastModifiedTime(path);
            long ms = time.toMillis();
            return new Date(ms);
        }
        catch ( NoSuchFileException ex ) {
            throw FileException.notFound("No last modified time for non-existing file " + path, ex);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error accessing the last modified time for " + path, ex);
        }
    }

    // file:size($file as xs:string) as xs:integer
    // [file:not-found] is raised if $path does not exist.
    // [file:io-error] is raised if any other error occurs.
    public long size(String path)
            throws FileException
    {
        return size(getPath(path));
    }

    public long size(Path path)
            throws FileException
    {
        try {
            return Files.size(path);
        }
        catch ( NoSuchFileException ex ) {
            throw FileException.notFound("No size for non-existing file " + path, ex);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error accessing the size for " + path, ex);
        }
    }

    /**
     * TODO: ...
     * 
     * @param path
     * @return 
     */
    public Path getPath(String path)
    {
        FileSystem fs = FileSystems.getDefault();
        return fs.getPath(path);
    }
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
