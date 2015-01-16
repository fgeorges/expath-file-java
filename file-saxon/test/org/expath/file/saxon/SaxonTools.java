/****************************************************************************/
/*  File:       SaxonTools.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-15                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file.saxon;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmValue;
import org.expath.tools.ToolsException;
import org.expath.tools.saxon.fun.Library;

/**
 * Saxon tools for tests of file-saxon features.
 * 
 * @author Florent Georges
 * @date   2015-01-15
 */
public class SaxonTools
{
    public static synchronized XPathCompiler compiler()
            throws ToolsException
    {
        if ( null == COMPILER ) {
            Processor saxon = new Processor(false);
            Library lib = new EXPathFileLibrary();
            lib.register(saxon.getUnderlyingConfiguration());
            COMPILER = saxon.newXPathCompiler();
            COMPILER.declareNamespace(
                    EXPathFileLibrary.NS_PREFIX,
                    EXPathFileLibrary.NS_URI);
        }
        return COMPILER;
    }

    public static XdmValue evaluate(String xpath)
            throws ToolsException
                 , SaxonApiException
    {
        XPathCompiler compiler = compiler();
        return evaluate(compiler, xpath);
    }

    public static XdmValue evaluate(XPathCompiler compiler, String xpath)
            throws ToolsException
                 , SaxonApiException
    {
        XPathExecutable exec = compiler.compile(xpath);
        XPathSelector expr = exec.load();
        return expr.evaluate();
    }

    private static XPathCompiler COMPILER = null;
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
