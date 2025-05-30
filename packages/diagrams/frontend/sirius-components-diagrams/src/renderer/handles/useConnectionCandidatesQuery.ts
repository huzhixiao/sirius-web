/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo and others.
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
import { gql, useQuery } from '@apollo/client';
import { useMultiToast } from '@eclipse-sirius/sirius-components-core';
import { Edge, Node, useReactFlow } from '@xyflow/react';
import { useEffect, useMemo } from 'react';
import {
  GQLDiagramDescription,
  GQLGetToolSectionsData,
  GQLGetToolSectionsVariables,
  GQLNodeDescription,
  GQLPaletteEntry,
  GQLRepresentationDescription,
  GQLSingleClickOnTwoDiagramElementsTool,
  GQLToolSection,
} from '../connector/useConnector.types';
import { EdgeData, NodeData } from '../DiagramRenderer.types';

const getToolSectionsQuery = gql`
  query getToolSections($editingContextId: ID!, $diagramId: ID!, $diagramElementId: ID!) {
    viewer {
      editingContext(editingContextId: $editingContextId) {
        representation(representationId: $diagramId) {
          description {
            ... on DiagramDescription {
              palette(diagramElementId: $diagramElementId) {
                paletteEntries {
                  ...ConnectionToolFields
                  ... on ToolSection {
                    tools {
                      ...ConnectionToolFields
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  fragment ConnectionToolFields on Tool {
    __typename
    ... on SingleClickOnTwoDiagramElementsTool {
      candidates {
        sources {
          id
        }
        targets {
          id
        }
      }
      dialogDescriptionId
    }
  }
`;

const isDiagramDescription = (
  representationDescription: GQLRepresentationDescription
): representationDescription is GQLDiagramDescription => representationDescription.__typename === 'DiagramDescription';
const isSingleClickOnTwoDiagramElementsTool = (
  entry: GQLPaletteEntry
): entry is GQLSingleClickOnTwoDiagramElementsTool => entry.__typename === 'SingleClickOnTwoDiagramElementsTool';
const isToolSection = (entry: GQLPaletteEntry): entry is GQLToolSection => entry.__typename === 'ToolSection';

export const useConnectionCandidatesQuery = (
  editingContextId: string,
  diagramId: string,
  nodeId: string
): GQLNodeDescription[] | null => {
  const { addErrorMessage } = useMultiToast();
  const { getNodes, getEdges } = useReactFlow<Node<NodeData>, Edge<EdgeData>>();

  const shouldSkip =
    getNodes().filter((node) => node.selected).length + getEdges().filter((edge) => edge.selected).length > 1;

  const { data, error } = useQuery<GQLGetToolSectionsData, GQLGetToolSectionsVariables>(getToolSectionsQuery, {
    variables: {
      editingContextId,
      diagramId,
      diagramElementId: nodeId,
    },
    skip: shouldSkip,
  });

  const diagramDescription: GQLRepresentationDescription | null =
    data?.viewer.editingContext.representation.description ?? null;

  const collectConnectionToolsFromPaletteEntry = (entry: GQLPaletteEntry): GQLNodeDescription[] => {
    const candidates: GQLNodeDescription[] = [];
    if (isSingleClickOnTwoDiagramElementsTool(entry)) {
      entry.candidates.forEach((candidate) => candidates.push(...candidate.targets));
    } else if (isToolSection(entry)) {
      entry.tools.filter(isSingleClickOnTwoDiagramElementsTool).forEach((tool) => {
        tool.candidates.forEach((candidate) => candidates.push(...candidate.targets));
      });
    }
    return candidates;
  };

  const nodeCandidates: GQLNodeDescription[] = useMemo(() => {
    const candidates: GQLNodeDescription[] = [];
    if (diagramDescription && isDiagramDescription(diagramDescription)) {
      diagramDescription.palette.paletteEntries.forEach((entry) =>
        candidates.push(...collectConnectionToolsFromPaletteEntry(entry))
      );
    }

    return candidates;
  }, [diagramDescription]);

  useEffect(() => {
    if (error) {
      addErrorMessage(error.message);
    }
  }, [error]);

  return nodeCandidates;
};
