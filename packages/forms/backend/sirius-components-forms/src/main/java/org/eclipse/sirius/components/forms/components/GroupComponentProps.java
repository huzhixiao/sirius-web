/*******************************************************************************
 * Copyright (c) 2019, 2023 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.components.forms.components;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.forms.description.GroupDescription;
import org.eclipse.sirius.components.forms.renderer.IWidgetDescriptor;
import org.eclipse.sirius.components.representations.IProps;
import org.eclipse.sirius.components.representations.VariableManager;

/**
 * The properties of the group component.
 *
 * @author sbegaudeau
 */
public class GroupComponentProps implements IProps {
    private VariableManager variableManager;

    private GroupDescription groupDescription;

    private final List<IWidgetDescriptor> widgetDescriptors;

    public GroupComponentProps(VariableManager variableManager, GroupDescription groupDescription, List<IWidgetDescriptor> widgetDescriptors) {
        this.variableManager = Objects.requireNonNull(variableManager);
        this.groupDescription = Objects.requireNonNull(groupDescription);
        this.widgetDescriptors = Objects.requireNonNull(widgetDescriptors);
    }

    public VariableManager getVariableManager() {
        return this.variableManager;
    }

    public GroupDescription getGroupDescription() {
        return this.groupDescription;
    }

    public List<IWidgetDescriptor> getWidgetDescriptors() {
        return this.widgetDescriptors;
    }
}
