/****************************************************************************/
/*  File:       Util.java                                                   */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-11                                                  */
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
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Utilities for implementing the functions of the EXPath File module.
 *
 * @author Florent Georges
 * @date   2015-01-11
 */
class Util
{
    public static void ensureNotNull(Object obj, String msg)
    {
        if ( null == obj ) {
            throw new NullPointerException(msg);
        }
    }

    public static Charset getCharset(String encoding)
            throws FileException
    {
        try {
            return Charset.forName(encoding);
        }
        catch ( IllegalCharsetNameException | UnsupportedCharsetException ex ) {
            throw FileException.unknownEncoding("Unsupported encoding: " + encoding, ex);
        }
    }

    public static byte[] readByteArray(InputStream in)
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

    public static byte[] readByteArray(InputStream in, long length)
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

    public static void close(InputStream in)
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

    public static void close(OutputStream out)
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

    public static void close(Writer out)
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

    public static void close(RandomAccessFile f)
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

    public static File openFile(String file)
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

    public static InputStream openInputStream(String file)
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

    public static Writer openWriter(String file, boolean append)
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

    public static OutputStream openOutputStream(String file, boolean append)
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

    public static RandomAccessFile openRandomAccess(String file)
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
