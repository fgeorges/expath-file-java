/****************************************************************************/
/*  File:       ListCall.java                                               */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2010-02-14                                                  */
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
import org.expath.files.saxon.CallHelper;
import org.expath.files.saxon.Files;

/**
 * TODO: ...
 *
 * @author Florent Georges
 * @date   2010-02-14
 */
public class ListCall
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
        if ( params.length != 1 ) {
            throw new XPathException("There is not exactly 1 param: " + params.length);
        }
        // the param
        // TODO: Resolve href against myBase?
        URI href = CallHelper.getExactlyOneUri(params[0], "$href");
        // the actual call
        Files files = new Files();
        return files.doOldList(ctxt, href);
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
