/****************************************************************************/
/*  File:       Driver.java                                                 */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-15                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.file.saxon.qt3;

import java.io.File;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.StandardNames;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.testdriver.Environment;
import net.sf.saxon.testdriver.QT3TestDriverHE;
import net.sf.saxon.trans.SymbolicName;
import org.expath.file.saxon.EXPathFileLibrary;
import org.expath.tools.ToolsException;
import org.expath.tools.saxon.fun.Library;

/**
 * Driver to the QT3 test suite from the EXPath Community Group.
 * 
 * Extends the Saxon driver to configure it properly with extension functions.
 * 
 * DO NOT RUN IT FROM NetBeans!  The Saxon test driver requires the current
 * working directory to be the QT3 directory, and there is no way to set the
 * current directory in Java.  This is because the EXPath File function create
 * sometimes files, and they use the current directory to resolve paths.
 * 
 * Execute the class from file-saxon/test-rsrc/qt3/Makefile, which (re)compiles
 * and sets the classpath properly.
 * 
 * @author Florent Georges
 * @date   2015-01-15
 */
public class Driver
        extends QT3TestDriverHE
{
    public static void main(String[] args)
            throws Exception
    {
        if ( args.length < 1 ) {
            throw new Exception(
                    "Missing parameter, must pass the test-rsrc/qt3/ absolut path,"
                    + " which must be the current directory as well!");
        }
        File qt3     = new File(args[0]);
        if ( ! qt3.exists() ) {
            throw new Exception("File does not exist: " + qt3);
        }
        if ( ! qt3.isDirectory() ) {
            throw new Exception("File is not a directory: " + qt3);
        }
        File catalog = new File(qt3, "catalog.xml");
        File results = new File(qt3, "results");
        new Driver().go(new String[]{
            qt3.getAbsolutePath(),
            catalog.getAbsolutePath(),
            "-o:" + results.getAbsolutePath(),
            // shows the actual result in case a test fails
            // comment it out except when you investigate a test failure
            "-debug",
            "-lang:XQ30" });
    }

    /**
     * This method return the environment to evaluate a test case.
     * 
     * The environment contains the processor to use.  This is the best
     * opportunity to catch it and register the extension functions.  It first
     * checks whether the functions already have been registered on that
     * processor.
     */
    @Override
    protected Environment getEnvironment(XdmNode testCase, XPathCompiler xpc)
            throws SaxonApiException
    {
        // the environments
        Environment env = super.getEnvironment(testCase, xpc);
        // the name of file:exists($path)
        SymbolicName exists = new SymbolicName(
                StandardNames.XSL_FUNCTION,
                new StructuredQName("file", "http://expath.org/ns/file", "exists"),
                1);
        // the config object
        Configuration conf = env.processor.getUnderlyingConfiguration();
        // has file:exists($path) already been registered?
        if ( ! conf.getIntegratedFunctionLibrary().isAvailable(exists) ) {
            try {
                Library lib = new EXPathFileLibrary();
                lib.register(conf);
            }
            catch ( ToolsException ex ) {
                throw new SaxonApiException("Error registering the EXPath File functions", ex);
            }
        }
        return env;
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
