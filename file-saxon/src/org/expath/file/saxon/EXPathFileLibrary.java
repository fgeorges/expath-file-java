/****************************************************************************/
/*  File:       EXPathFileLibrary.java                                      */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file.saxon;

import net.sf.saxon.trans.XPathException;
import org.expath.file.FileException;
import org.expath.file.saxon.inout.*;
import org.expath.file.saxon.paths.*;
import org.expath.file.saxon.props.*;
import org.expath.tools.ToolsException;
import org.expath.tools.saxon.fun.Function;
import org.expath.tools.saxon.fun.Library;

/**
 * The library of extension functions for Saxon, implementing EXPath File.
 *
 * @author Florent Georges
 * @date   2015-01-13
 */
public class EXPathFileLibrary
        extends Library
{
    public EXPathFileLibrary()
    {
        super(NS_URI, NS_PREFIX);
    }

    @Override
    protected Function[] functions()
            throws ToolsException
    {
        return new Function[] {
            // File Properties
            new Exists(this),
            new IsDir(this),
            new IsFile(this),
            new LastModified(this),
            new Size(this),
            // Input/Output
            new Append(this),
            new AppendBinary(this),
            new AppendText(this),
            new AppendTextLines(this),
            new Copy(this),
            new CreateDir(this),
            new CreateTempDir(this),
            new CreateTempFile(this),
            new Delete(this),
            new List(this),
            new Move(this),
            new ReadBinary(this),
            new ReadText(this),
            new ReadTextLines(this),
            new Write(this),
            new WriteBinary(this),
            new WriteText(this),
            new WriteTextLines(this),
            // Paths
            new Name(this),
            new Parent(this),
            new PathToNative(this),
            new PathToUri(this),
            new ResolvePath(this),
            // System Properties
            new DirSeparator(this),
            new LineSeparator(this),
            new PathSeparator(this),
            new TempDir(this)
        };
    }

    public XPathException error(FileException ex)
    {
        switch ( ex.getType() ) {
            case EXISTS:
                return error(ERR_EXISTS, ex.getMessage(), ex);
            case IO_ERROR:
                return error(ERR_IO_ERROR, ex.getMessage(), ex);
            case IS_DIR:
                return error(ERR_IS_DIR, ex.getMessage(), ex);
            case NOT_FOUND:
                return error(ERR_NOT_FOUND, ex.getMessage(), ex);
            case NO_DIR:
                return error(ERR_NO_DIR, ex.getMessage(), ex);
            case OUT_OF_RANGE:
                return error(ERR_OUT_OF_RANGE, ex.getMessage(), ex);
            case UNKNOWN_ENCODING:
                return error(ERR_UNKNOWN_ENCODING, ex.getMessage(), ex);
            default:
                return error(
                        ERR_IO_ERROR,
                        "Unknown error type: " + ex.getType() + ": " + ex.getMessage(),
                        ex);
        }
    }

    public static final String NS_URI    = "http://expath.org/ns/file";
    public static final String NS_PREFIX = "file";

    private static final String ERR_EXISTS           = "exists";
    private static final String ERR_IO_ERROR         = "io-error";
    private static final String ERR_IS_DIR           = "is-dir";
    private static final String ERR_NOT_FOUND        = "not-found";
    private static final String ERR_NO_DIR           = "no-dir";
    private static final String ERR_OUT_OF_RANGE     = "out-of-range";
    private static final String ERR_UNKNOWN_ENCODING = "unknown-encoding";
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
