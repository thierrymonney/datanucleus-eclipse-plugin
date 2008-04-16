/**********************************************************************
Copyright (c) 2005 Michael Grundmann and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
2007 Andy Jefferson - added ClassEnhancer selection
    ...
**********************************************************************/
package com.datanucleus.ide.eclipse.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.datanucleus.ide.eclipse.Localiser;

/**
 * Preferences Page for the JPOX Enhancer.
 *
 * @version $Revision$
 */
public class EnhancerPreferencePage extends PropertyAndPreferencePage implements IWorkbenchPreferencePage, PreferenceConstants
{
    public static final String PAGE_ID = "org.jpox.ide.eclipse.preferences.enhancer";

    /** Button containing whether to run the enhancer in verbose mode or not. */
    private Button verboseModeCheckButton;

    /** List containing the selected file extensions to use when enhancing. */
    private List fileExtensionsList;

    /** Button to add a file extension. */
    private Button fileExtensionsAddButton;

    /** Button to remove file extension(s). */
    private Button fileExtensionsRemoveButton;

    /** Text field containing the ClassEnhancer name to use. */
    private Text classEnhancerText;

    /** Combo selector for API. */
    private Combo apiCombo;

    /** Text field containing the persistence-unit name to use. */
    private Text persistenceUnitText;

    private SelectionListener selectionListener = createSelectionListener();

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent)
    {
        Composite composite = (Composite) super.createContents(parent);
        GridLayout mainLayout = new GridLayout();
        mainLayout.numColumns = 2;
        mainLayout.horizontalSpacing = 10;
        composite.setLayout(mainLayout);

        // Help text
        Label help = new Label(composite, SWT.SHADOW_IN | SWT.WRAP | SWT.BORDER);
        help.setBackground(new Color(composite.getDisplay(), 189, 211, 216));
        help.setText(Localiser.getString("EnhancerPreferences.Help"));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        help.setLayoutData(gd);

        // File Extensions
        Group group = new Group(composite, SWT.NONE);
        group.setText(Localiser.getString("EnhancerPreferences.FileExtensions.Label"));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.heightHint = 120;
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginWidth = layout.marginHeight = 10;
        layout.horizontalSpacing = 10;
        group.setLayout(layout);

        fileExtensionsList = new List(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        fileExtensionsList.setLayoutData(data);
        fileExtensionsList.setToolTipText(Localiser.getString("EnhancerPreferences.FileExtensions.Tooltip"));

        Composite buttonGroup = new Composite(group, SWT.NULL);
        GridLayout buttonLayout = new GridLayout();
        buttonLayout.marginTop = 0;
        buttonGroup.setLayout(buttonLayout);
        buttonGroup.setLayoutData(new GridData(GridData.FILL_BOTH, SWT.BEGINNING, false, false));

        fileExtensionsAddButton = new Button(buttonGroup, SWT.PUSH);
        fileExtensionsAddButton.setText(Localiser.getString("EnhancerPreferences.Add.Label"));
        fileExtensionsAddButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        fileExtensionsAddButton.addSelectionListener(selectionListener);

        fileExtensionsRemoveButton = new Button(buttonGroup, SWT.PUSH);
        fileExtensionsRemoveButton.setText(Localiser.getString("EnhancerPreferences.Remove.Label"));
        fileExtensionsRemoveButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        fileExtensionsRemoveButton.addSelectionListener(selectionListener);

        // Verbose
        verboseModeCheckButton = new Button(composite, SWT.CHECK);
        verboseModeCheckButton.setText(Localiser.getString("EnhancerPreferences.Verbose.Label"));
        verboseModeCheckButton.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false, 2, 1));
        verboseModeCheckButton.setToolTipText(Localiser.getString("EnhancerPreferences.Verbose.Tooltip"));

        // API
        Label apiLabel = new Label(composite, SWT.NULL);
        apiLabel.setText(Localiser.getString("EnhancerPreferences.API.Label"));

        apiCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        apiCombo.setItems(new String[] {"JDO", "JPA"});
        GridData apiGrid = new GridData(SWT.FILL, SWT.NULL, false, false);
        apiCombo.setLayoutData(apiGrid);
        apiCombo.setToolTipText(Localiser.getString("EnhancerPreferences.API.Tooltip"));

        // Class Enhancer
        Label classEnhancerLabel = new Label(composite, SWT.NULL);
        classEnhancerLabel.setText(Localiser.getString("EnhancerPreferences.ClassEnhancer.Label"));

        classEnhancerText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        GridData classEnhancerGrid = new GridData(SWT.FILL, SWT.NULL, false, false);
        classEnhancerGrid.widthHint = 50;
        classEnhancerText.setLayoutData(classEnhancerGrid);
        classEnhancerText.setToolTipText(Localiser.getString("EnhancerPreferences.ClassEnhancer.Tooltip"));

        // Persistence Unit
        Label persistenceUnitLabel = new Label(composite, SWT.NULL);
        persistenceUnitLabel.setText(Localiser.getString("EnhancerPreferences.PersistenceUnit.Label"));

        persistenceUnitText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        GridData persistenceUnitGrid = new GridData(SWT.FILL, SWT.NULL, false, false);
        persistenceUnitGrid.widthHint = 50;
        persistenceUnitText.setLayoutData(persistenceUnitGrid);
        persistenceUnitText.setToolTipText(Localiser.getString("EnhancerPreferences.PersistenceUnit.Tooltip"));

        initControls();

        return composite;
    }

    private SelectionAdapter createSelectionListener()
    {
        SelectionAdapter selectionListener = new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                Widget widget = event.widget;
                if (widget == fileExtensionsAddButton)
                {
                    handleAddFileExtension();
                }
                if (widget == fileExtensionsRemoveButton)
                {
                    handleRemoveFileExtension();
                }
            }
        };
        return selectionListener;
    }

    /**
     * Accessor for the file extensions from the List widget.
     * @return The file extensions (separated by ":")
     */
    private String getFileExtensions()
    {
        String[] fileExtensionsArray = fileExtensionsList.getItems();
        StringBuffer fileExtensions = new StringBuffer();
        for (int i = 0; i < fileExtensionsArray.length; i++)
        {
            fileExtensions.append(fileExtensionsArray[i]);
            fileExtensions.append(System.getProperty("path.separator")); //$NON-NLS-1$ 
        }
        return fileExtensions.toString();
    }

    private void handleAddFileExtension()
    {
        InputDialog dialog = new InputDialog(fileExtensionsList.getShell(),
            Localiser.getString("EnhancerPreferences.AddExtension.Title"),
            Localiser.getString("EnhancerPreferences.AddExtension.Label"), null, null);
        dialog.open();
        String result = dialog.getValue();
        if (result == null)
        {
            return;
        }

        if (fileExtensionsList.indexOf(result) < 0)
        {
            // Add the new item if not already there
            fileExtensionsList.add(result);
        }
    }

    private void handleRemoveFileExtension()
    {
        int index = fileExtensionsList.getSelectionIndex();
        if (index >= 0)
        {
            fileExtensionsList.remove(index);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
    }

    /**
     * Convenience method for initializing values. Called after the controls have been created.
     */
    private void initControls()
    {
        String extensions = getPreferenceStore().getString(ENHANCER_INPUT_FILE_EXTENSIONS);
        String[] extensionEntries = extensions.split(System.getProperty("path.separator"));
        fileExtensionsList.setItems(extensionEntries);

        verboseModeCheckButton.setSelection(getPreferenceStore().getBoolean(ENHANCER_VERBOSE_MODE));
        classEnhancerText.setText(getPreferenceStore().getString(ENHANCER_CLASS_ENHANCER));
        apiCombo.setText(getPreferenceStore().getString(ENHANCER_API));
        persistenceUnitText.setText(getPreferenceStore().getString(ENHANCER_PERSISTENCE_UNIT));
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk()
    {
        getPreferenceStore().setValue(ENHANCER_INPUT_FILE_EXTENSIONS, getFileExtensions());
        getPreferenceStore().setValue(ENHANCER_VERBOSE_MODE, verboseModeCheckButton.getSelection());
        getPreferenceStore().setValue(ENHANCER_CLASS_ENHANCER, classEnhancerText.getText());
        getPreferenceStore().setValue(ENHANCER_API, apiCombo.getText());
        getPreferenceStore().setValue(ENHANCER_PERSISTENCE_UNIT, persistenceUnitText.getText());

        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * @see org.jpox.ide.eclipse.preferences.PropertyAndPreferencePage#getPageId()
     */
    protected String getPageId()
    {
        return PAGE_ID;
    }
}