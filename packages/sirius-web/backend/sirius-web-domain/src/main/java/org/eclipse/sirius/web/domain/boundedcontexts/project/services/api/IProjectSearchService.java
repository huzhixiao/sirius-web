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
package org.eclipse.sirius.web.domain.boundedcontexts.project.services.api;

import java.util.Map;
import java.util.Optional;

import org.eclipse.sirius.web.domain.boundedcontexts.project.Project;
import org.eclipse.sirius.web.domain.boundedcontexts.project.services.Window;
import org.springframework.data.domain.KeysetScrollPosition;

/**
 * Used to retrieve projects.
 *
 * @author sbegaudeau
 */
public interface IProjectSearchService {

    boolean existsById(String projectId);

    Optional<Project> findById(String projectId);

    Window<Project> findAll(KeysetScrollPosition position, int limit, Map<String, Object> filter);
}
