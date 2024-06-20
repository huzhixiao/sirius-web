/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.sirius.components.gantt.tests.graphql;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.gantt.dto.input.CreateGanttTaskDependencyInput;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.springframework.stereotype.Service;

/**
 * Used to create a Task Dependency with the GraphQL API.
 *
 * @author lfasani
 */
@Service
public class CreateTaskDependencyMutationRunner implements IMutationRunner<CreateGanttTaskDependencyInput> {

    private static final String CREATE_TASK_DEPENDENCY_MUTATION = """
          mutation createGanttTaskDependency($input: CreateGanttTaskDependencyInput!) {
            createGanttTaskDependency(input: $input) {
              __typename
              ... on ErrorPayload {
                messages {
                  body
                  level
                }
              }
              ... on SuccessPayload {
                messages {
                  body
                  level
                }
              }
            }
          }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public CreateTaskDependencyMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(CreateGanttTaskDependencyInput input) {
        return this.graphQLRequestor.execute(CREATE_TASK_DEPENDENCY_MUTATION, input);
    }
}
