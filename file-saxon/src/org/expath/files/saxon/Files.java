/****************************************************************************/
/*  File:       Files.java                                                  */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2010-02-14                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2010 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.files.saxon;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.sf.saxon.event.Builder;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.type.Untyped;
import org.apache.commons.io.FileUtils;


/**
 * TODO: ...
 *
 * @author Florent Georges
 * @date   2010-02-14
 */
public class Files
{
    public static boolean copy(XPathContext ctxt, String source, String destination)
            throws XPathException
    {
        Files f = new Files();
        return f.doCopy(ctxt, source, destination, null);
    }

    public static boolean copy(XPathContext ctxt, String source, String destination, boolean overwrite)
            throws XPathException
    {
        Files f = new Files();
        return f.doCopy(ctxt, source, destination, overwrite);
    }

    public boolean doCopy(XPathContext ctxt, String source, String destination, Boolean overwrite)
            throws XPathException
    {
        File src = new File(source);
        File dst = new File(destination);
        boolean ovrt = overwrite == null ? false : overwrite;
        if ( ! src.exists() ) {
            return false;
        }
        if ( ! ovrt && dst.exists() ) {
            return false;
        }
        try {
            FileUtils.copyFile(src, dst);
            return true;
        }
        catch ( IOException ex ) {
            return false;
        }
    }

    public static boolean exists(XPathContext ctxt, String href)
            throws XPathException
    {
        Files f = new Files();
        return f.doExists(ctxt, href);
    }

    public boolean doExists(XPathContext ctxt, String href)
            throws XPathException
    {
        return new File(href).exists();
    }

    public static NodeInfo oldList(XPathContext ctxt, URI href)
            throws XPathException
    {
        Files f = new Files();
        return f.doOldList(ctxt, href);
    }

    public NodeInfo doOldList(XPathContext ctxt, URI href)
            throws XPathException
    {
        File dir = new File(href);
        if ( ! dir.exists() ) {
            return null;
        }
        if ( ! dir.isDirectory() ) {
            throw new XPathException("Href is not a directory: " + href);
        }
        // the config and pipeline objects
        Builder b = ctxt.getController().makeBuilder();
        // start...
        b.open();
        // root element
        startElement(b, NAME_DIR);
        attribute(b, NAME_HREF, href.toString());
        attribute(b, NAME_NAME, dir.getName());
        b.startContent();
        for ( File child : dir.listFiles() ) {
            outputChild(child, b);
        }
        b.endElement();
        b.close();
        return b.getCurrentRoot();
    }

    private void outputChild(File child, Builder b)
            throws XPathException
    {
        if ( child.isDirectory() ) {
            startElement(b, NAME_DIR);
            attribute(b, NAME_HREF, child.toURI().toString());
            attribute(b, NAME_NAME, child.getName());
            b.startContent();
            for ( File f : child.listFiles() ) {
                outputChild(f, b);
            }
            b.endElement();
        }
        else {
            startElement(b, NAME_FILE);
            attribute(b, NAME_HREF, child.toURI().toString());
            attribute(b, NAME_NAME, child.getName());
            b.startContent();
            b.endElement();
        }
    }

    private void startElement(Builder b, NodeName n)
            throws XPathException
    {
        b.startElement(n, Untyped.getInstance(), 0, 0);
    }

    private void attribute(Builder b, NodeName n, String v)
            throws XPathException
    {
        b.attribute(n, BuiltInAtomicType.UNTYPED_ATOMIC, v, 0, 0);
    }

    public static final String NS_URI    = "http://expath.org/ns/file";
    public static final String NS_PREFIX = "file";

    private static final NodeName NAME_FILE = new FingerprintedQName(NS_PREFIX, NS_URI, "file");
    private static final NodeName NAME_DIR  = new FingerprintedQName(NS_PREFIX, NS_URI, "dir");
    private static final NodeName NAME_HREF = new NoNamespaceName("href");
    private static final NodeName NAME_NAME = new NoNamespaceName("name");
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
