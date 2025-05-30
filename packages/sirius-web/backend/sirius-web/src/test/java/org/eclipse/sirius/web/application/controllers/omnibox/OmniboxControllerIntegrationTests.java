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
package org.eclipse.sirius.web.application.controllers.omnibox;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.omnibox.dto.ExecuteOmniboxCommandInput;
import org.eclipse.sirius.components.collaborative.omnibox.dto.ExecuteOmniboxCommandSuccessPayload;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteOmniboxCommandMutationRunner;
import org.eclipse.sirius.components.graphql.tests.OmniboxCommandsQueryRunner;
import org.eclipse.sirius.components.graphql.tests.OmniboxSearchQueryRunner;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.web.AbstractIntegrationTests;
import org.eclipse.sirius.web.data.PapayaIdentifiers;
import org.eclipse.sirius.web.data.StudioIdentifiers;
import org.eclipse.sirius.web.papaya.omnibox.PapayaCreateSampleProjectCommandProvider;
import org.eclipse.sirius.web.tests.data.GivenSiriusWebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the omnibox commands query.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OmniboxControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private OmniboxCommandsQueryRunner omniboxCommandsQueryRunner;

    @Autowired
    private OmniboxSearchQueryRunner omniboxSearchQueryRunner;

    @Autowired
    private ExecuteOmniboxCommandMutationRunner executeOmniboxCommandMutationRunner;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Test
    @GivenSiriusWebServer
    @DisplayName("Given a query, when a query is performed, then omnibox commands are returned")
    public void givenQueryWhenAQueryIsPerformedThenCommandsAreReturned() {
        Map<String, Object> firstQueryVariables = Map.of(
                "editingContextId", StudioIdentifiers.SAMPLE_STUDIO_EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", ""
        );
        var firstQueryResult = this.omniboxCommandsQueryRunner.run(firstQueryVariables);
        List<String> allCommandLabels = JsonPath.read(firstQueryResult, "$.data.viewer.omniboxCommands.edges[*].node.label");
        assertThat(allCommandLabels).hasSize(3).contains("Search", "Publish Studio", "Import studio libraries");

        Map<String, Object> secondQueryVariables = Map.of(
                "editingContextId", StudioIdentifiers.SAMPLE_STUDIO_EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", "Sea"
        );
        var secondQueryResult = this.omniboxCommandsQueryRunner.run(secondQueryVariables);
        List<String> seaFilteredCommandsLabels = JsonPath.read(secondQueryResult, "$.data.viewer.omniboxCommands.edges[*].node.label");
        assertThat(seaFilteredCommandsLabels).hasSize(1).anyMatch(label -> Objects.equals(label, "Search"));

        Map<String, Object> thirdQueryVariables = Map.of(
                "editingContextId", StudioIdentifiers.SAMPLE_STUDIO_EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", "yello"
        );
        var thirdQueryResult = this.omniboxCommandsQueryRunner.run(thirdQueryVariables);
        List<String> yelloFilteredCommandsLabels = JsonPath.read(thirdQueryResult, "$.data.viewer.omniboxCommands.edges[*].node.label");
        assertThat(yelloFilteredCommandsLabels).isEmpty();
    }

    @Test
    @GivenSiriusWebServer
    @DisplayName("Given a query, when the text-based objects are queried, then the objects are returned")
    public void givenQueryWhenTextBasedObjectsAreQueriedThenObjectsAreReturned() {
        Map<String, Object> emptyQueryVariables = Map.of(
                "editingContextId", StudioIdentifiers.SAMPLE_STUDIO_EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", ""
        );
        var emptyQueryResult = this.omniboxSearchQueryRunner.run(emptyQueryVariables);
        List<String> emptyQueryObjectLabels = JsonPath.read(emptyQueryResult, "$.data.viewer.omniboxSearch[*].label");
        assertThat(emptyQueryObjectLabels).isNotEmpty();

        String filterQuery = "yello";
        Map<String, Object> filterQueryVariables = Map.of(
                "editingContextId", StudioIdentifiers.SAMPLE_STUDIO_EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", filterQuery
        );
        var filterQueryResult = this.omniboxSearchQueryRunner.run(filterQueryVariables);
        List<String> filterQueryObjectLabels = JsonPath.read(filterQueryResult, "$.data.viewer.omniboxSearch[*].label");
        assertThat(filterQueryObjectLabels).isNotEmpty().allMatch(label -> label.toLowerCase().contains(filterQuery.toLowerCase()));
    }

    @Test
    @GivenSiriusWebServer
    @DisplayName("Given an omnibox command id, when the mutation is performed, then the command is executed")
    public void givenAnOmniboxCommandIdWhenTheMutationIsPerformedThenTheCommandIsExecuted() {
        var result = this.executeOmniboxCommandMutationRunner.run(new ExecuteOmniboxCommandInput(UUID.randomUUID(), PapayaIdentifiers.PAPAYA_EDITING_CONTEXT_ID.toString(), List.of(), PapayaCreateSampleProjectCommandProvider.CREATE_SAMPLE_PROJECT_COMMAND_ID));
        String typename = JsonPath.read(result, "$.data.executeOmniboxCommand.__typename");
        assertThat(typename).isEqualTo(ExecuteOmniboxCommandSuccessPayload.class.getSimpleName());

        BiFunction<IEditingContext, IInput, IPayload> function = (editingContext, executeEditingContextFunctionInput) -> {
            if (editingContext instanceof IEMFEditingContext emfEditingContext) {
                Optional<Resource> optionalResource = emfEditingContext.getDomain().getResourceSet().getResources().stream()
                        .filter(resource -> resource.eAdapters().stream()
                                .anyMatch(adapter -> adapter instanceof ResourceMetadataAdapter resourceMetadataAdapter && Objects.equals(resourceMetadataAdapter.getName(), "Sirius Web - Lifecycle")))
                        .findFirst();
                assertThat(optionalResource).isPresent();
                return new SuccessPayload(executeEditingContextFunctionInput.id());
            }
            return new ErrorPayload(executeEditingContextFunctionInput.id(), "Invalid editing context");
        };

        var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), PapayaIdentifiers.PAPAYA_EDITING_CONTEXT_ID.toString(), function);
        var payload = this.executeEditingContextFunctionRunner.execute(input).block();
        assertThat(payload).isInstanceOf(SuccessPayload.class);
    }

}
