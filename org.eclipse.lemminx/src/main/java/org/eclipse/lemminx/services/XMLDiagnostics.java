/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML diagnostics support.
 *
 */
class XMLDiagnostics {

	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLDiagnostics(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<Diagnostic> doDiagnostics(DOMDocument xmlDocument, XMLValidationSettings validationSettings,
			CancelChecker cancelChecker) {
		List<Diagnostic> diagnostics = new ArrayList<Diagnostic>();
		if (validationSettings == null || validationSettings.isEnabled()) {
			doExtensionsDiagnostics(xmlDocument, diagnostics, validationSettings, cancelChecker);
		}
		return diagnostics;
	}

	/**
	 * Do validation with extension (XML Schema, etc)
	 * 
	 * @param xmlDocument
	 * @param diagnostics
	 * @param validationSettings
	 * @param monitor
	 */
	private void doExtensionsDiagnostics(DOMDocument xmlDocument, List<Diagnostic> diagnostics,
			XMLValidationSettings validationSettings, CancelChecker monitor) {
		for (IDiagnosticsParticipant diagnosticsParticipant : extensionsRegistry.getDiagnosticsParticipants()) {
			monitor.checkCanceled();
			diagnosticsParticipant.doDiagnostics(xmlDocument, diagnostics, validationSettings, monitor);
		}
	}

}
