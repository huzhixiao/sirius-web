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
package org.eclipse.sirius.components.view.emf.form.converters;

import static org.eclipse.sirius.components.view.emf.form.ViewFormDescriptionConverterSwitch.VARIABLE_MANAGER;

import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.forms.TextareaStyle;
import org.eclipse.sirius.components.forms.WidgetIdProvider;
import org.eclipse.sirius.components.forms.description.TextareaDescription;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.interpreter.StringValueProvider;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.view.emf.form.IFormIdProvider;
import org.eclipse.sirius.components.view.emf.form.TextareaStyleProvider;
import org.eclipse.sirius.components.view.emf.form.converters.validation.DiagnosticKindProvider;
import org.eclipse.sirius.components.view.emf.form.converters.validation.DiagnosticMessageProvider;
import org.eclipse.sirius.components.view.emf.form.converters.validation.DiagnosticProvider;
import org.eclipse.sirius.components.view.emf.operations.api.IOperationExecutor;
import org.eclipse.sirius.components.view.form.TextAreaDescription;
import org.eclipse.sirius.components.view.form.TextareaDescriptionStyle;

/**
 * Used to convert textareas.
 *
 * @author sbegaudeau
 */
public class TextareaDescriptionConverter {

    private final AQLInterpreter interpreter;

    private final IObjectService objectService;

    private final IOperationExecutor operationExecutor;

    private final IFeedbackMessageService feedbackMessageService;

    private final IFormIdProvider widgetIdProvider;

    public TextareaDescriptionConverter(AQLInterpreter interpreter, IObjectService objectService, IOperationExecutor operationExecutor, IFeedbackMessageService feedbackMessageService, IFormIdProvider widgetIdProvider) {
        this.interpreter = Objects.requireNonNull(interpreter);
        this.objectService = Objects.requireNonNull(objectService);
        this.operationExecutor = Objects.requireNonNull(operationExecutor);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.widgetIdProvider = Objects.requireNonNull(widgetIdProvider);
    }

    public TextareaDescription convert(TextAreaDescription viewTextAreaDescription) {
        String descriptionId = this.widgetIdProvider.getFormElementDescriptionId(viewTextAreaDescription);

        Function<VariableManager, TextareaStyle> styleProvider = variableManager -> {
            VariableManager childVariableManager = variableManager.createChild();
            childVariableManager.put(VARIABLE_MANAGER, variableManager);
            var effectiveStyle = viewTextAreaDescription.getConditionalStyles().stream()
                    .filter(style -> this.interpreter.evaluateExpression(childVariableManager.getVariables(), style.getCondition())
                            .asBoolean()
                            .orElse(Boolean.FALSE))
                    .map(TextareaDescriptionStyle.class::cast)
                    .findFirst()
                    .orElseGet(viewTextAreaDescription::getStyle);
            if (effectiveStyle == null) {
                return null;
            }
            return new TextareaStyleProvider(effectiveStyle).apply(childVariableManager);
        };

        return TextareaDescription.newTextareaDescription(descriptionId)
                .idProvider(new WidgetIdProvider())
                .targetObjectIdProvider(new TargetObjectIdProvider(this.objectService))
                .labelProvider(new StringValueProvider(this.interpreter, viewTextAreaDescription.getLabelExpression()))
                .isReadOnlyProvider(new ReadOnlyValueProvider(this.interpreter, viewTextAreaDescription.getIsEnabledExpression()))
                .valueProvider(new StringValueProvider(this.interpreter, viewTextAreaDescription.getValueExpression()))
                .newValueHandler(new NewValueHandler<>(this.interpreter, this.operationExecutor, this.feedbackMessageService, viewTextAreaDescription.getBody()))
                .styleProvider(styleProvider)
                .diagnosticsProvider(new DiagnosticProvider(this.interpreter, viewTextAreaDescription.getDiagnosticsExpression()))
                .kindProvider(new DiagnosticKindProvider())
                .messageProvider(new DiagnosticMessageProvider())
                .helpTextProvider(new StringValueProvider(this.interpreter, viewTextAreaDescription.getHelpExpression()))
                .build();
    }
}
