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

import { GQLCell } from '../table/TableContent.types';

export interface CellProps {
  editingContextId: string;
  representationId: string;
  tableId: string;
  cell: GQLCell | null;
  disabled: boolean;
  enableSelectionSynchronization: boolean;
}

export interface CustomCellProps {
  editingContextId: string;
  representationId: string;
  tableId: string;
  cell: GQLCell | null;
  disabled: boolean;
}
