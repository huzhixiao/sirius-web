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

import { IconOverlay } from '@eclipse-sirius/sirius-components-core';
import { OmniboxCommandComponentProps } from '@eclipse-sirius/sirius-components-omnibox';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { useState } from 'react';
import { ImportLibraryCommandState } from './ImportLibraryCommand.types';
import { ImportLibraryDialog } from './ImportLibraryDialog';

export const ImportLibraryCommand = ({ command, onKeyDown, onClose }: OmniboxCommandComponentProps) => {
  const [state, setState] = useState<ImportLibraryCommandState>({
    open: false,
  });

  const handleClick = () => setState((prevState) => ({ ...prevState, open: true }));

  return (
    <>
      <ListItemButton key={command.id} data-testid={command.label} onClick={handleClick} onKeyDown={onKeyDown}>
        <ListItemIcon>
          <IconOverlay iconURL={command.iconURLs} alt={command.label} />
        </ListItemIcon>
        <ListItemText sx={{ whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>{command.label}</ListItemText>
      </ListItemButton>
      {state.open && <ImportLibraryDialog open={state.open} title="Import libraries" onClose={onClose} />}
    </>
  );
};
