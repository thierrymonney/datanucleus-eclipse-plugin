/**********************************************************************
 Copyright (c) 2004 Erik Bengtson and others.
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the JPOX License v1.0
 which accompanies this distribution. 

 Contributors:
 ...
 **********************************************************************/
package org.jpox.ide.eclipse.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jpox.ide.eclipse.project.ProjectHelper;
import org.jpox.ide.eclipse.project.ProjectNature;

/**
 * @author erik
 * @version $Revision: 1.2 $
 */
public class AddNatureAction implements IObjectActionDelegate
{
    private ISelection selection;

    public AddNatureAction()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
     *      org.eclipse.ui.IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {
        if (selection == null)
        {
            return;
        }
        if (!(selection instanceof StructuredSelection))
        {
            return;
        }
        StructuredSelection ss = (StructuredSelection) selection;
        if (!IJavaProject.class.isAssignableFrom(ss.getFirstElement().getClass()))
        {
            return;
        }
        IJavaProject javaProject = (IJavaProject) ss.getFirstElement();
        IProject project = javaProject.getProject();
        ProjectHelper.addNature(project, ProjectNature.NATURE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        this.selection = selection;
    }
}