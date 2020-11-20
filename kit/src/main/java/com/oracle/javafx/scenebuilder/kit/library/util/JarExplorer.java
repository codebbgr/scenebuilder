/*
 * Copyright (c) 2017, 2019, Gluon and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.javafx.scenebuilder.kit.library.util;

import com.oracle.javafx.scenebuilder.kit.library.util.JarReportEntry.Status;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** */
public class JarExplorer extends ExplorerBase {

  private final Path jar;

  public JarExplorer(Path jar) {
    assert jar != null;
    assert jar.isAbsolute();

    this.jar = jar;
  }

  public JarReport explore(ClassLoader classLoader) throws IOException {
    final JarReport result = new JarReport(jar);

    try (JarFile jarFile = new JarFile(jar.toFile())) {
      final Enumeration<JarEntry> e = jarFile.entries();
      while (e.hasMoreElements()) {
        final JarEntry entry = e.nextElement();
        JarReportEntry explored = exploreEntry(entry, classLoader);
        if (explored.getStatus() != Status.IGNORED) result.getEntries().add(explored);
      }
    }

    return result;
  }

  /*
   * Private
   */

  private JarReportEntry exploreEntry(JarEntry entry, ClassLoader classLoader) {
    if (entry.isDirectory()) {
      return new JarReportEntry(entry.getName(), JarReportEntry.Status.IGNORED, null, null, null);
    } else {
      String className = makeClassName(entry.getName(), "/");
      return super.exploreEntry(entry.getName(), classLoader, className);
    }
  }
}
