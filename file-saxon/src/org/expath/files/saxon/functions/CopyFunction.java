/****************************************************************************/
/*  File:       CopyFunction.java                                           */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2010-06-30                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2010 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.files.saxon.functions;

import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.type.ItemType;
import net.sf.saxon.value.SequenceType;
import org.expath.files.saxon.Files;

/**
 * TODO: ...
 *
 * See http://expath.org/modules/file-20100517.html.
 *
 * @author Florent Georges
 * @date   2010-06-30
 */
public class CopyFunction
        extends ExtensionFunctionDefinition
{
    @Override
    public StructuredQName getFunctionQName()
    {
        return new StructuredQName(Files.NS_PREFIX, Files.NS_URI, LOCAL_NAME);
    }

    @Override
    public int getMinimumNumberOfArguments()
    {
        return 2;
    }

    @Override
    public int getMaximumNumberOfArguments()
    {
        return 3;
    }

    @Override
    public SequenceType[] getArgumentTypes()
    {
        final int      one    = StaticProperty.EXACTLY_ONE;
        final ItemType itype1 = BuiltInAtomicType.STRING;
        SequenceType   string = SequenceType.makeSequenceType(itype1, one);
        final ItemType itype2 = BuiltInAtomicType.BOOLEAN;
        SequenceType   bool   = SequenceType.makeSequenceType(itype2, one);
        return new SequenceType[]{ string, string, bool };
    }

    @Override
    public SequenceType getResultType(SequenceType[] types)
    {
        final int      one   = StaticProperty.EXACTLY_ONE;
        final ItemType itype = BuiltInAtomicType.BOOLEAN;
        return SequenceType.makeSequenceType(itype, one);
    }

    @Override
    public ExtensionFunctionCall makeCallExpression()
    {
        return new CopyCall();
    }

    private static final String LOCAL_NAME = "copy";
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
