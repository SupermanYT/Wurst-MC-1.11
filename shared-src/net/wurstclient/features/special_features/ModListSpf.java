/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.features.special_features;

import net.wurstclient.features.Feature;
import net.wurstclient.features.SearchTags;
import net.wurstclient.features.Spf;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.ModeSetting;

@SearchTags({"ArrayList", "HackList", "CheatList", "mod list", "array list",
	"hack list", "cheat list"})
public final class ModListSpf extends Spf
{
	private final ModeSetting mode =
		new ModeSetting("Mode", new String[]{"Auto", "Count"}, 0)
		{
			@Override
			public void update()
			{
				updateAnimationsLock();
			}
		};
	private final ModeSetting position =
		new ModeSetting("Position", new String[]{"Left", "Right", "Hidden"}, 0)
		{
			@Override
			public void update()
			{
				updateAnimationsLock();
			}
		};
	private final CheckboxSetting animations =
		new CheckboxSetting("Animations", true);
	
	public ModListSpf()
	{
		super("ModList",
			"Shows a list of active mods on the screen.\n"
				+ "�lAuto�r mode renders the whole list if it fits onto the screen.\n"
				+ "�lCount�r mode only renders the number of active mods.");
		
		addSetting(mode);
		addSetting(position);
		addSetting(animations);
	}
	
	private void updateAnimationsLock()
	{
		if(isCountMode() || isHidden())
			animations.lock(() -> false);
		else
			animations.unlock();
	}
	
	@Override
	public Feature[] getSeeAlso()
	{
		return new Feature[]{wurst.special.tabGuiSpf};
	}
	
	public boolean isCountMode()
	{
		return mode.getSelected() == 1;
	}
	
	public boolean isPositionRight()
	{
		return position.getSelected() == 1;
	}
	
	public boolean isHidden()
	{
		return position.getSelected() == 2;
	}
	
	public boolean isAnimations()
	{
		return animations.isChecked();
	}
}
