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
package org.eclipse.sirius.web.application.table.customcells;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.representations.IProps;
import org.eclipse.sirius.components.representations.VariableManager;

/**
 * The props of the Checkbox-based cell component.
 *
 * @author lfasani
 */
public record CheckboxCellComponentProps(VariableManager variableManager, CheckboxCellDescription checkboxCellDescription, UUID cellId, UUID columnId, Object columnTargetObject) implements IProps {

    public CheckboxCellComponentProps {
        Objects.requireNonNull(variableManager);
        Objects.requireNonNull(checkboxCellDescription);
        Objects.requireNonNull(cellId);
        Objects.requireNonNull(columnId);
        Objects.requireNonNull(columnTargetObject);
    }
}
