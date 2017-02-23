/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.files;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraft.client.Minecraft;

public final class WurstFolders
{
	public static final Path MAIN =
		Minecraft.getMinecraft().mcDataDir.toPath().resolve("wurst");
	
	public static final Path AUTOBUILD = MAIN.resolve("autobuild");
	public static final Path SKINS = MAIN.resolve("skins");
	public static final Path SERVERLISTS = MAIN.resolve("serverlists");
	public static final Path SPAM = MAIN.resolve("spam");
	public static final Path SCRIPTS = SPAM.resolve("autorun");
	public static final Path RSA =
		Paths.get(System.getProperty("user.home"), ".ssh");
	
	public static void createFolders()
		throws ReflectiveOperationException, IOException
	{
		if(System.getProperty("user.home") == null)
			throw new IOException("user.home property is missing!");
		
		for(Field field : WurstFolders.class.getFields())
			Files.createDirectory((Path)field.get(null));
	}
}
