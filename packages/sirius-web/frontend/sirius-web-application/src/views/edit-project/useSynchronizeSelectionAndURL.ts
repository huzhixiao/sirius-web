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

import { useEffect } from 'react';
import { generatePath, useNavigate, useSearchParams } from 'react-router-dom';

const PROJECT_ID_SEPARATOR = '@';

export const useSynchronizeSelectionAndURL = (
  projectId: string,
  name: string,
  representationId: string,
  selectedProjectId: string | null,
  selectedRepresentationId: string | null,
  skip: boolean
): void => {
  const navigate = useNavigate();
  const [urlSearchParams] = useSearchParams();

  useEffect(() => {
    const projectIdToRedirect = name ? projectId + PROJECT_ID_SEPARATOR + name : projectId;
    let pathname: string | null = null;
    if (selectedRepresentationId && selectedRepresentationId !== representationId && selectedProjectId === projectId) {
      pathname = generatePath('/projects/:projectId/edit/:representationId', {
        projectId: projectIdToRedirect,
        representationId: selectedRepresentationId,
      });
    } else if (selectedRepresentationId === null && representationId) {
      pathname = generatePath('/projects/:projectId/edit/', { projectId: projectIdToRedirect });
    }

    if (!skip && pathname) {
      if (urlSearchParams !== null && urlSearchParams.size > 0) {
        navigate({
          pathname: pathname,
          search: `?${urlSearchParams}`,
        });
      } else {
        navigate(pathname);
      }
    }
  }, [projectId, selectedRepresentationId, representationId, skip, urlSearchParams]);
};
