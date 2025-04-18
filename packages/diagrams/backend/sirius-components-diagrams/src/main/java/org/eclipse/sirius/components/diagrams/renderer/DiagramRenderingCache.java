/*******************************************************************************
 * Copyright (c) 2019, 2025 Obeo.
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
package org.eclipse.sirius.components.diagrams.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.representations.Element;

/**
 * Cache used during the rendering of a diagram.
 *
 * @author sbegaudeau
 */
public class DiagramRenderingCache {

    private final Map<String, List<Element>> diagramElementDescriptionIdToElements = new LinkedHashMap<>();

    private final Map<String, Element> elementIdToElement = new LinkedHashMap<>();

    private final Map<Element, Object> elementToObject = new LinkedHashMap<>();

    private final Map<Object, List<Element>> objectToElements = new LinkedHashMap<>();

    private final Map<Element, String> nodeToParentElementId = new LinkedHashMap<>();

    private final Map<String, List<Element>> nodeIdToChildren = new LinkedHashMap<>();

    public void put(String nodeDescriptionId, Element nodeElement) {
        this.diagramElementDescriptionIdToElements.computeIfAbsent(nodeDescriptionId, id -> new ArrayList<>()).add(nodeElement);
    }

    public void put(Object object, Element nodeElement) {
        this.elementToObject.put(nodeElement, object);
        this.objectToElements.computeIfAbsent(object, obj -> new ArrayList<>()).add(nodeElement);
    }

    public void put(Element node, String parentNodeId) {
        if (node.getProps() instanceof NodeElementProps nodeElementProps) {
            this.elementIdToElement.put(nodeElementProps.getId(), node);
            this.nodeToParentElementId.put(node, parentNodeId);

            var children = this.nodeIdToChildren.getOrDefault(parentNodeId, new ArrayList<>());
            children.add(node);
            this.nodeIdToChildren.put(parentNodeId, children);
        }
    }

    public Map<String, List<Element>> getDiagramElementDescriptionIdToElements() {
        return this.diagramElementDescriptionIdToElements;
    }

    public Map<Element, Object> getElementToObject() {
        return this.elementToObject;
    }

    public Map<Object, List<Element>> getObjectToElements() {
        return this.objectToElements;
    }

    public List<Element> getElementsRepresenting(Object semanticObject) {
        return this.objectToElements.getOrDefault(semanticObject, Collections.emptyList());
    }

    public Optional<Element> getParent(String nodeId) {
        // @formatter:off
        return Optional.ofNullable(this.elementIdToElement.get(nodeId))
                .map(this.nodeToParentElementId::get)
                .map(this.elementIdToElement::get);
        // @formatter:on
    }

    public List<Element> getAncestors(String nodeId) {
        List<Element> ancestors = new ArrayList<>();

        var parent = this.getParent(nodeId).orElse(null);
        while (parent != null) {
            ancestors.add(parent);
            if (parent.getProps() instanceof NodeElementProps nodeElementProps) {
                parent = this.getParent(nodeElementProps.getId()).orElse(null);
            }
        }

        return ancestors;
    }

    public List<Element> getChildren(String nodeId) {
        return Optional.ofNullable(this.nodeIdToChildren.get(nodeId)).orElse(List.of());
    }

    public List<Element> getDescendants(String nodeId) {
        List<Element> descendants = new ArrayList<>();

        var children = this.getChildren(nodeId);
        children.forEach(child -> {
            descendants.add(child);
            if (child.getProps() instanceof NodeElementProps nodeElementProps) {
                descendants.addAll(this.getDescendants(nodeElementProps.getId()));
            }
        });

        return descendants;
    }

}
