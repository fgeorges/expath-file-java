/****************************************************************************/
/*  File:       Paths.java                                                  */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Facade for EXPath File module functions in the section "Paths".
 * 
 * @author Florent Georges
 * @date   2015-01-13
 * @see http://expath.org/spec/file#paths
 * @see http://expath.org/spec/file/20131203#paths
 */
public class Paths
{
    // file:name($path as xs:string) as xs:string
    public String name(String path)
    {
        if ( File.separator.equals(path) ) {
            return "";
        }
        else {
            File f = new File(path);
            return f.getName();
        }
    }

    // file:parent($path as xs:string) as xs:string?
    public String parent(String path)
    {
        File f   = new File(path);
        File abs = f.getAbsoluteFile();
        File p   = abs.getParentFile();
        return null == p ? null : Util.stringify(p);
    }

    // file:path-to-native($path as xs:string) as xs:string
    // [file:io-error] is raised if an error occurs while trying to obtain the native path.
    public String pathToNative(String path)
            throws FileException
    {
        try {
            File f = new File(path);
            return Util.stringify(f.getCanonicalFile());
        }
        catch ( IOException ex ) {
            throw FileException.ioError("Error getting the native path", ex);
        }
    }

    // file:path-to-uri($path as xs:string) as xs:anyURI
    public URI pathToUri(String path)
            throws FileException
    {
        File f = new File(path);
        File abs = f.getAbsoluteFile();
        return abs.toURI();
    }

    // file:resolve-path($path as xs:string) as xs:string
    public String resolvePath(String path)
    {
        File f = new File(path);
        return Util.stringify(f.getAbsoluteFile());
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
