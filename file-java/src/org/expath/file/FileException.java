/****************************************************************************/
/*  File:       FileException.java                                          */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2015-01-07                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

/**
 * Generic exception for the EXPath File implementation in Java.
 * 
 * TODO: Most error will be about a path.  Should we pass such paths through the
 * constructors? (and through the factory methods, then)
 *
 * @author Florent Georges
 * @date   2015-01-07
 */
public class FileException
        extends Exception
{
    private FileException(Type type, String msg)
    {
        super(msg);
        myType = type;
    }

    private FileException(Type type, String msg, Throwable cause)
    {
        super(msg, cause);
        myType = type;
    }

    public enum Type {
        NOT_FOUND,
        EXISTS,
        NO_DIR,
        IS_DIR,
        UNKNOWN_ENCODING,
        OUT_OF_RANGE,
        IO_ERROR
    }

    public Type getType() {
        return myType;
    }

    // file:not-found
    public static FileException notFound(String msg) {
        return new FileException(Type.NOT_FOUND, msg);
    }
    public static FileException notFound(String msg, Throwable cause) {
        return new FileException(Type.NOT_FOUND, msg, cause);
    }

    // file:exists
    public static FileException exists(String msg) {
        return new FileException(Type.EXISTS, msg);
    }
    public static FileException exists(String msg, Throwable cause) {
        return new FileException(Type.EXISTS, msg, cause);
    }

    // file:no-dir
    public static FileException noDir(String msg) {
        return new FileException(Type.NO_DIR, msg);
    }
    public static FileException noDir(String msg, Throwable cause) {
        return new FileException(Type.NO_DIR, msg, cause);
    }

    // file:is-dir
    public static FileException isDir(String msg) {
        return new FileException(Type.IS_DIR, msg);
    }
    public static FileException isDir(String msg, Throwable cause) {
        return new FileException(Type.IS_DIR, msg, cause);
    }

    // file:unknown-encoding
    public static FileException unknownEncoding(String msg) {
        return new FileException(Type.UNKNOWN_ENCODING, msg);
    }
    public static FileException unknownEncoding(String msg, Throwable cause) {
        return new FileException(Type.UNKNOWN_ENCODING, msg, cause);
    }

    // file:out-of-range
    public static FileException outOfRange(String msg) {
        return new FileException(Type.OUT_OF_RANGE, msg);
    }
    public static FileException outOfRange(String msg, Throwable cause) {
        return new FileException(Type.OUT_OF_RANGE, msg, cause);
    }

    // file:io-error
    public static FileException ioError(String msg) {
        return new FileException(Type.IO_ERROR, msg);
    }
    public static FileException ioError(String msg, Throwable cause) {
        return new FileException(Type.IO_ERROR, msg, cause);
    }

    private final Type myType;
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
