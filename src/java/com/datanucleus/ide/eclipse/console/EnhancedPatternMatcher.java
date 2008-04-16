/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.datanucleus.ide.eclipse.console;

import org.eclipse.ui.console.PatternMatchEvent;

public class EnhancedPatternMatcher extends AbstractJavacPatternMatcher
{

    /*
     *     " : class"
     */
    public void matchFound(PatternMatchEvent event)
    {
        String matchedText = getMatchedText(event);
        if (matchedText == null)
        {
            return;
        }

        int index = matchedText.indexOf(" : "); //$NON-NLS-1$
        String filePath = matchedText.substring(index + 3);
        filePath = filePath.trim();

        int fileStart = matchedText.indexOf(filePath);
        int eventOffset = event.getOffset() + fileStart;
        int eventLength = filePath.length();

        int lineNumber = -1;
        addLink(filePath, lineNumber, eventOffset, eventLength);
    }
}