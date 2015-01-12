/****************************************************************************/
/*  File:       InputOutput.java                                            */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-07                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    // ======================================================================
    //   Append
    //   ======
    // 
    //   All the `append*` functions in the spec.  They all are implemented
    //   using the corresponding `write*` implementations.
    // ----------------------------------------------------------------------

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
        write(file, items, params, true);
    }

    public void append(String file, Sequence items, SerialParameters params)
            throws FileException
    {
        write(file, items, params, true);
    }

    // file:append-binary($file as xs:string,
    //                    $value as xs:base64Binary) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:io-error] is raised if any other error occurs.
    public void appendBinary(String file, byte[] value)
            throws FileException
    {
        writeBinary(file, value, true);
    }

    // file:append-text($file as xs:string,
    //                  $value as xs:string) as empty-sequence()
    // file:append-text($file as xs:string,
    //                  $value as xs:string,
    //                  $encoding as xs:string) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
    // [file:io-error] is raised if any other error occurs.
    public void appendText(String file, String value)
            throws FileException
    {
        writeText(file, value, true);
    }

    public void appendText(String file, String value, String encoding)
            throws FileException
    {
        writeText(file, value, encoding, true);
    }

    // file:append-text-lines($file as xs:string,
    //                        $values as xs:string*) as empty-sequence()
    // file:append-text-lines($file as xs:string,
    //                        $lines as xs:string*,
    //                        $encoding as xs:string) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
    // [file:io-error] is raised if any other error occurs.
    public void appendTextLines(String file, List<String> values)
            throws FileException
    {
        writeTextLines(file, values, true);
    }

    public void appendTextLines(String file, List<String> values, String encoding)
            throws FileException
    {
        writeTextLines(file, values, encoding, true);
    }

    // ======================================================================
    //   Misc
    //   ====
    // 
    //   Miscellaneous functions from spec.
    // ----------------------------------------------------------------------

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

    // ======================================================================
    //   Read
    //   ====
    // 
    //   All the read* functions in the spec.
    // ----------------------------------------------------------------------

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
            File f = openFile(file);
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
        InputStream in = openInputStream(file);
        try {
            in.skip(offset);
            return readByteArray(in);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
        finally {
            close(in);
        }
    }

    public byte[] readBinary(String file, long offset, long length)
            throws FileException
    {
        InputStream in = openInputStream(file);
        try {
            in.skip(offset);
            return readByteArray(in, length);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
        finally {
            close(in);
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
        Charset cs = getCharset(encoding);
        return readText(file, cs);
    }

    public String readText(String file, Charset encoding)
            throws FileException
    {
        byte[] bytes = readBinary(file);
        return new String(bytes, encoding);
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
        Charset cs = getCharset(encoding);
        return readTextLines(file, cs);
    }

    public List<String> readTextLines(String file, Charset encoding)
            throws FileException
    {
        try {
            File f = openFile(file);
            Path p = f.toPath();
            return Files.readAllLines(p, encoding);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error reading from the file: " + file, ex);
        }
    }

    // ======================================================================
    //   Write
    //   =====
    // 
    //   All the `write*` functions in the spec.  They are all implemented
    //   with private functions taking an extra `append` parameter, telling
    //   whether it is a regular write or an append.  The same private
    //   functions are used from the `append*` functions.
    // ----------------------------------------------------------------------

    // file:write($file as xs:string,
    //            $items as item()*) as empty-sequence()
    // file:write($file as xs:string,
    //            $items as item()*,
    //            $params as element(output:serialization-parameters)) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:io-error] is raised if any other error occurs.
    public void write(String file, Sequence items)
            throws FileException
    {
        SerialParameters params = new SerialParameters();
        write(file, items, params);
    }

    public void write(String file, Sequence items, Element params)
            throws FileException
    {
        write(file, items, params, false);
    }

    public void write(String file, Sequence items, SerialParameters params)
            throws FileException
    {
        write(file, items, params, false);
    }

    private void write(String file, Sequence items, Element params, boolean append)
            throws FileException
    {
        try {
            SerialParameters sp = SerialParameters.parse(params);
            write(file, items, sp, append);
        }
        catch ( ToolsException ex ) {
            throw FileException.ioError("Error parsing the serialization parameters", ex);
        }
    }

    private void write(String file, Sequence items, SerialParameters params, boolean append)
            throws FileException
    {
        ensureNotNull(file, "file cannot be null");
        ensureNotNull(items, "items cannot be null");
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            items.serialize(out, params);
        }
        catch ( ToolsException ex ) {
            throw FileException.ioError("Error serializing to the file: " + file, ex);
        }
        finally {
            close(out);
        }
    }

    // file:write-binary($file as xs:string,
    //                   $value as xs:base64Binary) as empty-sequence()
    // file:write-binary($file as xs:string,
    //                   $value as xs:base64Binary,
    //                   $offset as xs:integer) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:out-of-range] is raised if $offset is negative, or if it exceeds the current file size.
    // [file:io-error] is raised if any other error occurs.
    public void writeBinary(String file, byte[] value)
            throws FileException
    {
        writeBinary(file, value, false);
    }

    public void writeBinary(String file, byte[] value, long offset)
            throws FileException
    {
        // is offset negative?
        if ( offset < 0 ) {
            throw FileException.outOfRange("Offset is negative: " + offset);
        }
        // open the file
        RandomAccessFile f = openRandomAccess(file);
        // global try/catch to properly close the file, whatever exec path is taken
        try {
            // get its length
            long len;
            try {
                len = f.length();
            }
            catch ( IOException ex ) {
                throw FileException.ioError("Error getting the size of the file: " + file, ex);
            }
            // is offset greater than file size?
            if ( offset > len ) {
                throw FileException.outOfRange("Offset (" + offset + ") is greater than the file size (" + len + "): " + file);
            }
            // position to offset
            try {
                f.seek(offset);
            }
            catch ( IOException ex ) {
                throw FileException.ioError("Error seeking to offset (" + offset + ") on the file: " + file, ex);
            }
            // write it!
            f.write(value);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing at offset (" + offset + ") "
                    + value.length + " bytes in the file: " + file, ex);
        }
        finally {
            close(f);
        }
    }

    private void writeBinary(String file, byte[] value, boolean append)
            throws FileException
    {
        ensureNotNull(file, "file cannot be null");
        ensureNotNull(value, "value cannot be null");
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            out.write(value);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing binary to the file: " + file, ex);
        }
        finally {
            close(out);
        }
    }

    // file:write-text($file as xs:string,
    //                 $value as xs:string) as empty-sequence()
    // file:write-text($file as xs:string,
    //                 $value as xs:string,
    //                 $encoding as xs:string) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
    // [file:io-error] is raised if any other error occurs.
    public void writeText(String file, String value)
            throws FileException
    {
        writeText(file, value, false);
    }

    public void writeText(String file, String value, String encoding)
            throws FileException
    {
        writeText(file, value, encoding, false);
    }

    private void writeText(String file, String value, boolean append)
            throws FileException
    {
        ensureNotNull(file, "file cannot be null");
        ensureNotNull(value, "value cannot be null");
        Writer out = null;
        try {
            out = openWriter(file, append);
            out.write(value);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing text to the file: " + file, ex);
        }
        finally {
            close(out);
        }
    }

    private void writeText(String file, String value, String encoding, boolean append)
            throws FileException
    {
        ensureNotNull(file, "file cannot be null");
        ensureNotNull(value, "value cannot be null");
        ensureNotNull(encoding, "encoding cannot be null");
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            byte[] bytes = value.getBytes(encoding);
            out.write(bytes);
        }
        catch ( UnsupportedEncodingException ex ) {
            throw FileException.unknownEncoding("Unsupported encoding: " + encoding, ex);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing text to the file: " + file, ex);
        }
        finally {
            close(out);
        }
    }

    // file:write-text-lines($file as xs:string,
    //                       $values as xs:string*) as empty-sequence()
    // file:write-text-lines($file as xs:string,
    //                       $values as xs:string*,
    //                       $encoding as xs:string) as empty-sequence()
    // [file:no-dir] is raised if the parent directory of $file does not exist.
    // [file:is-dir] is raised if $file points to a directory.
    // [file:unknown-encoding] is raised if $encoding is invalid or not supported by the implementation.
    // [file:io-error] is raised if any other error occurs.
    public void writeTextLines(String file, List<String> values)
            throws FileException
    {
        writeTextLines(file, values, false);
    }

    public void writeTextLines(String file, List<String> values, String encoding)
            throws FileException
    {
        writeTextLines(file, values, encoding, false);
    }

    private void writeTextLines(String file, List<String> values, boolean append)
            throws FileException
    {
        ensureNotNull(file, "file cannot be null");
        ensureNotNull(values, "values cannot be null");
        final String nl = Properties.lineSeparator();
        Writer out = null;
        try {
            out = openWriter(file, append);
            for ( String line : values ) {
                out.write(line);
                out.write(nl);
            }
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing text to the file: " + file, ex);
        }
        finally {
            close(out);
        }
    }

    private void writeTextLines(String file, List<String> values, String encoding, boolean append)
            throws FileException
    {
        ensureNotNull(file, "file cannot be null");
        ensureNotNull(values, "values cannot be null");
        ensureNotNull(encoding, "encoding cannot be null");
        // get the newline separator, as bytes
        final String nlstr = Properties.lineSeparator();
        final byte[] nl;
        try {
            nl = nlstr.getBytes(encoding);
        }
        catch ( UnsupportedEncodingException ex ) {
            throw FileException.unknownEncoding("Unsupported encoding: " + encoding, ex);
        }
        OutputStream out = null;
        try {
            out = openOutputStream(file, true);
            for ( String line : values ) {
                byte[] bytes = line.getBytes(encoding);
                out.write(bytes);
                out.write(nl);
            }
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing text to the file: " + file, ex);
        }
        finally {
            close(out);
        }
    }

    // ======================================================================
    //   Utility
    //   =======
    // 
    //   Utility functions.
    // ----------------------------------------------------------------------

    private void ensureNotNull(Object obj, String msg)
    {
        if ( null == obj ) {
            throw new NullPointerException(msg);
        }
    }

    private Charset getCharset(String encoding)
            throws FileException
    {
        try {
            return Charset.forName(encoding);
        }
        catch ( IllegalCharsetNameException | UnsupportedCharsetException ex ) {
            throw FileException.unknownEncoding("Unsupported encoding: " + encoding, ex);
        }
    }

    private byte[] readByteArray(InputStream in)
            throws IOException
    {
        // the buffer, 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] buf = new byte[4096];
        // suck it all
        while ( (read = in.read(buf, 0, buf.length)) != -1 ) {
            out.write(buf, 0, read);
        }
        // to byte[]
        return out.toByteArray();
    }

    private byte[] readByteArray(InputStream in, long length)
            throws FileException
                 , IOException
    {
        // the buffer, 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = 0;
        byte[] buf = new byte[4096];
        // suck it all
        while ( total < length ) {
            long to_read = length - total;
            if ( to_read > buf.length ) {
                to_read = buf.length;
            }
            // casting to_read to int is safe, as it is at maximum buf.length
            int read = in.read(buf, 0, (int) to_read);
            if ( read < 0 ) {
                throw FileException.outOfRange("Not enough bytes in file, read: " + total);
            }
            out.write(buf, 0, read);
            total += read;
        }
        // to byte[]
        return out.toByteArray();
    }

    private void close(InputStream in)
            throws FileException
    {
        if ( in != null ) {
            try {
                in.close();
            }
            catch ( IOException ex ) {
                throw FileException.ioError("Error closing the input stream", ex);
            }
        }
    }

    private void close(OutputStream out)
            throws FileException
    {
        if ( out != null ) {
            try {
                out.close();
            }
            catch ( IOException ex ) {
                throw FileException.ioError("Error closing the output stream", ex);
            }
        }
    }

    private void close(Writer out)
            throws FileException
    {
        if ( out != null ) {
            try {
                out.close();
            }
            catch ( IOException ex ) {
                throw FileException.ioError("Error closing the writer", ex);
            }
        }
    }

    private void close(RandomAccessFile f)
            throws FileException
    {
        if ( f != null ) {
            try {
                f.close();
            }
            catch ( IOException ex ) {
                throw FileException.ioError("Error closing the random-access file", ex);
            }
        }
    }

    private File openFile(String file)
            throws FileException
    {
        File f = new File(file);
        if ( ! f.exists() ) {
            throw FileException.notFound("File not found: " + file);
        }
        if ( f.isDirectory() ) {
            throw FileException.notFound("File points to a directory: " + file);
        }
        return f;
    }

    private InputStream openInputStream(String file)
            throws FileException
    {
        File f = new File(file);
        try {
            return new FileInputStream(f);
        }
        catch ( FileNotFoundException ex ) {
            if ( f.isDirectory() ) {
                throw FileException.isDir("File points to a directory: " + file, ex);
            }
            throw FileException.notFound("File not found: " + file, ex);
        }
    }

    private Writer openWriter(String file, boolean append)
            throws FileException
    {
        File f = new File(file);
        try {
            return new FileWriter(f, append);
        }
        catch ( IOException ex ) {
            if ( f.isDirectory() ) {
                throw FileException.isDir("The file already exists and is a directory: " + file, ex);
            }
            if ( ! f.exists() && ! f.getParentFile().isDirectory() ) {
                throw FileException.isDir("The file must be created and its directory does not exist: " + file, ex);
            }
            throw FileException.ioError("Error creating or opening the file: " + file, ex);
        }
    }

    private OutputStream openOutputStream(String file, boolean append)
            throws FileException
    {
        File f = new File(file);
        try {
            return new FileOutputStream(f, append);
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
    }

    private RandomAccessFile openRandomAccess(String file)
            throws FileException
    {
        File f = new File(file);
        RandomAccessFile raf;
        try {
            return new RandomAccessFile(f, "rw");
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
