/****************************************************************************/
/*  File:       ExistsTest.java                                             */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2015-01-14                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file.saxon.props;

import java.io.File;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.value.BooleanValue;
import org.expath.file.TestTools;
import org.expath.file.saxon.SaxonTools;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test the file:exists function.
 *
 * @author Florent Georges
 * @date   2015-01-13
 * @see http://expath.org/spec/file#pr.exists
 * @see http://expath.org/spec/file/20131203#pr.exists
 */
public class ExistsTest
{
    @Test
    public void exists_file()
            throws Exception
    {
        testExists(new File(LIST, "file.txt"), BooleanValue.TRUE);
    }

    @Test
    public void exists_dir()
            throws Exception
    {
        testExists(new File(LIST, "dir"), BooleanValue.TRUE);
    }

    @Test
    public void exists_notExisting()
            throws Exception
    {
        testExists(new File(LIST, "does-not-exist"), BooleanValue.FALSE);
    }

    private void testExists(File file, BooleanValue expected)
            throws Exception
    {
        String expr = "file:exists('" + file.getAbsolutePath() + "')";
        XdmValue value = SaxonTools.evaluate(expr);
        assertNotNull(value, "result must not be null");
        assertSame(value.getUnderlyingValue().getClass(), BooleanValue.class,
                "result must be boolean value");
        BooleanValue b = (BooleanValue) value.getUnderlyingValue();
        assertSame(b, expected, "result must be " + expected);
    }

    @BeforeClass
    public static void setUpClass()
            throws Exception
    {
        AREA = TestTools.initArea("directory");
        LIST = new File(AREA, "list");
    }

    private static File AREA = null;
    private static File LIST = null;
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
