/*******************************************************************************
 * Copyright (c) 2022, 2025 Obeo.
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
package org.eclipse.sirius.components.view.emf.diagram;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.emf.EPackageService;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.emf.diagram.providers.api.IViewToolImageProvider;
import org.eclipse.sirius.ecore.extender.business.api.accessor.EcoreMetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.internal.accessor.ecore.EcoreIntrinsicExtender;
import org.springframework.stereotype.Service;

/**
 * Class used to compute the image of a View tool.
 *
 * @author arichard
 */
@Service
public class ViewToolImageProvider implements IViewToolImageProvider {

    public static final String NODE_CREATION_TOOL_ICON = "/img/DefaultNodeIcon.svg";

    public static final String EDGE_CREATION_TOOL_ICON = "/img/DefaultEdgeIcon.svg";

    private static final Pattern SEPARATOR = Pattern.compile("(::?|\\.)");

    private final IObjectService objectService;

    private final EPackage.Registry ePackageRegistry;

    public ViewToolImageProvider(IObjectService objectService, EPackage.Registry ePackageRegistry) {
        this.objectService = Objects.requireNonNull(objectService);
        this.ePackageRegistry = Objects.requireNonNull(ePackageRegistry);
    }

    @Override
    public List<String> getIcon(DiagramElementDescription diagramElementDescription) {
        var imagePathFromDomainClass = this.getImagePathFromDomainClass(diagramElementDescription);
        if (imagePathFromDomainClass.isEmpty()) {
            return List.of(this.getDefaultImage(diagramElementDescription));
        }
        return imagePathFromDomainClass;
    }

    private List<String> getImagePathFromDomainClass(DiagramElementDescription diagramElementDescription) {
        var optionalInstance = Optional.ofNullable(diagramElementDescription.getDomainType())
                .filter(domainType -> !domainType.isBlank())
                .flatMap(this::getInstance);

        return optionalInstance.map(this.objectService::getImagePath).orElse(List.of());
    }

    private Optional<EObject> getInstance(String domainClass) {
        Matcher matcher = SEPARATOR.matcher(domainClass);
        if (matcher.find()) {
            String packageName = domainClass.substring(0, matcher.start());
            String className = domainClass.substring(matcher.end());

            var optionalEPackage = new EPackageService().findEPackage(this.ePackageRegistry, packageName);
            if (optionalEPackage.isPresent()) {
                EPackage ePackage = optionalEPackage.get();
                EcoreIntrinsicExtender ecoreIntrinsicExtender = new EcoreIntrinsicExtender();
                ecoreIntrinsicExtender.updateMetamodels(List.of(new EcoreMetamodelDescriptor(ePackage)));
                EObject instance = ecoreIntrinsicExtender.createInstance(className);
                return Optional.ofNullable(instance);
            }
        }
        return Optional.empty();
    }

    private String getDefaultImage(DiagramElementDescription diagramElementDescription) {
        String defaultImage = null;
        if (diagramElementDescription instanceof EdgeDescription) {
            defaultImage = EDGE_CREATION_TOOL_ICON;
        } else {
            defaultImage = NODE_CREATION_TOOL_ICON;
        }
        return defaultImage;
    }
}
