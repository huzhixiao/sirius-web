/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.sirius.web.application.project.data.versioning.dto;

import java.util.UUID;

import org.eclipse.sirius.components.core.api.IInput;

/**
 * The input object of the GetBranchesRestEventHandler.
 *
 * @author arichard
 */
public record GetBranchesRestInput(UUID id) implements IInput {

}
