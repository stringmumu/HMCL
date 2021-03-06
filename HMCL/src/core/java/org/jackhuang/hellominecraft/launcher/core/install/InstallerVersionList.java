/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2013  huangyuhui <huanghongxun2008@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hellominecraft.launcher.core.install;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.jackhuang.hellominecraft.util.tasks.Task;

/**
 *
 * @author huangyuhui
 */
public abstract class InstallerVersionList {

	/**
	 * Refresh installer versions list from the downloaded content.
	 *
	 * @param versions Minecraft versions you need to refresh
	 *
	 * @throws java.lang.Exception including network exceptions, IO exceptions.
	 */
	public abstract Task refresh(String[] versions);

	/**
	 * Installer name.
	 *
	 * @return installer name.
	 */
	public abstract String getName();

	/**
	 * Get installers you want.
	 *
	 * @param mcVersion the installers to this Minecraft version.
	 *
	 * @return cached result.
	 */
	protected abstract List<InstallerVersion> getVersionsImpl(String mcVersion);

	/**
	 * Get installers you want, please cache this method's result to save time.
	 *
	 * @param mcVersion the installers to this Minecraft version.
	 *
	 * @return a copy of the cached data to prevent
	 * ConcurrentModificationException.
	 */
	public List<InstallerVersion> getVersions(String mcVersion) {
		List<InstallerVersion> a = getVersionsImpl(mcVersion);
		if (a == null) {
			return null;
		} else {
			return new ArrayList<>(a);
		}
	}

	public static int compareVersion(String verOne, String verTwo) {
		String[] verInfoOne = verOne.split("\\.");
		String[] verInfoTwo = verTwo.split("\\.");
		int idx = 0;
		int minLength = Math.min(verInfoOne.length, verInfoTwo.length);
		int diff = 0;
		while (idx < minLength
				&& (diff = verInfoOne[idx].length() - verInfoTwo[idx].length()) == 0
				&& (diff = verInfoOne[idx].compareTo(verInfoTwo[idx])) == 0) {
			++idx;
		}
		diff = (diff != 0) ? diff : verInfoOne.length - verInfoTwo.length;
		return (diff == 0) ? 0 : (diff < 0 ? -1 : 1);
	}

	public static class InstallerVersion implements Comparable<InstallerVersion> {

		public String selfVersion, mcVersion;
		public String installer, universal;
		public String changelog;

		public InstallerVersion(String selfVersion, String mcVersion) {
			this.selfVersion = selfVersion;
			this.mcVersion = mcVersion;
		}

		@Override
		public int compareTo(InstallerVersion o) {
			return compareVersion(selfVersion, o.selfVersion);
		}

		@Override
		public int hashCode() {
			return selfVersion.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final InstallerVersion other = (InstallerVersion) obj;
			return Objects.equals(this.selfVersion, other.selfVersion);
		}

	}

	public static class InstallerVersionComparator implements Comparator<InstallerVersion>, Serializable {

		private static final long serialVersionUID = 3276198781795213723L;

		public static final InstallerVersionComparator INSTANCE = new InstallerVersionComparator();

		@Override
		public int compare(InstallerVersion o1, InstallerVersion o2) {
			return o2.compareTo(o1);
		}
	}
}
