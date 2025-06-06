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
package org.eclipse.sirius.components.view.builder.generated.form;

/**
 * Builder for RadioDescriptionBuilder.
 *
 * @author BuilderGenerator
 * @generated
 */
public class RadioDescriptionBuilder {

    /**
     * Create instance org.eclipse.sirius.components.view.form.RadioDescription.
     * @generated
     */
    private org.eclipse.sirius.components.view.form.RadioDescription radioDescription = org.eclipse.sirius.components.view.form.FormFactory.eINSTANCE.createRadioDescription();

    /**
     * Return instance org.eclipse.sirius.components.view.form.RadioDescription.
     * @generated
     */
    protected org.eclipse.sirius.components.view.form.RadioDescription getRadioDescription() {
        return this.radioDescription;
    }

    /**
     * Return instance org.eclipse.sirius.components.view.form.RadioDescription.
     * @generated
     */
    public org.eclipse.sirius.components.view.form.RadioDescription build() {
        return this.getRadioDescription();
    }

    /**
     * Setter for Name.
     *
     * @generated
     */
    public RadioDescriptionBuilder name(java.lang.String value) {
        this.getRadioDescription().setName(value);
        return this;
    }
    /**
     * Setter for LabelExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder labelExpression(java.lang.String value) {
        this.getRadioDescription().setLabelExpression(value);
        return this;
    }
    /**
     * Setter for HelpExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder helpExpression(java.lang.String value) {
        this.getRadioDescription().setHelpExpression(value);
        return this;
    }
    /**
     * Setter for DiagnosticsExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder diagnosticsExpression(java.lang.String value) {
        this.getRadioDescription().setDiagnosticsExpression(value);
        return this;
    }

    /**
     * Setter for ValueExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder valueExpression(java.lang.String value) {
        this.getRadioDescription().setValueExpression(value);
        return this;
    }
    /**
     * Setter for CandidatesExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder candidatesExpression(java.lang.String value) {
        this.getRadioDescription().setCandidatesExpression(value);
        return this;
    }
    /**
     * Setter for CandidateLabelExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder candidateLabelExpression(java.lang.String value) {
        this.getRadioDescription().setCandidateLabelExpression(value);
        return this;
    }
    /**
     * Setter for Body.
     *
     * @generated
     */
    public RadioDescriptionBuilder body(org.eclipse.sirius.components.view.Operation ... values) {
        for (org.eclipse.sirius.components.view.Operation value : values) {
            this.getRadioDescription().getBody().add(value);
        }
        return this;
    }

    /**
     * Setter for Style.
     *
     * @generated
     */
    public RadioDescriptionBuilder style(org.eclipse.sirius.components.view.form.RadioDescriptionStyle value) {
        this.getRadioDescription().setStyle(value);
        return this;
    }
    /**
     * Setter for ConditionalStyles.
     *
     * @generated
     */
    public RadioDescriptionBuilder conditionalStyles(org.eclipse.sirius.components.view.form.ConditionalRadioDescriptionStyle ... values) {
        for (org.eclipse.sirius.components.view.form.ConditionalRadioDescriptionStyle value : values) {
            this.getRadioDescription().getConditionalStyles().add(value);
        }
        return this;
    }

    /**
     * Setter for IsEnabledExpression.
     *
     * @generated
     */
    public RadioDescriptionBuilder isEnabledExpression(java.lang.String value) {
        this.getRadioDescription().setIsEnabledExpression(value);
        return this;
    }

}

