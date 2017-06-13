/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.features.mods.other;

import java.util.Random;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.wurstclient.ai.GoRandomAI;
import net.wurstclient.compatibility.WMinecraft;
import net.wurstclient.events.listeners.UpdateListener;
import net.wurstclient.features.Category;
import net.wurstclient.features.Mod;
import net.wurstclient.features.SearchTags;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.utils.RotationUtils;

@SearchTags({"AFKBot", "anti afk", "afk bot"})
@Mod.Bypasses(ghostMode = false)
@Mod.DontSaveState
public final class AntiAfkMod extends Mod implements UpdateListener
{
	private final CheckboxSetting useAi =
		new CheckboxSetting("Use AI (experimental)", false);
	
	private GoRandomAI ai;
	private int timer;
	private Random random = new Random();
	private BlockPos start;
	private BlockPos nextBlock;
	
	public AntiAfkMod()
	{
		super("AntiAFK",
			"Walks around randomly to hide you from AFK detectors.\n"
				+ "Needs 3x3 blocks of free space.");
		setCategory(Category.OTHER);
	}
	
	@Override
	public void initSettings()
	{
		addSetting(useAi);
	}
	
	@Override
	public void onEnable()
	{
		start = new BlockPos(WMinecraft.getPlayer());
		nextBlock = null;
		ai = new GoRandomAI(start, 16F);
		
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		
		mc.gameSettings.keyBindForward.pressed =
			GameSettings.isKeyDown(mc.gameSettings.keyBindForward);
		mc.gameSettings.keyBindJump.pressed =
			GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
		
		if(ai != null)
			ai.stop();
	}
	
	@Override
	public void onUpdate()
	{
		// check if player died
		if(WMinecraft.getPlayer().getHealth() <= 0)
		{
			setEnabled(false);
			return;
		}
		
		if(useAi.isChecked())
		{
			// update timer
			if(timer > 0)
			{
				timer--;
				mc.gameSettings.keyBindJump.pressed =
					WMinecraft.getPlayer().isInWater();
				return;
			}
			
			// walk around
			ai.update();
			
			// wait 2 - 3 seconds (40 - 60 ticks)
			if(ai.isDone())
			{
				ai.stop();
				timer = 40 + random.nextInt(21);
			}
		}else
		{
			// set next block
			if(timer <= 0 || nextBlock == null)
			{
				nextBlock =
					start.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
				timer = 40 + random.nextInt(21);
			}
			
			// face block
			RotationUtils.faceVectorForWalking(
				new Vec3d(nextBlock).addVector(0.5, 0.5, 0.5));
			
			// walk
			if(WMinecraft.getPlayer().getDistanceSqToCenter(nextBlock) > 0.5)
				mc.gameSettings.keyBindForward.pressed = true;
			else
				mc.gameSettings.keyBindForward.pressed = false;
			
			// swim up
			mc.gameSettings.keyBindJump.pressed =
				WMinecraft.getPlayer().isInWater();
			
			// update timer
			if(timer > 0)
				timer--;
		}
	}
}
