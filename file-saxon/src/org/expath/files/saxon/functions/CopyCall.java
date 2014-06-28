/****************************************************************************/
/*  File:       CopyCall.java                                               */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2010-06-30                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2010 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.files.saxon.functions;

import java.net.URI;
import java.net.URISyntaxException;
import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.StaticContext;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.BooleanValue;
import org.expath.files.saxon.CallHelper;
import org.expath.files.saxon.Files;

/**
 * TODO: ...
 *
 * @author Florent Georges
 * @date   2010-06-30
 */
public class CopyCall
        extends ExtensionFunctionCall
{
    @Override
    public void supplyStaticContext(StaticContext ctxt, int location, Expression[] args)
            throws XPathException
    {
        try {
            myBase = new URI(ctxt.getBaseURI());
        }
        catch ( URISyntaxException ex ) {
            throw new XPathException(ex);
        }
    }

    @Override
    public Sequence call(XPathContext ctxt, Sequence[] params)
            throws XPathException
    {
        // num of params
        if ( params.length != 2 && params.length != 3 ) {
            throw new XPathException("There is not exactly 2 or 3 params: " + params.length);
        }
        // the params
        // TODO: Resolve source and dest against myBase?
        String  source    = CallHelper.getExactlyOneString(params[0], "$source");
        String  dest      = CallHelper.getExactlyOneString(params[1], "$destination");
        Boolean overwrite = null;
        if ( params.length == 3 ) {
            overwrite = CallHelper.getExactlyOneBoolean(params[2], "$overwrite");
        }
        // the actual call
        Files files = new Files();
        boolean res = files.doCopy(ctxt, source, dest, overwrite);
        if ( res ) {
            return BooleanValue.TRUE;
        }
        else {
            return BooleanValue.FALSE;
        }
    }

    private URI myBase;
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
