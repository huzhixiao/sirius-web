/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

import { InternalNode, Node, Position, XYPosition } from '@xyflow/react';
import { Handle } from '@xyflow/system';
import { BorderNodePosition, NodeData } from '../DiagramRenderer.types';
import { ConnectionHandle } from '../handles/ConnectionHandles.types';
import { getPositionAbsoluteFromNodeChange, isDescendantOf, isSiblingOrDescendantOf } from '../layout/layoutNode';
import { borderLeftAndRight, horizontalLayoutDirectionGap, verticalLayoutDirectionGap } from '../layout/layoutParams';
import {
  GetEdgeParameters,
  GetEdgeParametersWhileMoving,
  GetHandleCoordinatesByPosition,
  GetNodeCenter,
  GetParameters,
  GetUpdatedConnectionHandlesParameters,
  NodeCenter,
} from './EdgeLayout.types';

export const DEFAULT_HANDLE_SIZE = 6;

export const getNodesUpdatedWithHandles = (
  nodes: Node<NodeData>[],
  node: InternalNode<Node<NodeData>>,
  edgeId: string,
  handleId: string,
  XYPosition: XYPosition,
  position: Position
): Node<NodeData>[] => {
  if (node.height && node.width) {
    //Take the size of the handle and its position into account
    XYPosition = {
      x: XYPosition.x - node.internals.positionAbsolute.x,
      y: XYPosition.y - node.internals.positionAbsolute.y,
    };

    if (position === Position.Bottom) {
      XYPosition = {
        ...XYPosition,
        y: XYPosition.y - node.height - DEFAULT_HANDLE_SIZE,
      };
    }

    if (position === Position.Right) {
      XYPosition = {
        ...XYPosition,
        x: XYPosition.x - node.width - DEFAULT_HANDLE_SIZE,
      };
    }
  }

  const nodeId = node.id;
  return nodes.map((node) => {
    if (nodeId === node.id) {
      const handles = node.data.connectionHandles.map((handle) => {
        if (handle.id === handleId) {
          return {
            ...handle,
            edgeId,
            XYPosition,
            position,
          };
        }
        return handle;
      });

      return {
        ...node,
        data: {
          ...node.data,
          connectionHandles: handles,
        },
      };
    }
    return node;
  });
};

const clamp = (n: number, lower: number, upper: number) => Math.max(lower, Math.min(upper, n));

export const getNearestPointInPerimeter = (
  rectX: number,
  rectY: number,
  width: number,
  height: number,
  x: number,
  y: number
): { XYPosition: XYPosition; position: Position } => {
  const rectX2 = rectX + width;
  const rectY2 = rectY + height;

  x = clamp(x, rectX, rectX2);
  y = clamp(y, rectY, rectY2);

  const left = Math.abs(x - rectX);
  const right = Math.abs(x - rectX2);
  const top = Math.abs(y - rectY);
  const bottom = Math.abs(y - rectY2);
  const m = Math.min(left, right, top, bottom);

  if (m === top) {
    return { XYPosition: { x, y: rectY }, position: Position.Top };
  } else if (m === bottom) {
    return { XYPosition: { x, y: rectY2 }, position: Position.Bottom };
  } else if (m === left) {
    return { XYPosition: { x: rectX, y }, position: Position.Left };
  } else {
    return { XYPosition: { x: rectX2, y }, position: Position.Right };
  }
};

export const getUpdatedConnectionHandles: GetUpdatedConnectionHandlesParameters = (
  sourceNode,
  targetNode,
  sourcePosition,
  targetPosition,
  sourceHandle,
  targetHandle
) => {
  const sourceConnectionHandles: ConnectionHandle[] = sourceNode.data.connectionHandles.map(
    (nodeConnectionHandle: ConnectionHandle) => {
      if (
        nodeConnectionHandle.id === sourceHandle &&
        nodeConnectionHandle.type === 'source' &&
        !nodeConnectionHandle.XYPosition
      ) {
        nodeConnectionHandle.position = sourcePosition;
      }
      return nodeConnectionHandle;
    }
  );

  const targetConnectionHandles: ConnectionHandle[] = targetNode.data.connectionHandles.map(
    (nodeConnectionHandle: ConnectionHandle) => {
      if (
        nodeConnectionHandle.id === targetHandle &&
        nodeConnectionHandle.type === 'target' &&
        !nodeConnectionHandle.XYPosition
      ) {
        nodeConnectionHandle.position = targetPosition;
      }
      return nodeConnectionHandle;
    }
  );

  return {
    sourceConnectionHandles,
    targetConnectionHandles,
  };
};

export const getEdgeParametersWhileMoving: GetEdgeParametersWhileMoving = (
  movingNode,
  source,
  target,
  nodeLookup,
  layoutDirection
) => {
  const { position: sourcePosition } = getParameters(movingNode, source, target, nodeLookup, layoutDirection);
  const { position: targetPosition } = getParameters(movingNode, target, source, nodeLookup, layoutDirection);

  return {
    sourcePosition,
    targetPosition,
  };
};

export const getEdgeParameters: GetEdgeParameters = (source, target, nodeLookup, layoutDirection) => {
  const { position: sourcePosition } = getParameters(null, source, target, nodeLookup, layoutDirection);
  const { position: targetPosition } = getParameters(null, target, source, nodeLookup, layoutDirection);

  return {
    sourcePosition,
    targetPosition,
  };
};

const isVerticalLayoutDirection = (layoutDirection: string): boolean =>
  layoutDirection === 'DOWN' || layoutDirection === 'UP';
const isHorizontalLayoutDirection = (layoutDirection: string): boolean =>
  layoutDirection === 'RIGHT' || layoutDirection === 'LEFT';

const computeBorderNodeHandlePosition = (
  borderNodePosition: BorderNodePosition | null,
  isInside: boolean
): Position => {
  switch (borderNodePosition) {
    case BorderNodePosition.EAST:
      return isInside ? Position.Left : Position.Right;
    case BorderNodePosition.SOUTH:
      return isInside ? Position.Top : Position.Bottom;
    case BorderNodePosition.WEST:
      return isInside ? Position.Right : Position.Left;
    case BorderNodePosition.NORTH:
      return isInside ? Position.Bottom : Position.Top;
    default:
      return isInside ? Position.Left : Position.Right;
  }
};

const getParameters: GetParameters = (nodePositionChange, nodeA, nodeB, nodeLookup, layoutDirection) => {
  if (nodeA.data.isBorderNode) {
    const isInside = isSiblingOrDescendantOf(nodeA, nodeB, nodeLookup);
    return {
      position: computeBorderNodeHandlePosition(nodeA.data.borderNodePosition, isInside),
    };
  }

  let centerA: NodeCenter;
  if (nodePositionChange && nodePositionChange.id === nodeA.id) {
    const nodePositionAbsolute = getPositionAbsoluteFromNodeChange(nodePositionChange, nodeLookup);
    centerA = {
      x: (nodePositionAbsolute ? nodePositionAbsolute.x : 0) + (nodeA.width ?? 0) / 2,
      y: (nodePositionAbsolute ? nodePositionAbsolute.y : 0) + (nodeA.height ?? 0) / 2,
    };
  } else {
    centerA = getNodeCenter(nodeA, nodeLookup);
  }

  let centerB: NodeCenter;
  if (nodePositionChange && nodePositionChange.id === nodeB.id) {
    const nodePositionAbsolute = getPositionAbsoluteFromNodeChange(nodePositionChange, nodeLookup);
    centerB = {
      x: (nodePositionAbsolute ? nodePositionAbsolute.x : 0) + (nodeB.width ?? 0) / 2,
      y: (nodePositionAbsolute ? nodePositionAbsolute.y : 0) + (nodeB.height ?? 0) / 2,
    };
  } else {
    centerB = getNodeCenter(nodeB, nodeLookup);
  }

  const horizontalDifference = Math.abs(centerA.x - centerB.x);
  const verticalDifference = Math.abs(centerA.y - centerB.y);
  const isDescendant = nodeA.data.isBorderNode
    ? isDescendantOf(nodeA, nodeB, nodeLookup)
    : isDescendantOf(nodeB, nodeA, nodeLookup);
  let position: Position;
  if (isVerticalLayoutDirection(layoutDirection)) {
    if (Math.abs(centerA.y - centerB.y) < verticalLayoutDirectionGap) {
      position = centerA.x <= centerB.x ? Position.Right : Position.Left;
    } else if (isDescendant) {
      position = centerA.y <= centerB.y ? Position.Top : Position.Bottom;
    } else {
      position = centerA.y > centerB.y ? Position.Top : Position.Bottom;
    }
  } else if (isHorizontalLayoutDirection(layoutDirection)) {
    if (Math.abs(centerA.x - centerB.x) < horizontalLayoutDirectionGap) {
      position = centerA.y <= centerB.y ? Position.Bottom : Position.Top;
    } else if (isDescendant) {
      position = centerA.x <= centerB.x ? Position.Left : Position.Right;
    } else {
      position = centerA.x > centerB.x ? Position.Left : Position.Right;
    }
  } else {
    if (horizontalDifference > verticalDifference) {
      if (isDescendant) {
        position = centerA.x <= centerB.x ? Position.Left : Position.Right;
      } else {
        position = centerA.x > centerB.x ? Position.Left : Position.Right;
      }
    } else {
      if (isDescendant) {
        position = centerA.y <= centerB.y ? Position.Top : Position.Bottom;
      } else {
        position = centerA.y > centerB.y ? Position.Top : Position.Bottom;
      }
    }
  }
  return {
    position,
  };
};

export const getNodeCenter: GetNodeCenter = (node, nodeLookUp) => {
  //node.internals can be null when this method is called from convertDiagram
  if (node.internals) {
    return {
      x: node.internals.positionAbsolute.x + (node.width ?? 0) / 2,
      y: node.internals.positionAbsolute.y + (node.height ?? 0) / 2,
    };
  } else {
    let position = {
      x: (node.position?.x ?? 0) + (node.width ?? 0) / 2,
      y: (node.position?.y ?? 0) + (node.height ?? 0) / 2,
    };

    if (node.parentId) {
      let parentNode = nodeLookUp.get(node.parentId);
      while (parentNode) {
        position = {
          x: position.x + (parentNode.position?.x ?? 0),
          y: position.y + (parentNode.position?.y ?? 0),
        };
        let parentNodeId = parentNode.parentId ?? '';
        parentNode = nodeLookUp.get(parentNodeId);
      }
    }
    return position;
  }
};

export const getHandleCoordinatesByPosition: GetHandleCoordinatesByPosition = (
  node,
  handlePosition,
  handleId,
  calculateCustomNodeEdgeHandlePosition
) => {
  let handle: Handle | undefined = (node.internals.handleBounds?.source ?? []).find((handle) => handle.id === handleId);
  if (!handle) {
    handle = (node.internals.handleBounds?.target ?? []).find((handle) => handle.id === handleId);
  }

  let handleXYPosition: XYPosition = { x: 0, y: 0 };
  if (handle && handlePosition) {
    if (calculateCustomNodeEdgeHandlePosition) {
      handleXYPosition = calculateCustomNodeEdgeHandlePosition(node, handlePosition, handle);
    } else {
      let offsetX = handle.width / 2 + borderLeftAndRight / 2;
      let offsetY = handle.height / 2 + borderLeftAndRight / 2;

      if (handlePosition === Position.Left) {
        offsetX = handle.width - offsetX;
      } else if (handlePosition === Position.Top) {
        offsetY = handle.height - offsetY;
      }

      handleXYPosition = {
        x: handle.x + offsetX,
        y: handle.y + offsetY,
      };
    }
  }

  return {
    x: (node.internals.positionAbsolute.x ?? 0) + handleXYPosition.x,
    y: (node.internals.positionAbsolute.y ?? 0) + handleXYPosition.y,
  };
};
