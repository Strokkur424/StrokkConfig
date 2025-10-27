/*
 * StrokkCommands - A super simple annotation based zero-shade Paper configuration library.
 * Copyright (C) 2025 Strokkur24
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <https://www.gnu.org/licenses/>.
 */
package net.strokkur.config.testplugin;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

@SuppressWarnings("UnstableApiUsage")
public class TestPluginLoader implements PluginLoader {

  @Override
  public void classloader(PluginClasspathBuilder classpathBuilder) {
    final MavenLibraryResolver resolver = new MavenLibraryResolver();
    resolver.addRepository(new RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());
    resolver.addDependency(new Dependency(new DefaultArtifact("org.spongepowered:configurate-hocon:" + Versions.CONFIGURATE), null));
    resolver.addDependency(new Dependency(new DefaultArtifact("org.spongepowered:configurate-yaml:" + Versions.CONFIGURATE), null));
    classpathBuilder.addLibrary(resolver);
  }
}
