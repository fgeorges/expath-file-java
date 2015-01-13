/****************************************************************************/
/*  File:       InputOutput.java                                            */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-07                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Facade for EXPath File module functions in section "Input/Output".
 * 
 * The functions `read*` are in {@link Read}, and the functions `append*` and
 * `write*` are in {@link Write}, 
 * 
 * @author Florent Georges
 * @date   2015-01-07
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class InputOutput
{
    // file:copy($source as xs:string,
    //           $target as xs:string) as empty-sequence()
    // [file:not-found] is raised if the $source path does not exist.
    // [file:exists] is raised if $source points to a directory and $target points to an existing file.
    // [file:no-dir] is raised if the parent directory of $source does not exist.
    // [file:is-dir] is raised if $source points to a file and $target points to a directory, in which a subdirectory exists with the name of the source file.
    // [file:io-error] is raised if any other error occurs.
    public void copy(String source, String target)
            throws FileException
    {
        File src = new File(source);
        File trg = new File(target);
        if ( ! src.exists() ) {
            throw FileException.notFound("File not found: " + source);
        }
        if ( src.isDirectory() ) {
            copyDir(src, trg);
        }
        else {
            copyFile(src, trg);
        }
    }

    // precond: source exists and is a dir
    private void copyDir(File source, File target)
            throws FileException
    {
        if ( target.isDirectory() ) {
            // merge source and target dirs (aka "copy recursively")
            copyMergeDir(source, target);
        }
        else if ( target.exists() ) {
            throw FileException.exists("Target file already exists and is not a directory: " + target);
        }
        else {
            // create target as dir, and copy all content of source
            copyNewDir(source, target);
        }
    }

    private void copyMergeDir(File source, File target)
            throws FileException
    {
        // source is a directory
        if ( source.isDirectory() ) {
            // dir to existing dir
            if ( target.isDirectory() ) {
                File[] children = source.listFiles();
                if ( children == null ) {  // null if security restricted
                    throw FileException.ioError("Failed to list contents of " + source);
                }
                for ( File child : children ) {
                    File dest = new File(target, child.getName());
                    copyMergeDir(child, dest);
                }
            }
            // dir to existing file
            else if ( target.exists() ) {
                throw FileException.isDir("Source is a directory (" + source
                        + ") and target a file (" + target + ") in a merge copy");
            }
            // dir to new dir
            else {
                copyNewDir(source, target);
            }
        }
        // source is a regular file
        else if ( source.exists() ) {
            // file to existing dir
            if ( target.isDirectory() ) {
                throw FileException.isDir("Source is a file (" + source
                        + ") and target a directory (" + target + ") in a merge copy");
            }
            // file to existing file
            else if ( target.exists() ) {
                copyOverwrite(source, target);
            }
            // file to new file
            else {
                copyCreate(source, target);
            }
        }
        // source does not exist?!?
        else {
            throw FileException.ioError("Source does not exist?!?: " + source);
        }
    }

    private void copyNewDir(File source, File target)
            throws FileException
    {
        if ( target.exists() ) {
            throw FileException.ioError("Target '" + target + "' exists");
        }
        if ( ! target.mkdirs() ) {
            throw FileException.ioError("Target '" + target + "' directory cannot be created");
        }
        if ( ! target.canWrite() ) {
            throw FileException.ioError("Target '" + target + "' cannot be written to");
        }
        File[] files = source.listFiles();
        if ( files == null ) {  // null if security restricted
            throw FileException.ioError("Failed to list contents of " + source);
        }
        for ( File file : files ) {
            File copied = new File(target, file.getName());
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

    // precond: source exists and is a regular file
    private void copyFile(File source, File target)
            throws FileException
    {
        if ( target.isDirectory() ) {
            // create (or overwrite) a file with same name as source in target dir
            File dest = new File(target, source.getName());
            if ( dest.isDirectory() ) {
                throw FileException.isDir("The target dir has a subdir with the name of the source file: " + dest);
            }
            else if ( dest.exists() ) {
                // overwrite target
                copyOverwrite(source, dest);
            }
            else {
                // create target
                copyCreate(source, dest);
            }
        }
        else if ( target.exists() ) {
            // overwrite target
            copyOverwrite(source, target);
        }
        else {
            // create target
            copyCreate(source, target);
        }
    }

    // precond: both source and target exist and are regular files
    private void copyOverwrite(File source, File target)
            throws FileException
    {
        try {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error overwriting " + target + " by " + source, ex);
        }
    }

    // precond: source exists and is a regular file
    //          target does not exist, its parent does and is a dir
    private void copyCreate(File source, File target)
            throws FileException
    {
        try {
            Files.copy(source.toPath(), target.toPath());
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error copying " + source + " to " + target, ex);
        }
    }

    // file:create-dir($dir as xs:string) as empty-sequence()
    // [file:exists] is raised if the specified path, or any of its parent directories, points to an existing file.
    // [file:io-error] is raised if any other error occurs.
    public void createDir(String dir)
            throws FileException
    {
        File f = new File(dir);
        if ( f.isDirectory() ) {
            // nothing, already exists
        }
        else if ( f.exists() ) {
            // exists, not a dir
            throw FileException.exists("Already exists as a file: " + f);
        }
        else {
            if ( ! f.mkdirs() ) {
                throw FileException.ioError("Error creating the directory: " + f);
            }
        }
    }

    // file:create-temp-dir($prefix as xs:string,
    //                      $suffix as xs:string) as xs:string
    // file:create-temp-dir($prefix as xs:string,
    //                      $suffix as xs:string,
    //                      $dir as xs:string) as xs:string
    // [file:no-dir] is raised if the specified directory does not exist or points to a file.
    // [file:io-error] is raised if any other error occurs.
    public File createTempDir(String prefix, String suffix)
            throws FileException
    {
        try {
            File f = File.createTempFile(prefix, suffix);
            return turnIntoDir(f);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error creating the temporary directory", ex);
        }
    }

    public File createTempDir(String prefix, String suffix, String dir)
            throws FileException
    {
        try {
            File d = new File(dir);
            if ( d.isDirectory() ) {
                File f = File.createTempFile(prefix, suffix, d);
                return turnIntoDir(f);
            }
            else if ( d.exists() ) {
                throw FileException.noDir("The directory where to create a temp dir is not a dir: " + d);
            }
            else {
                throw FileException.noDir("The directory where to create a temp dir does not exist: " + d);
            }
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error creating the temporary directory", ex);
        }
    }

    private File turnIntoDir(File f)
            throws FileException
    {
        if ( ! f.delete() ) {
            throw FileException.ioError("Error deleting the temp file to create a dir file");
        }
        if ( ! f.mkdir() ) {
            throw FileException.ioError("Error creating the temp dir from the temp file");
        }
        return f;
    }

    // file:create-temp-file($prefix as xs:string,
    //                       $suffix as xs:string) as xs:string
    // file:create-temp-file($prefix as xs:string,
    //                       $suffix as xs:string,
    //                       $dir as xs:string) as xs:string
    // [file:no-dir] is raised if the specified directory does not exist or points to a file.
    // [file:io-error] is raised if any other error occurs.
    public File createTempFile(String prefix, String suffix)
            throws FileException
    {
        try {
            return File.createTempFile(prefix, suffix);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error creating the temporary file", ex);
        }
    }

    public File createTempFile(String prefix, String suffix, String dir)
            throws FileException
    {
        try {
            File d = new File(dir);
            if ( d.isDirectory() ) {
                return File.createTempFile(prefix, suffix, d);
            }
            else if ( d.exists() ) {
                throw FileException.noDir("The directory where to create a temp file is not a dir: " + d);
            }
            else {
                throw FileException.noDir("The directory where to create a temp file does not exist: " + d);
            }
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error creating the temporary file", ex);
        }
    }

    // file:delete($path as xs:string) as empty-sequence()
    // file:delete($path as xs:string,
    //             $recursive as xs:boolean) as empty-sequence()
    // [file:not-found] is raised if $path does not exist.
    // [file:is-dir] is raised if $file points to a non-empty directory.
    // [file:io-error] is raised if any other error occurs.
    public void delete(String path)
            throws FileException
    {
        delete(path, false);
    }

    public void delete(String path, boolean recursive)
            throws FileException
    {
        File f = new File(path);
        if ( f.isDirectory() ) {
            if ( f.list().length > 0 ) {
                // non-empty dir
                if ( recursive ) {
                    deleteDir(f);
                }
                else {
                    throw FileException.isDir("Directory to delete is not empty: " + f);
                }
            }
            else {
                // empty dir
                safeDelete(f);
            }
        }
        else if ( f.exists() ) {
            // regular file
            safeDelete(f);
        }
        else {
            throw FileException.notFound("File does not exist: " + f);
        }
    }

    private void safeDelete(File f)
            throws FileException
    {
        if ( ! f.delete() ) {
            throw FileException.isDir("Error deleting file: " + f);
        }
    }

    private void deleteDir(File f)
            throws FileException
    {
        if ( f.isDirectory() ) {
            for ( File child : f.listFiles() ) {
                deleteDir(child);
            }
        }
        safeDelete(f);
    }

    // file:list($dir as xs:string) as xs:string*
    // file:list($dir as xs:string,
    //           $recursive as xs:boolean) as xs:string*
    // file:list($dir as xs:string,
    //           $recursive as xs:boolean,
    //           $pattern as xs:string) as xs:string*
    // [file:no-dir] is raised $dir does not point to an existing directory.
    // [file:io-error] is raised if any other error occurs.
    public List<String> list(String dir)
            throws FileException
    {
        return list(dir, false);
    }

    public List<String> list(String dir, boolean recursive)
            throws FileException
    {
        return list(dir, recursive, null);
    }

    public List<String> list(String dir, boolean recursive, String pattern)
            throws FileException
    {
        File d = new File(dir);
        if ( ! d.isDirectory() ) {
            throw FileException.noDir("Not a directory: " + d);
        }
        FilenameFilter filter = pattern == null ? null : new ListFilter(pattern);
        if ( recursive ) {
            List<String> list = new ArrayList<>();
            listRecurse(d, "", list, filter);
            return list;
        }
        else {
            String[] list = d.list(filter);
            return Arrays.asList(list);
        }
    }

    private void listRecurse(File dir, String prefix, List<String> list, FilenameFilter filter)
    {
        for ( File f : dir.listFiles() ) {
            String name = f.getName();
            if ( filter == null || filter.accept(dir, name) ) {
                list.add(prefix + name);
            }
            if ( f.isDirectory() ) {
                listRecurse(f, prefix + name + "/" , list, filter);
            }
        }
    }

    private static class ListFilter
            implements FilenameFilter
    {
        public ListFilter(String pattern)
        {
            StringBuilder buf = new StringBuilder("^");
            for ( char c : pattern.toCharArray() ) {
                switch ( c ) {
                    case '*':
                        buf.append(".*");
                        break;
                    case '?':
                        buf.append('.');
                        break;
                    case '\\':
                        buf.append("\\\\");
                        break;
                    case '{':
                    case '}':
                    case '.':
                    case '(':
                    case ')':
                    case '+':
                    case '|':
                    case '^':
                    case '$':
                    case '@':
                    case '%':
                        buf.append('\\');
                        buf.append(c);
                        break;
                    default:
                        buf.append(c);
                }
            }
            buf.append("$");
            myPattern = Pattern.compile(buf.toString());
        }

        @Override
        public boolean accept(File dir, String name)
        {
            return myPattern.matcher(name).matches();
        }

        private final Pattern myPattern;
    }

//    // file:move($source as xs:string,
//    //           $target as xs:string) as empty-sequence()
//    // [file:not-found] is raised if the $source path does not exist.
//    // [file:exists] is raised if $source points to a directory and $target points to an existing file.
//    // [file:no-dir] is raised if the parent directory of $source does not exist.
//    // [file:is-dir] is raised if $target points to a directory, in which a subdirectory exists with the name of the source.
//    // [file:io-error] is raised if any other error occurs.
//    public void move()
//    {
//        ...
//    }
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
