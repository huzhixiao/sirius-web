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
package org.eclipse.sirius.components.papaya;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Project</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getProjects <em>Projects</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getComponents <em>Components</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getAllComponents <em>All Components</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getComponentExchanges <em>Component Exchanges</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getIterations <em>Iterations</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getTasks <em>Tasks</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getContributions <em>Contributions</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getApplicationConcerns <em>Application Concerns</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getDomains <em>Domains</em>}</li>
 * <li>{@link org.eclipse.sirius.components.papaya.Project#getChannels <em>Channels</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject()
 * @model
 * @generated
 */
public interface Project extends NamedElement {
    /**
     * Returns the value of the '<em><b>Projects</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Project}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Projects</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Projects()
     * @model containment="true"
     * @generated
     */
    EList<Project> getProjects();

    /**
     * Returns the value of the '<em><b>Components</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Component}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Components</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Components()
     * @model containment="true"
     * @generated
     */
    EList<Component> getComponents();

    /**
     * Returns the value of the '<em><b>All Components</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Component}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>All Components</em>' reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_AllComponents()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Component> getAllComponents();

    /**
     * Returns the value of the '<em><b>Component Exchanges</b></em>' containment reference list. The list contents are
     * of type {@link org.eclipse.sirius.components.papaya.ComponentExchange}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Component Exchanges</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_ComponentExchanges()
     * @model containment="true"
     * @generated
     */
    EList<ComponentExchange> getComponentExchanges();

    /**
     * Returns the value of the '<em><b>Iterations</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Iteration}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Iterations</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Iterations()
     * @model containment="true"
     * @generated
     */
    EList<Iteration> getIterations();

    /**
     * Returns the value of the '<em><b>Tasks</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Task}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Tasks</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Tasks()
     * @model containment="true"
     * @generated
     */
    EList<Task> getTasks();

    /**
     * Returns the value of the '<em><b>Contributions</b></em>' containment reference list. The list contents are of
     * type {@link org.eclipse.sirius.components.papaya.Contribution}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Contributions</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Contributions()
     * @model containment="true"
     * @generated
     */
    EList<Contribution> getContributions();

    /**
     * Returns the value of the '<em><b>Application Concerns</b></em>' containment reference list. The list contents are
     * of type {@link org.eclipse.sirius.components.papaya.ApplicationConcern}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Application Concerns</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_ApplicationConcerns()
     * @model containment="true"
     * @generated
     */
    EList<ApplicationConcern> getApplicationConcerns();

    /**
     * Returns the value of the '<em><b>Domains</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Domain}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Domains</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Domains()
     * @model containment="true"
     * @generated
     */
    EList<Domain> getDomains();

    /**
     * Returns the value of the '<em><b>Channels</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.papaya.Channel}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Channels</em>' containment reference list.
     * @see org.eclipse.sirius.components.papaya.PapayaPackage#getProject_Channels()
     * @model containment="true"
     * @generated
     */
    EList<Channel> getChannels();

} // Project
