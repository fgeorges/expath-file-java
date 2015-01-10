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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.expath.tools.model.Sequence;
import org.expath.tools.ToolsException;
import org.expath.tools.model.Element;
import org.expath.tools.serial.SerialParameters;

/**
 * Facade for EXPath File module function in section "Input/Output".
 * 
 * @author Florent Georges
 * @date   2015-01-07
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class InputOutput
{
    // file:append($file as xs:string,
    //             $items as item()*) as empty-sequence()
    // file:append($file as xs:string,
    //             $items as item()*,
    //             $params as element(output:serialization-parameters)) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:io-error] is raised if any other error occurs.
    public void append(String file, Sequence items)
            throws FileException
    {
        SerialParameters params = new SerialParameters();
        append(file, items, params);
    }

    public void append(String file, Sequence items, Element params)
            throws FileException
    {
        try {
            SerialParameters sp = SerialParameters.parse(params);
            append(file, items, sp);
        }
        catch ( ToolsException ex ) {
            throw FileException.ioError("Error parsing the serialization parameters", ex);
        }
    }

    public void append(String file, Sequence items, SerialParameters params)
            throws FileException
    {
        File f = new File(file);
        OutputStream out;
        try {
            out = new FileOutputStream(f, true);
        }
        catch ( FileNotFoundException ex ) {
            if ( f.isDirectory() ) {
                throw FileException.isDir("The file already exists and is a directory: " + file, ex);
            }
            if ( ! f.exists() && ! f.getParentFile().isDirectory() ) {
                throw FileException.isDir("The file must be created and its directory does not exist: " + file, ex);
            }
            throw FileException.ioError("Error creating or opening the file: " + file, ex);
        }
        try {
            items.serialize(out, params);
        }
        catch ( ToolsException ex ) {
            throw FileException.ioError("Error serializing to the file: " + file, ex);
        }
    }

//    // file:append-binary($file as xs:string,
//    //                    $value as xs:base64Binary) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:io-error] is raised if any other error occurs.
//    public void appendBinary()
//    {
//        ...
//    }
//
//    // file:append-text($file as xs:string,
//    //                  $value as xs:string) as empty-sequence()
//    // file:append-text($file as xs:string,
//    //                  $value as xs:string,
//    //                  $encoding as xs:string) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
//    // [file:io-error] is raised if any other error occurs.
//    public void appendText()
//    {
//        ...
//    }
//
//    // file:append-text-lines($file as xs:string,
//    //                        $values as xs:string*) as empty-sequence()
//    // file:append-text-lines($file as xs:string,
//    //                        $lines as xs:string*,
//    //                        $encoding as xs:string) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
//    // [file:io-error] is raised if any other error occurs.
//    public void appendTextLines()
//    {
//        ...
//    }
//
//    // file:copy($source as xs:string,
//    //           $target as xs:string) as empty-sequence()
//    // [file:not-found] is raised if the $source path does not exist.
//    // [file:exists] is raised if $source points to a directory and $target points to an existing file.
//    // [file:no-dir] is raised if the parent directory of $source does not exist.
//    // [file:is-dir] is raised if $source points to a file and $target points to a directory, in which a subdirectory exists with the name of the source file.
//    // [file:io-error] is raised if any other error occurs.
//    public void copy()
//    {
//        ...
//    }
//
//    // file:create-dir($dir as xs:string) as empty-sequence()
//    // [file:exists] is raised if the specified path, or any of its parent directories, points to an existing file.
//    // [file:io-error] is raised if any other error occurs.
//    public void createDir()
//    {
//        ...
//    }
//
//    // file:create-temp-dir($prefix as xs:string,
//    //                      $suffix as xs:string) as xs:string
//    // file:create-temp-dir($prefix as xs:string,
//    //                      $suffix as xs:string,
//    //                      $dir as xs:string) as xs:string
//    // [file:no-dir] is raised if the specified directory does not exist or points to a file.
//    // [file:io-error] is raised if any other error occurs.
//    public void createTempDir()
//    {
//        ...
//    }
//
//    // file:create-temp-file($prefix as xs:string,
//    //                       $suffix as xs:string) as xs:string
//    // file:create-temp-file($prefix as xs:string,
//    //                       $suffix as xs:string,
//    //                       $dir as xs:string) as xs:string
//    // [file:no-dir] is raised if the specified directory does not exist or points to a file.
//    // [file:io-error] is raised if any other error occurs.
//    public void createTempFile()
//    {
//        ...
//    }
//
//    // file:delete($path as xs:string) as empty-sequence()
//    // file:delete($path as xs:string,
//    //             $recursive as xs:boolean) as empty-sequence()
//    // [file:not-found] is raised if $path does not exist.
//    // [file:is-dir] is raised if $file points to a non-empty directory.
//    // [file:io-error] is raised if any other error occurs.
//    public void delete()
//    {
//        ...
//    }
//
//    // file:list($dir as xs:string) as xs:string*
//    // file:list($dir as xs:string,
//    //           $recursive as xs:boolean) as xs:string*
//    // file:list($dir as xs:string,
//    //           $recursive as xs:boolean,
//    //           $pattern as xs:string) as xs:string*
//    // [file:no-dir] is raised $dir does not point to an existing directory.
//    // [file:io-error] is raised if any other error occurs.
//    public void list()
//    {
//        ...
//    }
//
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
//
//    // file:read-binary($file as xs:string) as xs:base64Binary
//    // file:read-binary($file as xs:string,
//    //                  $offset as xs:integer) as xs:base64Binary
//    // file:read-binary($file as xs:string,
//    //                  $offset as xs:integer,
//    //                  $length as xs:integer) as xs:base64Binary
//    // [file:not-found] is raised if $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:out-of-range] is raised if $offset or $length is negative, or if the chosen values would exceed the file bounds.
//    // [file:io-error] is raised if any other error occurs.
//    public void readBinary()
//    {
//        ...
//    }
//
//    // file:read-text($file as xs:string) as xs:string
//    // file:read-text($file as xs:string,
//    //                $encoding as xs:string) as xs:string
//    // [file:not-found] is raised if $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
//    // [file:io-error] is raised if any other error occurs.
//    public void readText()
//    {
//        ...
//    }
//
//    // file:read-text-lines($file as xs:string) as xs:string*
//    // file:read-text-lines($file as xs:string,
//    //                      $encoding as xs:string) as xs:string*
//    // [file:not-found] is raised if $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
//    // [file:io-error] is raised if any other error occurs.
//    public void readTextLines()
//    {
//        ...
//    }
//
//    // file:write($file as xs:string,
//    //            $items as item()*) as empty-sequence()
//    // file:write($file as xs:string,
//    //            $items as item()*,
//    //            $params as element(output:serialization-parameters)) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:io-error] is raised if any other error occurs.
//    public void write()
//    {
//        ...
//    }
//
//    // file:write-binary($file as xs:string,
//    //                   $value as xs:base64Binary) as empty-sequence()
//    // file:write-binary($file as xs:string,
//    //                   $value as xs:base64Binary,
//    //                   $offset as xs:integer) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:out-of-range] is raised if $offset is negative, or if it exceeds the current file size.
//    // [file:io-error] is raised if any other error occurs.
//    public void writeBinary()
//    {
//        ...
//    }
//
//    // file:write-text($file as xs:string,
//    //                 $value as xs:string) as empty-sequence()
//    // file:write-text($file as xs:string,
//    //                 $value as xs:string,
//    //                 $encoding as xs:string) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
//    // [file:io-error] is raised if any other error occurs.
//    public void writeText()
//    {
//        ...
//    }
//
//    // file:write-text-lines($file as xs:string,
//    //                       $values as xs:string*) as empty-sequence()
//    // file:write-text-lines($file as xs:string,
//    //                       $values as xs:string*,
//    //                       $encoding as xs:string) as empty-sequence()
//    // [file:no-dir] is raised if the parent directory of $file does not exist.
//    // [file:is-dir] is raised if $file points to a directory.
//    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
//    // [file:io-error] is raised if any other error occurs.
//    public void writeTextLines()
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
