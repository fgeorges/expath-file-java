/****************************************************************************/
/*  File:       Read.java                                                   */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-07                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Facade for the `read*` functions of the EXPath File module.
 * 
 * @author Florent Georges
 * @date   2015-01-07
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class Read
{
    // file:read-binary($file as xs:string) as xs:base64Binary
    // file:read-binary($file as xs:string,
    //                  $offset as xs:integer) as xs:base64Binary
    // file:read-binary($file as xs:string,
    //                  $offset as xs:integer,
    //                  $length as xs:integer) as xs:base64Binary
    // [file:not-found] is raised if $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:out-of-range] is raised if $offset or $length is negative, or if the chosen values would exceed the file bounds.
    // [file:io-error] is raised if any other error occurs.
    public byte[] readBinary(String file)
            throws FileException
    {
        try {
            File f = Util.openFile(file);
            Path p = f.toPath();
            return Files.readAllBytes(p);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
    }

    public byte[] readBinary(String file, long offset)
            throws FileException
    {
        if ( offset < 0 ) {
            throw FileException.outOfRange("Offset is negative: " + offset);
        }
        InputStream in = Util.openInputStream(file);
        try {
            in.skip(offset);
            return Util.readByteArray(in);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
        finally {
            Util.close(in);
        }
    }

    public byte[] readBinary(String file, long offset, long length)
            throws FileException
    {
        if ( offset < 0 ) {
            throw FileException.outOfRange("Offset is negative: " + offset);
        }
        if ( length < 0 ) {
            throw FileException.outOfRange("Length is negative: " + length);
        }
        InputStream in = Util.openInputStream(file);
        try {
            in.skip(offset);
            return Util.readByteArray(in, length);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
        finally {
            Util.close(in);
        }
    }

    // file:read-text($file as xs:string) as xs:string
    // file:read-text($file as xs:string,
    //                $encoding as xs:string) as xs:string
    // [file:not-found] is raised if $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
    // [file:io-error] is raised if any other error occurs.
    public String readText(String file)
            throws FileException
    {
        return readText(file, StandardCharsets.UTF_8);
    }

    public String readText(String file, String encoding)
            throws FileException
    {
        Charset cs = Util.getCharset(encoding);
        return readText(file, cs);
    }

    public String readText(String file, Charset encoding)
            throws FileException
    {
        byte[] bytes = readBinary(file);
        CharsetDecoder decoder = encoding.newDecoder();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        try {
            CharBuffer chars = decoder.decode(buf);
            return chars.toString();
        }
        catch ( CharacterCodingException ex ) {
            throw FileException.ioError("Encoding error (" + encoding + ") reading: " + file, ex);
        }
    }

    // file:read-text-lines($file as xs:string) as xs:string*
    // file:read-text-lines($file as xs:string,
    //                      $encoding as xs:string) as xs:string*
    // [file:not-found] is raised if $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
    // [file:io-error] is raised if any other error occurs.
    public List<String> readTextLines(String file)
            throws FileException
    {
        return readTextLines(file, StandardCharsets.UTF_8);
    }

    public List<String> readTextLines(String file, String encoding)
            throws FileException
    {
        Charset cs = Util.getCharset(encoding);
        return readTextLines(file, cs);
    }

    public List<String> readTextLines(String file, Charset encoding)
            throws FileException
    {
        try {
            File f = Util.openFile(file);
            Path p = f.toPath();
            return Files.readAllLines(p, encoding);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
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
