/*******************************************************************************
 * Copyright (c) 2021, 2024 Obeo.
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
package org.eclipse.sirius.components.validation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.annotations.Immutable;
import org.eclipse.sirius.components.representations.IRepresentation;

/**
 * Root concept of the validation representation.
 *
 * @author gcoutable
 */
@Immutable
public final class Validation implements IRepresentation {
    public static final String KIND = IRepresentation.KIND_PREFIX + "?type=Validation";

    public static final String PREFIX = "validation://";

    private String id;

    private String kind;

    private String descriptionId;

    private String targetObjectId;

    private List<Diagnostic> diagnostics;

    private Validation() {
        // Prevent instantiation
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDescriptionId() {
        return this.descriptionId;
    }

    @Override
    public String getKind() {
        return this.kind;
    }

    @Override
    public String getTargetObjectId() {
        return this.targetObjectId;
    }

    public List<Diagnostic> getDiagnostics() {
        return this.diagnostics;
    }

    public static Builder newValidation(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, descriptionId: {2}'}'";
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.descriptionId);
    }

    /**
     * The builder used to create the validation.
     *
     * @author gcoutable
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {

        private String id;

        private String kind = KIND;

        private String descriptionId;

        private String targetObjectId;

        private List<Diagnostic> diagnostics;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder descriptionId(String descriptionId) {
            this.descriptionId = Objects.requireNonNull(descriptionId);
            return this;
        }

        public Builder targetObjectId(String targetObjectId) {
            this.targetObjectId = Objects.requireNonNull(targetObjectId);
            return this;
        }

        public Builder diagnostics(List<Diagnostic> diagnostics) {
            this.diagnostics = diagnostics;
            return this;
        }

        public Validation build() {
            Validation validation = new Validation();
            validation.id = Objects.requireNonNull(this.id);
            validation.kind = Objects.requireNonNull(this.kind);
            validation.descriptionId = Objects.requireNonNull(this.descriptionId);
            validation.targetObjectId = Objects.requireNonNull(this.targetObjectId);
            validation.diagnostics = Objects.requireNonNull(this.diagnostics);
            return validation;
        }
    }
}
