/****************************************************************************/
/*  File:       CallHelper.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2010-07-04                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2010 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.files.saxon;

import java.net.URI;
import java.net.URISyntaxException;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.StringValue;

/**
 * TODO: ...
 *
 * @author Florent Georges
 * @date   2010-07-04
 */
public class CallHelper
{
    public static Boolean getExactlyOneBoolean(Sequence seq, String name)
            throws XPathException
    {
        Item item = checkExactlyOne(seq, name);
        if ( ! ( item instanceof BooleanValue ) ) {
            throw new XPathException(name + " is not a boolean");
        }
        BooleanValue bool = (BooleanValue) item;
        return bool.getBooleanValue();
    }

    public static String getExactlyOneString(Sequence seq, String name)
            throws XPathException
    {
        Item item = checkExactlyOne(seq, name);
        if ( ! ( item instanceof StringValue ) ) {
            throw new XPathException(name + " is not a string");
        }
        return item.getStringValue();
    }

    public static URI getExactlyOneUri(Sequence seq, String name)
            throws XPathException
    {
        try {
            String str = getExactlyOneString(seq, name);
            return new URI(str);
        }
        catch ( URISyntaxException ex ) {
            throw new XPathException(ex);
        }
    }

    private static Item checkExactlyOne(Sequence seq, String name)
            throws XPathException
    {
        SequenceIterator it = seq.iterate();
        Item item = it.next();
        if ( item == null ) {
            throw new XPathException(name + " is an empty sequence");
        }
        if ( it.next() != null ) {
            throw new XPathException(name + " sequence has more than one item");
        }
        return item;
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
