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
package org.eclipse.sirius.components.collaborative.formdescriptioneditors.handlers;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.Monitoring;
import org.eclipse.sirius.components.collaborative.formdescriptioneditors.api.IFormDescriptionEditorContext;
import org.eclipse.sirius.components.collaborative.formdescriptioneditors.api.IFormDescriptionEditorEventHandler;
import org.eclipse.sirius.components.collaborative.formdescriptioneditors.api.IFormDescriptionEditorInput;
import org.eclipse.sirius.components.collaborative.formdescriptioneditors.dto.AddToolbarActionInput;
import org.eclipse.sirius.components.collaborative.formdescriptioneditors.messages.ICollaborativeFormDescriptionEditorMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.view.form.FormFactory;
import org.eclipse.sirius.components.view.form.GroupDescription;
import org.eclipse.sirius.components.view.form.PageDescription;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Handle the add toolbarAction event on Form Description Editor.
 *
 * @author arichard
 */
@Service
public class AddToolbarActionEventHandler implements IFormDescriptionEditorEventHandler {

    private final IObjectSearchService objectSearchService;

    private final ICollaborativeFormDescriptionEditorMessageService messageService;

    private final Counter counter;

    public AddToolbarActionEventHandler(IObjectSearchService objectSearchService, ICollaborativeFormDescriptionEditorMessageService messageService, MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.messageService = Objects.requireNonNull(messageService);

        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IFormDescriptionEditorInput formDescriptionEditorInput) {
        return formDescriptionEditorInput instanceof AddToolbarActionInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IFormDescriptionEditorContext formDescriptionEditorContext,
            IFormDescriptionEditorInput formDescriptionEditorInput) {
        this.counter.increment();

        String message = this.messageService.invalidInput(formDescriptionEditorInput.getClass().getSimpleName(), AddToolbarActionInput.class.getSimpleName());
        IPayload payload = new ErrorPayload(formDescriptionEditorInput.id(), message);
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, formDescriptionEditorInput.representationId(), formDescriptionEditorInput);

        if (formDescriptionEditorInput instanceof AddToolbarActionInput) {
            String containerId = ((AddToolbarActionInput) formDescriptionEditorInput).containerId();
            boolean addToolbarAction = this.addToolbarAction(editingContext, formDescriptionEditorContext, containerId);
            if (addToolbarAction) {
                payload = new SuccessPayload(formDescriptionEditorInput.id());
                changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, formDescriptionEditorInput.representationId(), formDescriptionEditorInput);
            }
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }

    private boolean addToolbarAction(IEditingContext editingContext, IFormDescriptionEditorContext formDescriptionEditorContext, String containerId) {
        boolean success = false;
        var optionalSelf = Optional.empty();
        if (containerId != null) {
            optionalSelf = this.objectSearchService.getObject(editingContext, containerId);
        } else {
            optionalSelf = this.objectSearchService.getObject(editingContext, formDescriptionEditorContext.getFormDescriptionEditor().getTargetObjectId());
        }
        if (optionalSelf.isPresent()) {
            Object container = optionalSelf.get();
            var toolbarActionDescription = FormFactory.eINSTANCE.createButtonDescription();
            toolbarActionDescription.setName("ToolbarAction");
            var toolbarActionDescriptionStyle = FormFactory.eINSTANCE.createButtonDescriptionStyle();
            toolbarActionDescription.setStyle(toolbarActionDescriptionStyle);
            if (container instanceof GroupDescription groupDescription) {
                groupDescription.getToolbarActions().add(toolbarActionDescription);
                success = true;
            } else if (container instanceof PageDescription pageDescription) {
                pageDescription.getToolbarActions().add(toolbarActionDescription);
                success = true;
            }
        }
        return success;
    }
}
