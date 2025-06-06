/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.sirius.components.tables.renderer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.representations.IInstancePropsValidator;
import org.eclipse.sirius.components.representations.IProps;
import org.eclipse.sirius.components.tables.components.ICustomCellDescriptor;
import org.eclipse.sirius.components.tables.elements.ColumnElementProps;
import org.eclipse.sirius.components.tables.elements.IconLabelCellElementProps;
import org.eclipse.sirius.components.tables.elements.LineElementProps;
import org.eclipse.sirius.components.tables.elements.MultiSelectCellElementProps;
import org.eclipse.sirius.components.tables.elements.SelectCellElementProps;
import org.eclipse.sirius.components.tables.elements.TableElementProps;
import org.eclipse.sirius.components.tables.elements.TextareaCellElementProps;
import org.eclipse.sirius.components.tables.elements.TextfieldCellElementProps;

/**
 * Used to validate the properties of the instance type.
 *
 * @author arichard
 */
public class TableInstancePropsValidator implements IInstancePropsValidator {

    private final List<ICustomCellDescriptor> customCellDescriptors;

    public TableInstancePropsValidator(List<ICustomCellDescriptor> customCellDescriptors) {
        this.customCellDescriptors = Objects.requireNonNull(customCellDescriptors);
    }

    @Override
    public boolean validateInstanceProps(String type, IProps props) {
        boolean checkValidProps = false;

        if (TableElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof TableElementProps;
        } else if (LineElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof LineElementProps;
        } else if (ColumnElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof ColumnElementProps;
        } else if (TextfieldCellElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof TextfieldCellElementProps;
        } else if (TextareaCellElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof TextareaCellElementProps;
        } else if (SelectCellElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof SelectCellElementProps;
        } else if (MultiSelectCellElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof MultiSelectCellElementProps;
        } else if (IconLabelCellElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof IconLabelCellElementProps;
        } else {
            checkValidProps = this.customCellDescriptors.stream()
                    .map(customCellDescriptor -> customCellDescriptor.validateInstanceProps(type, props))
                    .filter(Optional::isPresent)
                    .findFirst()
                    .map(Optional::get)
                    .orElse(false);
        }

        return checkValidProps;
    }

}
