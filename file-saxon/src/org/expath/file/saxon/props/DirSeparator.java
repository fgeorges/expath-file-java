/****************************************************************************/
/*  File:       DirSeparator.java                                           */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-14                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file.saxon.props;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import org.expath.file.Properties;
import org.expath.tools.ToolsException;
import org.expath.tools.saxon.fun.Definition;
import org.expath.tools.saxon.fun.Function;
import org.expath.tools.saxon.fun.Library;
import org.expath.tools.saxon.fun.Return;
import org.expath.tools.saxon.fun.Types;

/**
 * The file:dir-separator function.
 *
 * @author Florent Georges
 * @date   2015-01-14
 * @see http://expath.org/spec/file#pr.dir-separator
 * @see http://expath.org/spec/file/20131203#pr.dir-separator
 */
public class DirSeparator
        extends Function
{
    public DirSeparator(Library lib)
    {
        super(lib);
    }

    @Override
    protected Definition makeDefinition()
            throws ToolsException
    {
        return library()
                .function(this, LOCAL_NAME)
                .returns(Types.SINGLE_STRING)
                .make();
    }

    @Override
    public Sequence call(XPathContext ctxt, Sequence[] orig_params)
            throws XPathException
    {
        // the actual call
        Properties props = new Properties();
        String res = props.dirSeparator();
        return Return.value(res);
    }

    private static final String LOCAL_NAME = "dir-separator";
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
