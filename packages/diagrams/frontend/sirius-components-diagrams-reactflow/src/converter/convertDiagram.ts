/*******************************************************************************
 * Copyright (c) 2023 Obeo and others.
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

import { Edge, Node } from 'reactflow';
import { GQLDiagram } from '../graphql/subscription/diagramFragment.types';
import { GQLLabel, GQLLabelStyle } from '../graphql/subscription/labelFragment.types';
import { GQLImageNodeStyle, GQLNode, GQLRectangularNodeStyle } from '../graphql/subscription/nodeFragment.types';
import { Diagram, Label } from '../renderer/DiagramRenderer.types';
import { ImageNodeData } from '../renderer/ImageNode.types';
import { ListItemData, ListNodeData } from '../renderer/ListNode.types';
import { RectangularNodeData } from '../renderer/RectangularNode.types';
import { CustomEdgeData } from '../renderer/edge/CustomEdge.types';

const toRectangularNode = (gqlNode: GQLNode, gqlParentNode: GQLNode | null): Node<RectangularNodeData> => {
  const style = gqlNode.style as GQLRectangularNodeStyle;
  const { targetObjectId, targetObjectLabel, targetObjectKind, position, size } = gqlNode;
  const labelStyle = gqlNode.label.style;

  const data: RectangularNodeData = {
    targetObjectId,
    targetObjectLabel,
    targetObjectKind,
    style: {
      backgroundColor: style.color,
      borderColor: style.borderColor,
      borderRadius: style.borderRadius,
      borderWidth: style.borderSize,
      borderStyle: style.borderStyle,
    },
    label: {
      id: gqlNode.label.id,
      text: gqlNode.label.text,
      style: {
        textAlign: 'center',
        ...convertLabelStyle(labelStyle),
      },
      iconURL: labelStyle.iconURL,
    },
  };

  const verticalAlignmentIndex = gqlNode.label.type.indexOf('v_');
  const horitonzalAlignmentIndex = gqlNode.label.type.indexOf('-h_');
  const verticalAlignment = gqlNode.label.type.substring(
    verticalAlignmentIndex + 'v_'.length,
    horitonzalAlignmentIndex
  );
  const horizontalAliment = gqlNode.label.type.substring(horitonzalAlignmentIndex + '-h_'.length);

  if (verticalAlignment === 'top') {
    data.label.style.alignSelf = 'flex-start';
  } else if (verticalAlignment === 'center') {
    data.label.style.alignSelf = 'center';
  } else if (verticalAlignment === 'bottom') {
    data.label.style.alignSelf = 'flex-end';
  }

  if (horizontalAliment === 'start') {
    data.label.style.marginRight = 'auto';
  } else if (horizontalAliment === 'center') {
    data.label.style.marginLeft = 'auto';
    data.label.style.marginRight = 'auto';
  } else if (horizontalAliment === 'end') {
    data.label.style.marginLeft = 'auto';
  }

  if (gqlNode.label.type === 'label:inside-center') {
    data.label.style.marginLeft = 'auto';
    data.label.style.marginRight = 'auto';
  }

  const node: Node<RectangularNodeData> = {
    id: gqlNode.id,
    type: 'rectangularNode',
    data,
    position,
    style: {
      width: `${size.width}px`,
      height: `${size.height}px`,
    },
  };

  if (gqlParentNode) {
    node.parentNode = gqlParentNode.id;
    node.extent = 'parent';
  }

  return node;
};

const toListNode = (gqlNode: GQLNode, gqlParentNode: GQLNode | null): Node<ListNodeData> => {
  const style = gqlNode.style as GQLRectangularNodeStyle;
  const labelStyle = gqlNode.label.style;

  const listItems: ListItemData[] = (gqlNode.childNodes ?? []).map((gqlChildNode) => {
    const { id, label } = gqlChildNode;
    return {
      id,
      label: {
        id: label.id,
        text: label.text,
        style: {},
        iconURL: null,
      },
      style: {
        textAlign: 'left',
        ...convertLabelStyle(label.style),
      },
    };
  });

  const { targetObjectId, targetObjectLabel, targetObjectKind, position } = gqlNode;
  const data: ListNodeData = {
    targetObjectId,
    targetObjectLabel,
    targetObjectKind,
    style: {
      backgroundColor: style.color,
      borderColor: style.borderColor,
      borderRadius: style.borderRadius,
      borderWidth: style.borderSize,
      borderStyle: style.borderStyle,
    },
    label: {
      id: gqlNode.label.id,
      text: gqlNode.label.text,
      iconURL: labelStyle.iconURL,
      style: {
        textAlign: 'center',
        ...convertLabelStyle(labelStyle),
      },
    },
    listItems,
  };

  if (style.withHeader) {
    data.label.style.borderBottom = `${style.borderSize}px ${style.borderStyle} ${style.borderColor}`;
  }

  const verticalAlignmentIndex = gqlNode.label.type.indexOf('v_');
  const horitonzalAlignmentIndex = gqlNode.label.type.indexOf('-h_');
  const verticalAlignment = gqlNode.label.type.substring(
    verticalAlignmentIndex + 'v_'.length,
    horitonzalAlignmentIndex
  );
  const horizontalAliment = gqlNode.label.type.substring(horitonzalAlignmentIndex + '-h_'.length);

  if (verticalAlignment === 'top') {
    data.label.style.alignSelf = 'flex-start';
  } else if (verticalAlignment === 'center') {
    data.label.style.alignSelf = 'center';
  } else if (verticalAlignment === 'bottom') {
    data.label.style.alignSelf = 'flex-end';
  }

  if (horizontalAliment === 'start') {
    data.label.style.marginRight = 'auto';
  } else if (horizontalAliment === 'center') {
    data.label.style.marginLeft = 'auto';
    data.label.style.marginRight = 'auto';
  } else if (horizontalAliment === 'end') {
    data.label.style.marginLeft = 'auto';
  }

  if (gqlNode.label.type === 'label:inside-center') {
    data.label.style.marginLeft = 'auto';
    data.label.style.marginRight = 'auto';
  }

  const node: Node<ListNodeData> = {
    id: gqlNode.id,
    type: 'listNode',
    data,
    position,
  };

  if (gqlParentNode) {
    node.parentNode = gqlParentNode.id;
    node.extent = 'parent';
  }

  return node;
};

const toImageNode = (gqlNode: GQLNode, gqlParentNode: GQLNode | null): Node<ImageNodeData> => {
  const style = gqlNode.style as GQLImageNodeStyle;
  const { targetObjectId, targetObjectLabel, targetObjectKind, position, size } = gqlNode;

  const data: ImageNodeData = {
    targetObjectId,
    targetObjectKind,
    targetObjectLabel,
    label: null,
    imageURL: style.imageURL,
    style: {},
  };

  const node: Node<ImageNodeData> = {
    id: gqlNode.id,
    type: 'imageNode',
    data,
    position,
    style: {
      width: `${size.width}px`,
      height: `${size.height}px`,
    },
  };

  if (gqlParentNode) {
    node.parentNode = gqlParentNode.id;
    node.extent = 'parent';
  }

  return node;
};

const convertNode = (gqlNode: GQLNode, parentNode: GQLNode | null, nodes: Node[]): void => {
  if (gqlNode.style.__typename === 'RectangularNodeStyle') {
    const isList =
      (gqlNode.childNodes ?? []).filter((gqlChildNode) => gqlChildNode.style.__typename === 'IconLabelNodeStyle')
        .length > 0;
    if (!isList) {
      nodes.push(toRectangularNode(gqlNode, parentNode));

      (gqlNode.borderNodes ?? []).forEach((gqlBorderNode) => convertNode(gqlBorderNode, gqlNode, nodes));
      (gqlNode.childNodes ?? []).forEach((gqlChildNode) => convertNode(gqlChildNode, gqlNode, nodes));
    } else {
      nodes.push(toListNode(gqlNode, parentNode));

      (gqlNode.borderNodes ?? []).forEach((gqlBorderNode) => convertNode(gqlBorderNode, gqlNode, nodes));
    }
  } else if (gqlNode.style.__typename === 'ImageNodeStyle') {
    nodes.push(toImageNode(gqlNode, parentNode));

    (gqlNode.borderNodes ?? []).forEach((gqlBorderNode) => convertNode(gqlBorderNode, gqlNode, nodes));
    (gqlNode.childNodes ?? []).forEach((gqlChildNode) => convertNode(gqlChildNode, gqlNode, nodes));
  }
};

const nodeDepth = (nodeId2node: Map<string, Node>, nodeId: string): number => {
  const node = nodeId2node.get(nodeId);
  let depth = 0;

  let parentNode = node.parentNode ? nodeId2node.get(node.parentNode) : undefined;
  while (parentNode) {
    depth = depth + 1;
    parentNode = parentNode.parentNode ? nodeId2node.get(parentNode.parentNode) : undefined;
  }

  return depth;
};

const convertEdgeLabel = (gqlEdgeLabel: GQLLabel): Label => {
  return {
    id: gqlEdgeLabel.id,
    text: gqlEdgeLabel.text,
    iconURL: gqlEdgeLabel.style.iconURL,
    style: {
      position: 'absolute',
      background: 'transparent',
      padding: 10,
      zIndex: 999,
      ...convertLabelStyle(gqlEdgeLabel.style),
    },
  };
};

const convertLabelStyle = (gqlLabelStyle: GQLLabelStyle): React.CSSProperties => {
  const style: React.CSSProperties = {};

  if (gqlLabelStyle.bold) {
    style.fontWeight = 'bold';
  }

  if (gqlLabelStyle.italic) {
    style.fontStyle = 'italic';
  }

  if (gqlLabelStyle.fontSize) {
    style.fontSize = gqlLabelStyle.fontSize;
  }
  if (gqlLabelStyle.color) {
    style.color = gqlLabelStyle.color;
  }

  let decoration: string = '';
  if (gqlLabelStyle.strikeThrough) {
    decoration = decoration + 'line-through';
  }
  if (gqlLabelStyle.underline) {
    const separator: string = decoration.length > 0 ? ' ' : '';
    decoration = decoration + separator + 'underline';
  }
  if (decoration.length > 0) {
    style.textDecoration = decoration;
  }

  return style;
};

export const convertDiagram = (gqlDiagram: GQLDiagram): Diagram => {
  const nodes: Node[] = [];
  gqlDiagram.nodes.forEach((gqlNode) => convertNode(gqlNode, null, nodes));

  const nodeId2node = new Map<string, Node>();
  nodes.forEach((node) => nodeId2node.set(node.id, node));

  const nodeId2Depth = new Map<string, number>();
  nodes.forEach((node) => nodeId2Depth.set(node.id, nodeDepth(nodeId2node, node.id)));

  const edges: Edge[] = gqlDiagram.edges.map((gqlEdge) => {
    const zIndex = Math.max(nodeId2Depth.get(gqlEdge.sourceId), nodeId2Depth.get(gqlEdge.targetId));

    const data: CustomEdgeData = {};
    if (gqlEdge.beginLabel) {
      data.beginLabel = convertEdgeLabel(gqlEdge.beginLabel);
    }
    if (gqlEdge.centerLabel) {
      data.centerLabel = convertEdgeLabel(gqlEdge.centerLabel);
    }
    if (gqlEdge.endLabel) {
      data.endLabel = convertEdgeLabel(gqlEdge.endLabel);
    }

    return {
      id: gqlEdge.id,
      type: 'customEdge',
      source: gqlEdge.sourceId,
      target: gqlEdge.targetId,
      markerEnd: `open-arrow--${gqlEdge.id}--markerEnd`,
      zIndex,
      style: {
        stroke: gqlEdge.style.color,
        strokeWidth: gqlEdge.style.size,
      },
      data,
    };
  });

  return {
    metadata: {
      id: gqlDiagram.id,
      label: gqlDiagram.metadata.label,
      kind: gqlDiagram.metadata.kind,
      targetObjectId: gqlDiagram.targetObjectId,
    },
    nodes,
    edges,
  };
};
