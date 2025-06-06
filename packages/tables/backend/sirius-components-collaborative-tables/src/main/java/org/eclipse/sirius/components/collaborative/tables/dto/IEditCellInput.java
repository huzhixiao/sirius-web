/*******************************************************************************
 * Copyright (c) 2024, 2025 CEA LIST.
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
package org.eclipse.sirius.components.collaborative.tables.dto;

import java.util.UUID;

import org.eclipse.sirius.components.collaborative.tables.api.ITableInput;

/**
 * The input of a cell edition mutation.
 *
 * @author frouene
 */
public interface IEditCellInput extends ITableInput {

    UUID cellId();

    Object newValue();
}
