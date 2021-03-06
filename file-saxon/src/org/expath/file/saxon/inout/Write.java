/****************************************************************************/
/*  File:       Write.java                                                  */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-14                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file.saxon.inout;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import org.expath.file.FileException;
import org.expath.file.saxon.EXPathFileLibrary;
import org.expath.tools.ToolsException;
import org.expath.tools.model.Element;
import org.expath.tools.saxon.fun.Definition;
import org.expath.tools.saxon.fun.Function;
import org.expath.tools.saxon.fun.Parameters;
import org.expath.tools.saxon.fun.Return;
import org.expath.tools.saxon.fun.Types;

/**
 * The file:write function.
 *
 * @author Florent Georges
 * @date   2015-01-14
 * @see http://expath.org/spec/file#fn.write
 * @see http://expath.org/spec/file/20131203#fn.write
 */
public class Write
        extends Function
{
    public Write(EXPathFileLibrary lib)
    {
        super(lib);
        myLib = lib;
    }

    @Override
    protected Definition makeDefinition()
            throws ToolsException
    {
        return library()
                .function(this, LOCAL_NAME)
                .returns(Types.EMPTY_SEQUENCE)
                .param(Types.SINGLE_STRING,  PARAM_FILE)
                .param(Types.ANY_ITEM,       PARAM_ITEMS)
                .optional()
                // TODO: Add the element name: element(output:serialization-parameters)...
                .param(Types.SINGLE_ELEMENT, PARAM_PARAMS)
                .make();
    }

    @Override
    public Sequence call(XPathContext ctxt, Sequence[] orig_params)
            throws XPathException
    {
        // the params
        Parameters params = checkParams(orig_params);
        String file = params.asString(0, false);
        org.expath.tools.model.Sequence items = params.asSequence(1, false, ctxt);
        // the actual call
        try {
            org.expath.file.Write write = new org.expath.file.Write();
            if ( orig_params.length == 2 ) {
                write.write(file, items);
            }
            else {
                Element ser_params = params.asElement(2, false, ctxt);
                write.write(file, items, ser_params);
            }
            return Return.empty();
        }
        catch ( FileException ex ) {
            throw myLib.error(ex);
        }
    }

    private final EXPathFileLibrary myLib;
    private static final String LOCAL_NAME   = "write";
    private static final String PARAM_FILE   = "file";
    private static final String PARAM_ITEMS  = "items";
    private static final String PARAM_PARAMS = "params";
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
