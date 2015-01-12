/****************************************************************************/
/*  File:       Write.java                                                  */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-07                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import org.expath.tools.model.Sequence;
import org.expath.tools.ToolsException;
import org.expath.tools.model.Element;
import org.expath.tools.serial.SerialParameters;

/**
 * Facade for the `write*` and `append*` functions of the EXPath File module.
 * 
 * @author Florent Georges
 * @date   2015-01-07
 * @see http://expath.org/spec/file#in-out
 * @see http://expath.org/spec/file/20131203#in-out
 */
public class Write
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
        RandomAccessFile f = Util.openRandomAccess(file);
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
            Util.close(f);
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
        Util.ensureNotNull(file, "file cannot be null");
        Util.ensureNotNull(items, "items cannot be null");
        OutputStream out = null;
        try {
            out = Util.openOutputStream(file, append);
            items.serialize(out, params);
        }
        catch ( ToolsException ex ) {
            throw FileException.ioError("Error serializing to the file: " + file, ex);
        }
        finally {
            Util.close(out);
        }
    }

    private void writeBinary(String file, byte[] value, boolean append)
            throws FileException
    {
        Util.ensureNotNull(file, "file cannot be null");
        Util.ensureNotNull(value, "value cannot be null");
        OutputStream out = null;
        try {
            out = Util.openOutputStream(file, append);
            out.write(value);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing binary to the file: " + file, ex);
        }
        finally {
            Util.close(out);
        }
    }

    private void writeText(String file, String value, boolean append)
            throws FileException
    {
        Util.ensureNotNull(file, "file cannot be null");
        Util.ensureNotNull(value, "value cannot be null");
        Writer out = null;
        try {
            out = Util.openWriter(file, append);
            out.write(value);
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing text to the file: " + file, ex);
        }
        finally {
            Util.close(out);
        }
    }

    private void writeText(String file, String value, String encoding, boolean append)
            throws FileException
    {
        Util.ensureNotNull(file, "file cannot be null");
        Util.ensureNotNull(value, "value cannot be null");
        Util.ensureNotNull(encoding, "encoding cannot be null");
        OutputStream out = null;
        try {
            out = Util.openOutputStream(file, append);
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
            Util.close(out);
        }
    }

    private void writeTextLines(String file, List<String> values, boolean append)
            throws FileException
    {
        Util.ensureNotNull(file, "file cannot be null");
        Util.ensureNotNull(values, "values cannot be null");
        final String nl = Properties.lineSeparator();
        Writer out = null;
        try {
            out = Util.openWriter(file, append);
            for ( String line : values ) {
                out.write(line);
                out.write(nl);
            }
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error writing text to the file: " + file, ex);
        }
        finally {
            Util.close(out);
        }
    }

    private void writeTextLines(String file, List<String> values, String encoding, boolean append)
            throws FileException
    {
        Util.ensureNotNull(file, "file cannot be null");
        Util.ensureNotNull(values, "values cannot be null");
        Util.ensureNotNull(encoding, "encoding cannot be null");
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
            out = Util.openOutputStream(file, true);
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
            Util.close(out);
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
