/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.features.mods.fun;

import net.minecraft.network.play.client.CPacketPlayer;
import net.wurstclient.compatibility.WConnection;
import net.wurstclient.compatibility.WMinecraft;
import net.wurstclient.events.listeners.UpdateListener;
import net.wurstclient.features.HelpPage;
import net.wurstclient.features.Mod;
import net.wurstclient.features.SearchTags;

@SearchTags({"Retarded"})
@HelpPage("Mods/Derp")
@Mod.Bypasses(ghostMode = false, latestNCP = false, olderNCP = false)
public final class DerpMod extends Mod implements UpdateListener
{
	public DerpMod()
	{
		super("Derp", "While this is active, other people will think you are\n"
			+ "derping around.");
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		float yaw = WMinecraft.getPlayer().rotationYaw
			+ (float)(Math.random() * 360 - 180);
		float pitch = (float)(Math.random() * 180 - 90);
		WConnection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch,
			WMinecraft.getPlayer().onGround));
	}
}
